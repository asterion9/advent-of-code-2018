package fr.sma.adventofcode.resolve.day23;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * In order to find the point with the greatest number of nanobots connected, it is not possible to search each location.
 * however, considering the value of a point is vaguely continuous, I hypothesize that the point is in an area of high value points.
 * in order to find the point, I scan 10^3 points across the whole central area,
 * then I reduce the search area by 8 (2^3) and start again, centered around the previous highest point
 * until the area is small enough to be fully searched.
 */
@Component
public class Day23Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day23Ex2");
		
		String values = dataFetcher.fetch(23).trim();
		
		Set<Nanobot> nanobots = StreamEx.split(values, "\n").map(Nanobot::build).collect(Collectors.toSet());
		
		Point3d max = new Point3d(0, 0, 0);
		
		int nbPoint = 10;
		int r = 83886080;
		
		do {
			System.out.println("searching around " + max + " in a rayon of " + r);
			max = findApproxMax(nanobots, max, r, nbPoint);
			r /= 2;
		}while (r >= nbPoint);
		
		System.out.println("distance = " + max.distance(0, 0, 0));
	}
	
	private Point3d findApproxMax(Set<Nanobot> nanobots, Point3d center, int r, int nbPoint) {
		int step = r / nbPoint;
		
		Point3d max = center;
		int maxCount = 0;
		for (int x = center.x - r; x <= center.x + r; x += step) {
			for (int y = center.y - r; y <= center.y + r; y += step) {
				for (int z = center.z - r; z <= center.z + r; z += step) {
					int nbRange = 0;
					for (Nanobot nb : nanobots) {
						if (nb.getP().distance(x, y, z) <= nb.getR()) {
							nbRange++;
						}
					}
					if ((nbRange > maxCount) || (nbRange == maxCount && Point3d.manhattanDistance(0, 0, 0, x, y, z) < max.distance(0, 0, 0))) {
						max = new Point3d(x, y, z);
						maxCount = nbRange;
					}
				}
			}
		}
		return max;
	}
}