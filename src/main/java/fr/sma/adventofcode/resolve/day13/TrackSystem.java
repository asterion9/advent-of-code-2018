package fr.sma.adventofcode.resolve.day13;

import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.util.TriConsumer;

public class TrackSystem {
	private final TrackType[][] area;
	private final Set<Chariot> chariots;
	private final Set<Chariot> collidedChariots;
	
	private TrackSystem(TrackType[][] area, Set<Chariot> chariots) {
		this.area = area;
		this.chariots = chariots;
		this.collidedChariots = new HashSet<>();
	}
	
	public Set<Chariot> getRemaingChariot() {
		return Sets.difference(chariots, collidedChariots);
	}
	
	public static TrackSystem buildTrackSystem(String trackMap) {
		Set<Chariot> chariots = new HashSet<>();
		
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
	
	public List<Chariot> moveAll() {
		List<Chariot> collidedThisTick = StreamEx.of(chariots)
				.sorted(Comparator.comparing(Chariot::getY).thenComparing(Chariot::getX))
				.filter(c -> !collidedChariots.contains(c))
				.peek(c -> c.move(area))
				.cross(chariots)
				.filterKeyValue((chariot, chariot2) -> chariot != chariot2)
				.filterKeyValue(Chariot::areOnSameTrack)
				.flatMapKeyValue(StreamEx::of)
				.peek(collidedChariots::add)
				.collect(Collectors.toList());
		chariots.removeAll(collidedThisTick);
		return collidedThisTick;
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
					throw new IllegalStateException("can't move if not over a track" + this);
					
			}
		}
		
		@Override
		public String toString() {
			return "Chariot{" +
					"x=" + x +
					", y=" + y +
					", direction=" + direction +
					", intersectionDirectionOffest=" + intersectionDirectionOffest +
					", underTrack=" + underTrack +
					'}';
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
		
		private final Map<Direction, BufferedImage> chariotSprites =
				StreamEx.of(Direction.values())
				.toMap(Function.identity(), this::createChariotImage);
		
		private final BufferedImage chariotCollision = createChariotCollision();
		
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
						Image sprite = chariotSprites.get(c.direction);
						g.drawImage(sprite, c.getX() * scale - 1, c.getY() * scale - 1, (img, infoflags, x, y, width, height) -> false);
					});
					trackSystem.collidedChariots.forEach(c ->
							g.drawImage(chariotCollision, c.getX() * scale - 1, c.getY() * scale - 1, (img, infoflags, x, y, width, height) -> false)
					);
				}
			};
			drawPanel.setBackground(Color.white);
			this.setSize(new Dimension(size.width + 250, size.height + 50));
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.add(drawPanel);
			this.setVisible(true);
		}
		
		private BufferedImage createChariotImage(Direction direction) {
			BufferedImage upChariot = new BufferedImage(scale + 2, scale + 2, BufferedImage.TYPE_BYTE_BINARY,
					new IndexColorModel(1, 2,
							new byte[]{0x00, (byte) 0xff},
							new byte[]{0x00, (byte) 0x00},
							new byte[]{0x00, (byte) 0x00},
							new byte[]{0x00, (byte) 0xff})
			);
			
			Graphics2D chariotGraphic = upChariot.createGraphics();
			drawArrow(chariotGraphic, direction);
			chariotGraphic.dispose();
			return upChariot;
		}
		
		private BufferedImage createChariotCollision() {
			BufferedImage upChariot = new BufferedImage(scale + 2, scale + 2, BufferedImage.TYPE_BYTE_BINARY,
					new IndexColorModel(1, 2,
							new byte[]{0x00, (byte) 0x00},
							new byte[]{0x00, (byte) 0xdd},
							new byte[]{0x00, (byte) 0x00},
							new byte[]{0x00, (byte) 0xff})
			);
			
			Graphics2D chariotGraphic = upChariot.createGraphics();
			chariotGraphic.fillRect(0, 0, scale+2, scale+2);
/*
			chariotGraphic.drawLine(0, 0, scale+2, scale+2);
			chariotGraphic.drawLine(0, scale+2, scale+2, 0);
*/
			chariotGraphic.dispose();
			return upChariot;
		}
		
		private void drawArrow(Graphics g, Direction direction) {
			g.setColor(new Color(0xff0000));
			switch (direction) {
				case UP:
					g.fillPolygon(new int[]{(scale+2) / 2, scale +1, 0}, new int[]{0, scale + 1, scale + 1}, 3);
					break;
				case LEFT:
					g.fillPolygon(new int[]{0, scale + 1, scale + 1}, new int[]{(scale+2) / 2, scale +1, 0}, 3);
					break;
				case DOWN:
					g.fillPolygon(new int[]{0, scale + 1, (scale+2) / 2}, new int[]{0, 0, scale +1}, 3);
					break;
				case RIGHT:
					g.fillPolygon(new int[]{0, scale +1, 0}, new int[]{0, (scale+2) / 2, scale + 1}, 3);
					break;
			}
			return;
		}
	}
}
