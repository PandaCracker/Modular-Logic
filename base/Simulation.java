package base;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane mainGrid = new GridPane();
        ArrayList<Rectangle> rectangles = new ArrayList<>();

    }

    public static void main(String[] args) {
        Simulation sim = new Simulation();

        SignalSource src1 = new SignalSource();
        sim.addComponentAt(src1, new Coord(0, 5));
        SignalSource src2 = new SignalSource();
        sim.addComponentAt(src2, new Coord(0, 7));
        AND and1 = new AND();
        sim.addComponentAt(and1, new Coord(3, 5));
        Light l1 = new Light();
        sim.addComponentAt(l1, new Coord(7, 6));

        src1.connect(0, and1, 0);
        src2.connect(0, and1, 1);

        and1.connect(0, l1, 0);

        Simulation.launch();
    }
}
