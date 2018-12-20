package fr.sma.adventofcode.resolve.day6;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.IntCollector;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day06Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern LINE_PATTERN = Pattern.compile("(\\d+), (\\d+)");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day06Ex1");
		
		String values = dataFetcher.fetch(6).trim();
		
		AtomicInteger idCounter = new AtomicInteger(),
				minX = new AtomicInteger(),
				minY = new AtomicInteger(),
				maxX = new AtomicInteger(),
				maxY = new AtomicInteger();
		
		List<Point> points = StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.map(m -> new Point((short) idCounter.getAndIncrement(), Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2))))
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
		
		short[][][] grid = new short [maxX.get()-minX.get()][maxY.get()-minY.get()][2];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j][0] = Short.MAX_VALUE;
				grid[i][j][1] = -1;
			}
		}
		
		short size = 0;
		List<Point> remaningPoints = points;
		while(!remaningPoints.isEmpty()) {
			short curSize = size;
			remaningPoints = StreamEx.of(remaningPoints)
					.filter(p -> extendArea(p, grid, curSize))
					.collect(Collectors.toList());
			size++;
		}
		
		Map<Integer, Long> pointsStatistics = IntStreamEx.range(0, grid.length)
				.flatMap(x -> IntStreamEx.range(0, grid[x].length).map(y -> grid[x][y][1]))
				.collect(IntCollector.groupingBy(value -> value, IntCollector.counting()));
		
		Set<Integer> infinitePointsId = IntStreamEx.range(0, grid.length).flatMap(x -> IntStream.of(grid[x][0][1], grid[x][grid[x].length - 1][1]))
				.append(IntStreamEx.range(0, grid[0].length).flatMap(y -> IntStream.of(grid[0][y][1], grid[grid.length - 1][y][1])))
				.distinct()
				.boxed()
				.collect(Collectors.toSet());
		
		pointsStatistics.entrySet().stream()
				.filter(entry -> !infinitePointsId.contains(entry.getKey()))
				.max(Comparator.comparing(Map.Entry::getValue))
				.ifPresent(System.out::println);
	}
	
	
	
	private boolean extendArea(Point p, short[][][] grid, short size) {
		if(size == 0){
			return putMarker(grid, p.x, p.y, size, p.id);
		}
		int[] intX = IntStreamEx.rangeClosed(p.x - size, p.x + size)
				.append(IntStreamEx.range(p.x + size - 1, p.x - size,-1))
				.toArray();
		int[] intY = IntStreamEx.rangeClosed(p.y, p.y + size)
				.append(IntStreamEx.range(p.y + size - 1, p.y - size, -1))
				.append(IntStreamEx.range(p.y - size, p.y))
				.toArray();
		return IntStreamEx.zip(intX, intY, (x,  y) -> putMarker(grid, x, y, size, p.id) ? 1 : 0)
				.sum() > 0;
	}
	
	private boolean putMarker(short[][][] grid, int x, int y, short size, short id) {
		if (x>=0 && x<grid.length) {
			if(y>=0 && y<grid[x].length) {
				if(grid[x][y][0] > size) {
					grid[x][y][0] = size;
					grid[x][y][1] = id;
					return true;
				} else if (grid[x][y][0] == size) {
					grid[x][y][1] = -2;
					return true;
				}
			}
		}
		return false;
	}
	
	private class Point {
		private final short id;
		private final int x, y;
		
		public Point(short id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "Point{" +
					"id=" + id +
					", x=" + x +
					", y=" + y +
					'}';
		}
	}
}