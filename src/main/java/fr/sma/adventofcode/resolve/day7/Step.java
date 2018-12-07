package fr.sma.adventofcode.resolve.day7;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

class Step implements Comparable<Step> {
	private final String id;
	private final Set<Step> dependsOn;
	
	public Step(String id, Step dependsOn) {
		this(id);
		this.dependsOn.add(dependsOn);
	}
	
	public Step(String id) {
		this.id = id;
		this.dependsOn = new TreeSet<>();
	}
	
	public Step absorb(Step other) {
		if (!other.getId().equals(id)) {
			throw new IllegalArgumentException("can't merge two different step");
		}
		dependsOn.addAll(other.getDependsOn());
		return this;
	}
	
	public String getId() {
		return id;
	}
	
	public Set<Step> getDependsOn() {
		return dependsOn;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Step step = (Step) o;
		return Objects.equals(id, step.id);
	}
	
	@Override
	public String toString() {
		return "Step{" +
				"id='" + id + '\'' +
				", dependsOn=" + dependsOn +
				'}';
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public int compareTo(Step o) {
		return id.compareTo(o.getId());
	}
}
