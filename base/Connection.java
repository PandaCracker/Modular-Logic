package base;

import javafx.scene.canvas.GraphicsContext;
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

    private boolean on;

    public Connection(Port sourcePort, Port destPort) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;

        Circle srcCirc = sourcePort.getCircle();
        Circle dstCirc = destPort.getCircle();

        this.line = new Line(srcCirc.getCenterX(), srcCirc.getCenterY(),
                dstCirc.getCenterX(), dstCirc.getCenterY());

        this.on = false;
    }

    public Line getLine() {return line;}

    public boolean isOn() {return on;}

    public void setState(boolean state) {
        this.on = state;
    }
}
