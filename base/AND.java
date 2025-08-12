package base;

import javafx.scene.paint.Color;

public class AND extends Component {

    private final static int WIDTH = 2;
    private final static int HEIGHT = 3;
    private final static Color COLOR = Color.RED;

    public AND(int x, int y) {
        super(x, y, WIDTH, HEIGHT, COLOR, 2, 1);
    }

    @Override
    public void update() {

    }

    @Override
    public String toString() {
        return "AND " + super.toString();
    }
}