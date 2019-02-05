package fr.sma.adventofcode.resolve.day11;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * dynamically generate grid values for the calculation.
 * calculate all values and return the biggest.
 */
@Component
public class Day11Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day11Ex1");
		
		String value = dataFetcher.fetch(11).trim();
		
		int gridSerial = Integer.parseInt(value);
		
		Grid grid = new Grid(gridSerial, 300);
		
		System.out.println(grid.findMaxScanArea(3));
	}
}