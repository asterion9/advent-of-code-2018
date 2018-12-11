package fr.sma.adventofcode.resolve.day11;

import fr.sma.adventofcode.resolve.ExSolution;
import java.util.Comparator;
import java.util.Map;
import one.util.streamex.IntStreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Day11Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${input}")
	private int input;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day11Ex2");
		
		Grid grid = new Grid(input, 300);
		
		// for performance reason, I stop at 30, it becomes very slow after for an ever decreasing max value.
		// if, for some extraordinary reason it is not enough, then raise the value.
		IntStreamEx.rangeClosed(1, 30)
				.mapToObj(grid::findMaxScanArea)
				.reverseSorted(Comparator.comparing(Map.Entry::getValue))
				.forEachOrdered(System.out::println);
	}
	
	
}