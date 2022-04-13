package DecisionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	//Clear enough I think.
	static public ImplicationGraph deepCopy(ImplicationGraph graph) {
		// TODO
		return null;
	}
	
	//Add nodes according to some filtering realized by constraint, resulting in domains.
	//Decision level is always 1.
	public void nodeFiltering(Constraint constraint, Map<String, Set> domains) {
		// TODO Auto-generated method stub
		// Add nodes of variables with corresponding domain and dl = 1
		
	}
	
	//Method used to get a constraint out of the conflict node. 
	//If there is no conflict node in the problem, this methode should return null.
	//This method is the Java interpretation of the algorithm presented in...
	//"A Proof-Producing CSP Solver", by M.Veksler and O.Strichman.
	public Constraint conflictLearn() {
		// TODO Auto-generated method stub
		return null;
	}

	//Adds a decision node, making it the current node of specified var, with values.
	//Decision level is always 0.
	public void decisionNode(String var, Set values) {
		Node decision = new Node(var, values, 0);
		for (Node n : this.getCurrNodes()) {
			if (n.getVar() == var) {
				this.getCurrNodes().remove(n);
				this.getCurrNodes().add(decision);
			}
		}
	}
}
