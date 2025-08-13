package base.components;

import base.fundamentals.Component;
import javafx.scene.paint.Color;

/**
 * A Component which lights up when given a signal
 *
 * @author Lucas Peterson
 */
public class Light extends Component {

    /** The width of a Light in cells */
    private final static double WIDTH = 1;
    /** The height of a Light in cells */
    private final static double HEIGHT = 1;
    /** The Color of an active Light */
    private final static Color ON_COLOR = Color.YELLOW;
    /** The String displayed on an active Light */
    private final static String ON_TEXT = "O";
    /** The Color of the text on an active Light */
    private final static Color ON_TEXT_COLOR = Color.BLACK;
    /** The Color of an inactive Light */
    private final static Color OFF_COLOR = Color.BLACK;
    /** The String displayed on an inactive Light */
    private final static String OFF_TEXT = "-";
    /** The Color of the text on an inactive Light */
    private final static Color OFF_TEXT_COLOR = Color.WHITE;


    /**
     * Create a new Light Component
     * @param x The x coordinate (in cells) of the new Light
     * @param y The y coordinate (in cells) of the new light
     */
    public Light(double x, double y) {
        super(x, y, WIDTH, HEIGHT, OFF_COLOR, 1, 0, OFF_TEXT, OFF_TEXT_COLOR);
    }

    @Override
    public void update() {
        boolean on = getInputPort(0).isOn();
        getRect().setFill(on ? ON_COLOR : OFF_COLOR);
        setText(on ? ON_TEXT : OFF_TEXT);
        setTextColor(on ? ON_TEXT_COLOR : OFF_TEXT_COLOR);
    }

    /**
     * Produces a String representation of this Light
     * Gives a String of the form:
     * <p>
     *     Light Component at [X], [Y]
     * </p>
     * Where X and Y are the pixel coordinates of this Light
     * @return The String described above
     */
    @Override
    public String toString() {
        return "Light " + super.toString();
    }
}
