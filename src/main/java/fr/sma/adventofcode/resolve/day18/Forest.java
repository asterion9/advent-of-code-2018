package fr.sma.adventofcode.resolve.day18;

import java.util.Arrays;
import java.util.function.Predicate;
import one.util.streamex.StreamEx;

public class Forest {
	public enum Landscape {
		OPEN("."),
		TREE("|"),
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
		
		public static final Predicate<int[]>[] evolutionRules = new Predicate[]{
				(Predicate<int[]>) counts -> counts[TREE.ordinal()] >= 3,
				(Predicate<int[]>) counts -> counts[LUMBER.ordinal()] >= 3,
				(Predicate<int[]>) counts -> counts[TREE.ordinal()] == 0 || counts[LUMBER.ordinal()] == 0
		};
	}
	
	private int[][] curForest;
	
	private final int minute;
	
	public Forest(Forest forest, int minute) {
		this(StreamEx.of(forest.curForest)
				.map(line -> Arrays.copyOf(line, line.length))
				.toArray(new int[0][0]), minute);
	}
	
	private Forest(int[][] curForest, int minute) {
		this.curForest = curForest;
		this.minute = minute;
	}
	
	public static Forest build(String values) {
		int[][] landscapes = StreamEx.split(values, "\n")
				.map(l -> StreamEx.split(l, "")
						.map(Landscape::get)
						.mapToInt(Landscape::ordinal)
						.toArray()
				).toArray(new int[0][0]);
		
		int[][] forest = new int[landscapes[0].length][landscapes.length];
		
		for (int x = 0; x < forest.length; x++) {
			for (int y = 0; y < forest[x].length; y++) {
				forest[x][y] = landscapes[y][x];
			}
		}
		return new Forest(forest, 0);
	}
	
	public int getMinute() {
		return minute;
	}
	
	private int predict(int x0, int y0) {
		int[] counts = new int[3];
		for (int y = y0-1 ; y <= y0+1 ; y++) {
			for (int x=x0-1; x<=x0+1; x++) {
				if (x<0 || y < 0 || x >= curForest.length || y >= curForest[x].length) {
					continue;
				}
				if (x != x0 || y != y0){
					counts[curForest[x][y]]++;
				}
			}
		}
		if (Landscape.evolutionRules[curForest[x0][y0]].test(counts)) {
			return (curForest[x0][y0] +1) % 3;
		}
		return curForest[x0][y0];
	}
	
	public Forest getNextMinute() {
		int[][] nextForest = new int[curForest.length][curForest[0].length];
		for (int x = 0; x < curForest.length; x++) {
			for (int y = 0; y < curForest[0].length; y++) {
				nextForest[x][y] = predict(x, y);
			}
		}
		return new Forest(nextForest, minute + 1);
	}
	
	public Landscape[][] getForest() {
		Landscape[][] forest = new Landscape[curForest.length][curForest[0].length];
		for (int x = 0; x < curForest.length; x++) {
			for (int y = 0; y < curForest[x].length; y++) {
				forest[x][y] = Landscape.values()[curForest[x][y]];
			}
		}
		return forest;
	}
	
	public String printForest() {
		StringBuilder sb = new StringBuilder();
		Landscape[][] forest = getForest();
		for (int y = 0; y < forest[0].length; y++) {
			for (int x = 0; x < forest.length; x++) {
				sb.append(forest[x][y].symbol);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public boolean hasSamePattern(Forest other) {
		return Arrays.deepEquals(curForest, other.curForest);
	}
}
