package fr.sma.adventofcode.resolve.day8;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

/**
 * same as before, but calculate the required size recursively.
 */
@Component
public class Day08Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day08Ex2");
		
		String values = dataFetcher.fetch(8).trim();
		
		Deque<Integer> sequence = StreamEx.split(values, " ")
				.map(Integer::parseInt)
				.collect(Collectors.toCollection(ArrayDeque::new));
		
		Node root = new Node(sequence);
		
		System.out.println(root.getSize());
	}
}