package DecisionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Constraints.Clause;
import SCPsolver.Constraint;
import SCPsolver.Tuple;

public class ImplicationGraph {
	private List<Node> nodes = new ArrayList<Node>();
	private Node conflict = null;
	private List<Node> currNodes;
	
	public ImplicationGraph(List<Node> nodes, List<Node> currNodes) {
		this.currNodes = currNodes;
		this.nodes = nodes;
	}
	
	public ImplicationGraph() {
	}

	/**
	 * @return the conflict
	 */
	public Node getConflict() {
		return conflict;
	}

	/**
	 * @param conflict the conflict to set
	 */
	public void setConflict(Node conflict) {
		this.conflict = conflict;
	}

	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the currNodes
	 */
	public List<Node> getCurrNodes() {
		return currNodes;
	}

	/**
	 * @param currNodes the currNodes to set
	 */
	public void setCurrNodes(List<Node> currNodes) {
		this.currNodes = currNodes;
	}
	
	//Add nodes according to some filtering realized by constraint, resulting in domains.
	//Decision level is always 1.
	public void nodeFiltering(Constraint constraint, Map<String, Set> domains, List<String> modifiedVar) {
		List<Node> addedNodes = new ArrayList<Node>();
		List<Tuple<Node, Constraint>> ascNodes = new ArrayList<Tuple<Node, Constraint>>();
		
		//Retrieving ascending nodes
		int decisionLevel = 0;
		for(Node n : this.getCurrNodes()) {
			if (constraint.getVariables().contains(n.getVar())) {
				ascNodes.add(new Tuple<Node, Constraint>(n, constraint));
				if (n.getDecisionLevel() > decisionLevel) {
					decisionLevel = n.getDecisionLevel();
				}
			}
		}
		
		//Creating new nodes
		for(String var : modifiedVar) {
			Node newNode = new Node(var, domains.get(var), decisionLevel, ascNodes, new ArrayList<Tuple<Node, Constraint>>());
			addedNodes.add(newNode);
		}
		
		//Updating nodes list (adding new nodes and actualising current nodes in nodes list)
		for(Node currN : this.getCurrNodes()) {
			//Updating nodes in nodes list
			for(Node n : this.getNodes()) {
				if(n.equals(currN)){
					for(Node addedNode : addedNodes) {
						n.getDescNodes().add(new Tuple<Node, Constraint>(addedNode, constraint));
					}
				}
			}
		}
		this.getNodes().addAll(addedNodes);
		
		//Updating current nodes
		List<Node> toDel = new ArrayList<Node>();
		for(Node currNode : this.getCurrNodes()) {
			for(Node newNode : addedNodes) {
				if (currNode.getVar() == newNode.getVar()) {
					toDel.add(currNode);
				}
			}
		}
		this.getCurrNodes().removeAll(toDel);
		this.getCurrNodes().addAll(addedNodes);
	}
	
	//Method used to get a constraint out of the conflict node. 
	//If there is no conflict node in the problem, this methode should return null.
	//This method is the Java interpretation of the algorithm presented in...
	//"A Proof-Producing CSP Solver", by M.Veksler and O.Strichman.
/*
	public Clause conflictLearn() {
		if (this.getConflict() == null) {
			return null;
		}
		
		
		Clause cl = this.getConflict().explain();
		
		List<Tuple<Node, Constraint>> pre_pred = this.getConflict().getAscNodes();
		List<Node> pred = new ArrayList<Node>();
		for(Tuple<Node, Constraint> t : pre_pred) {
			pred.add(t.get1());
		}
		
		List<Node> front = new ArrayList<Node>();
		for(Node n : pred) {
			if (cl.getVariables().contains(n.getVar())) {
				front.add(n);
			}
		}
		
		boolean stopCriterion = true;
		
		boolean alreadyMet = false;
		for(Node n : front) {
			if(n.getDecisionLevel() == 1) {
				if(alreadyMet) {
					stopCriterion = false;
				}
				alreadyMet = true;
			}
		}
		
		while(!stopCriterion) {
			Node currNode = front.get(0);
			front.remove(currNode);
			Clause expl = currNode.explain();
			cl = cl.resolve(expl, currNode.getVar());
			
			pre_pred = currNode.getAscNodes();
			pred = new ArrayList<Node>();
			for(Tuple<Node, Constraint> t : pre_pred) {
				pred.add(t.get1());
			}
			
			for(Node n : front) {
				if (!cl.getVariables().contains(n.getVar())) {
					front.remove(n);
				}
			}
			for(Node n : pred) {
				if (cl.getVariables().contains(n.getVar()) && (!front.contains(n))) {
					front.add(n);
				}
			}
			
			alreadyMet = false;
			for(Node n : front) {
				if(n.getDecisionLevel() == 1) {
					if(alreadyMet) {
						stopCriterion = false;
					}
					alreadyMet = true;
				}
			}			
		}
		
		return cl;
		
	}
	*/
	
	//Adds a decision node, making it the current node of specified var, with values.
	//Decision level is always 0.
	public void decisionNode(String var, Set values) {
		
		Node toRemove = null;
		int decisionLevel = 0;
		for (Node n : this.getCurrNodes()) {
			if (n.getDecisionLevel() > decisionLevel) {
				decisionLevel = n.getDecisionLevel();
			}
			if (n.getVar() == var) {
				toRemove = n;
			}
		}
		
		Node decision = new Node(var, values, 1 + decisionLevel);
		
		this.getCurrNodes().remove(toRemove);
		
		this.getCurrNodes().add(decision);
		this.getNodes().add(decision);
	}
	
	public void print() {
		List<Node> visited = new ArrayList<Node>();
		
		for (Node n : this.getNodes()) {
			if (!visited.contains(n)) {
				visited.add(n);
				n.print();
			}
		}
		
	}

}
