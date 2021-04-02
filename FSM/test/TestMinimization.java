import static org.junit.Assert.*;

import org.junit.Test;

import ac.il.afeka.fsm.DFSM;

public class TestMinimization {

	@Test
	public void testUnreachableStates() throws Exception {
		
		String original = "0 1/a b/0,a,0;0,b,0;1,a,0;1,b,1/0/0";

		String minimal = "0/a b/0,a,0;0,b,0/0/0";

		assertEquals(minimal, new DFSM(original).removeUnreachableStates().encode());

	}

	@Test
	public void testRemoveUnreachableIdempotency() throws Exception {
		
		String original = "1 2 3 4 5 6/a b/1,a,2;1,b,4;2,a,3;2,b,6;3,a,2;3,b,4;4,a,6;4,b,5;5,a,2;5,b,4;6,a,6;6,b,6/1/2 4";
		
		assertEquals(original, new DFSM(original).removeUnreachableStates().encode());
	}

	@Test
	public void testExample5dot28() throws Exception {
		
		String original = "1 2 3 4 5 6/a b/1,a,2;1,b,4;2,a,3;2,b,6;3,a,2;3,b,4;4,a,6;4,b,5;5,a,2;5,b,4;6,a,6;6,b,6/1/2 4";
		String minimal = "0 1 2 3/a b/0,a,1;0,b,2;1,a,0;1,b,3;2,a,3;2,b,0;3,a,3;3,b,3/0/1 2";
		
		assertEquals(minimal, new DFSM(original).minimize().toCanonicForm().encode());
	}


	@Test
	public void testMinimizationIdempotency() throws Exception {
		
		String original = "1 2 3 4 5 6/a b/1,a,2;1,b,4;2,a,3;2,b,6;3,a,2;3,b,4;4,a,6;4,b,5;5,a,2;5,b,4;6,a,6;6,b,6/1/2 4";
		String minimal = "0 1 2 3/a b/0,a,1;0,b,2;1,a,0;1,b,3;2,a,3;2,b,0;3,a,3;3,b,3/0/1 2";
		
		assertEquals(minimal, new DFSM(original).minimize().minimize().toCanonicForm().encode());
	}
}
