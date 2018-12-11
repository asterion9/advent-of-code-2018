package fr.sma.adventofcode.resolve.day11;

import java.util.Comparator;
import java.util.Map;
import one.util.streamex.IntStreamEx;

class Grid {
	private final int serialNum;
	private final int squareSize;
	
	public Grid(int serialNum, int squareSize) {
		this.serialNum = serialNum;
		this.squareSize = squareSize;
	}
	
	public Map.Entry<String, Integer> findMaxScanArea(int scanSize) {
		Map.Entry<String, Integer> value = IntStreamEx.rangeClosed(1, squareSize - scanSize + 1)
				.flatMapToObj(x -> IntStreamEx.rangeClosed(1, squareSize - scanSize + 1).mapToEntry(
						y -> String.format("%d,%d,%d", x, y, scanSize),
						y -> sumSquare(x, y, scanSize)))
				.reverseSorted(Comparator.comparing(Map.Entry::getValue))
				.findFirst().get();
		return value;
	}
	
	private int sumSquare(int x0, int y0, int size) {
		return IntStreamEx.range(x0, x0 + size)
				.flatMap(x -> IntStreamEx.range(y0, y0  + size).map(y -> fuelValue(x, y)))
				.sum();
	}
	
	private int fuelValue(int x, int y) {
		return ((((x + 10) * y) + serialNum)*(x + 10)/100)%10 - 5;
	}
}
