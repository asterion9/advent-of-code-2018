package fr.sma.adventofcode.resolve.day15;

import fr.sma.adventofcode.resolve.util.Point;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.sma.adventofcode.resolve.day15.Element.Type.ELF;
import static fr.sma.adventofcode.resolve.day15.Element.Type.EMPTY;
import static fr.sma.adventofcode.resolve.day15.Element.Type.GOBELIN;

public class Area {
	private Element[][] area;
	private Map<Unit, Point> allUnits;
	private Set<Unit> deadUnits;
	
	private Area(Element[][] map, Map<Unit, Point> allUnits) {
		this.area = map;
		this.allUnits = allUnits;
		this.deadUnits = new HashSet<>();
	}
	
	public static Area buildArea(String data) {
		Map<Unit, Point> allUnits = new HashMap<>();
		
		Element[][] elements = EntryStream.of(data.split("\n"))
				.mapToValue((y, l) ->
						EntryStream.of(l.split(""))
								.mapToValue((x, s) -> Element.build(s))
								.peekKeyValue((x, element) -> Optional.of(element)
										.filter(e -> e.getType() == ELF || e.getType() == GOBELIN)
										.map(e -> (Unit) e)
										.ifPresent(u -> allUnits.put(u, new Point(x, y)))
								).values().toArray(Element.class)
				).values().toArray(new Element[][]{});
		
		Element[][] areaElements = new Element[elements[0].length][elements.length];
		for (int y = 0; y < elements.length; y++) {
			for (int x = 0; x < elements[y].length; x++) {
				areaElements[x][y] = elements[y][x];
			}
		}
		return new Area(areaElements, allUnits);
	}
	
	public Set<Unit> doTurn() {
		return EntryStream.of(allUnits)// for each active unit
				.sortedBy(Map.Entry::getValue) //sorted by reading order
				.filterKeys(unit -> !deadUnits.contains(unit)) // filter out unit that are dead
				.mapToValue((u, p) -> getPathToTarget(new NodePoint(p, null), buildDistanceMap(p), u.getTargetType()) // get the path to the target
							.map(NodePoint::getPoints))
				.flatMapValues(Optional::stream)
				.filterValues(this::moveToward)
				.mapKeyValue((unit, path) -> attack(unit, path.getFirst()))
				.flatMap(Optional::stream)
				.collect(Collectors.toSet());
	}
	
	public int getHp(Element.Type type) {
		return EntryStream.of(allUnits)
				.keys()
				.filterBy(Unit::getType, type)
				.mapToInt(Unit::getHp)
				.sum();
	}
	
	public String drawMap() {
		return IntStreamEx.range(0, area[0].length)
				.mapToObj(y ->
						IntStreamEx.range(0, area.length)
								.mapToObj(x -> area[x][y])
								.map(Element::getType)
								.map(Element.Type::getSymbol)
								.append(EntryStream.of(allUnits)
										.filterValues(p -> p.getY() == y)
										.map(e -> e.getKey().toString())
										.joining(" ", "  ", "")
								)
						.joining()
				).joining("\n");
	}
	
	private boolean moveToward(Deque<Point> path) {
		if(path.size() > 2) {
			Point from = path.removeLast();
			Point to = path.peekLast();
			Unit u = (Unit) area[from.getX()][from.getY()];
			area[from.getX()][from.getY()] = NonInteractiveElements.EMPTY;
			area[to.getX()][to.getY()] = u;
			allUnits.put(u, to);
		}
		return path.size() <= 2;
	}
	
	private Optional<Unit> attack(Unit attacker, Point target) {
		Unit targetUnit = ((Unit)area[target.getX()][target.getY()]);
		targetUnit.setHp(targetUnit.getHp() - attacker.getAttackPoint()); //decrease its hp
		if(targetUnit.getHp() <= 0) {
			deadUnits.add(targetUnit);
			area[target.getX()][target.getY()] = NonInteractiveElements.EMPTY;
			return Optional.of(targetUnit);
		}
		return Optional.empty();
	}
	
	private Optional<NodePoint> getPathToTarget(NodePoint from, Integer[][] distanceMap, Element.Type targetElement) {
		int curDistance = distanceMap[from.getX()][from.getY()];
		
		return from.around(1) // for each point around
				.filter(p -> p.getIn(distanceMap).filter(i -> i > curDistance + 1).isPresent()) // filter out of bound and already closer
				.peek(p -> distanceMap[p.getX()][p.getY()] = curDistance + 1) // set the distance
				.map(p -> new NodePoint(p, from))
				.map(p -> {
					if(p.getIn(area).filter(e -> e.getType() == EMPTY).isPresent()) {
						return getPathToTarget(p, distanceMap, targetElement);
					} else if(p.getIn(area).filter(e -> e.getType() == targetElement).isPresent()) {
						return Optional.of(p);
					} else {
						return Optional.<NodePoint>empty();
					}
				}) // filter obstacle
				.flatMap(Optional::stream) // filter point that have no closest
				.min(Comparator.comparing(NodePoint::getDepth).thenComparing(Point.comparator)); // keep the closests then first by reading order
	}
	
	private Integer[][] buildDistanceMap(Point start) {
		Integer[][] integers = IntStreamEx.range(0, area.length)
				.mapToObj(x -> new Integer[area[x].length])
				.peek(a -> Arrays.fill(a, Integer.MAX_VALUE))
				.toArray(new Integer[][]{});
		integers[start.getX()][start.getY()] = 0;
		return integers;
	}
}
