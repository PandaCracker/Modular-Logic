package base.components;

import base.fundamentals.Component;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * A Component which has no inputs and outputs only one constant signal
 *
 * @author Lucas Peterson
 */
public class SignalSource extends Component {

    /** The width of a Signal Source in cells */
    private final static double WIDTH = 1;
    /** The height of a Signal Source in cells */
    private final static double HEIGHT = 1;
    /** The color of an active Signal Source */
    private final static Color ON_COLOR = Color.LIGHTGREEN;
    /** The Sting displayed on an active Signal Source */
    private final static String ON_TEXT = "O";
    /** The Color of the text on an active Signal Source */
    private final static Color ON_TEXT_COLOR = Color.BLACK;
    /** The color of an inactive Signal Source */
    private final static Color OFF_COLOR = Color.DARKGRAY;
    /** The Sting displayed on an inactive Signal Source */
    private final static String OFF_TEXT = "-";
    /** The Color of the text on an inactive Signal Source */
    private final static Color OFF_TEXT_COLOR = Color.WHITE;

    /** Whether this Signal Source is outputting a signal or not */
    private boolean on;

    /**
     * Create a new Signal Source
     * @param x The x position (in cells) of the new Signal Source
     * @param y The y position (in cells) of the new Signal Source
     */
    public SignalSource(double x, double y) {
        super(x, y, WIDTH, HEIGHT, OFF_COLOR, 0, 1, OFF_TEXT, OFF_TEXT_COLOR);
        this.on = false;

        getRect().setOnMouseClicked(this::toggle);
    }

    /**
     * Create a new SignalSource at the top right of the display screen
     */
    public SignalSource() {
        this(1,1);
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
            setText(on ? ON_TEXT : OFF_TEXT);
            setTextColor(on ? ON_TEXT_COLOR : OFF_TEXT_COLOR);
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
