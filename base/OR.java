package base;

import javafx.scene.paint.Color;

public class OR extends Component {

    private final static int WIDTH = 2;
    private final static int HEIGHT = 3;
    private final static Color COLOR = Color.BLUE;

    public OR(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 2, 1);
    }

    @Override
    public String toString() {
        return "OR " + super.toString();
    }
}
