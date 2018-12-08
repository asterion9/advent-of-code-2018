package fr.sma.adventofcode.resolve.day8;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day08Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day08Ex1");
		
		String values = dataFetcher.fetch(8).trim();
		
		Deque<Integer> sequence = StreamEx.split(values, " ")
				.map(Integer::parseInt)
				.collect(Collectors.toCollection(ArrayDeque::new));
		
		Node root = new Node(sequence);
		
		int metadataSum = StreamEx.ofTree(root, node -> node.getChildren().stream())
				.map(Node::getMetadatas)
				.flatMapToInt(IntStreamEx::of)
				.sum();
		
		System.out.println(metadataSum);
	}
	
}