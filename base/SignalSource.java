package base;

import javafx.scene.input.MouseEvent;
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
    /** The color of an active Signal Source */
    private final static Color ON_COLOR = Color.LIGHTGREEN;
    /** The color of an inactive Signal Source */
    private final static Color OFF_COLOR = Color.BLACK;


    /** Whether this Signal Source is outputting a signal or not */
    private boolean on;

    /**
     * Create a new Signal Source
     * @param x The x position (in cells) of the new Signal Source
     * @param y The y position (in cells) of the new Signal Source
     */
    public SignalSource(int x, int y) {
        super(x, y, WIDTH, HEIGHT, OFF_COLOR, 0, 1);
        this.on = false;

        getRect().setOnMouseClicked(this::toggle);
    }

    /**
     * Get if this Signal Source is on
     * @return Whether this Signal Source is on
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Toggle the state of this Signal Source
     *
     * @param me The MouseEvent which triggered this toggle
     */
    public void toggle(MouseEvent me) {
        // Don't toggle when you're dragging
        if (me.isStillSincePress()) {
            on = !on;
            getRect().setFill(on ? ON_COLOR : OFF_COLOR);
        }
    }

    @Override
    public void update() {
        getOutputPort(0).setState(on);
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
