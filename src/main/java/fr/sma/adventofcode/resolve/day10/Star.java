package fr.sma.adventofcode.resolve.day10;

class Star {
	private final int x0;
	private final int y0;
	private final int dx;
	private final int dy;
	
	Star(int x0, int y0, int dx, int dy) {
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
