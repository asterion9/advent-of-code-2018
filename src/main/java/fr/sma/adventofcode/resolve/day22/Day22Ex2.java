package fr.sma.adventofcode.resolve.day22;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.EntryStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;

@Component
public class Day22Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Da22Ex2");
		
		String values = dataFetcher.fetch(22).trim();
		String[] inputs = values.split("[\\s,]");
		int depth = Integer.valueOf(inputs[1]);
		int tx = Integer.valueOf(inputs[3]);
		int ty = Integer.valueOf(inputs[4]);
		
		Cave cave = new Cave(tx, ty, depth);
		
		Comparator<Cave.CaveNode> caveNodeComparator = Comparator.<Cave.CaveNode>comparingInt(cn -> cn.getTimes().entrySet().stream().mapToInt(e -> e.getKey() == Cave.Equipment.TORCH ? e.getValue() : e.getValue() + 7).min().getAsInt() + manhattanDist(cn.getX(), cn.getY(), tx, ty))
				.thenComparingInt(Objects::hashCode);
		TreeSet<Cave.CaveNode> nextNodes = new TreeSet<>(
				caveNodeComparator
		);
		Cave.CaveNode lower = cave.getCaveNode(0, 0);
		lower.getTimes().replaceAll((e, t) -> e == Cave.Equipment.TORCH ? 0 : 7);
		nextNodes.add(lower);
		Cave.CaveNode target = cave.getCaveNode(tx, ty);
		
		while (caveNodeComparator.compare(lower = nextNodes.first(), target) < 0) {
			nextNodes.remove(lower);
			final Cave.CaveNode current = lower;
			EntryStream.of(
					current.getX()-1, current.getY(),
					current.getX()+1, current.getY(),
					current.getX(), current.getY()-1,
					current.getX(), current.getY()+1)
					.filterKeyValue((x, y) -> x >= 0 && y >= 0)
					.mapKeyValue(cave::getCaveNode)
					.filter(cn -> {
						boolean present = nextNodes.remove(cn);
						boolean changed = cn.setTimeFrom(current);
						return present || changed;
					})
					.forEach(nextNodes::add);
		}
		
		System.out.println(cave.print());
		System.out.println("\ntarget.getTimes().get(Cave.Equipment.TORCH) = " + target.getTimes().get(Cave.Equipment.TORCH));
	}
	
	private int manhattanDist(int x0, int y0, int x, int y) {
		return Math.abs(x0 - x) + Math.abs(y0 + y);
	}
	
}
