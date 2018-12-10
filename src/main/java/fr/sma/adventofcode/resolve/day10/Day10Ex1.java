package fr.sma.adventofcode.resolve.day10;

import com.google.common.math.PairedStats;
import com.google.common.math.PairedStatsAccumulator;
import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import java.util.Comparator;
import java.util.Map;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.StringLiteral;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Day10Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern LINE_PATTERN = Pattern.compile("position=< *(-?\\d+), *(-?\\d+)> velocity=< *(-?\\d+), *(-?\\d+)>");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day10Ex1");
		
		String values = dataFetcher.fetch(10).trim();
		
		List<Star> starList = StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.map(m -> new Star(
						Integer.parseInt(m.group(1)),
						Integer.parseInt(m.group(2)),
						Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4))
				))
				.collect(Collectors.toList());
		
		IntStreamEx.range(10805, 10820, 1)
				.mapToEntry(i -> i, i -> calculateStarArea(i, starList))
				.forEach(System.out::println);
	}
	
	public double calculateStarArea(int t, List<Star> starList) {
		PairedStatsAccumulator stat = new PairedStatsAccumulator();
		starList.forEach(s -> stat.add(s.getX(t), s.getY(t)));
		return (stat.xStats().max() - stat.xStats().min()) * (stat.yStats().max() - stat.yStats().min());
	}
	
	public String printSky(int t, List<Star> starList) {
		StreamEx.of(starList)
				.sorted(Comparator.<Star, Integer>comparing(s -> s.getX(t)).thenComparing(s -> s.getY(t)))
				.collapse((s1, s2) -> s1.getX(t) == s2.getX(t), )
	}
	
	
	private static class Star {
		final int x0;
		final int y0;
		final int dx;
		final int dy;
		
		
		
		private Star(int x0, int y0, int dx, int dy) {
			this.x0 = x0;
			this.y0 = y0;
			this.dx = dx;
			this.dy = dy;
		}
		
		public int getX(int t) {
			return x0 + dx*t;
		}
		
		
		public int getY(int t) {
			return y0 + dy*t;
		}
		
		/*public static Star merge(Star s1, Star s2) {
			float newX = (s1.x0 *s1.weight + s2.x0 *s2.weight)/(s1.weight + s2.weight);
			float newY = (s1.y0 *s1.weight + s2.y0 *s2.weight)/(s1.weight + s2.weight);
			
			float newDx = (s1.dx*s1.weight + s2.dx*s2.weight)/(s1.weight + s2.weight);
			float newDy = (s1.dy*s1.weight + s2.dy*s2.weight)/(s1.weight + s2.weight);
			
			float newWeight = s1.weight + s2.weight;
			
			float newSize = (float) Math.hypot(Math.abs(s2.x0 - s1.x0), Math.abs(s2.y0 - s1.y0)) + s1.size/2 + s2.size/2;
			
			return new Star(newX, newY, newDx, newDy, newWeight, newSize);
		}*/
		
		public int getX0() {
			return x0;
		}
		
		public int getY0() {
			return y0;
		}
		
		public int getDx() {
			return dx;
		}
		
		public int getDy() {
			return dy;
		}
	}
}