package fr.sma.adventofcode.resolve.day15;

public enum NonInteractiveElements implements Element {
	WALL(Element.Type.WALL),
	EMPTY(Element.Type.EMPTY);
	
	private final Element.Type type;
	
	public Element.Type getType() {
		return type;
	}
	
	NonInteractiveElements(Element.Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.getSymbol();
	}
}
