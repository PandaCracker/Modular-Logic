package base.components;

import base.Simulation;
import base.Utils;
import base.fundamentals.*;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * A Component which is made up of multiple other Components,
 * encapsulating the functionality of all contained Components
 * @author Lucas Peterson
 */
public class CompoundComponent extends Component {
    /** The default width of a CompoundComponent in pixels*/
    private final static double DEFAULT_WIDTH = 90;
    /** The default height of a CompoundComponent in pixels*/
    private final static double DEFAULT_HEIGHT= 120;
    /** The default color of a CompoundComponent */
    public final static Color DEFAULT_COLOR = Color.BLUE;
    /** The default String displayed on this CompoundComponent */
    private final static String DEFAULT_TEXT = "CompoundComponent";
    /** The default Color of the Text displayed on this CompoundComponent */
    private final static Color DEFAULT_TEXT_COLOR = Color.WHITE;

    /** Unique Pane on which the contents of this CompoundComponent are displayed */
    private final DisplayPane internalDisplayPane;

    /** Threshold beyond which successive clicks are no longer considered a double click (in milliseconds) */
    private final static int DOUBLE_CLICK_DELAY = 500;
    /** Time value when the last time this Compound Component was left-clicked */
    private long lastClickTime;

    /**
     * Create a new CompoundComponent
     * @param x The x coordinate (in cells) of the CompoundComponent
     * @param y The y coordinate (in cells) of the CompoundComponent
     * @param width The width of the CompoundComponent
     * @param height The height of the CompoundComponent
     * @param color The Color of the CompoundComponent
     * @param numInputs The number of input Ports on the CompoundComponent
     * @param numOutputs The number of output Port on the CompoundComponent
     * @param name The name displayed on the CompoundComponent
     * @param displayPane The Pane on which this CompoundComponent lives
     * @param internalDisplayPane The Internal Display Pane which shows the interior of the Compound Component
     */
    private CompoundComponent(double x, double y, double width, double height, Color color, int numInputs,
                              int numOutputs, String name, DisplayPane displayPane, DisplayPane internalDisplayPane) {
        super(x, y, width, height, color, numInputs, numOutputs, name, DEFAULT_TEXT_COLOR, displayPane);
        this.internalDisplayPane = internalDisplayPane;
        init();
    }

    /**
     * Create a new deep copy from a Compound Component
     * @param other The Compound Component to copy
     */
    public CompoundComponent(CompoundComponent other) {
        super(other.getRect().getX(), other.getRect().getY(), other.getRect().getWidth(), other.getRect().getHeight(),
                other.getRect().getFill(), other.getNumInputs(), other.getNumOutputs(), other.getText().getText(),
                DEFAULT_TEXT_COLOR, (DisplayPane) (other.getRect().getParent().getUserData()));

        // Set up, copy over, and connect the internal Display Pane and its Components
        this.internalDisplayPane = new DisplayPane(other.getText().getText() + " View");
        List<Component> originals = Utils.componentsFromChildren(other.internalDisplayPane.getChildren());
        originals.forEach(c -> c.copy(internalDisplayPane));
        connectComponents(originals, Utils.componentsFromChildren(internalDisplayPane.getChildren()));
        init();
    }

