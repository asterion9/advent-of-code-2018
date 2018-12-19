package fr.sma.adventofcode.resolve.day18;

import fr.sma.adventofcode.resolve.util.Point;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import one.util.streamex.StreamEx;

public class Forest {
	public enum Landscape {
		TREE("|"),
		OPEN("."),
		LUMBER("#");
		
		private final String symbol;
		
		Landscape(String symbol) {
			this.symbol = symbol;
		}
		
		public String getSymbol() {
			return symbol;
		}
		
		public static Landscape get(String symbol) {
			return StreamEx.of(Landscape.values())
					.filterBy(Landscape::getSymbol, symbol)
					.findFirst()
					.orElseThrow();
		}
		
		public Landscape getNext(Map<Landscape, Long> counts) {
			return evolutionRules.get(this).apply(counts);
		}
		
		public static final Map<Landscape, Function<Map<Landscape, Long>, Landscape>> evolutionRules = Map.of(
				TREE, counts -> Optional.ofNullable(counts.get(LUMBER)).filter(lum -> lum >= 3).map(l -> LUMBER).orElse(TREE),
				OPEN, counts -> Optional.ofNullable(counts.get(TREE)).filter(tree -> tree >= 3).map(l -> TREE).orElse(OPEN),
				LUMBER, counts -> counts.containsKey(LUMBER) && counts.containsKey(TREE) ? LUMBER : OPEN
		);
	}
	
	private Landscape[][] curForest;
	private Landscape[][] nextForest;
	
	private Forest(Landscape[][] curForest) {
		this.curForest = curForest;
		nextForest = new Landscape[curForest[0].length][curForest.length];
	}
	
	public static Forest build(String values) {
		Landscape[][] landscapes = StreamEx.split(values, "\n")
				.map(l -> StreamEx.split(l, "")
						.map(Landscape::get)
						.toArray(Landscape.class)
				).toArray(new Landscape[0][0]);
		
		Landscape[][] forest = new Landscape[landscapes[0].length][landscapes.length];
		
		for (int x = 0; x < forest.length; x++) {
			for (int y = 0; y < forest[x].length; y++) {
				forest[x][y] = landscapes[y][x];
			}
		}
		return new Forest(forest);
	}
	
	private Landscape predict(Point point) {
		Map<Landscape, Long> landscapeCount = point.around(1).map(p -> p.getIn(curForest))
				.flatMap(Optional::stream)
				.groupingBy(Function.identity(), Collectors.counting());
		return curForest[point.getX()][point.getY()].getNext(landscapeCount);
	}
	
	public void doTurn() {
		for (int x = 0; x < curForest.length; x++) {
			for (int y = 0; y < curForest[0].length; y++) {
			
			}
		}
	}
}
