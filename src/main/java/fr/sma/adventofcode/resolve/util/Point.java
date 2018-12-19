package fr.sma.adventofcode.resolve.util;

import java.util.Optional;

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
}
