package Constraints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import SCPsolver.Constraint;
import SCPsolver.Problem;

public class NotEqualPlusC extends Constraint {

	private int c;
	
	public NotEqualPlusC(String x1, String x2, int c) {
		this.variables = new ArrayList<String>();
		this.variables.add(x1);
		this.variables.add(x2);
		this.c = c;
	}
	
	public List<String> getVariables(){
		return this.variables;
	}
	
	public int getC() {
		return this.c;
	}
	
	
	//On veut x1 != x2 + c
	public boolean filter(Problem problem) {
		String x1 = this.getVariables().get(0);
		String x2 = this.getVariables().get(1);
		
		
		Set<Integer> d1 = problem.getDomains().get(x1);
		Set<Integer> d2 = problem.getDomains().get(x2);
		
		if((d1.size() == 0 || d2.size() == 0) || (d1.size() > 1 && d2.size() > 1)) {
			return false;
		}
		
		Set<Integer> new_d1 = new HashSet<Integer>();
		Set<Integer> new_d2 = new HashSet<Integer>();
		
		if(d1.size() == 1) {
			Integer e1 = d1.iterator().next();
			for(Integer e2 : d2) {
				if(e2 + this.getC() != e1) {
					new_d2.add(e2);
				}
			}
			new_d1.add(e1);
		} else if(d2.size() == 1) {
			Integer e2 = d2.iterator().next();
			for(Integer e1 : d1) {
				if(e2 + this.getC() != e1) {
					new_d1.add(e1);
				}
			}			
			new_d2.add(e2);
		}	

		problem.getDomains().put(x1, new_d1);
		problem.getDomains().put(x2, new_d2);
		
		int size = d1.size() + d2.size();
		int sizeThen = new_d1.size() + new_d2.size();
		return (size != sizeThen);
	}
}
