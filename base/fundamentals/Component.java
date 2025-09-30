package base.fundamentals;

import base.components.CompoundComponent;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * Base class for anything on the Simulation board which can exist by itself
 *
 * @author Lucas Peterson
 */
public abstract class Component implements Comparable<Component>{
    /** How many pixels wide the rounded portion on the corner of Components is */
    private final static double ROUND_CORNER_WIDTH = 12;
    /** How many pixels tall the rounded portion on the corner of Components is */
    private final static double ROUND_CORNER_HEIGHT = 12;

    /** Default Component border Color */
    private final static Color BORDER_COLOR = Color.BLACK;
    /** Default border width on the display Rectangle */
    private final static double STROKE_WIDTH = 2.0;

    /** Font used by all text displayed on Components */
    private final static Font DEFAULT_FONT = Font.getDefault();
    /** Text field placed on the center of every Component */
    private final Text text;
    /** Distance between the center of the Text's LayoutBounds and its origin for the current Text*/
    private double halfTextWidth;

    /** Rectangle object which holds all display and location info */
    private final Rectangle rect;
    /** The Display Pane this Component lives on */
    private final DisplayPane parentPane;

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

    /** Whether this Component is a part of a multi-selection */
    private boolean selected;
    /** The SelectionArea this Component is being multi-selected with */
    private SelectionArea selector;
    /** Whether this Component should relay MouseEvents to other selected Components. Used to avoid feedback loops */
    private boolean canEcho;

