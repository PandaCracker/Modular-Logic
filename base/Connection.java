package base;

/**
 * A basic representation of an object on the board which carries
 * a signal from one component output to another input
 * 
 * @author Lucas Peterson
 */
public class Connection {
    private Component sourceComponent;
    private int sourcePort;
    
    private Component destComponent;
    private int destPort;

    private boolean on;

    public Connection(Component sourceComponent, int sourcePort, Component destComponent, int destPort) {
        this.sourceComponent = sourceComponent;
        // At some point add check that the port number actually exists
        this.sourcePort = sourcePort;

        this.destComponent = destComponent;
        // Same check here
        this.destPort = destPort;

        this.on = false;
    }

    public Component getSourceComponent() {return sourceComponent;}
    public int getSourcePort() {return sourcePort;}
    public Component getDestComponent() {return destComponent;}
    public int getDestPort() {return destPort;}
    public boolean getState() {return on;}

    public void setState(boolean state) {
        this.on = state;
        destComponent.update();
    }
}
