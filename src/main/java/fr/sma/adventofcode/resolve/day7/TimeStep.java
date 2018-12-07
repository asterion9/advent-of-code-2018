package fr.sma.adventofcode.resolve.day7;

public class TimeStep extends Step {
	public TimeStep(String id, TimeStep dependsOn) {
		super(id, dependsOn);
	}
	
	public TimeStep(String id) {
		super(id);
	}
	
	public int duration(){
		return 61 + getId().charAt(0) - 'A';
	}
	
	public TimeStep absorb(TimeStep other) {
		super.absorb(other);
		return this;
	}
}
