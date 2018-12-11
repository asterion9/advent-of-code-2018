package fr.sma.adventofcode.resolve.day10;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
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
		
		Sky sky = new Sky(starList);
		
		int time = findMin(0, 20000, sky::calculateStarArea);
		
		System.out.println(sky.printSky(time));
	}
	
	private int findMin(int start, int end, Function<Integer, Double> f) {
		if (start == end) {
			return start;
		}
		
		double minV = f.apply(start);
		double maxV = f.apply(end);
		
		if (end - start == 1) {
			if (minV < maxV) {
				return start;
			} else {
				return end;
			}
		}
		
		if (minV < maxV) {
			return findMin(start, (end + start)/2, f);
		} else {
			return findMin((end + start)/2, end, f);
		}
	}
	
}