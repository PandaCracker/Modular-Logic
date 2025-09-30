package base.fundamentals;

import base.Simulation;
import base.Utils;
import base.events.AddChildrenEvent;
import base.events.DeleteChildrenEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class DisplayPane {
    private Pane pane;
    private String name;
    private SelectionArea selection;
    private boolean selecting;
    private List<Node> children;

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

        // Set up Component addition/removal processes
        pane.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e ->
                children.removeAll(Arrays.asList(e.getChildrenToRemove()))
        );
        pane.addEventHandler(AddChildrenEvent.EVENT_TYPE, e ->
                children.addAll(Arrays.asList(e.getChildrenToAdd()))
        );
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
     * Get whether this Display Pane has a selection in progress
     * @return Whether this Display Pane has an active Selection Area
     */
    public boolean isSelecting() {
        return selecting;
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
