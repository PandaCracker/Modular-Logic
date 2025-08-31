package base.fundamentals;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    private final HashSet<Component> selected;

    /**
     * Creates a new SelectionArea with an anchored corner
     * @param anchorX The x coordinate of the anchor
     * @param anchorY The y coordinate of the anchor
     */
    public SelectionArea(double anchorX, double anchorY) {
        this.rect = new Rectangle(0, 0, selectionColor);
        rect.setId("selection");

        rect.setX(anchorX);
        rect.setY(anchorY);

        this.anchor = new double[2];
        anchor[0] = anchorX;
        anchor[1] = anchorY;

        this.selected = new HashSet<>();
    }

    /**
     * Getter for this SelectionArea's Rectangle
     * @return This SelectionArea's Rectangle
     */
    public Rectangle getRect() {
        return rect;
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
