package ac.il.afeka.fsm;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransitionFunction {

	private Map<State, Map<Character, State> > delta;
	
	/** 
	 * Creates a transition function from a set of transitions.
	 * @param transitions a set of transitions
	 */
	public TransitionFunction(Set<Transition> transitions) {
		
		Map<State, Map<Character, State> > delta = new HashMap<State, Map<Character, State> >();
		
		for(Transition t : transitions) {
			if (!delta.containsKey(t.fromState()))
					delta.put(t.fromState(), new HashMap<Character, State>());
			delta.get(t.fromState()).put(t.symbol(), t.toState());
		}
		
		this.delta = delta;
	}
	
	/**
	 * Returns the next state that this machine moves to.
	 * 
	 * <p>Returns the state that this machine move to, 
	 * when it is currently in <code>fromState</code> and 
	 * reads <code>symbol</code> from its input.
	 * </p>
	 *    
	 * @param fromState	the current state
	 * @param symbol	the input symbol
	 * @return			the next state
	 */
	public State applyTo(State fromState, Character symbol) {
		return delta.get(fromState).get(symbol);
	}
	
	/**
	 * Check if a transition exists.
	 * 
	 * <p>Returns true if and only if there is a transition from <code>fromState</code> on <code>symbol</code>.</p>
	 * 
	 * @param fromState a state
	 * @param symbol	an input symbol
	 * @return		true if and only if there is a transition
	 */
	public boolean maps(State fromState, Character symbol) {
		return delta.containsKey(fromState) && delta.get(fromState).containsKey(symbol);
	}

	/**
	 *  
	 * @return the set of transitions of this function
	 */
	public Set<Transition> transitions() {
		
		Set<Transition> transitions = new HashSet<Transition>();
		
		for(Map.Entry<State, Map<Character, State> > p : delta.entrySet()) {
			for(Map.Entry<Character, State> q : p.getValue().entrySet()) {
				transitions.add(new Transition(p.getKey(), q.getKey(), q.getValue()));
			}
		}
		
		return transitions;
	}
	
	/** 
	 * Prints a humanly readable description of this transition function.
	 * @param out the print stream on which to print the description.
	 */
	public void prettyPrint(PrintStream out) {

		out.print("{");
		
		Iterator<Transition> p = transitions().iterator();
		
		if (p.hasNext()) {
			p.next().prettyPrint(out);
		}
		
		while(p.hasNext()) {
			out.print(", ");
			p.next().prettyPrint(out);
		}
		
		out.print("}");

	}

	/**
	 *  
	 * @return a string encoding of this transition function
	 */
	public String encode() {
		
		String encoding = "";
		
		List<Transition> transitionsList = new ArrayList<Transition>(transitions());
		Collections.sort(transitionsList);
		
		Iterator<Transition> p = transitionsList.iterator();
		
		if (p.hasNext()) {
			encoding = encoding + p.next().encode();
		}
		
		while(p.hasNext()) {
			encoding = encoding + ";" + p.next().encode();
		}
 		
		return encoding;
	}

	/** Checks that the transition function contains valid states and symbols. 
	 * 
	 * @param states the states of the state machine that holds this mapping
	 * @param alphabet the alphabet of the state machine that holds this mapping	 
	 * @throws Exception if it finds a transition with a state that does not belong to the machine or a symbol that does not belong to the machine's alphabet.
	 */
	public void verifyTransitionMapping(Set<State> states, Alphabet alphabet) throws Exception {
		
		for(Map.Entry<State, Map<Character, State> > p : delta.entrySet()) {
			for(Map.Entry<Character, State> q : p.getValue().entrySet()) {
				State fromState = p.getKey();
				Character symbol = q.getKey();
				State toState = q.getValue();
				if (!states.contains(fromState)) {
					throw new Exception("Transition mapping contains a state (id " + fromState + ") that is not a part of the state machine.");
				}
				
				if (symbol != null && !alphabet.contains(symbol))
					throw new Exception("Transition contains symbol " + symbol +" that is not a part of the machine's alphabet");
				
				if (!states.contains(toState)) {
					throw new Exception("Transition mapping contains a state (id " + toState + ") that is not a part of the state machine.");
				}
			}
		}
	}

	/** Checks that the transition function is total.
	 * 
	 *  @param states	all the states of the DFSM
	 *  @param alphabet	the alphabet of the DFSM
	 * @throws Exception if there is a state that does not have a transition on all the symbols in the machine's alphabet.
	 */
	public void veryifyTotal(Set<State> states, Alphabet alphabet) throws Exception {
		
		for(Character symbol : alphabet) {
			for(State state: states) {
				if (!maps(state, symbol))
					throw new Exception("The transition function is missing a transition from state " + state + " on symbol " + symbol);
			}
		}
	}

	/** Checks that the transition function has no epsilon transitions.
	 * 
	 * @throws Exception if there is an epsilon transition in the function 
	 */
	public void verifyNoEpsilonTransitions() throws Exception {
		
		for(Map<Character, State> m : delta.values()) {
			if (m.keySet().contains(Alphabet.EPSILON))
				throw new Exception("The transition function has an epsilon transition");
		}
	}
}
