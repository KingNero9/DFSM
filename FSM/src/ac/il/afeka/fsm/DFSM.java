package ac.il.afeka.fsm;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class DFSM {

	protected TransitionFunction transitions;
	protected Set<State> states;
	protected Set<State> acceptingStates;
	protected State initialState;
	protected Alphabet alphabet;
	
	/**
	 * Builds a DFSM from a string representation (encoding) 
	 *  
	 * @param encoding	the string representation of a DFSM
	 * @throws Exception if the encoding is incorrect or if it does not represent a deterministic machine
	 */
	public DFSM(String encoding) throws Exception {
		parse(encoding);
		
		transitions.verifyTransitionMapping(states, alphabet);
		transitions.veryifyTotal(states, alphabet);
		transitions.verifyNoEpsilonTransitions();
	}
	
	/**
	 * Build a DFSM from its components
	 * 
	 * @param states			the set of states for this machine
	 * @param alphabet			this machine's alphabet
	 * @param transitions		the transition mapping of this machine
	 * @param initialState		the initial state (must be a member of states)
	 * @param acceptingStates	the set of accepting states (must be a subset of states)
	 * @throws Exception if the components do not represent a deterministic machine
	 */
	public DFSM(Set<State> states, Alphabet alphabet, TransitionFunction transitions, State initialState,
			Set<State> acceptingStates) throws Exception {
		
		this.states = states;
		
		this.alphabet = alphabet;
		this.transitions = transitions;
		this.initialState = initialState;
		this.acceptingStates = acceptingStates;
		
		transitions.verifyTransitionMapping(states, alphabet);
		transitions.veryifyTotal(states, alphabet);
		transitions.verifyNoEpsilonTransitions();
	}
	
	protected DFSM() {
		// for internal use
	}

	/** Overrides this machine with the machine encoded in string.
	 * 
	 *  <p>Here's an example of the encoding:</p>
	 <pre>
	0 1/a b/0 , a , 0; 0,b, 1 ;1, a, 0 ; 1, b, 1/0/ 1
	</pre>
	<p>This is the encoding of a finite state machine with two states (identified as 0 and 1), 
	an alphabet that consists of the two characters 'a' and 'b', and four transitions:</p>
	<ol>
	<li>From state 0 on character a it moves to state 0</li>
	<li>from state 0 on character b it moves to state 1,</li>
	<li>from state 1 on character a it moves to state 0,</li>
	<li>from state 1 on character b it moves to state 1.</li>
	</ol>
	<p>The initial state of this machine is 0, and the set of accepting states consists of 
	just one state 1. Here is the format in general:</p>
	  
	 <pre>
	 {@code
	<states> / <alphabet> / <transitions> / <initial state> / <accepting states>
	}
	</pre>
	
	where:
	
	<pre>
	{@code
	<alphabet> is <char> <char> ...
	
	<transitions> is <transition> ; <transition> ...
	
	<transition> is from , char, to
	
	<initial state> is an integer
	
	<accepting states> is <state> <state> ...
	
	<state> is an integer
	}
	</pre>
	
	@param string the string encoding 
	@throws Exception if the string encoding is invalid
	*/
	public void parse(String string) throws Exception {
		
		Scanner scanner = new Scanner(string);
		
		scanner.useDelimiter("\\s*/");
			
		Map<Integer, State> states = new HashMap<Integer, State>();
		
		for(Integer stateId : IdentifiedState.parseStateIdList(scanner.next())) {
			states.put(stateId, new IdentifiedState(stateId));
		}

		Alphabet alphabet = Alphabet.parse(scanner.next());
		
		Set<Transition> transitions = new HashSet<Transition>();
		
		for (TransitionTuple t: TransitionTuple.parseTupleList(scanner.next())) {
			transitions.add(new Transition(states.get(t.fromStateId()), t.symbol(), states.get(t.toStateId())));
		}
		
		State initialState = states.get(scanner.nextInt());
		
		Set<State> acceptingStates = new HashSet<State>();

		if (scanner.hasNext())
			for(Integer stateId : IdentifiedState.parseStateIdList(scanner.next())) {
				acceptingStates.add(states.get(stateId));
			}
		
		scanner.close();
		
		this.states = new HashSet<State>(states.values());
		this.alphabet = alphabet;
		this.transitions = new TransitionFunction(transitions);
		this.initialState = initialState;
		this.acceptingStates = acceptingStates;
		
	}

	/** Encodes this state machine as a string
	 * 
	 * @return the string encoding of this state machine
	 */
	public String encode() {
		return  State.encodeStateSet(new HashSet<State>(states)) + "/" +
				alphabet.encode() + "/" + 
				transitions.encode() + "/" + 
				initialState.encode() + "/" +
				State.encodeStateSet(acceptingStates);
	}
	
	/** Prints a set notation description of this machine.
	 * 
	 * <p>To see the Greek symbols on the console in Eclipse, go to Window -&gt; Preferences -&gt; General -&gt; Workspace 
	 * and change <tt>Text file encoding</tt> to <tt>UTF-8</tt>.</p>
	 * 
	 * @param out the output stream on which the description is printed.
	 */
	public void prettyPrint(PrintStream out) {
		out.print("K = ");
		State.prettyPrintStateSet(states, out);
		out.println("");
		
		out.print("\u03A3 = ");
		alphabet.prettyPrint(out);
		out.println("");
		
		out.print("\u03B4 = ");
		transitions.prettyPrint(out);
		out.println("");
		
		out.print("s = ");
		initialState.prettyPrint(out);
		out.println("");
		
		out.print("A = ");
		State.prettyPrintStateSet(acceptingStates, out);
		out.println("");		
	}

	/** Returns a minimal version of this state machine
	 * 
	 * @return a DFSM that recognizes the same language as this machine, but has a minimal number of states.
	 */
	public DFSM minimize() {
		return removeUnreachableStates().minimizeWithNoUnreachableStates();
	}
	
	/** Returns a version of this state machine with all the unreachable states removed.
	 * 
	 * @return DFSM that recognizes the same language as this machine, but has no unreachable states.
	 */
	public DFSM removeUnreachableStates() {

		Set<State> reachableStates = reachableStates();

		Set<Transition> transitionsToReachableStates = new HashSet<Transition>();
		
		for(Transition t : transitions.transitions()) {
			if (reachableStates.contains(t.fromState()) && reachableStates.contains(t.toState()))
				transitionsToReachableStates.add(t);
		}
		
		Set<State> reachableAcceptingStates = new HashSet<State>();
		for(State s : acceptingStates) {
			if (reachableStates.contains(s))
				reachableAcceptingStates.add(s);
		}
		
		DFSM aDFSM = new DFSM();
		
		aDFSM.states = reachableStates;
		aDFSM.alphabet = alphabet;
		aDFSM.transitions = new TransitionFunction(transitionsToReachableStates);
		aDFSM.initialState = initialState;
		aDFSM.acceptingStates = reachableAcceptingStates;
		
		return aDFSM;
	}
	
	// returns a set of all states that are reachable from the initial state
	
	private Set<State> reachableStates() {
		
		Set<State> reachable = new HashSet<State>();

		Set<State> newlyReachable = new HashSet<State>();

		newlyReachable.add(initialState);

		while(!newlyReachable.isEmpty()) {
			reachable.addAll(newlyReachable);
			newlyReachable = new HashSet<State>();
			for(State state : reachable) {
				for(Character symbol : alphabet) {
					State s = transitions.applyTo(state, symbol);
					if (!reachable.contains(s))
						newlyReachable.add(s);
				}
			}
		}
		
		return reachable;
	}
 
	private DFSM minimizeWithNoUnreachableStates()  {
	
		Map<State, State> equivalent = equivalentStates();
		
		Set<Transition> minimalTransitions = new HashSet<Transition>();
		
		for(Transition t : transitions.transitions()) {
			
				minimalTransitions.add(new Transition(equivalent.get(t.fromState()), t.symbol(), equivalent.get(t.toState())));
		}
		
		Set<State> minimalAccepting = new HashSet<State>();
		for(State s : acceptingStates) {
			minimalAccepting.add(equivalent.get(s));
		}

		DFSM aDFSM = new DFSM();
		
		aDFSM.states = new HashSet<State>(equivalent.values());
		aDFSM.alphabet = alphabet;
		aDFSM.transitions = new TransitionFunction(minimalTransitions);
		aDFSM.initialState = equivalent.get(initialState);
		aDFSM.acceptingStates = minimalAccepting;
		
		return aDFSM;
	}
	
	// returns a map that maps each state to a representative of their equivalence class.
	
	private Map<State, State> equivalentStates() {

		 Map<State, State> prevEcc = new HashMap<State, State>();

		 Map<State, State> ecc = new HashMap<State, State>();
		 
		 /* We will represent each equivalence classes with a representative member 
and use a dictionary to map each state to this representative.

First we create two equivalence classes, put all the accepting states in the first
and all the non accepting states in the second. */

		 if (!acceptingStates.isEmpty()) {
			 State rep = acceptingStates.iterator().next();
			 for(State state : acceptingStates)
					 ecc.put(state, rep);
		 }
		 
		 Set<State> nonAcceptingStates = new HashSet<State>(states);
		 nonAcceptingStates.removeAll(acceptingStates);
		 
		 if (!nonAcceptingStates.isEmpty()) {
			 State rep = nonAcceptingStates.iterator().next();
			 for(State state : nonAcceptingStates)
				 ecc.put(state, rep);
		 }
		 
/*		The invariant for the following loop is:

		1. for any s -> r association in ecc, s is equivalent to r in prevEcc,
		2. for any input symbol c, the destination of the transition from s on c is equivalent (in prevEcc) to the destiation of the transition from r to c,
		3. for any two values r1, r2 in ecc, they are not equivalent to each other in prevEcc,
		4. all the equivalence classes in prevEcc have a representative in ecc.

*/

		 while(!prevEcc.equals(ecc)) {
		
			 prevEcc = ecc;
			 
			 ecc = new HashMap<State, State>();
			 
/*		To establish the invariant we will set ecc with the associations of the form

		r -> r where r is a representative from prevEcc. 

		This will initially satisfy the invariant because our action establishes 
		condition (4) and conditions (1) and (2) and (3) are correct by induction 
		from the validity of prevEcc."
*/
			 for(State state : prevEcc.values()) {
				 ecc.put(state,  state);
			 }
			 
			 for(State state : states) {
				 
/*		For each state s, we look in ecc for a rep r that is equivalent to s in prevEcc 
		(that is, s's rep in prevEcc is r and for every input they transition to the same 
		equivalence class in prevEcc) and add s to ecc with the same equivalence rep. 
		If no state is equivalent to s, we add s to ecc as its own rep. */
			 
				 Iterator<State> p = ecc.keySet().iterator();
				 State rep = null;
				 boolean equivalent = false;
				 while(p.hasNext() && !equivalent) {
					 rep = p.next();
					 equivalent = equivalentIn(prevEcc, state, rep);
				 }
				 if (equivalent) 
					 ecc.put(state, rep);
				 else
					 ecc.put(state,  state);
			 }
		 }
		 
		 return ecc;
	}
	
	private boolean equivalentIn(Map<State, State> equivRel, State s, State t) {
		if (equivRel.get(s) != equivRel.get(t) )
			return false;
		
		boolean equiv = true;
		Iterator<Character> p = alphabet.iterator();
		Character symbol = null;
		while (p.hasNext() && equiv) {
			symbol = p.next();
			equiv = equivRel.get(transitions.applyTo(s, symbol)) == equivRel.get(transitions.applyTo(t,  symbol));
		}
		
		return equiv;
	}

	// Traverse the state machine graph in a depth first fashion, fixing the traversal order of the transitions 
	// according to the order of the alphabet symbols. Relabel the states with a running integer.

	/** Returns a canonic version of this machine. 

<p>The canonic encoding of two minimal state machines that recognize the same language is identical.</p>

@return a canonic version of this machine. 
*/
	
	public DFSM toCanonicForm() {
	
		Set<Transition> canonicTransitions = new HashSet<Transition>();
		Stack<State> todo = new Stack<State>();
		Map<State, State> canonicStates = new HashMap<State, State>();
		Integer free = 0;
		
		todo.push(initialState);
		canonicStates.put(initialState, new IdentifiedState(free));
		free++;
		
		while (!todo.isEmpty()) {
			State top = todo.pop();
			for(Character symbol : alphabet) {
				State nextState = transitions.applyTo(top, symbol);
				if (!canonicStates.containsKey(nextState)) {
					canonicStates.put(nextState, new IdentifiedState(free));
					todo.push(nextState);
					free++;
				}
				canonicTransitions.add(new Transition(canonicStates.get(top), symbol, canonicStates.get(nextState)));
			}
		}

		Set<State> canonicAcceptingStates = new HashSet<State>();
		for(State s : acceptingStates) {
			canonicAcceptingStates.add(canonicStates.get(s));
		}
				
		DFSM aDFSM = new DFSM();
		
		aDFSM.states = new HashSet<State>(canonicStates.values());
		aDFSM.alphabet = alphabet;
		aDFSM.transitions = new TransitionFunction(canonicTransitions);
		aDFSM.initialState = canonicStates.get(initialState);
		aDFSM.acceptingStates = canonicAcceptingStates;
		
		return aDFSM;
	}
	
	
	/** Returns true if and only if input belongs to this machine's language. 
	 * 
	 * @param input a string whose characters are members of this machine's alphabet
	 * @return a boolean that indicates if the input is a member of this machine's language or not
	 */
	public boolean compute(String input) {
		 char[] newArr = input.toCharArray();
		 State currState = initialState;
		 for(int i=0; i<newArr.length;i++) {
			 currState = transitions.applyTo(currState, newArr[i]);
		 }
		 return acceptingStates.contains(currState);
	}
}
