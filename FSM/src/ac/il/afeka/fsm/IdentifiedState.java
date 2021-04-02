package ac.il.afeka.fsm;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class IdentifiedState extends State {

	private Integer id;

	public IdentifiedState(Integer i) {
		this.id = i;
	}

	static public Set<Integer> parseStateIdList(String encoding) {
		Scanner scanner = new Scanner(encoding);
		
		Set<Integer> ids = new HashSet<Integer>();
		
		while(scanner.hasNext()) {
			ids.add(scanner.nextInt());
		}
		
		scanner.close();
		
		return ids;
	}

	public void prettyPrint(PrintStream out) {
		out.print(id);
	}

	public String toString() {
		return "id=" + id;
	}
	
	public String encode() {
		return "" + id;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifiedState other = (IdentifiedState) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(State other) {
		return id.compareTo(((IdentifiedState)other).id);
	}
}
