package base;

public abstract class Component {
    /* Where the component is placed on the board */
    protected Coord coords;
    /* How large the component is on the board */
    protected Coord size;

    // Input signal fields
    protected int numInputs;
    protected Connection[] inputConnections;

    // Output signal fields
    protected int numOutputs;
    protected Connection[] outputConnections;

    // Getters for important fields
    public Coord getCoords() {return coords;}
    public Coord getSize() {return size;}
    public int getNumInputs() {return numInputs;}
    public int getNumOutputs() {return numOutputs;}

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
     */
    public void connect(int outputPort, LogicalComponent destComponent, int destPort) {
        Connection connection = new Connection(this, outputPort, destComponent, destPort);
        this.outputConnections[outputPort] = connection;
        destComponent.addInputConnection(connection);
    }
}
