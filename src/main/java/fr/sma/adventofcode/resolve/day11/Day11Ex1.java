package fr.sma.adventofcode.resolve.day11;

import fr.sma.adventofcode.resolve.ExSolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Day11Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${input}")
	private int input;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day11Ex1");
		
		Grid grid = new Grid(input, 300);
		
		System.out.println(grid.findMaxScanArea(3));
	}
	
	
}