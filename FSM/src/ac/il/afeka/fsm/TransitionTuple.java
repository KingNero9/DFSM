package ac.il.afeka.fsm;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TransitionTuple {
	
	public Integer fromStateId;
	public Character symbol;
	public Integer toStateId;

	public TransitionTuple(Integer fromStateId, Character symbol, Integer toStateId) {
		this.fromStateId = fromStateId; 
		this.symbol = symbol;
		this.toStateId = toStateId;
	}

	public static TransitionTuple parseTuple(String encoding) {
		
		Scanner scanner = new Scanner(encoding);
		scanner.useDelimiter("\\s*,\\s*");
		
		Integer fromStateId = scanner.nextInt();
		Character symbol;
		String symbolOrNothing = scanner.next();
		if (symbolOrNothing.length() == 0)
			symbol = Alphabet.EPSILON;
		else
			symbol = symbolOrNothing.charAt(0);
		Integer toStateId = scanner.nextInt();
		scanner.close();
		return new TransitionTuple(fromStateId, symbol, toStateId);
	}

	public static Set<TransitionTuple> parseTupleList(String encoding) {
		
		Scanner scanner = new Scanner(encoding);
		scanner.useDelimiter("\\s*;\\s*");
		
		Set<TransitionTuple> tuples = new HashSet<TransitionTuple>();
		
		while(scanner.hasNext()) {
			tuples.add(parseTuple(scanner.next()));
		}
		scanner.close();
		return tuples;
	}

	public Integer fromStateId() {
		return fromStateId;
	}

	public Character symbol() {
		return symbol;
	}

	public Integer toStateId() {
		return toStateId;
	}
}
