package SCPsolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Constraints.LessThan;

public class Main {
	
	public static HashMap<String, Set> deepCopy(HashMap<String, Set> toCopy){
		HashMap<String, Set> newMap = new HashMap<String, Set>();
		
		for(String var : toCopy.keySet()) {
			
			Set tempSet = new HashSet();

			for(Object e : toCopy.get(var)) {
				tempSet.add(e);
			}
			
			newMap.put(var, tempSet);
		}
		
		return newMap;
	}

	public static void main(String[] args) {
		
		
		//Initial Restraining
		Map<String, Set> domains = new HashMap<String, Set>();
		
		Set<Integer> temp1 = new HashSet<Integer>();
		Set<Integer> temp2 = new HashSet<Integer>();
		
		temp1.add(1);
		temp1.add(2);
		temp1.add(3);
		
		temp2.add(1);
		temp2.add(2);
		temp2.add(3);
		
		domains.put("x1", temp1);
		domains.put("x2", temp2);
		
		List<Constraint> constraints = new ArrayList<Constraint>();
		LessThan lessThan = new LessThan("x1", "x2");
		constraints.add(lessThan);
		
		Problem problem = new Problem(domains, constraints);
		
		System.out.println(problem.getDomains().toString());
		
		Constraint temp = problem.getConstraint().get(0);
		boolean hasChanged = temp.filter(problem);
		
		System.out.println(problem.getDomains().toString());
		System.out.println(hasChanged);
		
		//Now, we need to dive in search space
		
		for(String var : problem.getDomains().keySet()) {
			if(problem.getDomains().get(var).size() > 1) {
				Object element = problem.getDomains().get(var).iterator().next();
				Set tempSet = new HashSet();
				tempSet.add(element);
				
				problem.getDomains().put(var, tempSet);
				
				hasChanged = temp.filter(problem);
			}
		}
		
		

		System.out.println(problem.getDomains().toString());		
		System.out.println(hasChanged);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
