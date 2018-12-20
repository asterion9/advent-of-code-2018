package fr.sma.adventofcode.resolve.day15;

public interface Element {
	
	public enum Type {
		GOBELIN("G"),
		ELF("E"),
		WALL("#"),
		EMPTY(".");
		
		private final String symbol;
		
		Type(String symbol) {
			this.symbol = symbol;
		}
		
		public String getSymbol() {
			return symbol;
		}
	}
	
	public Type getType();
	
	public static Element build(String s, int elfAp) {
		switch (s){
			case "#":
				return NonInteractiveElements.WALL;
			case ".":
				return NonInteractiveElements.EMPTY;
			case "E":
				return new Unit( Type.ELF, elfAp);
			case "G":
				return new Unit(Type.GOBELIN, 3);
			default:
				throw new IllegalArgumentException("can't parse string " + s);
		}
	}
}
