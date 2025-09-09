package base.components;

import base.fundamentals.Component;
import base.fundamentals.SelectionArea;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.TreeSet;

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

    private final TreeSet<Component> contents;

    private CompoundComponent(SelectionArea selection, double x, double y, double width, double height,
                              Color color, int numInputs, int numOutputs, String name) {
        super(x, y, width, height, color, numInputs, numOutputs, name, DEFAULT_TEXT_COLOR);
        this.contents = selection.getSelected();

        getRect().setOnMouseClicked(e -> System.out.println(contents));
    }

    public static void makeCompoundComponent(SelectionArea selection, String widthStr, String heightStr,
                                                   Color color, String name) {
        double width = widthStr.isBlank() ? DEFAULT_WIDTH : Double.parseDouble(widthStr);
        double height = heightStr.isBlank() ? DEFAULT_HEIGHT : Double.parseDouble(heightStr);
        int[] IOCounts = selection.getSelectedDependencies();
        name = name.isBlank() ? DEFAULT_TEXT : name;

        new CompoundComponent(selection, 1, 1, width, height, color, IOCounts[0], IOCounts[1], name);
    }

    private void assignOutsidePorts() {

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public String toString() {
        return "Compound " + super.toString() + " with " + getNumInputs() + " ins, "
                + getNumOutputs() + " outs";
    }
}