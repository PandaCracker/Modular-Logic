package base;

import base.events.AddChildEvent;
import base.events.DeleteChildEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
    private final static double LINE_WIDTH = 3.0;

    /** The Port the signal comes from */
    private Port sourcePort;
    /** The Port the signal is going to */
    private Port destPort;

    /** Whether this Connection has both a source Port and dest Port or just an anchor Port*/
    private boolean complete;
    /** The Port this Connection's Line is anchored to when the Connection is incomplete */
    private Port anchorPort;

    /** The Line which represents the Connection */
    private final Line line;

     /** Whether this Connection is carrying a signal */
    private boolean on;

    /**
     * Master Constructor for a new Connection
     * @param sourcePort The output Port signals will come from
     * @param destPort The input Port signals will go to
     * @param anchorPort The Port this Connection is anchored to when being dragged around
     */
    private Connection(Port sourcePort, Port destPort, Port anchorPort) {

        double[] lineStart;
        double[] lineEnd;
        Port existantPort; //< Port I know exists so I can use it to reference the display pane w/ circ.getParent()

        if (anchorPort != null) {
            Circle anchorCirc = anchorPort.getCircle();
            lineStart = lineEnd = new double[] {anchorCirc.getCenterX(), anchorCirc.getCenterY()};

            if (anchorPort.isInput()) {
                this.destPort = anchorPort;
            } else {
                this.sourcePort = anchorPort;
            }

            this.anchorPort = existantPort = anchorPort;

            this.complete = false;
        } else {
            Circle srcCirc = sourcePort.getCircle();
            lineStart = new double[] {srcCirc.getCenterX(), srcCirc.getCenterY()};

            Circle dstCirc = destPort.getCircle();
            lineEnd = new double[] {dstCirc.getCenterX(), dstCirc.getCenterY()};

            this.sourcePort = existantPort = sourcePort;
            this.destPort = destPort;

            this.complete = true;
        }

        this.line = new Line(lineStart[0], lineStart[1], lineEnd[0], lineEnd[1]);

        line.setMouseTransparent(!complete);

        line.setStrokeWidth(LINE_WIDTH);
        line.setId(String.valueOf(ID_COUNTER));
        ID_COUNTER--;

        // When making initial pieces none of the Shapes have a Parent
        EventTarget primeTarget = existantPort.getCircle().getParent();
        EventTarget actualTarget = primeTarget == null ? Event.NULL_SOURCE_TARGET : primeTarget;
        Event.fireEvent(actualTarget, new AddChildEvent(line));

        this.on = false;
    }

    /**
     * Create a new Connection between two Ports
     * @param sourcePort The output Port the signal will come from
     * @param destPort The input Port the signal will go to
     */
    public Connection(Port sourcePort, Port destPort) {
        this(sourcePort, destPort, null);
    }

    /**
     * Create a new Connection with only one side connected to a Port
     * <br>Used for dragging a new Connection between Ports
     * @param anchorPort The Port which is the known anchor for this Connection
     */
    public Connection(Port anchorPort) {
        this(null, null, anchorPort);
    }

    /**
     * Get the Line which represents the Connection
     * @return The Connection's Line
     */
    public Line getLine() {
        return line;
    }

    /**
     * Get the source Port for this Connection
     * @return This Connection's source Port
     */
    public Port getSourcePort() {
        return sourcePort;
    }

    /**
     * Get the destination Port for this Connection
     * @return This Connection's destination Port
     */
    public Port getDestPort() {
        return destPort;
    }

    /**
     * Get whether the Connection is on
     * @return Whether the Connection is on
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Get whether the Connection is complete, i.e. it has both a source and a dest Port.
     * @return Whether this Connection is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Remove this Connection from the display and use by its Ports
     */
    public void remove() {
        // Remove the connection from the main display Pane
        Event.fireEvent(line.getParent(), new DeleteChildEvent(line));
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
            System.out.println("Connection Registered!");
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
