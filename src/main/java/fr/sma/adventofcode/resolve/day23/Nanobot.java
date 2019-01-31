package fr.sma.adventofcode.resolve.day23;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Nanobot {
	private static Pattern NANOBOT_PATTERN = Pattern.compile("pos=<([-\\d]+),([-\\d]+),([-\\d]+)>, r=(\\d+)");
	
	private final int r;
	private final Point3d p;
	
	public static Nanobot build(String line) {
		Matcher matcher = NANOBOT_PATTERN.matcher(line);
		matcher.matches();
		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		int z = Integer.parseInt(matcher.group(3));
		int r = Integer.parseInt(matcher.group(4));
		
		return new Nanobot(x, y, z, r);
	}
	
	private Nanobot(int x, int y, int z, int r) {
		this.p = new Point3d(x, y, z);
		this.r = r;
	}
	
	public int getR() {
		return r;
	}
	
	public Point3d getP() {
		return p;
	}
}
