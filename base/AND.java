package base;

public class AND extends LogicalComponent {

    public AND(Coord coords) {
        this.coords = coords;
        this.size = new Coord(2, 3);

        this.numInputs = 2;
        this.numOutputs = 1;

        this.inputConnections = new Connection[numInputs];
        this.outputConnections = new Connection[numOutputs];
    }

    @Override
    public void runInputs() {
        outputConnections[0].setState(inputConnections[0].getState() && inputConnections[1].getState());
    }
}