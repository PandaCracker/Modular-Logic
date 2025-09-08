package base.components;

import base.fundamentals.Component;
import base.fundamentals.SelectionArea;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class CompoundComponent extends Component {
    /** The default width of a CompoundComponent */
    private final static double DEFAULT_WIDTH = 1.5;
    /** The default height of a CompoundComponent */
    private final static double DEFAULT_HEIGHT= 2;
    /** The default color of a CompoundComponent */
    private final static Color DEFAULT_COLOR = Color.BLUE;
    /** The default String displayed on this CompoundComponent */
    private final static String DEFAULT_TEXT = "CompoundComponent";
    /** The default Color of the Text displayed on this CompoundComponent */
    private final static Color DEFAULT_TEXT_COLOR = Color.WHITE;

    private final HashSet<Component> contents;

    private CompoundComponent(SelectionArea selection, double x, double y, double width, double height,
                              Color color, int[] IOCounts, String name) {
        super(x, y, width, height, color, IOCounts[0], IOCounts[1], name, DEFAULT_TEXT_COLOR);
        this.contents = selection.getSelected();
    }

    public CompoundComponent(SelectionArea selection, double x, double y, double width, double height,
                             Color color, String name) {
        this(selection, x, y, width, height, color, selection.getSelectedDependencies(), name);
    }

    public CompoundComponent(SelectionArea selection, double x, double y) {
        this(selection, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLOR, DEFAULT_TEXT);
    }

    public CompoundComponent(SelectionArea selection) {
        this(selection, 1,1);
    }
}
