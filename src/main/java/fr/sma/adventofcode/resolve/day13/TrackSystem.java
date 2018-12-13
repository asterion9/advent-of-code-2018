package fr.sma.adventofcode.resolve.day13;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.util.TriConsumer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TrackSystem {
	private final TrackType[][] area;
	private final List<Chariot> chariots;
	
	private TrackSystem(TrackType[][] area, List<Chariot> chariots) {
		this.area = area;
		this.chariots = chariots;
	}
	
	public static TrackSystem buildTrackSystem(String trackMap) {
		List<Chariot> chariots = new ArrayList<>();
		
		TrackType[][] area = EntryStream.of(trackMap.split("\n"))
				.mapToValue((y, l) ->
						EntryStream.of(l.split(""))
								.mapToValue((x, s) ->
										Chariot.buildChariot(x, y, s)
												.stream()
												.peek(chariots::add)
												.map(Chariot::getUnderTrack)
												.findFirst().orElseGet(() -> TrackType.getTrack(s))
								).values().toArray(new TrackType[0])
				).values().toArray(new TrackType[0][0]);
		
		return new TrackSystem(area, chariots);
	}
	
	public Optional<Chariot> moveAll() {
		StreamEx.of(chariots)
				.sorted(Comparator.comparing(Chariot::getY).thenComparing(Chariot::getX))
				.forEachOrdered(c -> c.move(area));
		
		return EntryStream.ofPairs(chariots).mapKeyValue((chariot1, chariot2) -> {
					if (Chariot.areOnSameTrack(chariot1, chariot2)) {
						return Optional.of(chariot1);
					} else {
						return Optional.<Chariot>empty();
					}
				}).filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();
				
	}
	
	public static class Chariot {
		private int x, y;
		
		private Direction direction;
		
		private int intersectionDirectionOffest = 1;
		
		private TrackType underTrack;
		
		private static final Map<Direction, Direction> slashTurn = Map.of(
				Direction.UP, Direction.RIGHT,
				Direction.RIGHT, Direction.UP,
				Direction.DOWN, Direction.LEFT,
				Direction.LEFT, Direction.DOWN);
		
		private static final Map<Direction, Direction> antiSlashTurn = Map.of(
				Direction.UP, Direction.LEFT,
				Direction.LEFT, Direction.UP,
				Direction.DOWN, Direction.RIGHT,
				Direction.RIGHT, Direction.DOWN);
		
		public static boolean areOnSameTrack(Chariot c1, Chariot c2) {
			return c1.x == c2.x && c1.y == c2.y;
		}
		
		public static Optional<Chariot> buildChariot(int x, int y, String symbol) {
			switch (symbol) {
				case "^":
					return Optional.of(new Chariot(x, y, Direction.UP));
				case ">":
					return Optional.of(new Chariot(x, y, Direction.RIGHT));
				case "v":
					return Optional.of(new Chariot(x, y, Direction.DOWN));
				case "<":
					return Optional.of(new Chariot(x, y, Direction.LEFT));
				default:
					return Optional.empty();
			}
		}
		
		private Chariot(int x, int y, Direction direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
			this.underTrack = direction == Direction.LEFT || direction == Direction.RIGHT ? TrackType.HORIZONTAL : TrackType.VERTICAL;
		}
		
		private Direction getNextMove() {
			switch (underTrack) {
				case VERTICAL:
					return direction;
				case HORIZONTAL:
					return direction;
				case CORNER_SLASH:
					return slashTurn.get(direction);
				case CORNER_ANTI_SLASH:
					return antiSlashTurn.get(direction);
				case INTERSECTION:
					return getNextIntersectionDirection();
				case NONE:
				default:
					throw new IllegalStateException("can't move if not over a track");
					
			}
		}
		
		private Direction getNextIntersectionDirection() {
			return direction.getTurnByQuarter(intersectionDirectionOffest);
		}
		
		private void move(TrackType[][] area) {
			Direction nextMove = this.getNextMove();
			if (underTrack == TrackType.INTERSECTION) {
				intersectionDirectionOffest = ((intersectionDirectionOffest+3)% 3) - 1;
			}
			switch (nextMove) {
				case LEFT:
					this.x--;
					break;
				case UP:
					this.y--;
					break;
				case RIGHT:
					this.x++;
					break;
				case DOWN:
					this.y++;
					break;
			}
			this.direction = nextMove;
			underTrack = area[this.y][this.x];
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public TrackType getUnderTrack() {
			return underTrack;
		}
		
		public void setUnderTrack(TrackType underTrack) {
			this.underTrack = underTrack;
		}
		
		public Direction getDirection() {
			return direction;
		}
		
		public void setDirection(Direction direction) {
			this.direction = direction;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
	}
	
	private enum TrackType {
		VERTICAL("|"),
		HORIZONTAL("-"),
		CORNER_SLASH("/"),
		CORNER_ANTI_SLASH("\\"),
		INTERSECTION("+"),
		NONE(" ");
		
		private final String symbol;
		
		TrackType(String symbol) {
			this.symbol = symbol;
		}
		
		public static TrackType getTrack(String symbol){
			return EnumSet.allOf(TrackType.class).stream()
					.filter(tt -> tt.symbol.equals(symbol))
					.findAny()
					.orElseThrow(() -> new IllegalArgumentException("can't find track for symbol " + symbol));
		}
		
		public String getSymbol() {
			return symbol;
		}
	}
	
	private enum Direction {
		UP,
		LEFT,
		DOWN,
		RIGHT;
		
		public Direction getTurnByQuarter(int quarterTurn) {
			return Direction.values()[(this.ordinal()+quarterTurn+Direction.values().length)%Direction.values().length];
		}
	}
	
	public TrackSystemPainter getPainter() {
		TrackSystemPainter trackSystemPainter = new TrackSystemPainter(this);
		return trackSystemPainter;
	}
	
	
	public static class TrackSystemPainter extends JFrame {
		private static final int scale = 5;
		
		private final Map<TrackType, TriConsumer<Integer, Integer, Graphics>> trackSprite = Map.of(
				TrackType.VERTICAL, (x, y, g) -> g.drawLine(x + scale/2, y, x + scale/2, y + scale-1),
				TrackType.HORIZONTAL, (x, y, g) -> g.drawLine(x, y + scale/2, x + scale-1, y + scale/2),
				TrackType.CORNER_SLASH, (x, y, g) -> g.drawLine(x + scale-1, y, x, y + scale-1),
				TrackType.CORNER_ANTI_SLASH, (x, y, g) -> g.drawLine(x + scale-1, y + scale-1, x, y),
				TrackType.INTERSECTION, (x, y, g) -> {
					g.drawLine(x, y + scale/2, x + scale-1, y + scale/2);
					g.drawLine(x + scale/2, y, x + scale/2, y + scale-1);
				},
				TrackType.NONE, (x, y, g) -> {
				}
		);
		
		
		private final JPanel drawPanel;
		
		private TrackSystemPainter(TrackSystem trackSystem) throws HeadlessException {
			
			
			super("day13Ex1");
			
			Dimension size = new Dimension(trackSystem.area[0].length * scale, trackSystem.area.length * scale);
			this.drawPanel = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.black);
					for (int i = 0; i < trackSystem.area.length; i++) {
						for (int j = 0; j < trackSystem.area[i].length; j++) {
							trackSprite.getOrDefault(trackSystem.area[i][j], (x0, y0, g0) -> {
							}).accept(j * scale, i * scale, g);
						}
					}
					trackSystem.chariots.forEach(c -> {
						Color oldColor = g.getColor();
						g.setColor(Color.red);
						g.drawRect(c.getX() * scale, c.getY() * scale, scale-1, scale-1);
						g.setColor(oldColor);
					});
				}
			};
			drawPanel.setBackground(Color.white);
			this.setSize(new Dimension(size.width + 250, size.height + 50));
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.add(drawPanel);
			this.setVisible(true);
		}
	}
}
