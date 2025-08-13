package base;

import base.events.AddChildEvent;
import base.events.DeleteChildEvent;
import base.fundamentals.*;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    /** Number of milliseconds between each logical update frame */
    public final static int FRAME_DELAY_MS = 33;

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
    private void addComponent(Component component) {
        this.components.add(component);
    }

    private void deleteChild(List<Node> childrenList, DeleteChildEvent deleteEvent) {
        System.out.println("Child to remove: " + deleteEvent.getChildToRemove());
        String problemChildID = deleteEvent.getChildToRemove().getId();
        childrenList.removeIf(n -> n.getId().equals(problemChildID));
    }

    private void addChild(List<Node> childrenList, AddChildEvent addEvent) {
        System.out.println("Child to add: " + addEvent.getChildToAdd());
        childrenList.add(addEvent.getChildToAdd());
    }

    /**
     * Sets up the initial state of the simulation board
     */
    @Override
    public void init() {
        SignalSource src1 = new SignalSource(2, 5);
        addComponent(src1);

        Splitter spl1 = new Splitter(3, 5);
        addComponent(spl1);

        SignalSource src2 = new SignalSource(2, 7);
        addComponent(src2);

        Splitter spl2 = new Splitter(3, 7);
        addComponent(spl2);

        AND and1 = new AND(5, 5);
        addComponent(and1);

        OR or1 = new OR(5, 10);
        addComponent(or1);

        Light l1 = new Light(9, 6);
        addComponent(l1);

        Light l2 = new Light(9, 11);
        addComponent(l2);

        src1.connect(0, spl1, 0);
        src2.connect(0, spl2, 0);
        spl1.connect(0, and1, 0);
        spl1.connect(1, or1,0);
        spl2.connect(0, and1, 1);
        spl2.connect(1, or1, 1);
        and1.connect(0, l1, 0);
        or1.connect(0, l2, 0);
    }

    /**
     * Construct the JavaFX Scene structure for this Application
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception If any exception occurs
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

        root.addEventHandler(DeleteChildEvent.EVENT_TYPE, e -> deleteChild(children, e));

        root.addEventHandler(AddChildEvent.EVENT_TYPE, e -> addChild(children, e));

        Timeline timeline = new Timeline( new KeyFrame(
                Duration.millis(FRAME_DELAY_MS),
                e -> components.forEach(Component::update)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        BorderPane window = new BorderPane(root);
        Scene scene = new Scene(window);
        primaryStage.setTitle("Modular Logic");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
