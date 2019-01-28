package fr.sma.adventofcode.resolve.day22;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day22Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Da22Ex1");
		
		String values = dataFetcher.fetch(22).trim();
		String[] inputs = values.split("[\\s,]");
		int depth = Integer.valueOf(inputs[1]);
		int tx = Integer.valueOf(inputs[3]);
		int ty = Integer.valueOf(inputs[4]);
		
		Cave cave = new Cave(tx, ty, depth);
		
		int riskLevel = 0;
		for (int x = 0; x <= tx; x++) {
			for (int y = 0; y <= ty; y++) {
				riskLevel += cave.getErosion(x, y) % 3;
			}
		}
		
		System.out.println(cave.print());
		
		System.out.println("riskLevel = " + riskLevel);
	}
	
	
}
