package fr.sma.adventofcode.resolve.util;

import java.util.Optional;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

public class Point {
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public int getX() {
	
		return x;
	}
	
	public <T> Optional<T> getIn(T[][] table) {
		if (x >= 0 && x < table.length && y >= 0 && y < table[x].length) {
			return Optional.of(table[x][y]);
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public String toString() {
		return "(" + x +
				", " + y +
				')';
	}
	
	
	
	public StreamEx<Point> around(int size) {
		return IntStreamEx.rangeClosed(y - size, y + size)
				.flatMapToObj(y -> IntStreamEx.rangeClosed(x - (size - Math.abs(y-this.y)), x + (size - Math.abs(y-this.y)), Math.max(1, 2*size))
						.mapToObj(x -> new Point(x, y)));
	}
}
