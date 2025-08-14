package base.fundamentals;

import base.Simulation;
import base.events.AddChildrenEvent;
import base.events.DeleteChildrenEvent;
import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * Represents one I/O point on a Component
 *
 * @author Lucas Peterson
 */
public class Port {
    /** Handles the two types of Ports, INPUT and OUTPUT */
    public enum PortType {INPUT, OUTPUT}

    /** The radius of every Port, in pixels */
    public double RADIUS = Simulation.CELL_SIZE / 6;
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

    private boolean inDrag;

    /**
     * Creates a new Port
     * @param parent The Component this Port is a part of
     * @param type The type of Port this is, either <i>PortType.INPUT</i> or <i>PortType.OUTPUT</i>
     * @param portNum The (zero-indexed) port number this port will be
     * @param ID A unique identifier for this port
     */
    public Port(Component parent, PortType type, int portNum, int ID) {
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

        circle.setId(String.valueOf(ID));
        circle.setUserData(this);

        // Input Handlers
        // Remove Connections on left-click
        circle.setOnMousePressed(me -> {
            if (me.getButton() == MouseButton.SECONDARY) {
                removeConnection();
            }
        });
        // Drag out a new Connection
        circle.setOnDragDetected(me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                circle.startFullDrag();
                inDrag = true;
                removeConnection();
                connection = new Connection(this);
            }
        });
        // Remove a dragged Connection that did not end over another Port
        circle.setOnMouseReleased(me -> {
            if (me.getButton() == MouseButton.PRIMARY && inDrag) {
                inDrag = false;
                if (!connection.isComplete()) {
                    removeConnection();
                }
            }
        });
        // Move a dragged Connection to match the cursor
        circle.setOnMouseDragged(me -> {
            if (me.getButton() == MouseButton.PRIMARY && connection != null) {
                connection.updateDrag(me);
            }
        });
        // Attach a dragged Connection which was dropped over this Port
        circle.setOnMouseDragReleased(mde -> {
            if (connection == null) {
                Shape otherShape = (Shape) mde.getGestureSource();
                Port otherPort = (Port) otherShape.getUserData();
                connection = otherPort.getConnection();
                connection.registerPort(this);
            }
        });

        Event.fireEvent(parent.getRect().getParent(), new AddChildrenEvent(circle));

        this.on = false;
        this.inDrag = false;
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
     * Get whether this Port is an input Port
     * @return Whether this is an Input Port
     */
    public boolean isInput() {
        return type == PortType.INPUT;
    }

    /**
     * Get whether this Port is an output Port
     * @return Whether this is an output Port
     */
    public boolean isOutput() {
        return type == PortType.OUTPUT;
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

    /**
     * Register that this Port's Connection has been removed
     * Should only be called by the Connection's remove() method
     * @see Connection#remove
     */
    public void deregisterConnection() {
        on = false;
        connection = null;
    }

    /**
     * Remove the Connection attached to this Port from the board and all use
     */
    public void removeConnection() {
        if (connection != null) {
            connection.remove();
        }
    }

    /**
     * Remove this Port and its Connection from the display
     */
    public void remove() {
        Event.fireEvent(circle.getParent(), new DeleteChildrenEvent(circle));
        if (isConnected()) {
            removeConnection();
        }
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
