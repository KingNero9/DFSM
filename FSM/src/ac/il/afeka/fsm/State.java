package ac.il.afeka.fsm;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class State implements Comparable<State> {

	public abstract void prettyPrint(PrintStream out);

	public abstract String toString(); 
	
	public abstract String encode();
 
	static public void prettyPrintStateSet(Collection<State> states, PrintStream out) {

		out.print("{");
		
		Iterator<State> p = states.iterator();
		
		if (p.hasNext()) {
			p.next().prettyPrint(out);
		}
		
		while(p.hasNext()) {
			out.print(", ");
			p.next().prettyPrint(out);
		}
		
		out.print("}");
	}
	
	public static String encodeStateSet(Set<State> states) {
		String encoding = "";
		
		List<State> statesList = new ArrayList<State>(states);
		Collections.sort(statesList);
		
		Iterator<State> p = statesList.iterator();

		if (p.hasNext()) {
			encoding = encoding + p.next().encode();
		}
		
		while(p.hasNext()) {
			encoding = encoding + " " + p.next().encode();
		}
		
		return encoding;
	}

}