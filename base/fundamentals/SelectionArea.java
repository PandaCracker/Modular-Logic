package base.fundamentals;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.TreeSet;

/**
 * An area dragged out which selects some number of screen elements.<br>
 * The same SelectionArea object is intended to be used and reset multiple times
 *
 * @author Lucas Peterson
 */
public class SelectionArea {
    public final static Color selectionColor = new Color(0.4, 0.6, 0.9, 0.3);

    /** The Rectangle which is represents this SelectionArea on screen */
    private final Rectangle rect;

    /** The coordinates of the anchored corner, where the click-drag started */
    private final double[] anchor;

    /**
     * An ordered set of all selected Components. Order is top to bottom on the screen,
     * w/ ties broken going left to right
     */
    private final TreeSet<Component> selected;

    /**
     * Creates a new SelectionArea
     */
    public SelectionArea() {
        this.rect = new Rectangle(0, 0, selectionColor);
        rect.setId("selection");

        rect.setX(0);
        rect.setY(0);

        this.anchor = new double[2];
        anchor[0] = 0;
        anchor[1] = 0;

        this.selected = new TreeSet<>();
    }

    /**
     * Getter for this SelectionArea's Rectangle
     * @return This SelectionArea's Rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Getter for this SelectionArea's set of Components which are currently selected
     * @return The set of all Components being selected by this area
     */
    public TreeSet<Component> getSelected() {
        return selected;
    }

    /**
     * Get the required number of inputs and outputs for the selected Components to fully function
     * <br>
     * The number of inputs is the number of non-connected input ports plus the number of signal sources. <br>
     * The number of outputs is the number of non-connected output ports. <br>
     * "Non-connected" here means any connection whose endpoint is either non-existent or on a Component not
     * currently selected.
     * @return An integer array of length two, [numInputs, numOutputs]
     */
    public int[] getSelectedDependencies() {
        int numInputs = 0, numOutputs = 0;
        for (Component component : selected) {
            for (Port port : component.getAllPorts()) {
                Component connectedComponent = port.getConnectedComponent();
                boolean portNotConnected = connectedComponent == null;
                boolean portConnectedOutsideSelection = !portNotConnected && !selected.contains(connectedComponent);
                if (portNotConnected || portConnectedOutsideSelection) {
                    if (port.isInput()) {
                        numInputs++;
                    } else {
                        numOutputs++;
                    }
                }
            }
        }
        return new int[] {numInputs, numOutputs};
    }

    /**
     * Moves the free corner to a specified position
     * @param x The x coordinate of the free corner
     * @param y The y coordinate of the free corner
     * @param components The list of all Components that may be newly selected.
     */
    public void expandSelection(double x, double y, List<Component> components) {
        double width = x - anchor[0];
        if (width < 0) {
            rect.setX(anchor[0] + width);
        } else {
            rect.setX(anchor[0]);
        }

        double height = y - anchor[1];
        if (height < 0) {
            rect.setY(anchor[1] + height);
        } else {
            rect.setY(anchor[1]);
        }

        rect.setWidth(Math.abs(width));
        rect.setHeight(Math.abs(height));

        Bounds selectionRange = rect.getLayoutBounds();

        for (Component component : components) {
            Bounds rectBounds = component.getRect().getLayoutBounds();
            boolean centerInSelectionRange = selectionRange.contains(rectBounds.getCenterX(), rectBounds.getCenterY());
            if (!selected.contains(component) && centerInSelectionRange) {
                selected.add(component);
                component.select(this);
            } else if (!centerInSelectionRange){
                // Innate check only tries to remove if present
                selected.remove(component);
                component.deselect();
            }
        }
    }

    /**
     * Moves the anchored corner to a specified position and resets state
     * @param anchorX The x coordinate of the anchored corner
     * @param anchorY The y coordinate of the anchored corner
     */
    public void startNew(double anchorX, double anchorY) {
        selected.forEach(Component::deselect);
        selected.clear();
        anchor[0] = anchorX;
        anchor[1] = anchorY;
    }

    /**
     * Echo a MouseEvent from one source across all selected Components
     * @param source The selected Component the MouseEvent came from
     * @param me The MouseEvent to echo
     */
    public void echo(Component source, MouseEvent me) {
        // Echoing a remove call modifies the selected list when the event is fired, so need to iterate from a copy
        List<Component> staticCopy = List.copyOf(selected);
        for (Component component : staticCopy) {
            if (component != source) {
                component.disallowEcho();
                MouseEvent.fireEvent(component.getRect(), me);
                component.allowEcho();
            }
        }
    }

    /**
     * Clear the SelectionArea from the screen once the MouseDrag is complete
     */
    public void done() {
        rect.setHeight(0);
        rect.setWidth(0);
    }
}