    /**
     * Copy and connect the internals of a new Compound Components from the collection of Components which make it up
     */
    private void init() {
        // Double-click detection to change view to internal display
        this.lastClickTime = 0;
        getRect().setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                processPrimaryClick();
            } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                super.remove();
            }
        });

        for (Port port : getAllPorts()) {
            new Port(this, port.getType(), port.getPortNum());
        }
    }

    /**
     * Creates a new CompoundComponent from the user-entered fields
     * @param selection The SelectionArea holding the Components which make up the CompoundComponent
     * @param widthStr The text currently in the width field
     * @param heightStr The text currently in the height field
     * @param color The color currently selected by the ColorPicker
     * @param name The text currently in the name field
     * @param displayPane The Pane currently being viewed
     */
    public static void makeCompoundComponent(SelectionArea selection, String widthStr, String heightStr,
                                                   Color color, String name, DisplayPane displayPane) {
        double width = widthStr.isBlank() ? DEFAULT_WIDTH : Double.parseDouble(widthStr);
        double height = heightStr.isBlank() ? DEFAULT_HEIGHT : Double.parseDouble(heightStr);
        name = name.isBlank() ? DEFAULT_TEXT : name;

        DisplayPane internalDisplayPane = new DisplayPane(name + " View");
        selection.getSelected().forEach(c -> c.copy(internalDisplayPane));

        int[] IOCounts = connectComponents(selection.getSelected(),
                Utils.componentsFromChildren(internalDisplayPane.getChildren()));

        boolean containsSomething = !selection.getSelected().isEmpty();
        if (containsSomething) {
            new CompoundComponent(1, 1, width, height, color, IOCounts[0], IOCounts[1], name, displayPane,
                    internalDisplayPane);
        }
    }

    /**
     * Process a left click, checking for double clicks.
     * Tells the Simulation to view this Compound Component's internal world when a double click is detected
     */
    private void processPrimaryClick() {
        long thisClickTime = System.currentTimeMillis();
        if (thisClickTime - lastClickTime < DOUBLE_CLICK_DELAY) {
            // Set simulation display Pane to this pane
            Simulation.setCenterPane(internalDisplayPane);
            lastClickTime = 0;
        } else {
            lastClickTime = thisClickTime;
        }
    }

    /**
     * Returns the first Component which is equivalent to the one provided from the Collection provided
     * @param collection The Collection of Components to search through
     * @param dupeToReturn The Component which will act as a key to compare against
     * @return The first Component found which is equivalent to the key Component provided, or null if no match is found
     */
    private static Component findIn(Collection<Component> collection, Component dupeToReturn) {
        return collection.stream().filter(c -> c.equals(dupeToReturn)).findFirst().orElse(null);
    }

    /**
     * Connect the Components in the internal display Pane in the same manner as those in the SelectionArea which
     * was used to create this CompoundComponent
     * @return An int array of length 2, with index 0 being the number of sources needed to cover all unconnected
     *      input Ports once all original connections have been carried over, and index 1 being the same for
     *      the number destinations needed to cover unconnected output Ports.
     */
    private static int[] connectComponents(Collection<Component> originals, Collection<Component> copies) {
        int[] IOPortsNeeded = new int[] {0,0};
        // For every component in the current display pane
        for (Component copy : copies) {
            // If it is also in the original set (copies should have the same hash)
            if (originals.contains(copy)) {
                // Get that original Component
                Component original = findIn(originals, copy);

                Port[] originalPorts = original.getAllPorts();
                Port[] copyPorts = copy.getAllPorts();
                // For every port
                for (int portNum = 0; portNum < originalPorts.length; portNum++) {
                    Port originalPort = originalPorts[portNum];
                    Component connectedToOriginal = originalPort.getConnectedComponent();
                    // If the port is connected to a Component which is also in the original set
                    if (connectedToOriginal != null && originals.contains(connectedToOriginal)) {
                        // Connect those components
                        copyPorts[portNum].connectTo(findIn(copies, connectedToOriginal),
                                originalPort.getConnectedPortNum());
                    } else {
                        // Otherwise, the Port was connected from outside or not at all. Either way, it needs somewhere
                        // to go. Input Ports need a source, output Ports need a destination
                        IOPortsNeeded[originalPort.isInput() ? 0 : 1]++;
                    }
                }
            }
        }
        return IOPortsNeeded;
    }

    @Override
    public void update() {

    }

    @Override
    public String toString() {
        return super.toString() + " with " + getNumInputs() + " ins, " + getNumOutputs() + " outs";
    }
}