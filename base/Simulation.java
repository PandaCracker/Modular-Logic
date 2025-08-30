package base;

import base.components.*;
import base.events.*;
import base.fundamentals.*;

import javafx.application.Application;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
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

    /** Whether there is a multi-selection action taking place */
    private boolean selecting;
    /** The SelectionArea object handling multi-selection */
    private final SelectionArea selection;

    /**
     * Create a new Simulation instance with default width, height, and no components
     */
    public Simulation() {
        this.boardWidth = INIT_BOARD_WIDTH;
        this.boardHeight = INIT_BOARD_HEIGHT;

        this.selecting = false;
        this.selection = new SelectionArea(0,0);
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
     * Get the Object clicked on by a MouseEvent, if any
     * @param me The MouseEvent in question. Assumed to be targeted at the displayPane
     * @param displayPane The Pane the MouseEvent is generated for
     * @return The Object, if any, on the display pane which was clicked on
     */
    private Object getClickedOn(MouseEvent me, Pane displayPane) {
        List<Node> children = displayPane.getChildren();
        if (displayPane.contains(me.getX(), me.getY())) {
            for (Node node : children) {
                if (node.getLayoutBounds().contains(me.getX(), me.getY())) {
                    return node.getUserData();
                }
            }
        }
        return null;
    }

    /**
     * Get a list of all Components in a list of all displayed items
     * @param children A list possibly containing Components
     * @return A list of all (if any) Components in the list provided
     */
    public List<Component> componentsFromChildren(List<Node> children) {
        return children.stream()
                .filter(n -> n.getUserData() instanceof Component)
                .map(n -> (Component) n.getUserData())
                .toList();
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


        // Multi-selection handling
        display.setOnDragDetected(e -> {
            // Know a multi-select is happening
            if (getClickedOn(e, display) == null) {
                selecting = true;
                children.add(selection.getRect());
            }
        });

        display.setOnMousePressed(e -> {
            // Possible multi-select start, more accurate than waiting for drag detection
            if (getClickedOn(e, display) == null) {
                selection.startNew(e.getX(), e.getY());
            }
        });

        display.setOnMouseDragged(e -> {
            if (selecting) {
                List<Component> components = componentsFromChildren(children);
                selection.expandSelection(e.getX(), e.getY(), components);
            }
        });

        display.setOnMouseReleased(e -> {
            if (selecting) {
                children.remove(selection.getRect());
                selection.done();
                selecting = false;
            }
        });

        // Set up Component addition/removal processes
        Component.setDisplayPane(display);
        display.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e -> deleteChild(children, e));
        display.addEventHandler(AddChildrenEvent.EVENT_TYPE, e -> addChild(children, e));

        addInitialComponents();

        // Set up logic update loop
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DELAY_MS),
                e -> componentsFromChildren(display.getChildren()).forEach(Component::update)
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
            String addText = componentSelector.getValue();
            switch((addText == null ? "" : addText)) {
                case "AND" : new AND(); break;
                case "OR" : new OR(); break;
                case "Light" : new Light(); break;
                case "Signal Source" : new SignalSource(); break;
                case "Splitter" : new Splitter(); break;
                default: break;
            }
        });

        componentSelector.getItems().addAll(List.of("AND", "OR", "Light", "Signal Source", "Splitter"));
        componentSelector.setOnAction(e -> {
            String addText = componentSelector.getValue();
            addButton.setText("Add new " + (addText == null ? "___" : addText));
        });

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
