package base;

import javafx.scene.shape.Rectangle;

public class Light extends Component {

    private final static int WIDTH = Simulation.CELL_SIZE;
    private final static int HEIGHT = Simulation.CELL_SIZE;

    private boolean on;

    public Light() {
        this.rect = new Rectangle(WIDTH, HEIGHT);

        this.numInputs = 1;
        this.numOutputs = 0;

        this.inputConnections = new Connection[numInputs];
        this.outputConnections = new Connection[numOutputs];

        this.on = false;
    }

    public boolean isOn() {return on;}

    @Override
    public void update() {
        on = inputConnections[0].getState();
    }
}
