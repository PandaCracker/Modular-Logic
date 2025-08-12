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
    private final Port sourcePort;
    private final Port destPort;

    private final Line line;

    public Connection(Port sourcePort, Port destPort) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;

        Circle srcCirc = sourcePort.getCircle();
        Circle dstCirc = destPort.getCircle();

        this.line = new Line(srcCirc.getCenterX(), srcCirc.getCenterY(),
                dstCirc.getCenterX(), dstCirc.getCenterY());
    }

    public Line getLine() {return line;}

    public void updatePosition() {
        line.setStartX(sourcePort.getCircle().getCenterX());
        line.setStartY(sourcePort.getCircle().getCenterY());
        line.setEndX(destPort.getCircle().getCenterX());
        line.setEndY(destPort.getCircle().getCenterY());
    }
}
