package fr.sma.adventofcode.resolve.day2;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class Day02Ex1 implements ExSolution {
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day02Ex1");
		
		String values = dataFetcher.fetch(2);
		
		List<String> ids = StreamEx.of(values.split("\n")).collect(Collectors.toList());
		
		AtomicInteger nbDouble = new AtomicInteger();
		AtomicInteger nbTriple = new AtomicInteger();
		
		ids.forEach(id -> countMultiple(id, nbDouble, nbTriple));
		System.out.println("nbDouble = " + nbDouble);
		System.out.println("nbTriple = " + nbTriple);
		System.out.println(nbDouble.get() * nbTriple.get());
	}
	
	public void countMultiple(String id, AtomicInteger nbDouble, AtomicInteger nbTriple) {
		Map<Integer, List<Integer>> letterGroups = id.chars().boxed().collect(Collectors.groupingBy(o -> o));
		if(letterGroups.values().stream().map(List::size).anyMatch(s -> s == 2)) {
			nbDouble.incrementAndGet();
		}
		if(letterGroups.values().stream().map(List::size).anyMatch(s -> s == 3)) {
			nbTriple.incrementAndGet();
		}
	}
}
