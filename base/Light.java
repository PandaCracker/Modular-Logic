package base;

import javafx.scene.paint.Color;

/**
 * A Component which lights up when given a signal
 *
 * @author Lucas Peterson
 */
public class Light extends Component {

    /** The width of a Light in cells */
    private final static int WIDTH = 1;
    /** The height of a Light in cells */
    private final static int HEIGHT = 1;
    /** The Color of an active Light */
    private final static Color ON_COLOR = Color.YELLOW;
    /** The Color of an inactive Light */
    private final static Color OFF_COLOR = Color.BLACK;

    /**
     * Create a new Light Component
     * @param x The x coordinate (in cells) of the new Light
     * @param y The y coordinate (in cells) of the new light
     */
    public Light(int x, int y) {
        super(x, y, WIDTH, HEIGHT, OFF_COLOR, 1, 0);
    }

    @Override
    public void update() {
        boolean on = getInputPort(0).isOn();
        getRect().setFill(on ? ON_COLOR : OFF_COLOR);
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
