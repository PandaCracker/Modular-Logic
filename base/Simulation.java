package base;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;

public class Simulation extends Application {
    public final static double CELL_SIZE = 40.0;

    public final static int INIT_BOARD_WIDTH = 10;
    public final static int INIT_BOARD_HEIGHT = 15;

    private int boardWidth;
    private int boardHeight;

    private boolean[][] boardFilled;

    private ArrayList<Component> components;

    public Simulation() {
        this.boardWidth = INIT_BOARD_WIDTH;
        this.boardHeight = INIT_BOARD_HEIGHT;
        this.boardFilled = new boolean[boardWidth][boardHeight];
        this.components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    @Override
    public void init() {
        SignalSource src1 = new SignalSource(0, 5);
        addComponent(src1);

        SignalSource src2 = new SignalSource(0, 7);
        addComponent(src2);

        AND and1 = new AND(3, 5);
        addComponent(and1);

        Light l1 = new Light(7, 6);
        addComponent(l1);

        src1.connect(0, and1, 0);
        src2.connect(0, and1, 1);
        and1.connect(0, l1, 0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(INIT_BOARD_WIDTH * CELL_SIZE, INIT_BOARD_HEIGHT * CELL_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (Component component : components) {
            component.draw(gc);
        }

        BorderPane window = new BorderPane(canvas);
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
