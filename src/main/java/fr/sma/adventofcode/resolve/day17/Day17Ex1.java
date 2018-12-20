package fr.sma.adventofcode.resolve.day17;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.util.Point;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day17Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	
	
	@Override
	public void run() throws Exception {
		System.out.println("Day17Ex1");
		
		String values = dataFetcher.fetch(17).trim();
		
		WaterMap waterMap = WaterMap.build(values);
		
		Point start = new Point(500, 0);
		
		waterMap.simulateWater(start);
		
		System.out.println("\nfinal result : \n" + waterMap.printArea(new Point(0,0), Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		long nbWater = StreamEx.of(waterMap.getArea())
				.flatMap(StreamEx::of)
				.filter(e -> e == Element.FLOWING_WATER || e == Element.STILL_WATER)
				.count();
		
		System.out.println("nbWater = " + nbWater);
	}
	
	
	
	
}