package fr.sma.adventofcode.resolve.day3;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day03Ex1 implements ExSolution {
	
	private static final Pattern PATTERN = Pattern.compile("#\\d+ @ (\\d+),(\\d+): (\\d+)x(\\d+)");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day03Ex1");
		
		String values = dataFetcher.fetch(3);
		
		
		List<byte[]> fabric = Stream.generate(() -> new byte[1000]).limit(1000).collect(Collectors.toCollection(ArrayList::new));
		
		int nbDoubleClaim = StreamEx.split(values, "\n")
				.map(PATTERN::matcher)
				.filter(Matcher::matches)
				.map(matcher -> {
					int marginH = Integer.parseInt(matcher.group(1));
					int marginV = Integer.parseInt(matcher.group(2));
					int width = Integer.parseInt(matcher.group(3));
					int height = Integer.parseInt(matcher.group(4));
					return new FabricClaim(marginH, marginV, width, height);
				}).map(fc -> claimFabric(fabric, fc))
				.mapToInt(Integer::intValue)
				.sum();
		System.out.println(nbDoubleClaim);
	}
	
	public int claimFabric(List<byte[]> fabric, FabricClaim claim) {
		return (int) Stream.iterate(claim.getMarginV(), y -> y+1)
				.limit(claim.getHeight())
				.map(fabric::get)
				.flatMap(fabricLine -> Stream.iterate(claim.getMarginH(), x -> x+1)
						.limit(claim.getWidth())
						.peek(x -> fabricLine[x]++)
						.filter(x -> fabricLine[x] == 2)
				).count();
	}
	
	public static class FabricClaim {
		private final int marginH, marginV;
		private final int width, height;
		
		public FabricClaim(int marginH, int marginV, int width, int height) {
			this.marginH = marginH;
			this.marginV = marginV;
			this.width = width;
			this.height = height;
		}
		
		public int getMarginH() {
			return marginH;
		}
		
		public int getMarginV() {
			return marginV;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
	}
}