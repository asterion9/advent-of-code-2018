package fr.sma.adventofcode.resolve.day18;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day18Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day18Ex1");
		
		String values = dataFetcher.fetch(18).trim();
		
		Forest forest = Forest.build(values);
		
		for (int i = 0; i < 10_000; i++) {
			forest = forest.getNextMinute();
		}
		
		GrowthPredictor.Builder builder = new GrowthPredictor.Builder();
		while (!builder.addForest(forest)) {
			forest = forest.getNextMinute();
		}
		
		GrowthPredictor gp = builder.build();
		
		Forest futureForest = gp.getForestAtMinute(1_000_000_000);
		
		System.out.println(futureForest.printForest());
		Map<Forest.Landscape, Long> landscapesCount = StreamEx.of(futureForest.getForest())
				.flatMap(StreamEx::of)
				.groupingBy(Function.identity(), Collectors.counting());
		
		
		long result = landscapesCount.getOrDefault(Forest.Landscape.TREE, 0L)* landscapesCount.getOrDefault(Forest.Landscape.LUMBER, 0L);
		
		System.out.println("result = " + result);
	}
	
	private static class GrowthPredictor {
		private final List<Forest> cycle;
		
		public GrowthPredictor(List<Forest> cycle) {
			this.cycle = cycle;
		}
		
		public Forest getForestAtMinute(int minute) {
			Forest schema = cycle.get((int) ((minute - cycle.get(0).getMinute())% cycle.size()));
			return new Forest(schema, minute);
		}
		
		private static class Builder {
			private final List<Forest> forest;
			
			private int cycleIndex;
			
			public Builder() {
				forest = new ArrayList<>();
				cycleIndex = -1;
			}
			
			public boolean addForest(Forest next) {
				if (cycleIndex != -1) {
					return true;
				}
				cycleIndex = findSamePattern(next);
				
				if (cycleIndex != -1) {
					forest.add(next);
					return true;
				} else {
					forest.add(next);
					return false;
				}
			}
			
			public GrowthPredictor build() {
				if (cycleIndex == -1) {
					throw new IllegalStateException("cycle not found yet");
				}
				return new GrowthPredictor(forest.subList(cycleIndex, forest.size()-1));
			}
			
			private int findSamePattern(Forest other) {
				return StreamEx.of(forest)
						.findFirst(other::hasSamePattern)
						.map(forest::indexOf)
						.orElse(-1);
			}
		}
	}
}