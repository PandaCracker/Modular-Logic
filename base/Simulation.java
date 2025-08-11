package base;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.layout.BorderPane;
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
    private ArrayList<Connection> connections;

    public Simulation() {
        this.boardSize = new Coord(INIT_BOARD_WIDTH, INIT_BOARD_HEIGHT);
        this.boardFilled = new boolean[boardSize.x()][boardSize.y()];
        this.components = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    public void addComponentAt(Component component, Coord coords) {
        component.rect.setX(coords.x() * CELL_SIZE);
        component.rect.setY(coords.y() * CELL_SIZE);
        this.components.add(component);
    }

    public void drawComponent(GraphicsContext gc, Component component) {
        Rectangle rect = component.getRect();
        gc.setFill(component.getColor());
        gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void drawConnection(GraphicsContext gc, Connection connection) {
        Rectangle src = connection.getSourceComponent().getRect();
        Rectangle dst = connection.getDestComponent().getRect();
        gc.strokeLine(src.getX(), src.getY(), dst.getX(), dst.getY());
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

        connections.addAll(List.of(
                src1.connect(0, and1, 0),
                src2.connect(0, and1, 1),
                and1.connect(0, l1, 0)));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(INIT_BOARD_WIDTH * CELL_SIZE, INIT_BOARD_HEIGHT * CELL_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        components.forEach(component -> drawComponent(gc, component));
        connections.forEach(connection -> drawConnection(gc, connection));

        BorderPane window = new BorderPane(canvas);
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
