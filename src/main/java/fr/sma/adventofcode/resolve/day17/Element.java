package fr.sma.adventofcode.resolve.day17;

import one.util.streamex.StreamEx;

public enum Element {
	SAND("."),
	CLAY("#"),
	STILL_WATER("~"),
	FLOWING_WATER("|");
	
	private final String symbol;
	
	public String getSymbol() {
		return symbol;
	}
	
	Element(String symbol) {
		this.symbol = symbol;
	}
	
	public Element findBySymbol(String symbol) {
		return StreamEx.of(Element.values())
				.filterBy(Element::getSymbol, symbol)
				.findFirst().orElseThrow();
	}
}
