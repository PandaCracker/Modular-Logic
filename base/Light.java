package base;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Light extends Component {

    private final static int WIDTH = 1;
    private final static int HEIGHT = 1;
    private final static String COLOR_STRING = "grey";

    private boolean on;

    public Light() {
        this.rect = new Rectangle(WIDTH * Simulation.CELL_SIZE, HEIGHT * Simulation.CELL_SIZE);
        rect.setFill(Paint.valueOf(COLOR_STRING));

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
