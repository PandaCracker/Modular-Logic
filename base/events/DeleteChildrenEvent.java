package base.events;

import javafx.event.*;
import javafx.scene.Node;

/**
 * Custom Event which tells the Simulation a Component, Port, or Connection needs to be removed from display
 *
 * @author Lucas Peterson
 */
public class DeleteChildrenEvent extends Event {

    /** EventType of this Event for handling purposes */
    public final static EventType<DeleteChildrenEvent> EVENT_TYPE =
            new EventType<>(Event.ANY, "Delete Me");

    /** The Shape(s) being removed from display */
    private final Node[] childrenToRemove;

    /**
     * Create a new DeleteChildrenEvent with multiple removals
     * @param childrenToRemove The children that need to be removed
     */
    public DeleteChildrenEvent(Node ... childrenToRemove) {
        super(EVENT_TYPE);

        this.childrenToRemove = childrenToRemove;
    }

    public DeleteChildrenEvent(Node childToRemove) {
        super(EVENT_TYPE);

        this.childrenToRemove = new Node[] {childToRemove};
    }

    /**
     * Get the child that needs to be removed
     * @return The child that needs to be removed
     */
    public Node[] getChildrenToRemove() {
        return childrenToRemove;
    }
}
