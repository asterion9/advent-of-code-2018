package fr.sma.adventofcode.resolve.day12;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Day12Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern POT_LINE_PATTERN = Pattern.compile("initial state: ([.#]+)");
	private final static Pattern RULE_LINE_PATTERN = Pattern.compile("([.#]{5}) => ([.#])");
	
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day12Ex2");
		
		String values = dataFetcher.fetch(12).trim();
		
		String pots = StreamEx.split(values, "\n")
				.map(POT_LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.map(m -> m.group(1))
				.findFirst().get();
		
		Map<String, String> rules = StreamEx.split(values, "\n")
				.map(RULE_LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.mapToEntry(m -> m.group(1), m -> m.group(2))
				.toMap();
		
		GrowthPredictor.Builder builder = new GrowthPredictor.Builder();
		
		StreamEx.iterate(new PotLine(pots, 0, 0), s -> s.grow(rules))
				.skip(200)
				.findFirst(builder::addPotLine);
		
		GrowthPredictor growthPredictor = builder.build();
		
		System.out.println(growthPredictor.getPotLineAtYear(50_000_000_000L).potSum());
	}
	
	private static class GrowthPredictor {
		private final List<PotLine> cycle;
		private final long offsetCycleChange;
		
		public GrowthPredictor(List<PotLine> cycle, long offsetCycleChange) {
			this.cycle = cycle;
			this.offsetCycleChange = offsetCycleChange;
		}
		
		public PotLine getPotLineAtYear(long year) {
			PotLine schema = cycle.get((int) ((year - cycle.get(0).getYear())% cycle.size()));
			return new PotLine(schema.getPots(), schema.getOffset() + (year - schema.getYear())/cycle.size()*this.offsetCycleChange, year);
		}
		
		
		private static class Builder {
			private final List<PotLine> pots;
			
			private int cycleIndex;
			
			public Builder() {
				pots = new ArrayList<>();
				cycleIndex = -1;
			}
			
			public boolean addPotLine(PotLine next) {
				if (cycleIndex != -1) {
					return true;
				}
				cycleIndex = findSamePattern(next);
				
				if (cycleIndex != -1) {
					pots.add(next);
					return true;
				} else {
					pots.add(next);
					return false;
				}
			}
			
			public GrowthPredictor build() {
				if (cycleIndex == -1) {
					throw new IllegalStateException("cycle not found yet");
				}
				return new GrowthPredictor(pots.subList(cycleIndex, pots.size()-1), pots.get(pots.size() -1).getOffset() - pots.get(cycleIndex).getOffset());
			}
			
			private int findSamePattern(PotLine other) {
				return StreamEx.of(pots)
						.findFirst(other::hasSamePattern)
						.map(pots::indexOf)
						.orElse(-1);
			}
		}
	}
}