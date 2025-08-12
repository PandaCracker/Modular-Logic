package base;

import javafx.scene.paint.Color;

public class SignalSource extends Component {

    private final static int WIDTH = 1;
    private final static int HEIGHT = 1;
    private final static Color COLOR = Color.GREY;

    private boolean on;

    public SignalSource(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 0, 1);

        this.on = false;
    }

    public boolean isOn() {return on;}

    public void toggle() {
        on ^= true;
        this.getOutputPort(0).setState(on);
    }

    @Override
    public void update() {
    }
}
