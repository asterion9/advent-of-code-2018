package fr.sma.adventofcode.resolve.day2;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * redefine the problem as a distance problem.
 * we define distance as the number of different letters between two strings.
 * for each pair of string, calculate the distance, then find the smallest.
 *
 * NB: it is possible to use a more efficient algorithm to find two closest point,
 * but it is difficult to implement in a n dimensional space and not necessary for such small input
 * see https://en.wikipedia.org/wiki/Closest_pair_of_points_problem
 */
@Component
public class Day02Ex2 implements ExSolution {
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day02Ex2");
		
		String values = dataFetcher.fetch(2);
		
		List<String> ids = StreamEx.of(values.split("\n")).collect(Collectors.toList());
		
		PairDistance smallest = bruteForce(ids);
		
		String result = StreamEx.of(smallest.getLeftId().split("")).zipWith(StreamEx.of(smallest.getRightId().split("")), (cl, cr) -> cl.equals(cr) ? cl : null)
				.filter(Objects::nonNull)
				.collect(Collectors.joining());
		
		System.out.println(result);
	}
	
	private PairDistance bruteForce(List<String> ids) {
		return EntryStream.ofPairs(ids)
				.mapKeyValue(Day02Ex2::distance)
				.min(Comparator.comparing(PairDistance::getDistance))
				.orElseThrow(() -> new IllegalArgumentException("empty list of id"));
	}
	
	public static PairDistance distance(String lid, String rid) {
		int distance = (int) StreamEx.of(lid.chars().iterator()).zipWith(StreamEx.of(rid.chars().iterator()), Object::equals)
				.filter(aBoolean -> !aBoolean)
				.count();
		return new PairDistance(lid, rid, distance);
	}
	
	public static class PairDistance {
		String leftId, rightId;
		int distance;
		
		public PairDistance(String leftId, String rightId, int distance) {
			this.leftId = leftId;
			this.rightId = rightId;
			this.distance = distance;
		}
		
		public String getLeftId() {
			return leftId;
		}
		
		public void setLeftId(String leftId) {
			this.leftId = leftId;
		}
		
		public String getRightId() {
			return rightId;
		}
		
		public void setRightId(String rightId) {
			this.rightId = rightId;
		}
		
		public int getDistance() {
			return distance;
		}
		
		public void setDistance(int distance) {
			this.distance = distance;
		}
		
		@Override
		public String toString() {
			return "PairDistance{" +
					"leftId='" + leftId + '\'' +
					", rightId='" + rightId + '\'' +
					", distance=" + distance +
					'}';
		}
	}
}
