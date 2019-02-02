package fr.sma.adventofcode.resolve.day5;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * same as before, but repeat with a letter removed each time.
 * kepp the most successful
 */
@Component
public class Day05Ex2 implements ExSolution {
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day05Ex1");
		
		String values = dataFetcher.fetch(5).trim(); // f***ing trim for line feed at end
		
		IntStreamEx.rangeClosed('a', 'z')
				.mapToObj(c -> StreamEx.split(values, "").filter(s -> c != s.toLowerCase().charAt(0)))
				.map(polyStream -> polyStream.collect(Collectors.toCollection(ArrayList::new)))
				.peek(this::polymerize)
				.map(List::size)
				.sorted()
				.findFirst()
				.ifPresent(System.out::println);
	}
	
	public void polymerize(ArrayList<String> polymer) {
		int index = 0;
		while(index < polymer.size() - 1) {
			if(doReact(polymer.get(index), polymer.get(index + 1))) {
				polymer.remove(index);
				polymer.remove(index);
				if(index > 0) {
					index--;
				}
			} else {
				index++;
			}
		}
	}
	
	private boolean doReact(String left, String right) {
		return left.toUpperCase().equals(right.toUpperCase()) && !left.equals(right);
	}
}