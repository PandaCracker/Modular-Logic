package base.components;

import base.fundamentals.Component;
import base.fundamentals.SelectionArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.TreeSet;

public class CompoundComponent extends Component {
    /** The default width of a CompoundComponent */
    private final static double DEFAULT_WIDTH = 1.5;
    /** The default height of a CompoundComponent */
    private final static double DEFAULT_HEIGHT= 2;
    /** The default color of a CompoundComponent */
    private final static Color DEFAULT_COLOR = Color.BLUE;
    /** The default String displayed on this CompoundComponent */
    private final static String DEFAULT_TEXT = "CompoundComponent";
    /** The default Color of the Text displayed on this CompoundComponent */
    private final static Color DEFAULT_TEXT_COLOR = Color.WHITE;

    /** Sorted set of the Components which make up this CompoundComponent */
    private final TreeSet<Component> contents;

    /** Unique Pane on which the contents of this CompoundComponent are displayed */
    private final Pane internalDisplayPane;

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
        this.contents = new TreeSet<>();
        for (Component component : selection.getSelected()) {
            contents.add(component.copy(displayPane));
        }

        getRect().setOnMouseClicked(e -> System.out.println(contents));
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
     * Getter for this CompoundComponent's internal display Pane
     * @return This CompoundComponent's internal display Pane
     */
    public Pane getInternalDisplayPane() {
        return internalDisplayPane;
    }

    @Override
    public void update() {

    }

    @Override
    public String toString() {
        return "Compound " + super.toString() + " with " + getNumInputs() + " ins, "
                + getNumOutputs() + " outs";
    }
}