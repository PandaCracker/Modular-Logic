package base.components;

import base.fundamentals.Component;
import base.fundamentals.DisplayPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * A Component which copies whatever input it receives into two outputs
 *
 * @author Lucas Peterson
 */
public class Splitter extends Component {

    /** The width of a Splitter in pixels */
    private final static double WIDTH = 60;
    /** The height of a Splitter in pixels */
    private final static double HEIGHT = 90;
    /** The color of every Splitter */
    private final static Color COLOR = Color.DARKGRAY;

    /**
     * Create a new Splitter
     * @param x The x coordinate (in pixels) of the new Splitter
     * @param y The y coordinate (in pixels) of the new Splitter
     * @param displayPane The Pane to display this Splitter on
     */
    public Splitter(double x, double y, DisplayPane displayPane) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 2, null, null, displayPane);
    }

    /**
     * Create a new Splitter at the top right of the main screen
     */
    public Splitter() {
        this(60, 60, null);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        getOutputPort(0).setState(in1);
        getOutputPort(1).setState(in1);
    }

    @Override
    public String toString() {
        return "Splitter " + super.toString();
    }
}
