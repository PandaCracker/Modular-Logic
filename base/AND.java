package base;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class AND extends Component {

    private final static int WIDTH = 2;
    private final static int HEIGHT = 3;
    private final static String COLOR_STRING = "red";

    public AND() {
        this.rect = new Rectangle(WIDTH * Simulation.CELL_SIZE, HEIGHT * Simulation.CELL_SIZE);
        rect.setFill(Paint.valueOf(COLOR_STRING));

        this.numInputs = 2;
        this.numOutputs = 1;

        this.inputConnections = new Connection[numInputs];
        this.outputConnections = new Connection[numOutputs];
    }

    @Override
    public void update() {
        outputConnections[0].setState(inputConnections[0].getState() && inputConnections[1].getState());
    }
}