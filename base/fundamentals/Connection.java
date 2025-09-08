package base.fundamentals;

import base.events.AddChildrenEvent;
import base.events.DeleteChildrenEvent;
import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * A basic representation of an object on the board which carries
 * a signal from one component output to another input
 * 
 * @author Lucas Peterson
 */
public class Connection {
    /** Counter used to give each Connection a unique ID. Starts negative to not interfere with Component/Port IDs */
    private static int ID_COUNTER = -1;

    /** The Color of a Connection Line when it is on */
    private final static Color ON_COLOR = Color.GREEN;
    /** The Color of a Connection Line when it is off */
    private final static Color OFF_COLOR = Color.BLACK;
    /** The width of a Connection Line in Pixels */
    private final static double LINE_WIDTH = 5.0;
    /** The end cap style of a Connection Line */
    private final static StrokeLineCap LINE_CAP = StrokeLineCap.ROUND;

    /** The Port the signal comes from */
    private Port sourcePort;
    /** The Port the signal is going to */
    private Port destPort;
    /** The Port this Connection's Line is anchored to when the Connection is incomplete */
    private Port anchorPort;

    /** Whether this Connection has both a source Port and dest Port or just an anchor Port*/
    private boolean complete;

    /** The Line which represents the Connection */
    private final Line line;

     /** Whether this Connection is carrying a signal */
    private boolean on;

    /**
     * Creates a new Connection
     * @param anchorPort The Port this Connection is anchored to when being dragged around
     */
    public Connection(Port anchorPort) {

        this.anchorPort = anchorPort;
        this.complete = false;
        this.on = false;

        if (anchorPort.isInput()) {
            this.destPort = anchorPort;
        } else {
            this.sourcePort = anchorPort;
        }

        Circle anchorCirc = anchorPort.getCircle();
        double[] anchorCoords = new double[] {anchorCirc.getCenterX(), anchorCirc.getCenterY()};
        this.line = new Line(anchorCoords[0], anchorCoords[1], anchorCoords[0], anchorCoords[1]);

        line.setMouseTransparent(!complete);
        line.setUserData(this);

        line.setStrokeWidth(LINE_WIDTH);
        line.setStrokeLineCap(LINE_CAP);

        line.setId(String.valueOf(ID_COUNTER));
        ID_COUNTER--;

        // Input Handlers
        // Left-click to remove a connection
        line.setOnMouseClicked(me -> {
            if(me.getButton() == MouseButton.SECONDARY) {
                remove();
            }
        });

        Event.fireEvent(anchorPort.getCircle().getParent(), new AddChildrenEvent(line));
    }

    /**
     * Get whether the Connection is complete, i.e. it has both a source and a dest Port.
     * @return Whether this Connection is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Get the Port this Connection is receiving signals from
     * @return The Port this Connection is receiving signals from
     */
    public Port getSourcePort() {
        return sourcePort;
    }

    /**
     * Get the Port this Connection is sending signals to
     * @return The Port this Connection is sending signals to
     */
    public Port getDestPort() {
        return destPort;
    }

    /**
     * Remove this Connection from the display and use by its Ports
     */
    public void remove() {
        // Remove the connection from the main display Pane
        Event.fireEvent(line.getParent(), new DeleteChildrenEvent(line));
        if(sourcePort != null) {sourcePort.deregisterConnection();}
        if(destPort != null) {destPort.deregisterConnection();}
    }

    /**
     * Update the state of this Connection to match the source Port
     */
    public void updateState() {
        if (isComplete()) {
            on = sourcePort.isOn();
            line.setStroke(on ? ON_COLOR : OFF_COLOR);
            destPort.setState(on);
        }
    }

    /**
     * Update the position of the Connection based on a Mouse Drag
     * @param me The MouseEvent representing the MouseMoved event
     */
    public void updateDrag(MouseEvent me) {
        if (!isComplete()) {
            if (anchorPort.isInput()) {
                line.setStartX(me.getX());
                line.setStartY(me.getY());
            } else {
                line.setEndX(me.getX());
                line.setEndY(me.getY());
            }
        }
    }

    /**
     * Add a Port to this Connection to complete it
     * <br>
     * This call will be ignored if newPort is of the same PortType as the currently connected Port,
     * or if this Connection already has two Ports connected
     * @param newPort The Port being added
     */
    public void registerPort(Port newPort) {
        if (!isComplete()) {
            if (anchorPort.isInput() && newPort.isOutput() || anchorPort.isOutput() && newPort.isInput()) {
                if (newPort.isInput()) {
                    this.destPort = newPort;
                    line.setEndX(destPort.getCircle().getCenterX());
                    line.setEndY(destPort.getCircle().getCenterY());
                } else {
                    this.sourcePort = newPort;
                    line.setStartX(sourcePort.getCircle().getCenterX());
                    line.setStartY(sourcePort.getCircle().getCenterY());
                }
            }
            complete = true;
            anchorPort = null;
            line.setMouseTransparent(false);
        }
    }

    /**
     * Update the position of the Connection's Line based on the current Port positions <br>
     * Should be called every time a Port gets moved
     */
    public void updatePosition() {
        if (isComplete()) {
            line.setStartX(sourcePort.getCircle().getCenterX());
            line.setStartY(sourcePort.getCircle().getCenterY());
            line.setEndX(destPort.getCircle().getCenterX());
            line.setEndY(destPort.getCircle().getCenterY());
        }
    }
}
