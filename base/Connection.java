package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * A basic representation of an object on the board which carries
 * a signal from one component output to another input
 * 
 * @author Lucas Peterson
 */
public class Connection {
    /** The Color of a Connection Line when it is on */
    private final static Color ON_COLOR = Color.GREEN;
    /** The Color of a Connection Line when it is off */
    private final static Color OFF_COLOR = Color.BLACK;
    private final static double LINE_WIDTH = 3.0;

    /** The Port the signal comes from */
    private final Port sourcePort;
    /** The Port the signal is going to */
    private final Port destPort;

    /** The Line which represents the Connection */
    private final Line line;

    /** Whether this Connection is carrying a signal */
    private boolean on;

    /**
     * Create a new Connection between two Ports
     * @param sourcePort The output Port the signal will come from
     * @param destPort The input Port the signal will go to
     */
    public Connection(Port sourcePort, Port destPort) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;

        Circle srcCirc = sourcePort.getCircle();
        Circle dstCirc = destPort.getCircle();

        this.line = new Line(srcCirc.getCenterX(), srcCirc.getCenterY(),
                dstCirc.getCenterX(), dstCirc.getCenterY());
        line.setStrokeWidth(LINE_WIDTH);

        this.on = false;
    }

    /**
     * Get the Line which represents the Connection
     * @return The Connection's Line
     */
    public Line getLine() {
        return line;
    }

    /**
     * Get whether the Connection is on
     * @return Whether the Connection is on
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Update the state of this Connection to match the source Port
     */
    public void updateState() {
        on = sourcePort.isOn();
        line.setStroke(on ? ON_COLOR : OFF_COLOR);
        destPort.setState(on);
    }

    /**
     * Update the position of the Connection's Line based on the current Port positions <br>
     * Should be called every time a Port gets moved
     */
    public void updatePosition() {
        line.setStartX(sourcePort.getCircle().getCenterX());
        line.setStartY(sourcePort.getCircle().getCenterY());
        line.setEndX(destPort.getCircle().getCenterX());
        line.setEndY(destPort.getCircle().getCenterY());
    }
}
