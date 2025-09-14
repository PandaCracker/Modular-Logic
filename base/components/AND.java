package base.components;

import base.fundamentals.Component;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * A component which outputs the logical and of two inputs
 *
 * @author Lucas Peterson
 */
public class AND extends Component {

    /** The width of an AND Component in pixels */
    private final static double WIDTH = 90;
    /** The height of an AND Component in pixels */
    private final static double HEIGHT = 120;
    /** The color of every AND Component */
    private final static Color COLOR = Color.RED;
    /** The String displayed on every AND Component */
    private final static String TEXT = "AND";
    /** The Color of the Text displayed on AND Components */
    private final static Color TEXT_COLOR = Color.WHITE;

    /**
     * Create a new AND Component
     * @param x The x coordinate (in pixels) of the new AND
     * @param y The y coordinate (in pixels) of the new AND
     * @param displayPane The pane to display this AND on
     */
    public AND(double x, double y, Pane displayPane) {
        super(x, y, WIDTH, HEIGHT, COLOR, 2, 1, TEXT, TEXT_COLOR, displayPane);
    }

    /**
     * Create a new AND at the top right corner of the main screen
     */
    public AND() {
        this(60, 60, null);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        boolean in2 = getInputPort(1).isOn();
        getOutputPort(0).setState(in1 && in2);
    }
}