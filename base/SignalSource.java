package base;

public class SignalSource extends Component{
    private boolean on;

    public SignalSource(Coord coords) {
        this.coords = coords;
        this.size = new Coord(1, 1);

        this.numInputs = 0;
        this.numOutputs = 1;

        this.inputConnections = new Connection[numInputs];
        this.outputConnections = new Connection[numOutputs];

        this.on = false;
    }

    public void toggle() {
        on ^= true;
        outputConnections[0].setState(on);
    }
}
