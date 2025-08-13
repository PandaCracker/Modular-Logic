package base;

import javafx.scene.paint.Color;

/**
 * A Component which has no inputs and outputs only one constant signal
 *
 * @author Lucas Peterson
 */
public class SignalSource extends Component {

    /** The width of a Signal Source in cells */
    private final static int WIDTH = 1;
    /** The height of a Signal Source in cells */
    private final static int HEIGHT = 1;
    /** The color of every Signal Source */
    private final static Color COLOR = Color.BLACK;

    /**
     * Create a new Signal Source
     * @param x The x position (in cells) of the new Signal Source
     * @param y The y position (in cells) of the new Signal Source
     */
    public SignalSource(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 0, 1);
    }

    /**
     * Produces a String representation of this Signal Source
     * Gives a String of the form:
     * <p>
     *     Signal Source Component at [X], [Y]
     * </p>
     * Where X and Y are the pixel coordinates of this Signal Source
     * @return The String described above
     */
    @Override
    public String toString() {
        return "Signal Source " + super.toString();
    }
}
