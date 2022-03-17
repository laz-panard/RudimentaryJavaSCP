package Constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import SCPsolver.Constraint;
import SCPsolver.Problem;


public class LessThan extends Constraint{
	
	public LessThan(String x1, String x2) {
		this.variables = new ArrayList<String>();
		this.variables.add(x1);
		this.variables.add(x2);
	}
	
	public List<String> getVariables(){
		return this.variables;
	}
	//On veut x1 < x2.
	public boolean filter(Problem problem) {
		String x1 = this.getVariables().get(0);
		String x2 = this.getVariables().get(1);
		
		
		Set<Integer> d1 = problem.getDomains().get(x1);
		Set<Integer> d2 = problem.getDomains().get(x2);
		
		int size = d1.size() + d2.size();
		
		Integer min1 = Collections.min(d1);
		Integer max2 = Collections.max(d2);
		
		Set<Integer> new_d1 = new HashSet<Integer>();
		Set<Integer> new_d2 = new HashSet<Integer>();
		
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
		
		problem.getDomains().put(x1, new_d1);
		problem.getDomains().put(x2, new_d2);
		
		int sizeThen = size - new_d1.size() - new_d2.size();
		return (!(size == sizeThen));
	
	}
}
