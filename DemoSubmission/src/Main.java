import java.util.Arrays;
import java.util.List;

import ac.il.afeka.Submission.Submission;
import ac.il.afeka.fsm.DFSM;

public class Main implements Submission, Assignment1 {
	public static void main(String[] args) {
		String dfsmEncoding = "0 1/a b/0 , a , 0; 0,b, 1 ;1, a, 0 ; 1, b, 1/0/ 1";
        DFSM mechine = null;
        try {
            mechine = new DFSM(dfsmEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String inputTrue = "aab";
        String inputFalse = "bba";

        System.out.println(mechine.compute(inputTrue));
        System.out.println(mechine.compute(inputFalse));
	}
	@Override
	public List<String> submittingStudentIds() {
		return Arrays.asList("0123", "4567");
	}

	@Override
	public boolean compute(String dfsmEncoding, String input) throws Exception {
		 DFSM mechine = new DFSM(dfsmEncoding);
		 return mechine.compute(input);
	}
}
