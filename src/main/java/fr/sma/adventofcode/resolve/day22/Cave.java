package fr.sma.adventofcode.resolve.day22;

import fr.sma.adventofcode.resolve.day20.Resizable2dArray;

import java.util.Optional;

public class Cave {
	private final Resizable2dArray<Integer> cave;
	private final int depth;
	
	public Cave(int tx, int ty, int depth) {
		this.depth = depth;
		this.cave = new Resizable2dArray<>();
		
		cave.put(0, 0, depth % 20183);
		cave.put(tx, ty, depth % 20183);
	}
	
	public String print() {
		StringBuilder sb = new StringBuilder();
		for (int y = cave.getyMin(); y <= cave.getyMax(); y++) {
			for (int x = cave.getxMin(); x <= cave.getxMax(); x++) {
				sb.append(Optional.ofNullable(cave.get(x, y))
						.map(value -> String.valueOf(value %3))
						.orElse(" "));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public int getErosion(int x, int y) {
		return cave.computeIfAbsent(x, y, (x1, y1) -> {
			if(x1 == 0) {
				return (y1 * 48271 + depth) % 20183;
			}
			if(y1 == 0) {
				return (x1 * 16807 + depth) % 20183;
			}
			return (modMul(getErosion(x1-1, y1), getErosion(x1, y1-1), 20183) + depth) % 20183;
		});
	}
	
	private static int modMul(int a, int b, int mod) {
		int res = 0;
		a %= mod;
		while (b > 0) {
			if ((b & 1) > 0) {
				res = (res + a) % mod;
			}
			a = (2 * a) % mod;
			b >>= 1;
		}
		return res;
	}
}
