package base.fundamentals;

import base.Simulation;
import base.events.DeleteChildrenEvent;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * Base class for anything on the Simulation board which can exist by itself
 *
 * @author Lucas Peterson
 */
public abstract class Component {
    /** Counter which gives each Component and/or Port a unique ID */
    private static int ID_COUNTER = 0;

    /** Font used by all text displayed on Components */
    private final static Font DEFAULT_FONT = Font.getDefault();
    /** Text field placed on the center of every Component */
    private final Text text;
    /** Distance between the center of the Text's LayoutBounds and its origin for the current Text*/
    private double halfTextWidth;

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
     * @param defaultText The text String to display on the Component
     * @param defaultTextColor The default Color of the Component's text
     */
    public Component(double x, double y, double width, double height, Color color, int numInputs, int numOutputs,
                     String defaultText, Color defaultTextColor) {
        // Set up basic Rectangle fields
        this.rect = new Rectangle(width * Simulation.CELL_SIZE, height * Simulation.CELL_SIZE);
        rect.setX(x * Simulation.CELL_SIZE);
        rect.setY(y * Simulation.CELL_SIZE);
        rect.setFill(color);

        rect.setId(String.valueOf(ID_COUNTER));
        ID_COUNTER++;

        // Set up I/O Ports
        this.numInputs = numInputs;
        this.inputPorts = new Port[numInputs];
        for (int i = 0; i < numInputs; i++, ID_COUNTER++) {
            inputPorts[i] = new Port(this, Port.PortType.INPUT, i, ID_COUNTER);
        }

        this.numOutputs = numOutputs;
        this.outputPorts = new Port[numOutputs];
        for (int i = 0; i < numOutputs; i++, ID_COUNTER++) {
            outputPorts[i] = new Port(this, Port.PortType.OUTPUT, i, ID_COUNTER);
        }

        // Set up display text
        this.text = new Text();
        text.setFont(DEFAULT_FONT);
        text.setTextOrigin(VPos.CENTER);
        text.setMouseTransparent(true);

        setTextColor(defaultTextColor);
        setText(defaultText);
        centerAlignText();

        // Movement Handlers
        rect.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                remove();
            }
        });

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
     * Get the Text object to display on this Component
     * @return The Text object displayed on this Component
     */
    public Text getText() {
        return text;
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
     * Set the Text displayed on this Component
     * @param newText The String to display
     */
    public void setText(String newText) {
        text.setText(newText);
        this.halfTextWidth = text.getLayoutBounds().getCenterX() - text.getX();
        centerAlignText();
    }

    /**
     * Set the Component's Text Color
     * @param textColor The Color to change the Text Color to
     */
    public void setTextColor(Color textColor) {
        text.setFill(textColor);
    }

    private void centerAlignText() {
        text.setX(rect.getLayoutBounds().getCenterX() - halfTextWidth);
        text.setY(rect.getLayoutBounds().getCenterY());
    }

    /**
     * Move this Component
     * @param sceneX The x position (in pixels) to move this Component to
     * @param sceneY The y position (in pixels) to move this Component to
     */
    public void move(double sceneX, double sceneY) {
        rect.setX(sceneX);
        rect.setY(sceneY);
        centerAlignText();
        Arrays.stream(getAllPorts()).forEach(Port::updatePosition);
    }

    /**
     * Remove this Component and all children (Ports, Connections) from the display and from any other connected
     * Components
     */
    public void remove() {
        Event.fireEvent(rect.getParent(), new DeleteChildrenEvent(rect, text));
        for (Port port : getAllPorts()) {
            port.remove();
        }
    }

    /**
     * Method which is called once per logic cycle to progress a Component's state.
     * <br>
     * Should implement each Component's unique logical functionality
     */
    public void update() {}

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
