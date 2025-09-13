package base.components;

import base.Simulation;
import base.fundamentals.Component;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Component which outputs the opposite of the input's current state
 */
public class NOT extends Component {

    /** The width of an NOT Component */
    private final static double WIDTH = 1;
    /** The height of an NOT Component */
    private final static double HEIGHT = 1;
    /** The color of every NOT Component */
    private final static Color COLOR = Color.RED;
    /** The String displayed on every OR Component */
    private final static String TEXT = "NOT";
    /** The Color of the Text displayed on OR Components */
    private final static Color TEXT_COLOR = Color.WHITE;

    /**
     * Create a new NOT Component
     * @param x The x coordinate (in cells) of the NOT
     * @param y The y coordinate (in cells) of the NOT
     * @param displayPane The pane to display this AND on
     */
    public NOT(double x, double y, Pane displayPane) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 1, TEXT, TEXT_COLOR, displayPane);
    }

    /**
     * Create a new NOT Component at the top right corner of the main screen
     */
    public NOT() {
        this(1, 1, Simulation.MAIN_PANE);
    }

    @Override
    public void update() {
        getOutputPort(0).setState(!getInputPort(0).isOn());
    }
}
