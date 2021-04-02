import static org.junit.Assert.*;

import org.junit.Test;

import ac.il.afeka.fsm.DFSM;

public class TestPrettyPrinting {

	@Test
	public void testBasicPrinting() throws Exception {
		
		String encoding = "0 1/a b/0,a,0;0,b,1;1,a,0;1,b,1/0/1";
		
		DFSM anDFSM = new DFSM(encoding);
		
		anDFSM.prettyPrint(System.out);
		
		assertTrue(true);
	}
	
}
