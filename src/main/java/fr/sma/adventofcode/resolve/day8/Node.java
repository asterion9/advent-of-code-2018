package fr.sma.adventofcode.resolve.day8;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

class Node {
	private final List<Integer> metadatas;
	private final List<Node> children;
	
	public Node(Deque<Integer> remainingData) {
		int nbChildren = remainingData.pop();
		int nbMetaData = remainingData.pop();
		children = new ArrayList<>();
		for (int i = 0; i < nbChildren; i++) {
			Node nextChild = new Node(remainingData);
			children.add(nextChild);
		}
		metadatas = StreamEx.generate(remainingData::pop).limit(nbMetaData).collect(Collectors.toList());
	}
	
	public List<Integer> getMetadatas() {
		return metadatas;
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public int getSize() {
		if(children.isEmpty()) {
			return IntStreamEx.of(metadatas).sum();
		}
		return IntStreamEx.of(metadatas)
				.map(i -> i-1)
				.filter(i -> i >= 0 && i < children.size())
				.mapToObj(children::get)
				.mapToInt(Node::getSize)
				.sum();
	}
}
