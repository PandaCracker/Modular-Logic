package base.components;

import base.fundamentals.Component;
import javafx.scene.paint.Color;

/**
 * A Component which copies whatever input it receives into two outputs
 *
 * @author Lucas Peterson
 */
public class Splitter extends Component {

    /** The width of a Splitter in cells */
    private final static double WIDTH = 1;
    /** The height of a Splitter in cells */
    private final static double HEIGHT = 1.5;
    /** The color of every Splitter */
    private final static Color COLOR = Color.BLACK;

    /**
     * Create a new Splitter
     * @param x The x coordinate (in cells) of the new Splitter
     * @param y The y coordinate (in cells) of the new Splitter
     */
    public Splitter(double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 2, null, null);
    }

    /**
     * Create a new Splitter at the top right of the display screen
     */
    public Splitter() {
        this(1, 1);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        getOutputPort(0).setState(in1);
        getOutputPort(1).setState(in1);
    }
}
