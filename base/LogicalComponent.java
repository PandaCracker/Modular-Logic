package base;

/**
 * A basic representation of any object which sits on the simulation board
 * and performs logical operations
 * 
 * @author Lucas Peterson
 */
public abstract class LogicalComponent extends Component{
    /**
     * A method which will perform whatever operation this component does,
     * then send the resulting signals to connected outputs
     */
    public abstract void runInputs();
}