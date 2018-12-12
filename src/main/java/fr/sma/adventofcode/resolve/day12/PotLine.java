package fr.sma.adventofcode.resolve.day12;

import one.util.streamex.IntStreamEx;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;

import java.util.Map;

class PotLine {
	private final String pots;
	private final long offset;
	private final long year;
	
	public PotLine(String pots, long offset, long year) {
		int startIndex = pots.indexOf("#");
		
		this.pots = pots.substring(startIndex, pots.lastIndexOf("#") + 1);
		this.offset = startIndex + offset;
		this.year = year;
	}
	
	public String getPots() {
		return pots;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public long potSum() {
		return StreamEx.split(pots, "")
				.zipWith(LongStreamEx.iterate(offset, i -> i+1))
				.filterKeys("#"::equals)
				.values()
				.mapToLong(i -> i)
				.sum();
	}
	
	public PotLine grow(Map<String, String> rules) {
		String buffer = "...";
		
		String potsLine = buffer + pots + buffer;
		
		String nextPots = IntStreamEx.range(2, potsLine.length() - 2)
				.mapToObj(i -> potsLine.substring(i - 2, i + 3))
				.map(s -> rules.getOrDefault(s, "."))
				.joining();
		
		return new PotLine(nextPots, offset - (buffer.length() - 2), year + 1);
	}
	
	public long getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return offset + "  " + pots;
	}
	
	public boolean hasSamePattern(PotLine other) {
		return this.pots.equals(other.pots);
	}
}
