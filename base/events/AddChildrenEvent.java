package base.events;

import javafx.event.*;
import javafx.scene.shape.Shape;

/**
 * Custom Event which tells the Simulation a Component, Port, or Connection needs to be added to the display
 *
 * @author Lucas Peterson
 */
public class AddChildrenEvent extends Event {

    /** EventType of this Event for handling purposes */
    public final static EventType<AddChildrenEvent> EVENT_TYPE =
            new EventType<>(Event.ANY, "Add Me");

    /** The Shape(s) being added to the display */
    private final Shape[] childrenToAdd;

    /**
     * Create a new AddChildrenEvent with one shape
     * @param childToAdd The child that needs to be added
     */
    public AddChildrenEvent(Shape childToAdd) {
        super(EVENT_TYPE);

        this.childrenToAdd = new Shape[] {childToAdd};
    }

    /**
     * Create a new AddChildrenEvent with many Shapes
     * @param childrenToAdd The children to be added
     */
    public AddChildrenEvent(Shape ... childrenToAdd) {
        super(EVENT_TYPE);

        this.childrenToAdd = childrenToAdd;
    }

    /**
     * Get the child that needs to be added
     * @return The child that needs to be added
     */
    public Shape[] getChildrenToAdd() {
        return childrenToAdd;
    }
}
