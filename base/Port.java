package base;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Port {
    public enum PortType {INPUT, OUTPUT}
    public double RADIUS = Simulation.CELL_SIZE / 2;

    private final Color COLOR = Color.SLATEGRAY;

    private final Component parent;
    private final PortType type;
    private Connection connection;

    private final double centerYOffset;
    private double centerY;
    private final double centerXOffset;
    private double centerX;

    private boolean on;

    public Port(Component parent, PortType type, int portNum) {
        this.parent = parent;
        this.type = type;

        this.connection = null;

        int numPorts = type == PortType.INPUT ? parent.getNumInputs() : parent.getNumOutputs();
        double percentDownOnComponent = (double) (portNum + 1) / (numPorts + 1);
        this.centerYOffset = (int) (parent.getRect().getHeight() * percentDownOnComponent);
        this.centerY = parent.getRect().getY() + centerYOffset;

        this.centerXOffset = type == PortType.INPUT ? 0 : (int) parent.getRect().getWidth();
        this.centerX = parent.getRect().getX() + centerXOffset;
    }

    public double getCenterX() {return centerX;}
    public double getCenterY() {return centerY;}
    public boolean isOn() { return connection.isOn(); }

    public void connectTo(Port other) {
        connection = new Connection(this, other);
    }

    public void setState(boolean state) {
        connection.setState(state);
    }

    private void updateCoords() {
        centerY = parent.getRect().getY() + centerYOffset;
        centerX = parent.getRect().getX() + centerXOffset;
    }

    public void draw(GraphicsContext gc) {
        if (type == PortType.OUTPUT) {
            connection.draw(gc);
        }

        updateCoords();
        gc.setFill(COLOR);
        gc.fillOval(centerX - RADIUS / 2, centerY - RADIUS / 2, RADIUS, RADIUS);
    }
}
