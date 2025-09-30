package base.components;

import base.fundamentals.Component;
import base.fundamentals.DisplayPane;
import javafx.scene.paint.Color;

/**
 * Component which outputs the opposite of the input's current state
 */
public class NOT extends Component {

    /** The width of an NOT Component in pixels*/
    private final static double WIDTH = 60;
    /** The height of an NOT Component in pixels*/
    private final static double HEIGHT = 60;
    /** The color of every NOT Component */
    private final static Color COLOR = Color.RED;
    /** The String displayed on every OR Component */
    private final static String TEXT = "NOT";
    /** The Color of the Text displayed on OR Components */
    private final static Color TEXT_COLOR = Color.WHITE;

    /**
     * Create a new NOT Component
     * @param x The x coordinate (in pixels) of the NOT
     * @param y The y coordinate (in pixels) of the NOT
     * @param displayPane The pane to display this AND on
     */
    public NOT(double x, double y, DisplayPane displayPane) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 1, TEXT, TEXT_COLOR, displayPane);
    }

    @Override
    public void update() {
        getOutputPort(0).setState(!getInputPort(0).isOn());
    }
}
