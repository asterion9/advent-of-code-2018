package fr.sma.adventofcode.resolve.day7;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * parse input to build a stream of step, each line creating 2 step.
 * merge the identical step to build a Stepqueue, it's a graph that can be iterated over.
 * iterate over the step queue for result.
 */
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
		
		StepQueue<Step> steps = new StepQueue<>(StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.flatMap(matcher -> {
					Step first = new Step(matcher.group(1));
					Step second = new Step(matcher.group(2), first);
					return StreamEx.of(first, second);
				})
				.sorted()
				.collapse(Step::equals, Step::absorb)
				.collect(Collectors.toList()));
		
		System.out.println(StreamEx.of(steps)
				.map(Step::getId)
				.joining());
	}
	
}