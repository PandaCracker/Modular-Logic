package base;

import base.components.*;
import base.events.*;
import base.fundamentals.Component;
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

    /**
     * Create a new Simulation instance with default width, height, and no components
     */
    public Simulation() {
        this.boardWidth = INIT_BOARD_WIDTH;
        this.boardHeight = INIT_BOARD_HEIGHT;
    }

    private void deleteChild(List<Node> childrenList, DeleteChildrenEvent deleteEvent) {
        childrenList.removeAll(Arrays.asList(deleteEvent.getChildrenToRemove()));
    }

    private void addChild(List<Node> childrenList, AddChildrenEvent addEvent) {
        childrenList.addAll(Arrays.asList(addEvent.getChildrenToAdd()));
    }

    private void addInitialComponents() {
        new SignalSource(2, 5);
        new Splitter(3.5, 5);
        new SignalSource(2, 7);
        new Splitter(3.5, 7);
        new AND(5, 5);
        new OR(5, 10);
        new Light(9, 6);
        new Light(9, 11);
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
        final Pane root = new Pane();
        List<Node> children = root.getChildren();

        root.setPrefWidth(boardWidth * CELL_SIZE);
        root.setPrefHeight(boardHeight * CELL_SIZE);

        // Set up Component addition/removal processes
        Component.setDisplayPane(root);
        root.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e -> deleteChild(children, e));
        root.addEventHandler(AddChildrenEvent.EVENT_TYPE, e -> addChild(children, e));

        addInitialComponents();

        // Set up logic update loop
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DELAY_MS),
                e -> root.getChildren().stream().map(Node::getUserData)
                        .filter(o -> o instanceof Component)
                        .forEach(o -> ((Component) o).update())
                ));

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
