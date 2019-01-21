package fr.sma.adventofcode.resolve.day20;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class Day20Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day20Ex1");
		
		String values = dataFetcher.fetch(20).trim();
		ListIterator<String> remainingPath = StreamEx.split(values, "").skip(1).collect(Collectors.toList()).listIterator();
		
		Room center = new Room();
		Resizable2dArray<Room> visitedRooms = new Resizable2dArray<>();
		visitedRooms.put(0, 0, center);
		ArrayDeque<Level> prevLevels = new ArrayDeque<>();
		Level curLevel = new Level(Set.of(new Position(0, 0)));
		
		RoomPainter roomPainter = new RoomPainter(visitedRooms);
		
		Timer t = new Timer(true);
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				roomPainter.repaint();
			}
		}, 0, 15);
		
		while(remainingPath.hasNext()) {
			String cur = remainingPath.next();
			if ("$".equals(cur)) {
				break;
			} else if ("(".equals(cur)) {
				prevLevels.addLast(curLevel); // store current paths
				Set<Position> curPos = StreamEx.of(curLevel.curPos).map(Position::copy).collect(Collectors.toSet()); // copy current paths as starting paths of next branch
				curLevel = new Level(curPos);
			} else if ("|".equals(cur)) {
				curLevel.branches.addAll(curLevel.curPos);
				curLevel.curPos = StreamEx.of(prevLevels.peekLast().curPos).map(Position::copy).collect(Collectors.toSet()); // copy previous paths as starting paths of next branch
			} else if (")".equals(cur)) {
				curLevel.branches.addAll(curLevel.curPos);
				prevLevels.peekLast().curPos = curLevel.branches;
				curLevel = prevLevels.removeLast();
			} else {
				Direction move = Direction.valueOf(cur);
				curLevel.curPos.forEach(p -> {
					Room curRoom = visitedRooms.get(p.x, p.y);
					move.movePosition(p);
					Room nextRoom = visitedRooms.computeIfAbsent(p.x, p.y, (x, y) -> {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return visitedRooms.put(x, y, new Room());
					});
					curRoom.doors.put(move, nextRoom);
					nextRoom.doors.put(move.opposite(), curRoom);
				});
			}
		}
		
		System.out.println(findFurther(0, center));
	}
	
	private int findFurther(int depth, Room room) throws InterruptedException {
		if(room.depth > depth) {
			room.depth = depth;
			Thread.sleep(1);
			int max = room.depth;
			for(Room adjacent : room.doors.values()) {
				int adDepth = findFurther(depth+1, adjacent);
				if(adDepth > max)
					max = adDepth;
			}
			return max;
			//return room.doors.values().stream().mapToInt(r -> findFurther(depth+1, r)).max().orElse(depth);
		} else if(room.depth < depth){
			return findFurther(room.depth, room);
		} else {
			return depth;
		}
	}
	
	private static class Level {
		Set<Position> curPos = new HashSet<>();
		Set<Position> branches = new HashSet<>();
		
		public Level(Set<Position> curPos) {
			this.curPos.addAll(curPos);
		}
	}
	
	private enum Direction {
		N(p -> p.y--) {@Override public Direction opposite() { return S; }},
		S(p -> p.y++) {@Override public Direction opposite() { return N; }},
		E(p -> p.x++) {@Override public Direction opposite() { return W; }},
		W(p -> p.x--) {@Override public Direction opposite() { return E; }};
		
		final Consumer<Position> mover;
		
		Direction(Consumer<Position> mover) {
			this.mover = mover;
		}
		
		public void movePosition(Position p) {
			mover.accept(p);
		}
		
		public abstract Direction opposite();
	}
	
	private static class Room {
		int depth = Integer.MAX_VALUE;
		EnumMap<Direction, Room> doors = new EnumMap<>(Direction.class);
	}
	
	private class Position {
		int x;
		int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Position copy() {
			return new Position(x, y);
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Position)) return false;
			Position position = (Position) o;
			return x == position.x &&
					y == position.y;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}
	
	public static class RoomPainter extends JFrame {
		private static final int scale = 5;
		
		private final JPanel drawPanel;
		
		private final Resizable2dArray<Room> rooms;
		
		private RoomPainter(Resizable2dArray<Room> rooms) throws HeadlessException {
			super("day20");
			
			this.rooms = rooms;
			
			this.drawPanel = new JPanel() {
				
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.black);
					for (int x = rooms.getxMin(); x <= rooms.getxMax(); x++) {
						for (int y = rooms.getyMin(); y <= rooms.getyMax(); y++) {
							Room curRoom = rooms.get(x, y);
							if(curRoom != null) {
								drawRoom((x - rooms.getxMin())*3, (y - rooms.getyMin())*3, curRoom, g);
							}
						}
					}
				}
			};
			drawPanel.setBackground(Color.white);
			//drawPanel.setSize((rooms.getxMax() - rooms.getxMin())*3, (rooms.getyMax() - rooms.getyMin())*3);
			drawPanel.setSize(500, 500);
			this.setSize(drawPanel.getSize());
			this.setResizable(true);
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.add(drawPanel);
			this.setVisible(true);
		}
		
		@Override
		public void repaint() {
			//drawPanel.setSize((rooms.getxMax() - rooms.getxMin())*3+50, (rooms.getyMax() - rooms.getyMin())*3+200);
			//this.setSize(drawPanel.getSize());
			super.repaint();
		}
		
		private void drawRoom(int x, int y, Room r, Graphics g) {
			
			if (r.depth >= 0 && r.depth <= 4000) {
				Color oldColor = g.getColor();
				g.setColor(Color.getHSBColor(r.depth/4000f,1,1));
				g.fillRect(x, y, 3, 3);
				g.setColor(oldColor);
			}
			g.fillRect(x + 1, y + 1, 1, 1);
			if (r.doors.containsKey(Direction.N))
				g.fillRect(x + 1, y, 1, 1);
			if (r.doors.containsKey(Direction.S))
				g.fillRect(x + 1, y + 2, 1, 1);
			if (r.doors.containsKey(Direction.E))
				g.fillRect(x + 2, y + 1, 1, 1);
			if (r.doors.containsKey(Direction.W))
				g.fillRect(x, y + 1, 1, 1);
		}
	}
}