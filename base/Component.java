package base;

/**
 * A basic representation of any object which sits on the simulation board
 * and performs logical operations
 * 
 * @author Lucas Peterson
 */
public abstract class Component {
    /* Where the component is placed on the board */
    private Coord coords;
    /* How large the component is on the board */
    private Coord size;

    // Input signal fields
    private int numInputs;
    private Connection inputConnections[] = new Connection[numInputs];
    
    // Output signal fields
    private int numOutputs;
    private Connection outputConnections[] = new Connection[numOutputs];

    // Getters for important fields
    public Coord getCoords() {return coords;}
    public Coord getSize() {return size;}
    public int getNumInputs() {return numInputs;}
    public int getNumOutputs() {return numOutputs;}

    /**
     * Add a component to this one as an input through an already created Connection object
     * @param connection The Connection object which connects the two desired components
     */
    public void addAsInput(Connection connection) {
        if (inputConnections[connection.getDestPort()] == null) {
            inputConnections[connection.getDestPort()] = connection;
        } else {
            System.out.println("Port " + connection.getDestPort() + " already in use on " + this);
        }
    }

    /**
     * Create a new Connection from this component to another
     * @param outputPort The port number the output signal will come from
     * @param destComponent The component the signal is going to
     * @param destPort The port number the output signal will go to on the destComponent
     */
    public void addOutput(int outputPort, Component destComponent, int destPort) {
        if (outputConnections[outputPort] == null) {
            Connection connection = new Connection(this, outputPort, destComponent, outputPort);
            outputConnections[outputPort] = connection;
            addAsInput(connection);
        }
    }

    /**
     * A method which will perform whatever operation this component does,
     * then send the resulting signals to connected outputs
     */
    public abstract void runInputs();
}