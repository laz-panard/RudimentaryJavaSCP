package SCPsolver;

import java.util.List;

public abstract class Constraint {
	protected List<String> variables;
	
	public boolean filter(Problem problem) {
		return true;
	}
	
}
