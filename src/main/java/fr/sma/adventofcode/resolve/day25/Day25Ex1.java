package fr.sma.adventofcode.resolve.day25;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * I test each unique pair of stars to connect them together, then I build constellations by removing all the connected stars at once.
 */
@Component
public class Day25Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	private static final Pattern POINT_PATTERN = Pattern.compile("(-?\\w),(-?\\w),(-?\\w),(-?\\w)");
	
	@Override
	public void run() throws Exception {
		System.out.println("Day25Ex1");
		
		String values = dataFetcher.fetch(25).trim();
		
		List<Point4D> points = StreamEx.split(values, "\n")
				.map(POINT_PATTERN::matcher)
				.filter(Matcher::find)
				.map(m -> new Point4D(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))))
				.collect(Collectors.toList());
		
		EntryStream.ofPairs(points)
				.filterKeyValue((p1, p2) -> p1.distance(p2) <= 3)
				.forKeyValue((p1, p2) -> {
					p1.getNear().add(p2);
					p2.getNear().add(p1);
				});
		
		Set<Point4D> remainingPoints = new HashSet<>(points);
		
		int nbConstellation = 0;
		while(!remainingPoints.isEmpty()) {
			nbConstellation++;
			StreamEx.ofTree(remainingPoints.iterator().next(), p -> p.getNear().stream().filter(remainingPoints::contains))
					.forEach(remainingPoints::remove);
		}
		System.out.println("nbConstellation = " + nbConstellation);
	}
	
	static class Point4D {
		private int x;
		private int y;
		private int z;
		private int t;
		
		private Set<Point4D> near;
		
		public Point4D(int x, int y, int z, int t) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.t = t;
			
			near = new HashSet<>();
		}
		
		public Set<Point4D> getNear() {
			return near;
		}
		
		public int distance(Point4D other) {
			return manhattanDistance(x, y, z, t, other.x, other.y, other.z, other.t);
		}
	}
	
	static int manhattanDistance(int x1, int y1, int z1, int t1, int x2, int y2, int z2, int t2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2) + Math.abs(t1 - t2);
	}
}