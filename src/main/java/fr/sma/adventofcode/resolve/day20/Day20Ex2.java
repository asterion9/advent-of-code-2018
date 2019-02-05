package fr.sma.adventofcode.resolve.day20;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * same as ex1, but count the cell instead of finding the farthest.
 */
@Component
public class Day20Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day20Ex2");
		
		String values = dataFetcher.fetch(20).trim();
		
		Maze maze = Maze.init(values, true);
		
		System.out.println(maze.getGreaterEqual(1000));
	}
}