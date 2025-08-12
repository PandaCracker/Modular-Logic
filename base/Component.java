package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;

public class Component {
    // Rectangle object which holds all display and location info
    private final Rectangle rect;

    // Input signal fields
    private final int numInputs;
    private final Port[] inputPorts;

    // Output signal fields
    private final int numOutputs;
    private final Port[] outputPorts;

    private boolean inDrag;
    private double dragOffsetX;
    private double dragOffsetY;

    public Component(int x, int y, int width, int height, Color color, int numInputs, int numOutputs) {
        this.rect = new Rectangle(width * Simulation.CELL_SIZE, height * Simulation.CELL_SIZE);
        rect.setX(x * Simulation.CELL_SIZE);
        rect.setY(y * Simulation.CELL_SIZE);
        rect.setFill(color);

        rect.setOnDragDetected(e -> {
            inDrag = true;
            dragOffsetX = e.getX() - rect.getX();
            dragOffsetY = e.getY() - rect.getY();
        });

        rect.setOnMouseDragged(e -> {
            if (inDrag) {
                move(e.getX() - dragOffsetX, e.getY() - dragOffsetY);
            }
        });

        rect.setOnMouseReleased(e -> {
            if (inDrag) {
                move(e.getSceneX() - dragOffsetX, e.getSceneY() - dragOffsetY);
                inDrag = false;
            }
        });

        this.numInputs = numInputs;
        this.numOutputs = numOutputs;

        this.inputPorts = new Port[numInputs];
        for (int i = 0; i < numInputs; i++) {
            inputPorts[i] = new Port(this, Port.PortType.INPUT, i);
        }

        this.outputPorts = new Port[numOutputs];
        for (int i = 0; i < numOutputs; i++) {
            outputPorts[i] = new Port(this, Port.PortType.OUTPUT, i);
        }

        this.inDrag = false;
        this.dragOffsetX = 0;
        this.dragOffsetY = 0;
    }

    // Getters for important fields
    public Rectangle getRect() {return rect;}

    public int getNumInputs() {return numInputs;}
    public Port[] getInputPorts() {return inputPorts;}
    public Port getInputPort(int inPort) {
        return inputPorts[inPort];
    }

    public int getNumOutputs() {return numOutputs;}
    public Port[] getOutputPorts() {return outputPorts;}
    public Port getOutputPort(int outPort) {
        return outputPorts[outPort];
    }

    public Port[] getAllPorts() {
        Port[] all =  Arrays.copyOf(inputPorts, numInputs + numOutputs);
        System.arraycopy(outputPorts, 0, all, numInputs, numOutputs);
        return all;
    }

    /**
     * Create a new Connection from this Component to another
     * @param outputPortNum The port number the output signal will come from
     * @param destComponent The component the signal is going to
     * @param destPortNum The port number the output signal will go to on the destination Component
     */
    public void connect(int outputPortNum, Component destComponent, int destPortNum) {
        outputPorts[outputPortNum].connectTo(destComponent.getInputPort(destPortNum));
    }

    public void move(double sceneX, double sceneY) {
        rect.setX(sceneX);
        rect.setY(sceneY);
        Arrays.stream(getAllPorts()).forEach(Port::updatePosition);
    }

    @Override
    public String toString() {
        return "Component at " + rect.getX() + ", " + rect.getY();
    }
}
