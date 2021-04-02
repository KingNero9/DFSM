import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import ac.il.afeka.fsm.Alphabet;

public class TestAlphabetEnumeration {

	@Test
	public void testFirst() {
		
		Alphabet alphabet = new Alphabet(new ArrayList<Character>(Arrays.asList('a','b')));
		
		assertEquals("", alphabet.first());
		
	}

	@Test
	public void testNext() {

		Alphabet alphabet = new Alphabet(new ArrayList<Character>(Arrays.asList('a','b')));

		String next = alphabet.first();
		
		next = alphabet.next(next);
		
		assertEquals("a", next);
	}

	@Test
	public void testNextNext() {

		Alphabet alphabet = new Alphabet(new ArrayList<Character>(Arrays.asList('a','b')));

		String next = alphabet.first();
		
		next = alphabet.next(next);
		next = alphabet.next(next);
		
		assertEquals("b", next);
	}

	@Test
	public void testNext3() {

		Alphabet alphabet = new Alphabet(new ArrayList<Character>(Arrays.asList('a','b')));

		String next = alphabet.first();
		
		next = alphabet.next(next);
		next = alphabet.next(next);
		next = alphabet.next(next);
		
		assertEquals("aa", next);
	}

	@Test
	public void testNext4() {

		Alphabet alphabet = new Alphabet(new ArrayList<Character>(Arrays.asList('a','b')));

		String next = alphabet.first();
		
		next = alphabet.next(next);
		next = alphabet.next(next);
		next = alphabet.next(next);
		next = alphabet.next(next);
		
		assertEquals("ab", next);
	}

	@Test
	public void testNext100() {

		Alphabet alphabet = new Alphabet(new ArrayList<Character>(Arrays.asList('a','b')));

		String next = alphabet.first();
		
		for(int i = 0; i < 100; i++)
			next = alphabet.next(next);
		
		/*
		
		100 + 1 - power(2, floor(log(2, 100+1)))
		= 
		101 - power(2, 6)
		=
		101 - 64
		= 
		37
		
		37 in binary is 32+4+1 = 100101
		 
		*/
		
		assertEquals("baabab",next);
	}

}
