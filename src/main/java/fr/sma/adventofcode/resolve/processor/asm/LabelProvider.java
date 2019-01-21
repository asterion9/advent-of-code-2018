package fr.sma.adventofcode.resolve.processor.asm;

import org.objectweb.asm.tree.LabelNode;

import java.util.HashMap;
import java.util.Map;

public class LabelProvider {
	private final int maxLabel;
	private final LabelNode endLabel;
	private Map<Integer, LabelNode> labelMap = new HashMap<>();
	
	public LabelProvider(int maxLabel, LabelNode endLabelNode) {
		this.maxLabel = maxLabel;
		endLabel = endLabelNode;
	}
	
	public LabelNode getForPosition(int position) {
		if(position > maxLabel) {
			return endLabel;
		}
		return labelMap.computeIfAbsent(position, integer -> new LabelNode());
	}
	
	public Map<Integer, LabelNode> getLabelMap() {
		return labelMap;
	}
}
