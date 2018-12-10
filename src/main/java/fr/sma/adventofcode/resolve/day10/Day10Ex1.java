package fr.sma.adventofcode.resolve.day10;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		
		List<StarCluster> starList = StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.map(m -> new StarCluster(
						Integer.parseInt(m.group(1)),
						Integer.parseInt(m.group(2)),
						Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4))
				))
				.collect(Collectors.toList());
		
		StarCluster sc0 = starList.stream()
				.reduce(StarCluster::merge).get();
		
		System.out.println("starcluster at t=0 : " + sc0);
	}
	
	private static class StarCluster {
		final float x;
		final float y;
		final float dx;
		final float dy;
		final float weight;
		final float size;
		
		@Override
		public String toString() {
			return "StarCluster{" +
					"x=" + x +
					", y=" + y +
					", dx=" + dx +
					", dy=" + dy +
					", weight=" + weight +
					", size=" + size +
					'}';
		}
		
		public StarCluster(float x, float y, float dx, float dy) {
			this(x, y, dx, dy, 1, 1);
		}
		
		private StarCluster(float x, float y, float dx, float dy, float weight, float size) {
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
			this.weight = weight;
			this.size = size;
		}
		
		public static StarCluster merge(StarCluster s1, StarCluster s2) {
			float newX = (s1.x*s1.weight + s2.x*s2.weight)/(s1.weight + s2.weight);
			float newY = (s1.y*s1.weight + s2.y*s2.weight)/(s1.weight + s2.weight);
			
			float newDx = (s1.dx*s1.weight + s2.dx*s2.weight)/(s1.weight + s2.weight);
			float newDy = (s1.dy*s1.weight + s2.dy*s2.weight)/(s1.weight + s2.weight);
			
			float newWeight = s1.weight + s2.weight;
			
			float newSize = (float) Math.hypot(Math.abs(s2.x - s1.x), Math.abs(s2.y - s1.y)) + s1.size/2 + s2.size/2;
			
			return new StarCluster(newX, newY, newDx, newDy, newWeight, newSize);
		}
		
		public float getX() {
			return x;
		}
		
		public float getY() {
			return y;
		}
		
		public float getDx() {
			return dx;
		}
		
		public float getDy() {
			return dy;
		}
		
		public float getWeight() {
			return weight;
		}
		
		public float getSize() {
			return size;
		}
	}
}