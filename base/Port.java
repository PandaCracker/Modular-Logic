package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Port {
    public enum PortType {INPUT, OUTPUT;}

    public double RADIUS = Simulation.CELL_SIZE / 4;
    private final Color COLOR = Color.SLATEGRAY;

    private final Circle circle;

    private final Component parent;
    private Connection connection;

    private final double centerYOffsetFromParent;
    private final double centerXOffsetFromParent;

    public Port(Component parent, PortType type, int portNum) {
        this.parent = parent;

        this.connection = null;

        int numPorts = type == PortType.INPUT ? parent.getNumInputs() : parent.getNumOutputs();
        double percentDownOnComponent = (double) (portNum + 1) / (numPorts + 1);
        this.centerYOffsetFromParent = parent.getRect().getHeight() * percentDownOnComponent;
        double centerY = parent.getRect().getY() + centerYOffsetFromParent;

        this.centerXOffsetFromParent = type == PortType.INPUT ? 0 : (int) parent.getRect().getWidth();
        double centerX = parent.getRect().getX() + centerXOffsetFromParent;

        this.circle = new Circle(centerX, centerY, RADIUS, COLOR);
    }

    public Circle getCircle() { return circle; }
    public Connection getConnection() { return connection; }
    public boolean isConnected() { return !(connection == null); }

    private Connection _connectTo(Port source) {
        connection = new Connection(source, this);
        return connection;
    }
    public void connectTo(Port other) {
        connection = other._connectTo(this);
    }

    public void setState(boolean state) {
        connection.setState(state);
    }

    public void updatePosition() {
        circle.setCenterX(parent.getRect().getX() + centerXOffsetFromParent);
        circle.setCenterY(parent.getRect().getY() + centerYOffsetFromParent);
        if (isConnected()) {
            connection.updatePosition();
        }
    }

}
