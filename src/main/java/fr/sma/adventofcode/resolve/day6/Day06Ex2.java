package fr.sma.adventofcode.resolve.day6;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Day06Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+), (\\d+)");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day06Ex2");
		
		String values = dataFetcher.fetch(6).trim();
		
		AtomicInteger minX = new AtomicInteger(),
				minY = new AtomicInteger(),
				maxX = new AtomicInteger(),
				maxY = new AtomicInteger();
		
		List<Point> points = StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.map(m -> new Point(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2))))
				.peek(p -> {
					if(p.x < minX.get()) {
						minX.set(p.x);
					}
					if(p.x > maxX.get()) {
						maxX.set(p.x);
					}
					if(p.y < minY.get()) {
						minY.set(p.y);
					}
					if(p.y > maxY.get()) {
						maxY.set(p.y);
					}
				})
				.collect(Collectors.toList());
		
		long count = IntStreamEx.range(minX.get(), maxX.get())
				.flatMap(x ->
						IntStreamEx.range(minY.get(), maxY.get())
								.map(y -> StreamEx.of(points)
										.mapToInt(p -> manhattanDistance(p, x, y))
										.sum()
								)
				).filter(i -> i < 10_000)
				.count();
		
		System.out.println(count);
	}
	
	private int manhattanDistance(Point p, int x, int y) {
		return Math.abs(p.x-x) + Math.abs(p.y-y);
	}
	
	private class Point {
		private final int x, y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "Point{" +
					"x=" + x +
					", y=" + y +
					'}';
		}
	}
}