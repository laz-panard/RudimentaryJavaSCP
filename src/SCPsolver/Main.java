package SCPsolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Constraints.LessThan;
import Constraints.NotEqualPlusC;

public class Main {
	
	

	public static void main(String[] args) {
		
		
		//Initial Restraining
		Map<String, Set> domains = new HashMap<String, Set>();
		
		Set<Integer> temp1 = new HashSet<Integer>();
		Set<Integer> temp2 = new HashSet<Integer>();
		Set<Integer> temp3 = new HashSet<Integer>();
		
		temp1.add(1);
		temp1.add(2);
		temp1.add(3);
		
		temp2.add(1);
		temp2.add(2);
		temp2.add(3);
		
		temp3.add(1);
		temp3.add(2);
		temp3.add(3);
		
		domains.put("x1", temp1);
		domains.put("x2", temp2);
		domains.put("x3", temp3);
		
		List<Constraint> constraints = new ArrayList<Constraint>();
		LessThan lessThan1 = new LessThan("x1", "x2");
		LessThan lessThan2 = new LessThan("x2", "x3");
		LessThan lessThan3 = new LessThan("x3", "x1");
		NotEqualPlusC x1Notx2Plus0 = new NotEqualPlusC("x1", "x2", 0);
		
		//ADDING CONSTRAINTS
		//constraints.add(lessThan1); // x1 < x2
		constraints.add(lessThan2); // x2 < x3
		//constraints.add(lessThan3); // x3 < x1
		constraints.add(x1Notx2Plus0); // x1 != x2
		
		Problem problem = new Problem(domains, constraints);
		
		System.out.println("Search has started");
		
		System.out.println(Problem.enumerate(problem) + " solutions have been found.");		
	}

}
