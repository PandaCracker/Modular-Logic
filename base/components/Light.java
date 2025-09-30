package base.components;

import base.fundamentals.Component;
import base.fundamentals.DisplayPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * A Component which lights up when given a signal
 *
 * @author Lucas Peterson
 */
public class Light extends Component {

    /** The width of a Light in pixels */
    private final static double WIDTH = 60;
    /** The height of a Light in pixels */
    private final static double HEIGHT = 60;
    /** The Color of an active Light */
    private final static Color ON_COLOR = Color.YELLOW;
    /** The String displayed on an active Light */
    private final static String ON_TEXT = "O";
    /** The Color of the text on an active Light */
    private final static Color ON_TEXT_COLOR = Color.BLACK;
    /** The Color of an inactive Light */
    private final static Color OFF_COLOR = Color.GRAY;
    /** The String displayed on an inactive Light */
    private final static String OFF_TEXT = "-";
    /** The Color of the text on an inactive Light */
    private final static Color OFF_TEXT_COLOR = Color.WHITE;


    /**
     * Create a new Light Component
     * @param x The x coordinate (in pixels) of the new Light
     * @param y The y coordinate (in pixels) of the new light
     * @param displayPane The Pane on which to display the Light
     */
    public Light(double x, double y, DisplayPane displayPane) {
        super(x, y, WIDTH, HEIGHT, OFF_COLOR, 1, 0, OFF_TEXT, OFF_TEXT_COLOR, displayPane);
    }

    /**
     * Create a new Light at the top-right corner of the main screen
     */
    public Light() {
        this(60, 60, null);
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
