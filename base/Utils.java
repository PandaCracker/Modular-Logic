package base;

import base.fundamentals.Component;
import javafx.scene.Node;

import java.util.List;

/**
 * Miscellaneous functions used by multiple classes
 */
public class Utils {
    /**
     * Get a list of every Component from a list of Nodes
     * @param children A list possibly containing Components
     * @return A list of all (if any) Components in the list provided
     */
    public static List<Component> componentsFromChildren(List<Node> children) {
        return children.stream()
                .filter(n -> n.getUserData() instanceof Component)
                .map(n -> (Component) n.getUserData())
                .toList();
    }
}
