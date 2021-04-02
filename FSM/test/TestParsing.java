import static org.junit.Assert.*;

import org.junit.Test;

import ac.il.afeka.fsm.DFSM;

public class TestParsing {

	@Test
	public void test() throws Exception {
		
		String encoding = "0 1/a b/0,a,0;0,b,1;1,a,0;1,b,1/0/1";
		
		DFSM aDFSM = new DFSM(encoding);
		
		assertEquals(encoding, aDFSM.encode());
	}
	
	@Test
	public void testNoAcceptingStates() throws Exception {

		String encoding = "0/a b/0,a,0;0,b,0/0/";
		
		DFSM aDFSM = new DFSM(encoding);
		
		assertEquals(encoding, aDFSM.encode());

	}
}
