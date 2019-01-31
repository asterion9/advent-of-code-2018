package fr.sma.adventofcode.resolve.day23;

class Point3d {
	final int x, y, z;
	
	public Point3d(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int distance(Point3d point) {
		return distance(point.x, point.y, point.z);
	}
	
	public int distance(int x, int y, int z) {
		return manhattanDistance(this.x, this.y, this.z, x, y, z);
	}
	
	public static int manhattanDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
	}
	
	@Override
	public String toString() {
		return "Point3d{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
