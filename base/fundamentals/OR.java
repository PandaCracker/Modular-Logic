package base.fundamentals;

import base.Component;
import javafx.scene.paint.Color;

/**
 * A Component which outputs the logical or of two inputs
 *
 * @author Lucas Peterson
 */
public class OR extends Component {

    /** The width of an OR Component */
    private final static int WIDTH = 1;
    /** The height of an OR Component */
    private final static int HEIGHT = 2;
    /** The color of every OR Component */
    private final static Color COLOR = Color.BLUE;

    /**
     * Create a new OR Component
     * @param x The x coordinate (in cells) of the new OR
     * @param y The y coordinate (in cells) of the new OR
     */
    public OR(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 2, 1);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        boolean in2 = getInputPort(1).isOn();
        getOutputPort(0).setState(in1 || in2);
    }

    /**
     * Produces a String representation of this OR
     * Gives a String of the form:
     * <p>
     *     OR Component at [X], [Y]
     * </p>
     * Where X and Y are the pixel coordinates of this OR
     * @return The String described above
     */
    @Override
    public String toString() {
        return "OR " + super.toString();
    }
}
