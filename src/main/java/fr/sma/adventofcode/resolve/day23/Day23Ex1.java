package fr.sma.adventofcode.resolve.day23;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Day23Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day23Ex1");
		
		String values = dataFetcher.fetch(23).trim();
		
		Set<Nanobot> nanobots = StreamEx.split(values, "\n").map(Nanobot::build).collect(Collectors.toSet());
		
		Nanobot maxNb = StreamEx.of(nanobots).max(Comparator.comparing(Nanobot::getR)).get();
		
		long count = StreamEx.of(nanobots).map(Nanobot::getP).mapToInt(p -> p.distance(maxNb.getP())).filter(d -> d <= maxNb.getR()).count();
		
		System.out.println("count = " + count);
	}
	
	
}