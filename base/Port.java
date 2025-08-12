package base;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Port {
    public enum PortType {INPUT, OUTPUT}

    private final Circle circle;
    public double RADIUS = Simulation.CELL_SIZE / 2;

    private final Color COLOR = Color.SLATEGRAY;

    private final Component parent;
    private final PortType type;
    private Connection connection;

    private boolean on;

    public Port(Component parent, PortType type, int portNum) {
        this.parent = parent;
        this.type = type;

        this.connection = null;

        int numPorts = type == PortType.INPUT ? parent.getNumInputs() : parent.getNumOutputs();
        double percentDownOnComponent = (double) (portNum + 1) / (numPorts + 1);
        double centerYOffset = (int) (parent.getRect().getHeight() * percentDownOnComponent);
        double centerY = parent.getRect().getY() + centerYOffset;

        double centerXOffset = type == PortType.INPUT ? 0 : (int) parent.getRect().getWidth();
        double centerX = parent.getRect().getX() + centerXOffset;

        this.circle = new Circle(centerX, centerY, RADIUS, COLOR);
    }

    public Circle getCircle() { return circle; }
    public Connection getConnection() { return connection; }
    public boolean isOn() { return connection.isOn(); }
    public boolean isConnected() { return !(connection == null); }

    public void connectTo(Port other) {
        connection = new Connection(this, other);
    }

    public void setState(boolean state) {
        connection.setState(state);
    }

}
