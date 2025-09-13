package base.components;

import base.Simulation;
import base.fundamentals.Component;
import javafx.scene.layout.Pane;
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
    private final static Color COLOR = Color.DARKGRAY;

    /**
     * Create a new Splitter
     * @param x The x coordinate (in cells) of the new Splitter
     * @param y The y coordinate (in cells) of the new Splitter
     * @param displayPane The Pane to display this Splitter on
     */
    public Splitter(double x, double y, Pane displayPane) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 2, null, null, displayPane);
    }

    /**
     * Create a new Splitter at the top right of the main screen
     */
    public Splitter() {
        this(1, 1, Simulation.MAIN_PANE);
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
