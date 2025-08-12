package base;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Component {
    // Rectangle object which holds location and size info
    private Rectangle rect;
    // The color of the component on the sim board
    private final Color color;

    // Input signal fields
    private final int numInputs;
    private final Port[] inputPorts;

    // Output signal fields
    private final int numOutputs;
    private final Port[] outputPorts;

    public Component(int x, int y, int width, int height, Color color, int numInputs, int numOutputs) {
        this.rect = new Rectangle(width * Simulation.CELL_SIZE, height * Simulation.CELL_SIZE);
        rect.setX(x * Simulation.CELL_SIZE);
        rect.setY(y * Simulation.CELL_SIZE);

        this.color = color;

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
    }

    // Getters for important fields
    public Rectangle getRect() {return rect;}
    public Color getColor() {return color;}

    public int getNumInputs() {return numInputs;}
    public Port[] getInputPorts() {return inputPorts;}
    public int getNumOutputs() {return numOutputs;}
    public Port[] getOutputPorts() {return outputPorts;}

    public Port getInputPort(int inPort) {
        return inputPorts[inPort];
    }
    public Port getOutputPort(int outPort) {
        return outputPorts[outPort];
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

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        drawPorts(gc);
    }

    public void drawPorts(GraphicsContext gc) {
        for (Port inPort : inputPorts) {
            inPort.draw(gc);
        }
        for (Port outPort : outputPorts) {
            outPort.draw(gc);
        }
    }

    public void moveComponent(double sceneX, double sceneY) {
        rect.setX(sceneX);
        rect.setY(sceneY);
    }

    public void update() {
    }

    @Override
    public String toString() {
        return "Component at " + rect.getX() + ", " + rect.getY();
    }
}
