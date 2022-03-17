package SCPsolver;

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
	
	
	//

	
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
			}
		}
		
		if(notFound) {
			return null;
		}
		
		return new Tuple(chosenVar, chosenElem);
	}
	
	
	
	
	
	
	
	
	

}
