package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;

/**
 * Base class for anything on the Simulation board which can exist by itself
 *
 * @author Lucas Peterson
 */
public abstract class Component {
    /** Rectangle object which holds all display and location info */
    private final Rectangle rect;

    /** Number of input Ports on this Component */
    private final int numInputs;
    /** Array of all input Ports on this Component */
    private final Port[] inputPorts;

    /** Number of output Ports on this Component */
    private final int numOutputs;
    /** Array of all output Ports on this Component */
    private final Port[] outputPorts;

    /** Whether this is currently being dragged around the screen */
    private boolean inDrag;
    /** How far away from the origin of this Component's rect the most recent drag originated from in the X direction */
    private double dragOffsetX;
    /** How far away from the origin of this Component's rect the most recent drag originated from in the Y direction */
    private double dragOffsetY;

    /**
     * Set up the basic fields of a new object extending from Component
     * @param x The x position (in cells) of where the Component's top left corner will be
     * @param y The y position (in cells) of where the Component's top left corner will be
     * @param width The width (in cells) of the Component
     * @param height The height (in cells) of the Component
     * @param color The Color of the Component
     * @param numInputs The number of input Ports the Component will have
     * @param numOutputs The number of output Ports the Component will have
     */
    public Component(int x, int y, int width, int height, Color color, int numInputs, int numOutputs) {
        // Set up basic Rectangle fields
        this.rect = new Rectangle(width * Simulation.CELL_SIZE, height * Simulation.CELL_SIZE);
        rect.setX(x * Simulation.CELL_SIZE);
        rect.setY(y * Simulation.CELL_SIZE);
        rect.setFill(color);

        // Movement Handlers
        rect.setOnDragDetected(e -> {
            inDrag = true;
            dragOffsetX = e.getX() - rect.getX();
            dragOffsetY = e.getY() - rect.getY();
        });

        rect.setOnMouseDragged(e -> {
            if (inDrag) {
                move(e.getX() - dragOffsetX, e.getY() - dragOffsetY);
            }
        });

        rect.setOnMouseReleased(e -> {
            if (inDrag) {
                move(e.getSceneX() - dragOffsetX, e.getSceneY() - dragOffsetY);
                inDrag = false;
            }
        });

        // Set up I/O Ports
        this.numInputs = numInputs;
        this.inputPorts = new Port[numInputs];
        for (int i = 0; i < numInputs; i++) {
            inputPorts[i] = new Port(this, Port.PortType.INPUT, i);
        }

        this.numOutputs = numOutputs;
        this.outputPorts = new Port[numOutputs];
        for (int i = 0; i < numOutputs; i++) {
            outputPorts[i] = new Port(this, Port.PortType.OUTPUT, i);
        }

        // Init dragging vars
        this.inDrag = false;
        this.dragOffsetX = 0;
        this.dragOffsetY = 0;
    }

    /**
     * Get the Rectangle object representing this Component
     * @return This Component's Rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Get the number of input Ports this Component has
     * @return The number of input Ports on this Component
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * Get an array of every input Port this Component has
     * @return An array of this Component's input Ports
     */
    public Port[] getInputPorts() {
        return inputPorts;
    }

    /**
     * Get a specific input Port
     * @param inPort The (zero-indexed) input Port number
     * @return The specified input Port
     */
    public Port getInputPort(int inPort) {
        return inputPorts[inPort];
    }

    /**
     * Get the number of output Ports this Component has
     * @return The number of output Ports on this Component
     */
    public int getNumOutputs() {
        return numOutputs;
    }

    /**
     * Get an array of every output Port this Component has
     * @return An array of this Component's output Ports
     */
    public Port[] getOutputPorts() {
        return outputPorts;
    }

    /**
     * Get a specific output Port
     * @param outPort The (zero-indexed) output Port number
     * @return The specified output Port
     */
    public Port getOutputPort(int outPort) {
        return outputPorts[outPort];
    }

    /**
     * Get an array of every Port on this Component
     * @return An array containing every input and output Port on this Component
     */
    public Port[] getAllPorts() {
        Port[] all =  Arrays.copyOf(inputPorts, numInputs + numOutputs);
        System.arraycopy(outputPorts, 0, all, numInputs, numOutputs);
        return all;
    }

    /**
     * Create a new Connection from one of this Component's Ports to another
     * @param outputPortNum The Port number the output signal will come from
     * @param destComponent The Component the signal is going to
     * @param destPortNum The Port number the output signal will go to on the destination Component
     */
    public void connect(int outputPortNum, Component destComponent, int destPortNum) {
        outputPorts[outputPortNum].connectTo(destComponent.getInputPort(destPortNum));
    }

    /**
     * Move this Component
     * @param sceneX The x position (in pixels) to move this Component to
     * @param sceneY The y position (in pixels) to move this Component to
     */
    public void move(double sceneX, double sceneY) {
        rect.setX(sceneX);
        rect.setY(sceneY);
        Arrays.stream(getAllPorts()).forEach(Port::updatePosition);
    }

    /**
     * Provides a baseline String representation of a Component.
     * <br><br>
     * Gives a string of the form:
     * <p><i>
     *     Component at [X], [Y]
     * </i></p>
     * Where X and Y are the Component's coordinates on the Simulation board
     * @return The string explained above
     */
    @Override
    public String toString() {
        return "Component at " + rect.getX() + ", " + rect.getY();
    }
}
