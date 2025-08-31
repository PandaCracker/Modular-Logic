package base.components;

import base.fundamentals.Component;
import javafx.scene.paint.Color;

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

    public NOT(double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 1, TEXT, TEXT_COLOR);
    }

    public NOT() {
        this(1, 1);
    }

    @Override
    public void update() {
        getOutputPort(0).setState(!getInputPort(0).isOn());
    }
}
