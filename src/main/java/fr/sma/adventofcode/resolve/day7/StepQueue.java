package fr.sma.adventofcode.resolve.day7;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

class StepQueue<T extends Step> implements Iterator<T> {
	private final TreeSet<T> remainingSteps;
	
	private final Set<T> blockedSteps;
	
	public StepQueue(Collection<T> steps) {
		remainingSteps = new TreeSet<>(steps);
		blockedSteps = new HashSet<>();
	}
	
	@Override
	public boolean hasNext() {
		return !remainingSteps.isEmpty();
	}
	
	@Override
	public T next() {
		return this.acquireStep()
				.filter(this::releaseStep)
				.orElseThrow(() -> new IllegalStateException("can't not return an element"));
	}
	
	public Optional<T> acquireStep() {
		return remainingSteps.stream()
				.filter(step -> !blockedSteps.contains(step) && Collections.disjoint(remainingSteps, step.getDependsOn()))
				.peek(blockedSteps::add)
				.findFirst();
	}
	
	public boolean releaseStep(T step) {
		return blockedSteps.remove(step) && remainingSteps.remove(step);
	}
}