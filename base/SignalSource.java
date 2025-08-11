package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SignalSource extends Component {

    private final static int WIDTH = 1;
    private final static int HEIGHT = 1;
    private final static Color COLOR = Color.GREY;

    private boolean on;

    public SignalSource() {
        this.rect = new Rectangle(WIDTH * Simulation.CELL_SIZE, HEIGHT * Simulation.CELL_SIZE);
        this.color = COLOR;

        this.numInputs = 0;
        this.numOutputs = 1;

        this.inputConnections = new Connection[numInputs];
        this.outputConnections = new Connection[numOutputs];

        this.on = false;
    }

    public boolean isOn() {return on;}

    public void toggle() {
        on ^= true;
        outputConnections[0].setState(on);
    }

    @Override
    public void update() {
        outputConnections[0].setState(on);
    }
}
