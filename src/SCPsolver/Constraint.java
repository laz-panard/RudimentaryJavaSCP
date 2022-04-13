package SCPsolver;

import java.util.List;

//Abstract class describing constraints and what they should contain in a more general way.
public abstract class Constraint {
	protected List<String> variables;
	
	//Filter return true if it has indeed filtered. Else it return false. While true, problem is unsolved. 
	public boolean filter(Problem problem) {
		return true;
	}
	
	public List<String> getVariables(){
		return this.variables;
	}
	
}
