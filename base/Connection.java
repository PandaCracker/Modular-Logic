package base;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * A basic representation of an object on the board which carries
 * a signal from one component output to another input
 * 
 * @author Lucas Peterson
 */
public class Connection {
    /** The Port the signal comes from */
    private final Port sourcePort;
    /** The Port the signal is going to */
    private final Port destPort;

    /** The Line which represents the Connection */
    private final Line line;

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
    }

    /**
     * Get the Line which represents the Connection
     * @return The Connection's Line
     */
    public Line getLine() {
        return line;
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
