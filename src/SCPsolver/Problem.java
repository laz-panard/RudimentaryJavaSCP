package SCPsolver;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DecisionTree.ImplicationGraph;
import DecisionTree.Node;

public class Problem {
	private Map<String, Set> domains;
	private List<Constraint> constraint;
	private ImplicationGraph graph;
	
	/**
	 * @param domains
	 * @param constraint
	 */
	public Problem(Map<String, Set> domains, List<Constraint> constraint, ImplicationGraph graph) {
		this.domains = domains;
		this.constraint = constraint;
		this.graph = graph;
	}
	
	public Problem(Map<String, Set> domains, List<Constraint> constraint) {
		this.domains = domains;
		this.constraint = constraint;
		this.graph = this.graphInitialization();
	}
	
	public ImplicationGraph graphInitialization() {
		List<Node> nodes = new ArrayList<Node>();
		for (String var : domains.keySet()) {
			Node tempNode = new Node(var, this.domains.get(var), 0);
			nodes.add(tempNode);
		}
		
		List<Node> currNodes = new ArrayList<Node>(nodes);
		
		ImplicationGraph graph = new ImplicationGraph(nodes, currNodes);
		return graph;
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
	
	public ImplicationGraph getGraph() {
		return this.graph;
	}
	
	public void setGraph(ImplicationGraph graph) {
		this.graph = graph;
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
	static public Triple<Integer, List<Constraint>, ImplicationGraph> enumerate(Problem problem) {
		//We try to filter with all constraints and repeat while all constraints
		//actually restrain variables' domains. We stop once every constraint proves
		//not to be able to restrain anymore in a single iteration.
		boolean notStable = true;
		boolean conflict = false;
		while(notStable) {
			notStable = false;			
			for(Constraint c : problem.getConstraint()) {
				
				if(!conflict) {
					List<String> modifiedVar = c.filter(problem);
					if(!modifiedVar.isEmpty()) {
						notStable = true;
						problem.getGraph().nodeFiltering(c, problem.getDomains(), modifiedVar);
					
						//We check wether or not a conflicting node has been created
					
						for(Node n : problem.getGraph().getCurrNodes()) {
							if (n.getDomain().isEmpty()) {
								problem.getGraph().setConflict(n);
								conflict = true;
								notStable = false;
							}
						}
					}
				}
				
			}
		}
		
		/*if (problem.getGraph().getConflict() != null) {
			problem.getConstraint().add(problem.getGraph().conflictLearn());
		}*/
		
		//Then, we enter search space by making a decision
		Tuple<String, Object> decision = problem.createDecision();
		//If so, we have a solution since all domains are singletons.
		if(decision == null) {
			return new Triple<Integer, List<Constraint>, ImplicationGraph>(1, problem.getConstraint(), problem.getGraph());
		}
		
		String var = decision.get1();
		Object val = decision.get2();
		
		//Problem is unsolvable in this part of search space.
		if(val == null) {
			return new Triple<Integer, List<Constraint>, ImplicationGraph>(0, problem.getConstraint(), problem.getGraph());
		}
		
		//We redo the same operation, once with var instantiated to val,
		//and once with val taken away from val's domain. Propagate only
		//allows to do so without actually modifying our problem's domain.
		
		List<Node> currNodes = problem.getGraph().getCurrNodes();
		List<Node> nodes = problem.getGraph().getNodes();
		List<Integer> currNodesIndex = new ArrayList<Integer>();
		
		for(Node n : nodes) {
			if (currNodes.contains(n)) {
				currNodesIndex.add(nodes.indexOf(n));
			}
		}
		
		Triple<Integer, List<Constraint>, ImplicationGraph> leftSearch = Problem.propagate(problem, var, val, true);
		
		//Updating problem values with recursively-updated values of the accumulator
		problem.setConstraint(leftSearch.get2());
		ImplicationGraph newGraph = leftSearch.get3();
		List<Node> newCurrNodes = new ArrayList<Node>();
		for(Integer i : currNodesIndex) {
			newCurrNodes.add(newGraph.getNodes().get(i));
		}
		newGraph.setCurrNodes(newCurrNodes);
		newGraph.setConflict(null);
		problem.setGraph(newGraph);
		
		Triple<Integer, List<Constraint>, ImplicationGraph> rightSearch = Problem.propagate(problem, var, val, false);
		
		problem.setConstraint(rightSearch.get2());
		newGraph = rightSearch.get3();
		newCurrNodes = new ArrayList<Node>();
		for(Integer i : currNodesIndex) {
			newCurrNodes.add(newGraph.getNodes().get(i));
		}
		newGraph.setCurrNodes(newCurrNodes);
		newGraph.setConflict(null);
		problem.setGraph(newGraph);
		
		return new Triple<Integer, List<Constraint>, ImplicationGraph>(leftSearch.get1() + rightSearch.get1(), problem.getConstraint(), problem.getGraph());
	}
	
	//Allow to instantiate variables to chosen values and to restrain abusively variables' domains
	//Without actually modifying our problem's domains. Doing so, we instantiate new problems.
	static public Triple<Integer, List<Constraint>, ImplicationGraph> propagate(Problem problem, String var, Object val, boolean apply) {
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
		
		problem.getGraph().decisionNode(var, tempSet);
		
		//We now apply enumerate as told in the eponym method to the newly instantiated problem.
		//Doing so we only dive deeper in the search space.
		return Problem.enumerate(new Problem(incrDomains, problem.getConstraint(), problem.getGraph()));
	}
	
	
	
	
	
	
	
	
	
	
	

}
