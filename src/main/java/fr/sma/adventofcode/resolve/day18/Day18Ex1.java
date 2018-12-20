package fr.sma.adventofcode.resolve.day18;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day18Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day18Ex1");
		
		String values = dataFetcher.fetch(18).trim();
		
		Forest forest = Forest.build(values);
		
		for (int i = 0; i < 10; i++) {
			forest = forest.getNextMinute();
			System.out.println(forest.printForest());
		}
		Map<Forest.Landscape, Long> landscapesCount = StreamEx.of(forest.getForest())
				.flatMap(StreamEx::of)
				.groupingBy(Function.identity(), Collectors.counting());
		
		
		long result = landscapesCount.getOrDefault(Forest.Landscape.TREE, 0L)* landscapesCount.getOrDefault(Forest.Landscape.LUMBER, 0L);
		
		System.out.println("result = " + result);
	}
}