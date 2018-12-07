package fr.sma.adventofcode.resolve.day7;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day07Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern LINE_PATTERN = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day07Ex1");
		
		String values = dataFetcher.fetch(7).trim();
		
		TreeSet<Step> remainingSteps = StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.flatMap(matcher -> {
					Step first = new Step(matcher.group(1));
					Step second = new Step(matcher.group(2), first);
					return StreamEx.of(first, second);
				})
				.sorted()
				.collapse(Step::equals, Step::absorb)
				.collect(Collectors.toCollection(TreeSet::new));
		
		StringBuilder result = new StringBuilder();
		while (!remainingSteps.isEmpty()) {
			Step nextStep = remainingSteps.stream().filter(step -> Collections.disjoint(remainingSteps, step.getDependsOn()))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("can't not return an element"));
			remainingSteps.remove(nextStep);
			result.append(nextStep.getId());
		}
		System.out.println(result);
	}
	
	private static class Step implements Comparable<Step> {
		final String id;
		final Set<Step> dependsOn;
		
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
}