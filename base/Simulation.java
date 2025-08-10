package base;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class Simulation extends Application {
    public final static int CELL_SIZE = 40;

    public final static int INIT_BOARD_WIDTH = 10;
    public final static int INIT_BOARD_HEIGHT = 15;

    private Coord boardSize;
    private boolean[][] boardFilled;
    private ArrayList<Component> components;

    public Simulation() {
        this.boardSize = new Coord(INIT_BOARD_WIDTH, INIT_BOARD_HEIGHT);
        this.boardFilled = new boolean[boardSize.x()][boardSize.y()];
        this.components = new ArrayList<>();
    }

    public void addComponentAt(Component component, Coord coords) {
        component.rect.setX(coords.x() * CELL_SIZE);
        component.rect.setY(coords.y() * CELL_SIZE);
        this.components.add(component);
    }

    public void placeRect(GridPane gp, Rectangle rect) {
        gp.add(rect, (int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
    }

    public GridPane makeBackgroundGrid() {
        GridPane grid = new GridPane();
        for (int i = 0; i < INIT_BOARD_HEIGHT; i++) {
            for (int j = 0; j < INIT_BOARD_WIDTH; j++) {
                Rectangle filler = new Rectangle(CELL_SIZE, CELL_SIZE);
                grid.add(filler, j, i);
            }
        }
        return grid;
    }

    @Override
    public void init() {
        SignalSource src1 = new SignalSource();
        this.addComponentAt(src1, new Coord(0, 5));
        SignalSource src2 = new SignalSource();
        this.addComponentAt(src2, new Coord(0, 7));
        AND and1 = new AND();
        this.addComponentAt(and1, new Coord(3, 5));
        Light l1 = new Light();
        this.addComponentAt(l1, new Coord(7, 6));

        src1.connect(0, and1, 0);
        src2.connect(0, and1, 1);

        and1.connect(0, l1, 0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane backgroundGrid = makeBackgroundGrid();
        GridPane componentGrid = new GridPane();

        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (Component component : this.components) {
            rectangles.add(component.rect);
        }

        rectangles.forEach(r -> placeRect(componentGrid, r));

        StackPane stack = new StackPane(backgroundGrid, componentGrid);
        Scene scene = new Scene(stack);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
