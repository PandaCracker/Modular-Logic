package base.fundamentals;

import base.Simulation;
import base.Utils;
import base.events.AddChildrenEvent;
import base.events.DeleteChildrenEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class DisplayPane {
    private Pane display;
    private String name;
    private boolean selecting;
    private List<Node> children;

    public DisplayPane(String name) {
        this.display = new Pane();
        this.name = name;
        this.selecting = false;

        this.children = display.getChildren();

        display.setUserData(name);
        display.setPrefWidth(Simulation.INIT_BOARD_WIDTH);
        display.setPrefHeight(Simulation.INIT_BOARD_HEIGHT);

    }

    public void configure(Pane pane, String name) {
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
                selection.done();
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
}
