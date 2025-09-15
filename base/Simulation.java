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

/// Things to do:
/// Check connections on new compound components
/// Consolidate the two methods of getting IO counts for Compounds, removing SelectionArea method
/// Compound update method
/// Make copy constructor for compound components for multi-level compounds
/// Persistence of compounds

/**
 * Main simulation class <br>
 * Sets up the Application and holds every Component
 * @author Lucas Peterson
 */
public class Simulation extends Application {
    /** Number of cells wide the board is at the start of the simulation */
    public final static int INIT_BOARD_WIDTH = 900;
    /** Number of cells tall the board is at the start of the simulation */
    public final static int INIT_BOARD_HEIGHT = 900;
    /** Pixels wide the side UI frame should be */
    public final static int PREF_UI_WIDTH = 225;
    /** Max pixels tall the TextAreas should be */
    public final static int MAX_TEXT_AREA_HEIGHT = 40;

    /** Number of milliseconds between each logical update frame */
    public final static int FRAME_DELAY_MS = 33;

    /** Main top-level display Pane */
    private final static Pane mainPane = new Pane();
    /** Current display Pane being viewed */
    private static Pane currentPane = mainPane;
    /** Top-level layout object all UI and display objects */
    private final static BorderPane window = new BorderPane();

    /** Whether there is a multi-selection action taking place */
    private static boolean selecting;
    /** The SelectionArea object handling multi-selection */
    private final static SelectionArea selection = new SelectionArea();

    /** Stack of Pane view history, for back-history jumps */
    private final static Deque<Pane> paneViewStack = new LinkedList<>();


    /**
     * Get the main-level display pane used by the Simulation Class
     * @return The top level Pane object
     */
    public static Pane getMainPane() {
        return mainPane;
    }

    /**
     * Get the Object clicked on by a MouseEvent, if any.
     * @param me The MouseEvent in question. Assumed to be targeted at the displayPane
     * @return The Object, if any, on the display pane which was clicked on
     */
    private static Object getClickedOn(MouseEvent me) {
        List<Node> children = mainPane.getChildren();
        if (mainPane.contains(me.getX(), me.getY())) {
            for (Node node : children) {
                if (node.getLayoutBounds().contains(me.getX(), me.getY())) {
                    return node.getUserData();
                }
            }
        }
        return null;
    }

    /**
     * Get a list of every Component from a list of Nodes
     * @param children A list possibly containing Components
     * @return A list of all (if any) Components in the list provided
     */
    public static List<Component> componentsFromChildren(List<Node> children) {
        return children.stream()
                .filter(n -> n.getUserData() instanceof Component)
                .map(n -> (Component) n.getUserData())
                .toList();
    }

    /**
     * Initializes everything related to the main display window
     */
    private static void initDisplay() {
        // Name of the view
        mainPane.setUserData("Main View");
        List<Node> children = mainPane.getChildren();

        mainPane.setPrefWidth(INIT_BOARD_WIDTH);
        mainPane.setPrefHeight(INIT_BOARD_HEIGHT);

        // Multi-selection handling
        mainPane.setOnDragDetected(e -> {
            // Know a multi-select is happening
            if (selecting) {
                children.add(selection.getRect());
            }
        });

        mainPane.setOnMousePressed(e -> {
            // Possible multi-select start, more accurate than waiting for drag detection
            if (getClickedOn(e) == null && e.getButton() == MouseButton.PRIMARY) {
                selecting = true;
                selection.startNew(e.getX(), e.getY());
            }
        });

        mainPane.setOnMouseDragged(e -> {
            // Expand Selection Area
            if (selecting) {
                List<Component> components = componentsFromChildren(children);
                selection.expandSelection(e.getX(), e.getY(), components);
            }
        });

        mainPane.setOnMouseReleased(e -> {
            // Done selecting more elements, remove selection rect
            if (selecting) {
                children.remove(selection.getRect());
                selection.done();
                selecting = false;
            }
        });

        // Set up Component addition/removal processes
        mainPane.addEventHandler(DeleteChildrenEvent.EVENT_TYPE, e ->
                children.removeAll(Arrays.asList(e.getChildrenToRemove()))
        );
        mainPane.addEventHandler(AddChildrenEvent.EVENT_TYPE, e ->
                children.addAll(Arrays.asList(e.getChildrenToAdd()))
        );

        // Set up logic update loop
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DELAY_MS),
                e -> componentsFromChildren(mainPane.getChildren()).forEach(Component::update)
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Creates a new Text area with standard attributes. Helper function for text-entry UIs
     * @param promptText The prompt text to display before a user starts typing
     * @return The initialized TextArea
     */
    private static TextArea createTextArea(String promptText) {
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
    private static VBox initAddCompoundComponentUI() {
        VBox addCompoundComponentUI = new VBox();

        TextArea nameField = createTextArea("Enter Compound Component Name, e.g. XOR");

        Label colorLabel = new Label("Select Compound Component Color");
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(CompoundComponent.DEFAULT_COLOR);

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
                    mainPane);
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
    private static VBox initAddComponentUI() {
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
     * Changes the view displayed on the window, saving the previous view in the view history
     */
    public static void setCenterPane(Pane paneToView) {
        try {
            // Save previous view
            paneViewStack.push(currentPane);
            currentPane = paneToView;
            updateCurrentPane();
        } catch (IllegalStateException ise) {
            System.out.println("You're digging too deep! You've hit the max view level");
        }
    }

    /**
     * Returns to the view visited most recently in the view history
     */
    private static void previousCenterPane() {
        if (!paneViewStack.isEmpty()) {
            currentPane = paneViewStack.pop();
            updateCurrentPane();
        } else {
            System.out.println("You're back to the top level!");
        }
    }

    /**
     * Updates the UI to reflect the value of currentPane
     */
    private static void updateCurrentPane() {
        window.setCenter(currentPane);
        Label viewLabel = (Label) window.lookup("#viewLabel");
        viewLabel.setText("Current View: " + currentPane.getUserData());
    }

    /**
     * Creates a UI to control the current view Pane
     * @return An HBox containing the UI described above
     */
    private static HBox initPaneViewUI() {
        Button backButton = new Button("<");
        Label viewLabel = new Label("Current View: Main View");
        viewLabel.setId("viewLabel");

        backButton.setOnAction(e -> previousCenterPane());
        HBox paneViewUI = new HBox(backButton, viewLabel);
        paneViewUI.setAlignment(Pos.CENTER);

        return paneViewUI;
    }

    /**
     * Sets up everything related to the UI
     * @return The initialized UI frame
     */
    private static VBox initUI() {
        VBox frame = new VBox();
        frame.setAlignment(Pos.CENTER);
        frame.setPrefWidth(PREF_UI_WIDTH);
        frame.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        frame.getChildren().addAll(
                initPaneViewUI(),
                new Separator(Orientation.HORIZONTAL),
                initAddComponentUI(),
                new Separator(Orientation.HORIZONTAL),
                initAddCompoundComponentUI());

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
        VBox addUI = initUI();

        window.setCenter(mainPane);
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
