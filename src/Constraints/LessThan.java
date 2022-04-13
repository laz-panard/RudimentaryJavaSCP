package Constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import SCPsolver.Constraint;
import SCPsolver.Problem;


public class LessThan extends Constraint{
	
	//By default, it's always x1 < x2.
	public LessThan(String x1, String x2) {
		this.variables = new ArrayList<String>();
		this.variables.add(x1);
		this.variables.add(x2);
	}
	
	
	
	
	//We filter by taking away all values of x1 that 
	//are superior or equal to max(x2) and values of x2 that
	//are inferior or equal to min(x1).
	//
	//We shall return if the method has indeed filtered (true) or not (false).
	public boolean filter(Problem problem) {
		String x1 = this.getVariables().get(0);
		String x2 = this.getVariables().get(1);
		
		
		Set<Integer> d1 = problem.getDomains().get(x1);
		Set<Integer> d2 = problem.getDomains().get(x2);
		
		//If domains are empty, we shall not filter anymore since 
		//problem is not solved in this branch of search space
		if((d1.size() == 0) || (d2.size() == 0)) {
			return false;
		}
		
		Integer min1 = Collections.min(d1);
		Integer max2 = Collections.max(d2);
		
		//New domains will replace the old ones.
		Set<Integer> new_d1 = new HashSet<Integer>();
		Set<Integer> new_d2 = new HashSet<Integer>();
		
		//We add values according to the header comment of the method.
		for(Integer e : d1) {
			if(e < max2) {
				new_d1.add(e);
			}
		}
		
		for(Integer e : d2) {
			if(e > min1) {
				new_d2.add(e);
			}
		}
		
		//We actualise the domains of our problem.
		problem.getDomains().put(x1, new_d1);
		problem.getDomains().put(x2, new_d2);
		
		//If the added sizes of our two new domains is the same than the old ones,
		//it is clear that no value has been deleted (we don't add any new value).
		int size = d1.size() + d2.size();
		int sizeThen = new_d1.size() + new_d2.size();
		return (size != sizeThen); 
	
	}
}
