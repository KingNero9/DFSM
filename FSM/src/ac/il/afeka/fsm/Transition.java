package ac.il.afeka.fsm;
import java.io.PrintStream;

public class Transition implements Comparable<Transition> {

	/**
	 * Creates a new transition from its components:
	 * 
	 * @param fromState	the state from which this transition exits
	 * @param symbol	the symbol that triggers this transition
	 * @param toState	the state into which this transition enters
	 */
	public Transition(State fromState, Character symbol, State toState) {
		this.fromState = fromState;
		this.symbol = symbol;
		this.toState = toState;
	}
	
	/**
	 * 
	 * @return the state from which this transition exits
	 */
	public State fromState() { return fromState; }
	
	/**
	 * 
	 * @return the input symbol symbol that triggers this transition
	 */
	public Character symbol() { return symbol; }
	
	/**
	 * 
	 * @return the state into which this transition enters
	 */
	public State toState() { return toState; }
	
	private State fromState;
	private Character symbol;
	private State toState;
	
	/**
	 * Prints a human readable description of this transition.
	 * 
	 * @param out	the print stream on which to print the description.
	 */
	public void prettyPrint(PrintStream out) {
		out.print("(");
		fromState.prettyPrint(out);
		out.print(", ");
		if (symbol == null)
			out.print("\u03B5");
		else
			out.print(symbol);
		out.print(", ");
		toState.prettyPrint(out);
		out.print(")");
	}

	/**
	 * Returns a string encoding of this transition.
	 * @return the encoded string
	 */
	public String encode() {
		return fromState.encode() + "," + (symbol == null ? "" : symbol) + "," + toState.encode();
	}

	@Override
	public int compareTo(Transition other) {
		
		int result = fromState.compareTo(other.fromState); 
		if (result != 0)
			return result;

		if (symbol == null)
			result = other.symbol == null ? 0 : -1;
		else if (other.symbol == null)
			result = symbol == null ? 0 : 1;
		else
			result = symbol.compareTo(other.symbol);
		
		if (result != 0)
			return result;
		
		return toState.compareTo(other.toState);
	}
}
