package base.components;

import base.fundamentals.Component;
import javafx.scene.paint.Color;

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

    public CompoundComponent(double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLOR, 0, 0,
                DEFAULT_TEXT, DEFAULT_TEXT_COLOR);
    }
}
