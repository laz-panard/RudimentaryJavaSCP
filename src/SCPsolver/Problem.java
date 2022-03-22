package SCPsolver;

import java.util.HashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Problem {
	private Map<String, Set> domains;
	private List<Constraint> constraint;
	
	/**
	 * @param domains
	 * @param constraint
	 */
	public Problem(Map<String, Set> domains, List<Constraint> constraint) {
		this.domains = domains;
		this.constraint = constraint;
	}
	
	/**
	 * @return the domains
	 */
	public Map<String, Set> getDomains() {
		return domains;
	}
	
	/**
	 * @param domains the domains to set
	 */
	public void setDomains(Map<String, Set> domains) {
		this.domains = domains;
	}
	
	/**
	 * @return the constraint
	 */
	public List<Constraint> getConstraint() {
		return constraint;
	}
	
	/**
	 * @param constraint the constraint to set
	 */
	public void setConstraint(List<Constraint> constraint) {
		this.constraint = constraint;
	}
	
	public Tuple<String, Object> createDecision(){
		boolean notFound = true;
		String chosenVar = null;
		Object chosenElem = null;
		Map<String, Set> domains = this.getDomains();
		
		for(String var : this.getDomains().keySet()) {
			if(notFound) {
				if(domains.get(var).size() > 1) {
					notFound = false;
					chosenVar = var;
					chosenElem = domains.get(var).iterator().next();
				}
				
				if(domains.get(var).size() == 0) {
					notFound = false;
					chosenVar = var;
					chosenElem = null;
				}
			}
		}
		
		if(notFound) {
			return null;
		}
		return new Tuple(chosenVar, chosenElem);
	}

	
	static public Map<String, Set> deepCopy(Map<String, Set> domains){
		Map<String, Set> newMap = new HashMap<String, Set>();
		
		for(String var : domains.keySet()) {
			
			Set tempSet = new HashSet();

			for(Object e : domains.get(var)) {
				tempSet.add(e);
			}
			
			newMap.put(var, tempSet);
		}
		
		return newMap;
	}
	
	static public int enumerate(Problem problem) {
		//We filter everything we can filter
		boolean notStable = true;
		while(notStable) {
			notStable = false;
			for(Constraint c : problem.getConstraint()) {
				notStable = notStable || c.filter(problem);
			}
		}
		
		//Then, we enter search space by making a decision
		Tuple<String, Object> decision = problem.createDecision();
		if(decision == null) {
			return 1;
		}
		
		String var = decision.get1();
		Object val = decision.get2();
		
		if(val == null) {
			return 0;
		}
		
		return Problem.propagate(problem, var, val, true) + Problem.propagate(problem, var, val, false);
	}
	
	static public int propagate(Problem problem, String var, Object val, boolean apply) {
		Map<String, Set> incrDomains = Problem.deepCopy(problem.getDomains());
		
		Set tempSet = incrDomains.get(var);
		for(Object e : problem.getDomains().get(var)) {
			if(apply != (e == val)) {
				tempSet.remove(e);
			}
		}
		incrDomains.put(var, tempSet);
		
		return Problem.enumerate(new Problem(incrDomains, problem.getConstraint()));
	}
	
	
	
	
	
	
	
	
	
	
	

}
