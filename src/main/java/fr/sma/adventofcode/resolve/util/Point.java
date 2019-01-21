package fr.sma.adventofcode.resolve.util;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class Point implements Comparable<Point> {
	public static final Comparator<Point> comparator = Comparator.comparing(Point::getY).thenComparing(Point::getX);
	
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p1, Point p2) {
		this.x = p1.x + p2.x;
		this.y = p1.y + p2.y;
	}
	
	public StreamEx<Point> square(int radius) {
		return IntStreamEx.rangeClosed(getY() - radius, getY() + radius, 1)
				.flatMapToObj(y -> IntStreamEx.rangeClosed(
						getX() - radius,
						getX() + radius,
						Math.abs(y - getY()) == radius ? 1 : radius*2
				).mapToObj(x -> new Point(x, y)));
	}
	
	public StreamEx<Point> around(int radius) {
		return IntStreamEx.rangeClosed(getY() - radius, getY() + radius, 1)
				.flatMapToObj(y -> IntStreamEx.rangeClosed(
						getX() - (radius - Math.abs(getY()-y)),
						getX() + (radius - Math.abs(getY()-y)),
						Math.max(2*(radius - Math.abs(getY()-y)), 1)
				).mapToObj(x -> new Point(x, y)));
	}
	
	public <T> Optional<T> getIn(T[][] array) {
		if (getX()<0 || getY() < 0 || getX() >= array.length || getY() >= array[getX()].length) {
			return Optional.empty();
		}
		return Optional.ofNullable(array[getX()][getY()]);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public int compareTo(Point o) {
		return comparator.compare(this, o);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Point)) return false;
		Point point = (Point) o;
		return x == point.x &&
				y == point.y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public String toString() {
		return "Point{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
