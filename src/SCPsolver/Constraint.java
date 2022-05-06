package SCPsolver;

import java.util.List;

//Abstract class describing constraints and what they should contain in a more general way.
public abstract class Constraint {
	protected List<String> variables;
	
	//Filter return list of variables modified by filtering 
	public List<String> filter(Problem problem) {
		return null;
	}
	
	public List<String> getVariables(){
		return this.variables;
	}
	
	
}
