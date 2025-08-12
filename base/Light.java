package base;

import javafx.scene.paint.Color;

public class Light extends Component {

    private final static int WIDTH = 1;
    private final static int HEIGHT = 1;
    private final static Color COLOR = Color.BLACK;

    public Light(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 1, 0);
    }

    @Override
    public String toString() {
        return "Light " + super.toString();
    }
}
