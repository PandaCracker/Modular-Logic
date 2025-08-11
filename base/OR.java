package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OR extends Component {

    private final static int WIDTH = 2;
    private final static int HEIGHT = 3;
    private final static Color COLOR = Color.BLUE;

    public OR() {
        this.rect = new Rectangle(WIDTH * Simulation.CELL_SIZE, HEIGHT * Simulation.CELL_SIZE);
        this.color = COLOR;

        this.numInputs = 2;
        this.numOutputs = 1;

        this.inputConnections = new Connection[numInputs];
        this.outputConnections = new Connection[numOutputs];
    }

    @Override
    public void update() {
        outputConnections[0].setState(inputConnections[0].getState() || inputConnections[1].getState());
    }
}
