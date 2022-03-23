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
	
	//Returns a tuple (variable, value) indicating what variable
	//has been chosen by the method to be instantiated and to what
	//value. It doesn't actually instantiate though, just indicates what should.
	//If all domains are reduced to singleton, return null
	//If one domain is empty, it returns a tuple (var, null) with domain of var empty. 
	public Tuple<String, Object> createDecision(){
		boolean notFound = true;
		String chosenVar = null;
		Object chosenElem = null;
		Map<String, Set> domains = this.getDomains();
		
		//We check domain size for each variable.
		for(String var : this.getDomains().keySet()) {
			//If we've found a variable to instantiate or which domain is empty, we stop.
			if(notFound) {
				//Having more than one element is enough to make a change by instantiating.
				if(domains.get(var).size() > 1) {
					notFound = false;
					chosenVar = var;
					chosenElem = domains.get(var).iterator().next();
				}
				//If empty, problem is not solved in this branch of the search space
				if(domains.get(var).size() == 0) {
					notFound = false;
					chosenVar = var;
					chosenElem = null;
				}
			}
		}
		//Every domain is a singleton.
		if(notFound) {
			return null;
		}
		return new Tuple(chosenVar, chosenElem);
	}

	//Used to copy domains for proper navigation in the search space.
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
	
	//The main recursive method navigating through the search space.
	//Returns the number of solutions to the problem.
	static public int enumerate(Problem problem) {
		//We try to filter with all constraints and repeat while all constraints
		//actually restrain variables' domains. We stop once every constraint proves
		//not to be able to restrain anymore in a single iteration.
		boolean notStable = true;
		while(notStable) {
			notStable = false;
			for(Constraint c : problem.getConstraint()) {
				notStable = notStable || c.filter(problem);
			}
		}
		
		//Then, we enter search space by making a decision
		Tuple<String, Object> decision = problem.createDecision();
		//If so, we have a solution since all domains are singletons.
		if(decision == null) {
			return 1;
		}
		
		String var = decision.get1();
		Object val = decision.get2();
		
		//Problem is unsolvable.
		if(val == null) {
			return 0;
		}
		
		//We redo the same operation, once with var instantiated to val,
		//and once with val taken away from val's domain. Propagate only
		//allows to do so without actually modifying our problem's domain.
		return Problem.propagate(problem, var, val, true) + Problem.propagate(problem, var, val, false);
	}
	
	//Allow to instantiate variables to chosen values and to restrain abusively variables' domains
	//Without actually modifying our problem's domains. Doing so, we instantiate new problems.
	static public int propagate(Problem problem, String var, Object val, boolean apply) {
		///In order to instantiate a new problem, we make a deepCopy of the true domains.
		Map<String, Set> incrDomains = Problem.deepCopy(problem.getDomains());
		
		//If apply, then we're instantiating var to val. 
		//If not, we're taking it away from var's domain to explore other possibilites.
		Set tempSet = incrDomains.get(var);
		for(Object e : problem.getDomains().get(var)) {
			if(apply != (e == val)) {
				tempSet.remove(e);
			}
		}
		incrDomains.put(var, tempSet);
		
		//We now apply enumerate as told in the eponym method to the newly instantiated problem.
		//Doing so we only dive deeper in the search space.
		return Problem.enumerate(new Problem(incrDomains, problem.getConstraint()));
	}
	
	
	
	
	
	
	
	
	
	
	

}
