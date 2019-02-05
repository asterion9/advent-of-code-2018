package fr.sma.adventofcode.resolve.day20;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * the input regex represent all the paths that are matched by it,
 * all these paths create a maze.
 * I then recursively calculate the distance from the center of all the maze cells to find its fathest cell.
 * a swing visualization is available (by default)
 */
@Component
public class Day20Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day20Ex1");
		
		String values = dataFetcher.fetch(20).trim();
		
		Maze maze = Maze.init(values, true);
		
		System.out.println(maze.getMax());
	}
}