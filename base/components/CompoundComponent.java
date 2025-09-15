package base.components;

import base.Simulation;
import base.events.*;
import base.fundamentals.Component;
import base.fundamentals.Port;
import base.fundamentals.SelectionArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
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
    private final Pane internalDisplayPane;

    /** Threshold beyond which successive clicks are no longer considered a double click (in milliseconds) */
    private final static int DOUBLE_CLICK_DELAY = 500;
    /** Time value when the last time this Compound Component was left-clicked */
    private long lastClickTime;

    /**
     * Create a new CompoundComponent
     * @param selection The Selection Area which holds the Components making up the CompoundComponent
     * @param x The x coordinate (in cells) of the CompoundComponent
     * @param y The y coordinate (in cells) of the CompoundComponent
     * @param width The width of the CompoundComponent
     * @param height The height of the CompoundComponent
     * @param color The Color of the CompoundComponent
     * @param numInputs The number of input Ports on the CompoundComponent
     * @param numOutputs The number of output Port on the CompoundComponent
     * @param name The name displayed on the CompoundComponent
     * @param displayPane The Pane on which this CompoundComponent lives
     */
    private CompoundComponent(SelectionArea selection, double x, double y, double width, double height,
                              Color color, int numInputs, int numOutputs, String name, Pane displayPane) {
        super(x, y, width, height, color, numInputs, numOutputs, name, DEFAULT_TEXT_COLOR, displayPane);

        // Create 'internal world' of Components
        this.internalDisplayPane = new Pane();
        internalDisplayPane.setUserData(name);

        internalDisplayPane.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e ->
                internalDisplayPane.getChildren().removeAll(Arrays.asList(e.getChildrenToRemove()))
        );
        internalDisplayPane.addEventHandler(AddChildrenEvent.EVENT_TYPE, e ->
                internalDisplayPane.getChildren().addAll(Arrays.asList(e.getChildrenToAdd()))
        );

        // Add Components to internal display
        TreeSet<Component> selected = selection.getSelected();
        selected.forEach(c -> c.copy(internalDisplayPane));
        connectComponents(selected);

        // Double-click detection to change view to internal display
        this.lastClickTime = 0;
        getRect().setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                processPrimaryClick();
            } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                super.remove();
            }
        });
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
                                                   Color color, String name, Pane displayPane) {
        double width = widthStr.isBlank() ? DEFAULT_WIDTH : Double.parseDouble(widthStr);
        double height = heightStr.isBlank() ? DEFAULT_HEIGHT : Double.parseDouble(heightStr);
        int[] IOCounts = selection.getSelectedDependencies();
        name = name.isBlank() ? DEFAULT_TEXT : name;

        new CompoundComponent(selection, 1, 1, width, height, color, IOCounts[0], IOCounts[1], name, displayPane);
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
    private Component findIn(Collection<Component> collection, Component dupeToReturn) {
        return collection.stream().filter(c -> c.equals(dupeToReturn)).findFirst().orElse(null);
    }

    /**
     * Connect the Components in the internal display Pane in the same manner as those in the SelectionArea which
     * was used to create this CompoundComponent
     * @return An int array of length 2, with index 0 being the number of sources needed to cover all unconnected
     *      input Ports once all original connections have been carried over, and index 1 being the same for
     *      the number destinations needed to cover unconnected output Ports.
     */
    private int[] connectComponents(Set<Component> originals) {
        int[] IOPortsNeeded = new int[] {0,0};
        List<Component> copies = Simulation.componentsFromChildren(internalDisplayPane.getChildren());
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
                        IOPortsNeeded[originalPort.isInput() ? 1 : 0]++;
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