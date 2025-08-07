package base;

import javafx.scene.shape.Rectangle;

public class OR extends Component {

    private final static int WIDTH = 2 * Simulation.CELL_SIZE;
    private final static int HEIGHT = 3 * Simulation.CELL_SIZE;

    public OR() {
        this.rect = new Rectangle(WIDTH, HEIGHT);

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
