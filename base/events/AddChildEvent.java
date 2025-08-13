package base.events;

import javafx.event.*;
import javafx.scene.shape.Shape;

/**
 * Custom Event which tells the Simulation a Component, Port, or Connection needs to be added to the display
 *
 * @author Lucas Peterson
 */
public class AddChildEvent extends Event {

    /** EventType of this Event for handling purposes */
    public final static EventType<AddChildEvent> EVENT_TYPE =
            new EventType<>(Event.ANY, "Add Me");

    /** The Shape being added to the display */
    private final Shape childToAdd;

    /**
     * Create a new AddChildEvent Event
     * @param childToAdd The child that needs to be added
     */
    public AddChildEvent(Shape childToAdd) {
        super(EVENT_TYPE);

        this.childToAdd = childToAdd;
    }

    /**
     * Get the child that needs to be added
     * @return The child that needs to be added
     */
    public Shape getChildToAdd() {
        return childToAdd;
    }
}
