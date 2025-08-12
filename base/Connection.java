package base;

import javafx.scene.canvas.GraphicsContext;

/**
 * A basic representation of an object on the board which carries
 * a signal from one component output to another input
 * 
 * @author Lucas Peterson
 */
public class Connection {
    private final Port sourcePort;
    private final Port destPort;

    private boolean on;

    public Connection(Port sourcePort, Port destPort) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;

        this.on = false;
    }

    public boolean isOn() {return on;}

    public void setState(boolean state) {
        this.on = state;
    }

    public void draw(GraphicsContext gc) {
        gc.strokeLine(sourcePort.getCenterX(), sourcePort.getCenterY(),
                destPort.getCenterX(), destPort.getCenterY());
    }
}
