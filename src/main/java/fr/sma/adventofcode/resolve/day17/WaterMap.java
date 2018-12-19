package fr.sma.adventofcode.resolve.day17;

import com.google.common.math.PairedStatsAccumulator;
import fr.sma.adventofcode.resolve.util.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.util.TriConsumer;

public class WaterMap {
	
	private final Element[][] area;
	
	private WaterPainter painter;
	
	private final int paddingX;
	
	private WaterMap(Element[][] area, int paddingX) {
		this.area = area;
		this.paddingX = paddingX;
	}
	
	public static WaterMap build(String values) {
		PairedStatsAccumulator stat = new PairedStatsAccumulator();
		
		List<Point> clayPoints = StreamEx.split(values, "\n")
				.flatMap(WaterMap::fromLine)
				.peek(point -> stat.add(point.getX(),point.getY()))
				.collect(Collectors.toList());
		
		int paddingX = (int) stat.xStats().min()-1;
		
		int paddingY = (int) stat.yStats().min();
		
		Element[][] area = new Element[(int) stat.xStats().max()+2 - paddingX][(int) stat.yStats().max()+1 - paddingY];
		
		for (int x = 0; x < area.length; x++) {
			for (int y = 0; y < area[x].length; y++) {
				area[x][y] = Element.SAND;
			}
		}
		
		clayPoints.forEach(p -> area[p.getX() - paddingX][p.getY() - paddingY] = Element.CLAY);
		
		return new WaterMap(area, paddingX);
	}
	
	public WaterPainter getPainter() {
		if (painter == null) {
			painter = new WaterPainter(area, new Point(0, 0), Integer.MAX_VALUE);
		}
		return painter;
	}
	
	public void simulateWater(Point spring){
		flowWater(new Point(spring.getX() - paddingX, spring.getY()));
	}
	
	private boolean flowWater(Point p) {
		Optional<Element> cur = p.getIn(area);
		if (!cur.isPresent()) { //if we are out of bound
			return true;
		}
		Element curElem = cur.get();
		Point down = new Point(p.getX(), p.getY() + 1);
		switch (curElem) {
			case CLAY:
			case STILL_WATER:
				return false;
			case FLOWING_WATER:
				return down.getIn(area).filter(e -> e == Element.FLOWING_WATER).isPresent();
			case SAND:
				area[p.getX()][p.getY()] = Element.FLOWING_WATER;
				if(painter != null) {
					painter.repaint(p);
				}
				if(down.getIn(area).filter(e -> e == Element.FLOWING_WATER).isPresent()) {
					return true;
				}
				if(flowWater(down)) {
					return true;
				} else if(down.getIn(area).filter(e -> e == Element.FLOWING_WATER).isPresent()) {
					makeWaterLineStill(down);
					if(painter != null) {
						painter.repaint(p);
					}
				}
				Point left = new Point(p.getX() - 1, p.getY());
				Point right = new Point(p.getX() + 1, p.getY());
				boolean canFlowLeft = flowWater(left);
				boolean canFlowRight = flowWater(right);
				if (!canFlowLeft && !canFlowRight) {
					return false;
				} else {
					return true;
				}
		}
		throw new IllegalStateException("can't not have an element");
	}
	
	private void makeWaterLineStill(Point p) {
		if (p.getIn(area).filter(e -> e == Element.FLOWING_WATER).isPresent()) {
			area[p.getX()][p.getY()] = Element.STILL_WATER;
			makeWaterLineStill(new Point(p.getX()-1, p.getY()));
			makeWaterLineStill(new Point(p.getX()+1, p.getY()));
		}
	}
	
	private static final Pattern LINE_PATTERN = Pattern.compile("([x|y])=(\\d+), [x|y]=(\\d+)..(\\d+)");
	
	private static StreamEx<Point> fromLine(String line) {
		Matcher matcher = LINE_PATTERN.matcher(line);
		matcher.matches();
		int a = Integer.parseInt(matcher.group(2));
		IntStreamEx b = IntStreamEx.rangeClosed(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
		if ("x".equals(matcher.group(1))) {
			return b.mapToObj(y -> new Point(a, y));
		} else {
			return b.mapToObj(x -> new Point(x, a));
		}
	}
	
	public Element[][] getArea() {
		return area;
	}
	
	public String printArea(Point center, int sizeX, int sizeY) {
		StringBuilder sb = new StringBuilder();
		for (int y = Math.max(0, center.getY() - sizeY); y < Math.min(area[0].length, center.getY() + sizeY); y++) {
			for (int x = Math.max(0, center.getX() - sizeX); x < Math.min(area.length, center.getX() + sizeX); x++){
				sb.append(area[x][y].getSymbol());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static class WaterPainter extends JFrame {
		private static final int scale = 1;
		
		private Point center;
		
		private final Map<Element, TriConsumer<Integer, Integer, Graphics>> elementSprite = Map.of(
				Element.SAND, (x, y, g) -> {g.setColor(Color.yellow);g.fillRect(x, y, scale, scale);},
				Element.FLOWING_WATER, (x, y, g) -> {g.setColor(Color.blue);g.drawLine(x, y+ scale/2, x + scale, y + scale/2);},
				Element.STILL_WATER, (x, y, g) -> {g.setColor(Color.blue);g.fillRect(x, y, scale, scale);},
				Element.CLAY, (x, y, g) -> {g.setColor(Color.gray);g.fillRect(x, y, scale, scale);}
		);
		
		private final JPanel drawPanel;
		
		public void repaint(Point center) {
			this.center = center;
			super.repaint();
		}
		
		private WaterPainter(Element[][] area, Point start, int size) throws HeadlessException {
			super("day17Ex1");
			
			this.center = start;
			
			Dimension dim = new Dimension(Math.min(area[0].length, size) * scale, Math.min(area.length, size) * scale);
			this.drawPanel = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					int startY = Math.max(0, center.getY() - size/2);
					int startX = Math.max(0, center.getX() - size/2);
					for (int y = 0; y <Math.min(area[0].length, size); y++) {
						for (int x = 0; x < Math.min(area.length, size); x++){
							Element element = area[startX + x][startY + y];
							elementSprite.get(element).accept(y*scale, x*scale, g);
						}
					}
				}
			};
			drawPanel.setBackground(Color.white);
			this.setSize(new Dimension(dim.width + 20, dim.height + 50));
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.add(drawPanel);
			this.setVisible(true);
		}
	}
}
