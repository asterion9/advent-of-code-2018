package fr.sma.adventofcode.resolve.day3;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day03Ex2 implements ExSolution {
	
	private static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day03Ex2");
		
		String values = dataFetcher.fetch(3);
		
		List<short[]> fabric = Stream.generate(() -> new short[1000]).limit(1000).collect(Collectors.toCollection(ArrayList::new));
		
		Set<Short> unOverridenIds = new HashSet<Short>();
		
		StreamEx.split(values, "\n")
				.map(PATTERN::matcher)
				.filter(Matcher::matches)
				.map(matcher -> {
					short id = Short.parseShort(matcher.group(1));
					int marginH = Integer.parseInt(matcher.group(2));
					int marginV = Integer.parseInt(matcher.group(3));
					int width = Integer.parseInt(matcher.group(4));
					int height = Integer.parseInt(matcher.group(5));
					return new FabricClaim(id, marginH, marginV, width, height);
				})
				.peek(fc -> unOverridenIds.add(fc.getId()))
				.flatMap(fc -> claimFabric(fabric, fc).stream())
				.forEach(unOverridenIds::remove);
		
		unOverridenIds.forEach(System.out::println);
	}
	
	public List<Short> claimFabric(List<short[]> fabric, FabricClaim claim) {
		return Stream.iterate(claim.getMarginV(), y -> y+1)
				.limit(claim.getHeight())
				.map(fabric::get)
				.flatMap(fabricLine -> Stream.iterate(claim.getMarginH(), x -> x+1)
						.limit(claim.getWidth())
						.flatMap(x -> {
							short oldValue = fabricLine[x];
							fabricLine[x] = claim.getId();
							if(oldValue != 0) {
								return Stream.of(oldValue, claim.getId());
							}
							return Stream.empty();
						})
				)
				.distinct()
				.collect(Collectors.toList());
	}
	
	public static class FabricClaim {
		private final short id;
		private final int marginH, marginV;
		private final int width, height;
		
		public FabricClaim(short id, int marginH, int marginV, int width, int height) {
			this.id = id;
			this.marginH = marginH;
			this.marginV = marginV;
			this.width = width;
			this.height = height;
		}
		
		public short getId() {
			return id;
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