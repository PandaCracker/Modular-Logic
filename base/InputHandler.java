package base;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class InputHandler {

    private final ArrayList<Component> components;
    private Component subject;
    private boolean inDrag;

    public InputHandler(ArrayList<Component> components) {
        this.components = components;
        this.subject = null;
        this.inDrag = false;
    }

    public void dragStart(MouseEvent me) {
        double sceneX = me.getSceneX();
        double sceneY = me.getSceneY();
        for (Component component : components) {
            if (component.getRect().contains(sceneX, sceneY)) {
                subject = component;
                inDrag = true;
                System.out.println("Drag started on " + subject);
                break;
            }
        }
    }

    public void mouseRelease(MouseEvent me) {
        if (inDrag) {
            double sceneX = me.getSceneX();
            double sceneY = me.getSceneY();
            System.out.println("Drag ended on " + subject);
            subject.moveComponent(sceneX, sceneY);
        }
        inDrag = false;
    }
}
