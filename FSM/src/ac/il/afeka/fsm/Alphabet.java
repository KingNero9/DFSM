package ac.il.afeka.fsm;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Alphabet implements Iterable<Character> {

	public static final Character EPSILON = new Character('\0');
	
	private List<Character> symbols;
	
	private Map<Character, Character> succ;
	
	/** 
	 * Creates a new alphabet from the given list of symbols. 
	We assume that the list does not contain duplicates. 
	
	<p>For example, the statement</p>
	
	<code>
	Alphabet abc = new Alphabet(new ArrayList<Character>;(Arrays.asList('a', 'b', 'c')));
	</code>
	
	<p>creates a new alphabet object that represents the alphabet {a,b,c} with the lexicographical 
	order given by a <; b <; c.</p>
	
	@param symbols a list of character symbols
	*/
	
	public Alphabet(List<Character> symbols) {
		
		this.symbols = symbols;
		
		this.succ = new HashMap<Character, Character>();
		
		Iterator<Character> p = symbols.iterator();
		
		Character current = null;
		
		if (p.hasNext()) 
			current = p.next();
		
		while(p.hasNext()) {
			succ.put(current, p.next());
		}
	}

	/** Creates a new alphabet from the string encoding of an alphabet. 
	 * 
	 * <p>The format of the encoding is</p>
	 * 
	 * <pre> &lt;char&gt; &lt;char&gt; ...  </pre>
	 *  
	 *  <p>That is, the symbols are characters, separated by whitespace. The lexicographical order of
	 *  the alphabet follows the order of the list. We assume that the list does not contain duplicates. 
	 *  For example, the statement</p>
	 *  
	 *  <code>
	 *  Alphabet abc = Alphabet.parse('a b c');
	 *  </code>
	 *  
	 *  <p>creates the alphabet <pre>{a,b,c}</pre> with the lexicographical order given by <pre>a &lt; b &lt; c.</pre>
	 
	 *	@param encoding a string encoding of an alphabet
	 *  @return the alphabet object representing the encoded alphabet
	 */
	public static Alphabet parse(String encoding) {
		
		Scanner scanner = new Scanner(encoding);
		
		List<Character> symbols = new ArrayList<Character>();
		
		while(scanner.hasNext()) {
			symbols.add(scanner.next().charAt(0));
		}
		
		scanner.close();
		
		return new Alphabet(symbols);
	}

	/** Prints the alphabet in set notation. 
	 * 
	 * <p>For example, the statements</p>
	 * <code> 
	 * Alphabet abc = new Alphabet(new ArrayList&lt;Character&gt;(Arrays.asList('a', 'b', 'c')));
	 * abc.prettyPrint(System.out);
	 * </code>
	 * <p>will print</p>
	 * <code>
	 * {a, b, c}
	 * </code>
	 * <p>on standard output.</p>
	 * 
	 * @param out	the print stream on which to print this alphabet
	 * 
	 */
	public void prettyPrint(PrintStream out) {

		out.print("{");

		Iterator<Character> p = symbols.iterator();
		
		if (p.hasNext()) {
			out.print(p.next());
		}
		
		while(p.hasNext()) {
			out.print(", ");
			out.print(p.next());
		}
		out.print("}");
	}

	/** Returns a string encoding of this alphabet. 
	 * 
	 * <p> The encoding is identical to the format that <code>Alphabet.parse</code>
	 * accepts. In other words, encode() and parse() are inverses:</p>
	 * <code>
	 * forall anAlphabet : Alphabet | Alphabet.parse(anAlphabet.encode()).equals(anAlphabet)
	 * </code>
	 * 
	 * @return a string encoding of this alphabet
	 */
	public String encode() {
		String encoding = "";
		
		Iterator<Character> p = symbols.iterator();

		if (p.hasNext())
			encoding = encoding + p.next();
		
		while (p.hasNext()) {
			encoding = encoding + " " + p.next();
		}
		return encoding;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Character> iterator() {
		return symbols.iterator();
	}

	/** Returns true if and only if symbol is a member of this alphabet.
	 * 
	 * @param symbol a character 
	 * @return true if and only if symbol is a member of this alphabet
	 */
	public boolean contains(Character symbol) {
		return symbols.contains(symbol);
	}
	
	/** Returns the first string in the lexicographical order of this alphabet (it's always the empty string).
	 
	 @return the empty string
	 */
	public String first() {
		return "";
	}
	
	/** Given a string from this alphabet, returns the next string in this alphabet's lexicographical order.
	 * 
	 * <p>For example, if alphabet is the alphabet {a,b,c} then</p>
	 * <code>
	 * alphabet.next("abc") 
	 * </code>
	 * <p>returns the string <code>"aca"</code>.</p>
	 * 
	 * @param string a string whose characters are members of this alphabet
	 * @return the next string in this alphabet's lexicographical order
	 */
	public String next(String string) {
		
		String nextString = "";
		
		int i = string.length() - 1;
		
		// abb      baa
		
		while(i >=0 && succ.get(string.charAt(i)) == null) {
			nextString = nextString + symbols.get(0);
			--i;
		}
		
		if (i >= 0) {
			nextString = nextString + succ.get(string.charAt(i));
			i--;
			while(i >= 0) {
				nextString = nextString + string.charAt(i);
				i--;
			}
		}
		else
			nextString = nextString + symbols.get(0);
		
		return new StringBuffer(nextString).reverse().toString();
	}
}
