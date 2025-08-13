package base;

import javafx.scene.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

/**
 * Main simulation class
 * <p>
 * Sets up the Application and holds every Component
 *
 * @author Lucas Peterson
 */
public class Simulation extends Application {
    /** Number of pixels one 'cell' is wide and tall */
    public final static double CELL_SIZE = 40.0;

    /** Number of cells wide the board is at the start of the simulation */
    public final static int INIT_BOARD_WIDTH = 15;
    /** Number of cells tall the board is at the start of the simulation */
    public final static int INIT_BOARD_HEIGHT = 15;

    /** Number of cells wide the board currently is */
    private final int boardWidth;
    /** Number of cells tall the board currently is */
    private final int boardHeight;

    /** A list of every Component on the board in this Simulation */
    private final ArrayList<Component> components;

    /**
     * Create a new Simulation instance with default width, height, and no components
     */
    public Simulation() {
        this.boardWidth = INIT_BOARD_WIDTH;
        this.boardHeight = INIT_BOARD_HEIGHT;
        this.components = new ArrayList<>();
    }

    /**
     * Add a component to the Simulation
     * @param component The Component to add
     */
    public void addComponent(Component component) {
        this.components.add(component);
    }

    /**
     * Sets up the initial state of the simulation board
     */
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

    /**
     * Construct the JavaFX Scene structure for this Application
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        root.setPrefWidth(boardWidth * CELL_SIZE);
        root.setPrefHeight(boardHeight * CELL_SIZE);

        List<Node> children = root.getChildren();

        // Add initial component, port, and connection Shapes to the display Pane
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
