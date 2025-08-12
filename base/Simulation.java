package base;

import javafx.scene.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class Simulation extends Application {
    public final static double CELL_SIZE = 40.0;

    public final static int INIT_BOARD_WIDTH = 15;
    public final static int INIT_BOARD_HEIGHT = 15;

    private final int boardWidth;
    private final int boardHeight;

    private final ArrayList<Component> components;

    public Simulation() {
        this.boardWidth = INIT_BOARD_WIDTH;
        this.boardHeight = INIT_BOARD_HEIGHT;
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        root.setPrefWidth(boardWidth * CELL_SIZE);
        root.setPrefHeight(boardHeight * CELL_SIZE);

        List<Node> children = root.getChildren();

        for (Component component : components) {
            Rectangle rect = component.getRect();
            children.add(rect);

            for (Port outPort : component.getOutputPorts()) {
                if (outPort.isConnected()) {
                    children.add(outPort.getConnection().getLine());
                }
                children.add(outPort.getCircle());
            }

            children.addAll(Arrays.stream(component.getInputPorts()).map(Port::getCircle).toList());
        }

        BorderPane window = new BorderPane(root);
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