    /**
     * Set up the basic fields of a new object extending from Component
     * @param x The x position (in pixels) of where the Component's top left corner will be
     * @param y The y position (in pixels) of where the Component's top left corner will be
     * @param width The width (in pixels) of the Component
     * @param height The height (in pixels) of the Component
     * @param color The Color of the Component
     * @param numInputs The number of input Ports the Component will have
     * @param numOutputs The number of output Ports the Component will have
     * @param defaultText The default text String to display on the Component
     * @param defaultTextColor The default Color of the Component's text
     * @param displayPane The Pane on which to add the Component
     */
    public Component(double x, double y, double width, double height, Paint color, int numInputs, int numOutputs,
                     String defaultText, Color defaultTextColor, DisplayPane displayPane) {
        // Set up basic Rectangle fields
        this.rect = new Rectangle(width, height);
        rect.setX(x);
        rect.setY(y);

        // Rectangle corners
        rect.setArcWidth(ROUND_CORNER_WIDTH);
        rect.setArcHeight(ROUND_CORNER_HEIGHT);

        // Rectangle color and borders
        rect.setFill(color);
        rect.setStroke(BORDER_COLOR);
        rect.setStrokeWidth(STROKE_WIDTH);

        // Save this Component object in the Rect for referencing on the Pane
        rect.setUserData(this);

        this.parentPane = displayPane;

        // Set up display text
        this.text = new Text();
        text.setFont(DEFAULT_FONT);
        text.setTextOrigin(VPos.CENTER);
        text.setMouseTransparent(true);

        setTextColor(defaultTextColor);
        setText(defaultText);
        centerAlignText();

        // Init dragging vars
        this.inDrag = false;
        this.dragOffsetX = 0;
        this.dragOffsetY = 0;

        // Init multiselect vars
        this.selected = false;
        this.selector = null;
        this.canEcho = true;

        displayPane.addChildren(rect, text);

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

        // Input Handlers
        // Delete on left click
        rect.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                remove();
                if (selected && canEcho) {
                    selector.echo(this, e);
                }
            }
        });

        // Save drag start pos on drag start
        rect.setOnDragDetected(e -> {
            inDrag = true;
            dragOffsetX = e.getX() - rect.getX();
            dragOffsetY = e.getY() - rect.getY();

            if (selected && canEcho) {
                selector.echo(this, e);
            }
        });

        // Move on mouse drag
        rect.setOnMouseDragged(e -> {
            if (inDrag) {
                move(e.getX() - dragOffsetX, e.getY() - dragOffsetY);
                if (selected && canEcho) {
                    selector.echo(this, e);
                }
            }
        });

        // Stop dragging when mouse released
        rect.setOnMouseReleased(e -> {
            if (inDrag) {
                move(e.getX() - dragOffsetX, e.getY() - dragOffsetY);
                inDrag = false;
                if (selected && canEcho) {
                    selector.echo(this, e);
                }
            }
        });
    }

    /**
     * Get the Rectangle object representing this Component
     * @return This Component's Rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Get the Display Pane this Component lives on
     * @return The Display Pane this Component lives on
     */
    public DisplayPane getParentPane() {
        return parentPane;
    }

    /**
     * Get the Text associated with this Component
     * @return The Text associated with this Component
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
     * Set the Component's Text Color
     * @param textColor The Color to change the Text Color to
     */
    public void setTextColor(Color textColor) {
        text.setFill(textColor);
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
     * Center-align the Text displayed on this Component
     */
    private void centerAlignText() {
        text.setX(rect.getLayoutBounds().getCenterX() - halfTextWidth);
        text.setY(rect.getLayoutBounds().getCenterY());
    }

    /**
     * Move this Component to a specified position
     * @param x The x position (in pixels) to move this Component to
     * @param y The y position (in pixels) to move this Component to
     */
    public void move(double x, double y) {
        // Calculate the boundaries for sensible movement (don't go past screen edges)
        Bounds bounds = rect.getParent().getLayoutBounds();

        double inPortAdjustment = Math.min(numInputs, 1) * Port.RADIUS;
        double minInBoundsX = bounds.getMinX() + inPortAdjustment;

        double outPortAdjustment = Math.min(numOutputs, 1) * Port.RADIUS;
        double maxInBoundsX = bounds.getMaxX() - rect.getWidth() - outPortAdjustment;

        double maxInBoundsY = bounds.getMaxY() - rect.getHeight();

        x = Math.max( Math.min(x, maxInBoundsX), minInBoundsX );
        y = Math.max( Math.min(y, maxInBoundsY), bounds.getMinY() );

        // Actually move the Component
        rect.setX(x);
        rect.setY(y);
        centerAlignText();
        Arrays.stream(getAllPorts()).forEach(Port::updatePosition);
    }

    /**
     * Remove this Component and all children (Ports, Connections) from the display and from any other connected
     * Components
     */
    public void remove() {
        if (rect.getParent() != null) {
            parentPane.removeChildren(rect, text);
            for (Port port : getAllPorts()) {
                port.remove();
            }
        }
    }

    /**
     * Set this Component to be able to echo MouseEvents to other selected Components
     */
    public void allowEcho() {
        canEcho = true;
    }

    /**
     * Stop this Component from echoing MouseEvents to other selected Components.
     * Should be called whenever receiving an echo to avoid feedback loops.
     */
    public void disallowEcho() {
        canEcho = false;
    }

    /**
     * Notify this Component it is a part of a multi-selection
     */
    public void select(SelectionArea selector) {
        if (!selected) {
            selected = true;
            this.selector = selector;
            rect.setStrokeWidth(rect.getStrokeWidth() * 2);
        }
    }

    /**
     * Notify this Component it is no longer part of a multi-selection
     */
    public void deselect() {
        selected = false;
        selector = null;
        rect.setStrokeWidth(STROKE_WIDTH);
    }

    /**
     * Creates a deep copy of this Component, adding it to the specified screen
     * @param displayPane The Pane to add this new Component to
     */
    public void copy(DisplayPane displayPane) {
        Rectangle rect = getRect();
        if (getClass() == CompoundComponent.class) {
            new CompoundComponent((CompoundComponent) this);
        } else {
            try {
                getClass().getDeclaredConstructor(Double.TYPE, Double.TYPE, DisplayPane.class)
                        .newInstance(rect.getX(), rect.getY(), displayPane);
            } catch (Exception e) {
                System.out.println("Exception during copying process: " + e.getClass() + " " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Method which is called once per logic cycle to progress a Component's state.
     * <br>
     * Should implement each Component's unique logical functionality
     */
    public abstract void update();

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
        return text.getText() + " component at " + rect.getX() + ", " + rect.getY();
    }

    /**
     * Gives a hashcode for this Component <br>
     * Hashcode is based on its Rectangle, so two components with the same Rectangle are considered equal.
     * @return A hashcode for this Component
     */
    @Override
    public int hashCode() {
        return (int) (rect.getX() * numInputs + rect.getY() * (17.4 + numOutputs));
    }

    /**
     * Defines a natural ordering of Components.
     * Components which are placed higher on the screen are defined as less than another Component.
     * A Component which is of equal y-value to another Component but lesser x-value is defined as less than.
     * Components which have exactly the same positioning are considered equal.
     * @param o the object to be compared.
     * @return A negative integer if the other Component is less than this Component, 0 if equal, and a positive
     *      integer otherwise
     */
    @Override
    public int compareTo(Component o) {
        Rectangle otherRect = o.getRect();
        int res = (int) (rect.getY() - otherRect.getY());
        if (res == 0) {
            res = (int) (rect.getX() - otherRect.getX());
        }
        return res;
    }

    /**
     * Two Components are equal if they have the same underlying Rectangle
     * @param obj The object to compare
     * @return False unless obj is a Component with an equivalent Rectangle
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Component) {
            Rectangle otherRect = ((Component) obj).getRect();
            return rect.getX() == otherRect.getX() && rect.getY() == otherRect.getY();
        }
        return false;
    }
}
