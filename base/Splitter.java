package base;

import javafx.scene.paint.Color;

/**
 * A Component which copies whatever input it receives into two outputs
 *
 * @author Lucas Peterson
 */
public class Splitter extends Component {

    /** The width of a Splitter in cells */
    private final static int WIDTH = 1;
    /** The height of a Splitter in cells */
    private final static int HEIGHT = 1;
    /*8 The color of every Splitter */
    private final static Color COLOR = Color.BLACK;

    /**
     * Create a new Splitter
     * @param x The x coordinate (in cells) of the new Splitter
     * @param y The y coordinate (in cells) of the new Splitter
     */
    public Splitter(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 2);
    }

    @Override
    public void update() {
        boolean in1 = getInputPort(0).isOn();
        getOutputPort(0).setState(in1);
        getOutputPort(1).setState(in1);
    }
}
