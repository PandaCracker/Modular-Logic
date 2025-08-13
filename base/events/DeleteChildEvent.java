package base.events;

import javafx.event.*;
import javafx.scene.shape.Shape;

/**
 * Custom Event which tells the Simulation a Component, Port, or Connection needs to be removed from display
 *
 * @author Lucas Peterson
 */
public class DeleteChildEvent extends Event {

    /** EventType of this Event for handling purposes */
    public final static EventType<DeleteChildEvent> EVENT_TYPE =
            new EventType<>(Event.ANY, "Delete Me");

    /** The Shape being removed from display */
    private final Shape childToRemove;

    /**
     * Create a new DeleteChild Event
     * @param childToRemove The child that needs to be removed
     */
    public DeleteChildEvent(Shape childToRemove) {
        super(EVENT_TYPE);

        this.childToRemove = childToRemove;
    }

    /**
     * Get the child that needs to be removed
     * @return The child that needs to be removed
     */
    public Shape getChildToRemove() {
        return childToRemove;
    }
}
