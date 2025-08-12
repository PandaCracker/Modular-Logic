package base;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.canvas.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation extends Application {
    public final static double CELL_SIZE = 40.0;

    public final static int INIT_BOARD_WIDTH = 15;
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
        SignalSource src1 = new SignalSource(2, 5);
        addComponent(src1);

        SignalSource src2 = new SignalSource(2, 7);
        addComponent(src2);

        AND and1 = new AND(5, 5);
        addComponent(and1);

        Light l1 = new Light(9, 6);
        addComponent(l1);

        src1.connect(0, and1, 0);
        src2.connect(0, and1, 1);
        and1.connect(0, l1, 0);
    }

    private void update() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.setPrefWidth(boardWidth * CELL_SIZE);
        root.setPrefHeight(boardHeight * CELL_SIZE);
        InputHandler ih = new InputHandler(components);

        for (Component component : components) {
            List<Node> children = root.getChildren();
            children.add(component.getRect());
            children.addAll(Arrays.stream(component.getInputPorts()).map(Port::getCircle).toList());
            for (Port outPort : component.getOutputPorts()) {
                children.add(outPort.getCircle());
                if (outPort.isConnected()) {
                    children.add(outPort.getConnection().getLine());
                }
            }

            component.getRect().setOnDragDetected(ih::dragStart);
            component.getRect().setOnMouseReleased(ih::mouseRelease);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(333), e -> {
                        update();
                    }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        BorderPane window = new BorderPane(root);
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
