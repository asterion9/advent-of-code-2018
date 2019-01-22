package fr.sma.adventofcode.resolve.day20;

import one.util.streamex.StreamEx;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
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

public class Maze {
	private Resizable2dArray<Room> visitedRooms = new Resizable2dArray<>();
	private RoomPainter roomPainter;
	public Maze() {
		visitedRooms.put(0, 0, new Room());
	}
	
	public static Maze init(String regex, boolean print) {
		ListIterator<String> remainingPath = StreamEx.split(regex, "").skip(1).collect(Collectors.toList()).listIterator();
		Maze maze = new Maze();
		Level curLevel = new Level(Set.of(new Position(0, 0)));
		ArrayDeque<Level> prevLevels = new ArrayDeque<>();
		
		if(print) {
			maze.initRoomPainter();
		}
		
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
					Room curRoom = maze.visitedRooms.get(p.x, p.y);
					move.movePosition(p);
					Room nextRoom = maze.visitedRooms.computeIfAbsent(p.x, p.y, (x, y) -> {
						if(print) {
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) { }
						}
						return maze.visitedRooms.put(x, y, new Room());
					});
					curRoom.doors.put(move, nextRoom);
					nextRoom.doors.put(move.opposite(), curRoom);
				});
			}
		}
		
		maze.setDistance(0, maze.visitedRooms.get(0, 0), print);
		
		return maze;
	}
	
	private void setDistance(int depth, Room room, boolean print) {
		if(room.depth > depth) {
			room.depth = depth;
			if(print) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) { }
			}
			for(Room adjacent : room.doors.values()) {
				setDistance(depth+1, adjacent, print);
			}
		} else if(room.depth < depth){
			setDistance(room.depth, room, print);
		}
	}
	
	private void initRoomPainter() {
		roomPainter = new RoomPainter(visitedRooms);
		Timer t = new Timer(true);
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				roomPainter.repaint();
			}
		}, 0, 20);
	}
	
	public int getMax() {
		int max = Integer.MIN_VALUE;
		for (int x = visitedRooms.getxMin(); x <=visitedRooms.getxMax(); x++) {
			for (int y = visitedRooms.getyMin(); y <= visitedRooms.getyMax(); y++) {
				Room room = visitedRooms.get(x, y);
				if(room != null) {
					if(room.depth > max) {
						max = room.depth;
					}
				}
			}
		}
		return max;
	}
	
	public int getGreaterEqual(int limit) {
		int nb = 0;
		for (int x = visitedRooms.getxMin(); x <= visitedRooms.getxMax(); x++) {
			for (int y = visitedRooms.getyMin(); y <= visitedRooms.getyMax(); y++) {
				Room room = visitedRooms.get(x, y);
				if(room != null) {
					if(room.depth >= limit) {
						nb++;
					}
				}
			}
		}
		return nb;
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
	
	private static class Position {
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
				g.setColor(Color.getHSBColor(r.depth/6000f,1,1));
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
