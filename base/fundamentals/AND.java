package base.fundamentals;

import base.Component;
import javafx.scene.paint.Color;

/**
 * A component which outputs the logical and of two inputs
 *
 * @author Lucas Peterson
 */
public class AND extends Component {

    /** The width of an AND Component */
    private final static int WIDTH = 1;
    /** The height of an AND Component */
    private final static int HEIGHT = 2;
    /** The color of every AND Component */
    private final static Color COLOR = Color.RED;

    /**
     * Create a new AND Component
     * @param x The x coordinate (in cells) of the new AND
     * @param y The y coordinate (in cells) of the new AND
     */
    public AND(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 2, 1);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        boolean in2 = getInputPort(1).isOn();
        getOutputPort(0).setState(in1 && in2);
    }

    /**
     * Produces a String representation of this AND
     * Gives a String of the form:
     * <p>
     *     AND Component at [X], [Y]
     * </p>
     * Where X and Y are the pixel coordinates of this AND
     * @return The String described above
     */
    @Override
    public String toString() {
        return "AND " + super.toString();
    }
}