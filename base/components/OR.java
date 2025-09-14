package base.components;

import base.Simulation;
import base.fundamentals.Component;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * A Component which outputs the logical or of two inputs
 *
 * @author Lucas Peterson
 */
public class OR extends Component {

    /** The width of an OR Component in pixels */
    private final static double WIDTH = 90;
    /** The height of an OR Component in pixels */
    private final static double HEIGHT = 120;
    /** The color of every OR Component */
    private final static Color COLOR = Color.BLUE;
    /** The String displayed on every OR Component */
    private final static String TEXT = "OR";
    /** The Color of the Text displayed on OR Components */
    private final static Color TEXT_COLOR = Color.WHITE;

    /**
     * Create a new OR Component
     * @param x The x coordinate (in pixels) of the new OR
     * @param y The y coordinate (in pixels) of the new OR
     * @param displayPane The Pane to display the OR on
     */
    public OR(double x, double y, Pane displayPane) {
        super(x, y, WIDTH, HEIGHT, COLOR, 2, 1, TEXT, TEXT_COLOR, displayPane);
    }

    /**
     * Create a new OR at the top right of the main screen
     */
    public OR() {
        this(60,60, Simulation.MAIN_PANE);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        boolean in2 = getInputPort(1).isOn();
        getOutputPort(0).setState(in1 || in2);
    }
}
