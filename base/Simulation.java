package base;

import base.components.*;
import base.events.*;
import base.fundamentals.*;

import javafx.application.Application;
import javafx.animation.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
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
    public final static double CELL_SIZE = 60;
    /** Number of cells wide the board is at the start of the simulation */
    public final static int INIT_BOARD_WIDTH = 15;
    /** Number of cells tall the board is at the start of the simulation */
    public final static int INIT_BOARD_HEIGHT = 15;
    /** Pixels wide the side UI frame should be */
    public final static int PREF_UI_WIDTH = 225;
    /** Max pixels tall the TextAreas should be */
    public final static int MAX_TEXT_AREA_HEIGHT = 40;

    /** Number of milliseconds between each logical update frame */
    public final static int FRAME_DELAY_MS = 33;

    /** Main top-level display pane */
    public final static Pane MAIN_PANE = new Pane();

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

    /**
     * Get the Object clicked on by a MouseEvent, if any
     *
     * @param me The MouseEvent in question. Assumed to be targeted at the displayPane
     * @return The Object, if any, on the display pane which was clicked on
     */
    private Object getClickedOn(MouseEvent me) {
        List<Node> children = MAIN_PANE.getChildren();
        if (MAIN_PANE.contains(me.getX(), me.getY())) {
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
     * Initializes everything related to the main display window
     */
    private void initDisplay() {
        List<Node> children = MAIN_PANE.getChildren();

        MAIN_PANE.setPrefWidth(boardWidth * CELL_SIZE);
        MAIN_PANE.setPrefHeight(boardHeight * CELL_SIZE);


        // Multi-selection handling
        MAIN_PANE.setOnDragDetected(e -> {
            // Know a multi-select is happening
            if (selecting) {
                children.add(selection.getRect());
            }
        });

        MAIN_PANE.setOnMousePressed(e -> {
            // Possible multi-select start, more accurate than waiting for drag detection
            if (getClickedOn(e) == null && e.getButton() == MouseButton.PRIMARY) {
                selecting = true;
                selection.startNew(e.getX(), e.getY());
            }
        });

        MAIN_PANE.setOnMouseDragged(e -> {
            if (selecting) {
                List<Component> components = componentsFromChildren(children);
                selection.expandSelection(e.getX(), e.getY(), components);
            }
        });

        MAIN_PANE.setOnMouseReleased(e -> {
            if (selecting) {
                children.remove(selection.getRect());
                selection.done();
                selecting = false;
            }
        });

        // Set up Component addition/removal processes
        MAIN_PANE.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e -> deleteChild(children, e));
        MAIN_PANE.addEventHandler(AddChildrenEvent.EVENT_TYPE, e -> addChild(children, e));

        // Set up logic update loop
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DELAY_MS),
                e -> componentsFromChildren(MAIN_PANE.getChildren()).forEach(Component::update)
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    TextArea createTextArea(String promptText) {
        TextArea ret = new TextArea();
        ret.setMaxHeight(MAX_TEXT_AREA_HEIGHT);
        ret.setWrapText(true);
        ret.setPromptText(promptText);
        return ret;
    }

    /**
     * Sets up everything related to the Compound Component UI elements
     * @return The initialized VBox UI frame
     */
    private VBox initAddCompoundComponentUI() {
        VBox addCompoundComponentUI = new VBox();

        TextArea nameField = createTextArea("Enter Compound Component Name, e.g. XOR");

        Label colorLabel = new Label("Select Compound Component Color");
        ColorPicker colorPicker = new ColorPicker();

        TextArea widthField = createTextArea("Enter Compound Component Width (in cells)");
        TextArea heightField = createTextArea("Enter Compound Component Height (in cells)");

        Button createCompoundButton = new Button("Create new Compound Component from highlighted Components");
        createCompoundButton.setWrapText(true);
        createCompoundButton.setTextAlignment(TextAlignment.CENTER);
        createCompoundButton.setOnAction(e -> {
            CompoundComponent.makeCompoundComponent(
                    selection,
                    widthField.getText(),
                    heightField.getText(),
                    colorPicker.getValue(),
                    nameField.getText(),
                    MAIN_PANE);
            selection.getSelected().forEach(Component::remove);
        });

        addCompoundComponentUI.getChildren().addAll(
                nameField, colorLabel, colorPicker, widthField, heightField, createCompoundButton);
        addCompoundComponentUI.setAlignment(Pos.CENTER);

        return addCompoundComponentUI;
    }

    /**
     * Creates a VBox which holds UI elements to add new basic Components to the current Screen
     * @return The VBox described above
     */
    private VBox initAddComponentUI() {
        // TODO clean up this to try and get class variables to avoid the switches and duplicate literal strings
        Button addButton = new Button("Add new ____");
        ChoiceBox<String> componentSelector = new ChoiceBox<>();

        addButton.setOnAction(ae -> {
            String addText = componentSelector.getValue();
            switch((addText == null ? "" : addText)) {
                case "AND" : new AND(); break;
                case "OR" : new OR(); break;
                case "NOT" : new NOT(); break;
                case "Light" : new Light(); break;
                case "Signal Source" : new SignalSource(); break;
                case "Splitter" : new Splitter(); break;
                default: break;
            }
        });

        componentSelector.getItems().addAll(List.of("AND", "OR", "NOT", "Light", "Signal Source", "Splitter"));
        componentSelector.setOnAction(e -> {
            String addText = componentSelector.getValue();
            addButton.setText("Add new " + (addText == null ? "___" : addText));
        });

        VBox addComponentUI = new VBox(addButton, componentSelector);
        addComponentUI.setAlignment(Pos.CENTER);

        return addComponentUI;
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

        VBox addComponentUI = initAddComponentUI();
        VBox addCompoundComponentUI = initAddCompoundComponentUI();

        frame.getChildren().addAll(
                addComponentUI,
                new Separator(Orientation.HORIZONTAL),
                addCompoundComponentUI);

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
        initDisplay();
        VBox addUI = initAddUI();

        BorderPane window = new BorderPane();
        window.setCenter(MAIN_PANE);
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
