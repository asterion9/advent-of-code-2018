package fr.sma.adventofcode.resolve.day15;

import fr.sma.adventofcode.resolve.util.Point;
import one.util.streamex.StreamEx;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.stream.Collectors;

public class NodePoint extends Point {
	private final NodePoint prev;
	
	public NodePoint(Point p, NodePoint prev) {
		super(p.getX(), p.getY());
		this.prev = prev;
	}
	
	public Deque<Point> getPoints() {
		return StreamEx.ofTree(this, nodePoint -> Optional.ofNullable(nodePoint.getPrev()).stream())
				.collect(Collectors.toCollection(ArrayDeque::new));
	}
	
	public int getDepth() {
		if (prev == null) {
			return 1;
		} else {
			return 1 + prev.getDepth();
		}
	}
	
	public NodePoint(int x, int y, NodePoint prev) {
		super(x, y);
		this.prev = prev;
	}
	
	public NodePoint getPrev() {
		return prev;
	}
}
