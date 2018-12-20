package fr.sma.adventofcode.resolve.day11;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import java.util.Comparator;
import java.util.Map;
import one.util.streamex.IntStreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day11Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day11Ex2");
		
		String value = dataFetcher.fetch(11).trim();
		
		int gridSerial = Integer.parseInt(value);
		
		Grid grid = new Grid(gridSerial, 300);
		
		// for performance reason, I stop at 30, it becomes very slow after for an ever decreasing max value.
		// if, for some extraordinary reason it is not enough, then raise the value.
		IntStreamEx.rangeClosed(1, 30)
				.mapToObj(grid::findMaxScanArea)
				.reverseSorted(Comparator.comparing(Map.Entry::getValue))
				.forEachOrdered(System.out::println);
	}
	
	
}