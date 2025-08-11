package base;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Component {
    // Rectangle object which holds location and size info
    protected Rectangle rect;
    // The color of the component on the sim board
    protected Color color;

    // Input signal fields
    protected int numInputs;
    protected Connection[] inputConnections;

    // Output signal fields
    protected int numOutputs;
    protected Connection[] outputConnections;

    // Getters for important fields
    public Rectangle getRect() {return rect;}
    public Color getColor() {return color;}
    public int getNumInputs() {return numInputs;}
    public int getNumOutputs() {return numOutputs;}

    public Connection getInputConnection(int inPort) {
        return inputConnections[inPort];
    }
    public Connection getOutputConnection(int outPort) {
        return outputConnections[outPort];
    }

    /**
     * Adds a Connection to this Component as an input
     * @param connection The already created Connection with this Component as a destination
     */
    public void addInputConnection(Connection connection) {
        this.inputConnections[connection.getDestPort()] = connection;
    }

    /**
     * Create a new Connection from this Component to another
     * @param outputPort The port number the output signal will come from
     * @param destComponent The component the signal is going to
     * @param destPort The port number the output signal will go to on the destination Component
     * @return The Connection created
     */
    public Connection connect(int outputPort, Component destComponent, int destPort) {
        Connection connection = new Connection(this, outputPort, destComponent, destPort);
        this.outputConnections[outputPort] = connection;
        destComponent.addInputConnection(connection);
        return connection;
    }

    public abstract void update();
}
