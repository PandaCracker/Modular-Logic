package base;

import javafx.scene.paint.Color;

public class SignalSource extends Component {

    private final static int WIDTH = 1;
    private final static int HEIGHT = 1;
    private final static Color COLOR = Color.BLACK;

    public SignalSource(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 0, 1);
    }

    @Override
    public String toString() {
        return "Signal Source " + super.toString();
    }
}
