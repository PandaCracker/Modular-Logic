package base.fundamentals;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
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

    private List<Component> selected;

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

        this.selected = new ArrayList<>();
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
     */
    public void move(double x, double y) {
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

        selected =
    }

    /**
     * Moves the anchored corner to a specified position
     * @param anchorX The x coordinate of the anchored corner
     * @param anchorY The y coordinate of the anchored corner
     */
    public void moveAnchor(double anchorX, double anchorY) {
        anchor[0] = anchorX;
        anchor[1] = anchorY;
    }

    /**
     * Gets the list of Components currently being selected
     * @return A list of all (if any) Components that are in the SelectionArea
     */
    public List<Component> getSelected(List<Component> components) {
        Bounds selectionArea = getRect().getLayoutBounds();
        return components.stream()
                .filter(component -> selectionArea.contains(component.getRect().getLayoutBounds()))
                .toList();
    }

    /**
     * Reset the SelectionArea once the MouseDrag is complete
     */
    public void done() {
        rect.setHeight(0);
        rect.setWidth(0);
    }
}
