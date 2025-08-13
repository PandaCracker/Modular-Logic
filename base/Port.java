package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents one I/O point on a Component
 *
 * @author Lucas Peterson
 */
public class Port {
    /** Handles the two types of Ports, INPUT and OUTPUT */
    public enum PortType {INPUT, OUTPUT}

    /** The radius of every Port, in pixels */
    public double RADIUS = Simulation.CELL_SIZE / 8;
    /** The Color of every Port */
    private final Color COLOR = Color.SLATEGRAY;

    /** The Circle which represents the Port on the Simulation Board */
    private final Circle circle;

    /** The Component this Port is a part of */
    private final Component parent;
    /** Which type of Port this is */
    private final PortType type;
    /** The Connection, if any, this Port has */
    private Connection connection;

    /** How far, in pixels, the center of the Port is from the top right corner of its Parent in the X direction */
    private final double centerXOffsetFromParent;
    /** How far, in pixels, the center of the Port is from the top right corner of its Parent in the Y direction */
    private final double centerYOffsetFromParent;

    /** Whether this Port is carrying a signal */
    private boolean on;

    /**
     * Creates a new Port
     * @param parent The Component this Port is a part of
     * @param type The type of Port this is, either <i>PortType.INPUT</i> or <i>PortType.OUTPUT</i>
     * @param portNum The (zero-indexed) port number this port will be
     */
    public Port(Component parent, PortType type, int portNum) {
        // TODO Some sort of system ensuring that portNums aren't reused/actually exist on the parent Component
        this.parent = parent;
        this.type = type;
        this.connection = null;

        // Get the constant offsets from the parent's corners
        int numPorts = type == PortType.INPUT ? parent.getNumInputs() : parent.getNumOutputs();
        // How far down this port should be on the parent. 0.33 means a third of the way down
        double percentDownOnComponent = (double) (portNum + 1) / (numPorts + 1);
        this.centerYOffsetFromParent = parent.getRect().getHeight() * percentDownOnComponent;

        this.centerXOffsetFromParent = type == PortType.INPUT ? 0 : (int) parent.getRect().getWidth();

        double centerY = parent.getRect().getY() + centerYOffsetFromParent;
        double centerX = parent.getRect().getX() + centerXOffsetFromParent;
        this.circle = new Circle(centerX, centerY, RADIUS, COLOR);

        this.on = false;
    }

    /**
     * Get the Circle which represents this Port
     * @return This Port's Circle
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * Get the Connection this Port is an endpoint of (if any)
     * @return The Connection, or null if this Port is not connected
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get whether this Port is connected to another Port
     * @return Whether this Port is connected to another Port
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Get whether this Port is carrying a signal
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Set the state of this Port to be on (true) or off (false)
     *
     * @param state The desired state of this Port
     */
    public void setState(boolean state) {
        on = state;
        if (isConnected() && type == PortType.OUTPUT) {
            connection.updateState();
        }
    }

    // TODO clean up this connection mess. No wacky functions,
    //  and make sure the type of port matches the role its supposed to play. Enforce w/ separate classes?

    /**
     * Helper function (read: hack) for the connection process between two Ports
     * <p>
     * Actually creates the Connection object and ensures both Ports recognize the new Connection
     * @param source The Port on the source end of this Connection
     * @return The Connection created
     */
    private Connection _connectTo(Port source) {
        connection = new Connection(source, this);
        return connection;
    }

    /**
     * Connect this Port to another Port
     * The Port being called with will be the source
     * @param other The input Port which will receive signals from this Port
     */
    public void connectTo(Port other) {
        connection = other._connectTo(this);
    }

    /**
     * Update the position of this Port's Circle to match any movements made by the parent Component's Rectangle. <br>
     * Should be called whenever the parent Component moves
     */
    public void updatePosition() {
        circle.setCenterX(parent.getRect().getX() + centerXOffsetFromParent);
        circle.setCenterY(parent.getRect().getY() + centerYOffsetFromParent);
        if (isConnected()) {
            connection.updatePosition();
        }
    }
}
