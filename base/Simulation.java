package base;

import base.components.*;
import base.events.*;
import base.fundamentals.Component;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    /** Pixels wide the side UI frame should be */
    public final static int PREF_UI_WIDTH = 150;

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
     * Sets up everything related to the main display window
     * @return The initialized and update-driven Pane object
     */
    private Pane initDisplay() {
        final Pane display = new Pane();
        List<Node> children = display.getChildren();

        display.setPrefWidth(boardWidth * CELL_SIZE);
        display.setPrefHeight(boardHeight * CELL_SIZE);

        // Set up Component addition/removal processes
        Component.setDisplayPane(display);
        display.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e -> deleteChild(children, e));
        display.addEventHandler(AddChildrenEvent.EVENT_TYPE, e -> addChild(children, e));

        addInitialComponents();

        // Set up logic update loop
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DELAY_MS),
                e -> display.getChildren().stream().map(Node::getUserData)
                        .filter(o -> o instanceof Component)
                        .forEach(o -> ((Component) o).update())
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return display;
    }

    /**
     * Sets up everything related to the UI
     * @return The initialized UI frame
     */
    private VBox initAddUI() {
        VBox frame = new VBox();
        frame.setAlignment(Pos.CENTER);
        frame.setPrefWidth(PREF_UI_WIDTH);
        frame.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        ChoiceBox<String> componentSelector = new ChoiceBox<>();
        Button addButton = new Button("Add new ____");

        addButton.setOnAction(ae -> {
            switch(componentSelector.getValue()) {
                case "AND" : new AND(); break;
                case "OR" : new OR(); break;
                case "Light" : new Light(); break;
                case "Signal Source" : new SignalSource(); break;
                case "Splitter" : new Splitter(); break;
                default: break;
            }
        });

        componentSelector.getItems().addAll(List.of("AND", "OR", "Light", "Signal Source", "Splitter"));
        componentSelector.setOnAction(e ->
                addButton.setText("Add new " + componentSelector.getValue()));
        frame.getChildren().addAll(addButton, componentSelector);

        return frame;
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
        Pane display = initDisplay();
        VBox addUI = initAddUI();

        BorderPane window = new BorderPane();
        window.setCenter(display);
        window.setLeft(addUI);

        Scene scene = new Scene(window);
        primaryStage.setTitle("Modular Logic");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Simulation.launch();
    }
}
