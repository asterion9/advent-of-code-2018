package fr.sma.adventofcode.resolve.day22;

import fr.sma.adventofcode.resolve.day20.Resizable2dArray;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Optional;

public class Cave {
	private final Resizable2dArray<CaveNode> cave;
	private final int depth;
	
	public Cave(int tx, int ty, int depth) {
		this.depth = depth;
		this.cave = new Resizable2dArray<>();
		
		cave.put(0, 0, new CaveNode(0, 0, depth % 20183));
		cave.put(tx, ty,  new CaveNode(tx, ty, depth % 20183));
	}
	
	public String print() {
		StringBuilder sb = new StringBuilder();
		for (int y = cave.getyMin(); y <= cave.getyMax(); y++) {
			for (int x = cave.getxMin(); x <= cave.getxMax(); x++) {
				sb.append(Optional.ofNullable(cave.get(x, y))
						.map(value -> String.valueOf(value.terrain.ordinal()))
						.orElse(" "));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static class CaveNode {
		private final int x;
		private final int y;
		private final int erosion;
		private final Terrain terrain;
		private final EnumMap<Equipment, Integer> times;
		
		public CaveNode(int x, int y, int erosion) {
			this(x, y, erosion, Terrain.values()[erosion%3]);
		}
		
		public CaveNode(int x, int y, int erosion, Terrain terrain) {
			this.x = x;
			this.y = y;
			this.erosion = erosion;
			this.terrain = terrain;
			times = StreamEx.of(terrain.availableEquipments)
					.mapToEntry(e -> Integer.MAX_VALUE/2)
					.toCustomMap(() -> new EnumMap<>(Equipment.class));
		}
		
		public int getErosion() {
			return erosion;
		}
		
		public Terrain getTerrain() {
			return terrain;
		}
		
		public EnumMap<Equipment, Integer> getTimes() {
			return times;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public boolean updateTime(Equipment equipment, int time) {
			if(!times.containsKey(equipment)) {
				return false;
			}
			boolean hasChanged = EntryStream.of(times)
					.filterKeyValue((e, t) -> {
						if (equipment == e && t > time + 1) {
							times.put(e, time + 1);
							return true;
						} else if (t > time + 8) {
							times.put(e, time + 8);
							return true;
						}
						return false;
					}).count() > 0;
			return hasChanged;
		}
		
		public boolean setTimeFrom(CaveNode from) {
			return EntryStream.of(from.times).filterKeyValue(this::updateTime).count() > 0;
		}
	}
	
	public enum Equipment {
		TORCH,
		GEAR,
		NOTHING;
	
	}
	public enum Terrain {
		ROCKY(EnumSet.of(Equipment.TORCH, Equipment.GEAR)),
		WET(EnumSet.of(Equipment.GEAR, Equipment.NOTHING)),
		NARROW(EnumSet.of(Equipment.TORCH, Equipment.NOTHING));
		
		Terrain(EnumSet<Equipment> availableEquipments) {
			this.availableEquipments = availableEquipments;
		}
		
		private final EnumSet<Equipment> availableEquipments;
		
		public EnumSet<Equipment> getAvailableEquipments() {
			return availableEquipments;
		}
	}
	
	public CaveNode getCaveNode(int x, int y) {
		return cave.computeIfAbsent(x, y, (x1, y1) -> {
			if(x1 == 0) {
				return new CaveNode(x, y, (y1 * 48271 + depth) % 20183);
			}
			if(y1 == 0) {
				return  new CaveNode(x, y, (x1 * 16807 + depth) % 20183);
			}
			return new CaveNode(x, y, (modMul(getCaveNode(x1-1, y1).erosion, getCaveNode(x1, y1-1).erosion, 20183) + depth) % 20183);
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
