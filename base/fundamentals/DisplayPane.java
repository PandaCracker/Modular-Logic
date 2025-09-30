package base.fundamentals;

import base.Simulation;
import base.Utils;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class DisplayPane {
    private final Pane pane;
    private final String name;
    private final SelectionArea selection;
    private boolean selecting;
    private final List<Node> children;

    public DisplayPane(String name) {
        this.pane = new Pane();
        this.name = name;
        this.selecting = false;
        this.selection = new SelectionArea();

        this.children = pane.getChildren();

        pane.setUserData(this);
        pane.setPrefWidth(Simulation.INIT_BOARD_WIDTH);
        pane.setPrefHeight(Simulation.INIT_BOARD_HEIGHT);

        addEventHandling();
    }

    public void addEventHandling() {
        // Multi-selection handling
        pane.setOnDragDetected(e -> {
            // Know a multi-select is happening
            if (selecting) {
                children.add(selection.getRect());
            }
        });

        pane.setOnMousePressed(e -> {
            // Possible multi-select start, more accurate than waiting for drag detection
            if (getClickedOn(e) == null && e.getButton() == MouseButton.PRIMARY) {
                selecting = true;
                selection.startNew(e.getX(), e.getY());
            }
        });

        pane.setOnMouseDragged(e -> {
            // Expand Selection Area
            if (selecting) {
                List<Component> components = Utils.componentsFromChildren(children);
                selection.expandSelection(e.getX(), e.getY(), components);
            }
        });

        pane.setOnMouseReleased(e -> {
            // Done selecting more elements, remove selection rect
            if (selecting) {
                children.remove(selection.getRect());
                selection.doneSelecting();
                selecting = false;
            }
        });
    }

    /**
     * Retrieve this Display Pane's list of children
     * @return This Display Pane's list of children
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Retrieve this Display Pane's Pane object
     * @return This Display Pane's Pane object
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * Retrieve this Display Pane's name
     * @return This Display Pane's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve this Display Pane's Selection Area object
     * @return This Display Pane's Selection Area
     */
    public SelectionArea getSelection() {
        return selection;
    }

    /**
     * Add children from this display
     * @param childrenToAdd An array of the Children Nodes to be removed
     */
    public void addChildren(Node ... childrenToAdd) {
        children.addAll(Arrays.asList(childrenToAdd));
    }

    /**
     * Remove children from this display. <br>
     * Children in the provided array which are not on this Display Pane will be ignored
     * @param childrenToRemove An array of the Children Nodes to be removed
     */
    public void removeChildren(Node ... childrenToRemove) {
        children.removeAll(Arrays.asList(childrenToRemove));
    }

    /**
     * Get the Object clicked on by a MouseEvent, if any.
     * @param me The MouseEvent in question. Assumed to be targeted at this DisplayPane
     * @return The Object, if any, on the display pane which was clicked on
     */
    private Object getClickedOn(MouseEvent me) {
        List<Node> children = pane.getChildren();
        if (pane.contains(me.getX(), me.getY())) {
            for (Node node : children) {
                if (node.getLayoutBounds().contains(me.getX(), me.getY())) {
                    return node.getUserData();
                }
            }
        }
        return null;
    }
}
