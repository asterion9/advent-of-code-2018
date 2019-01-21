package fr.sma.adventofcode.resolve.processor;

import one.util.streamex.StreamEx;

import java.util.Set;

public class InstructionLine {
	private final BaseOperation operation;
	private final int[] inputs;
	private final Set<Integer> readIndexes;
	private final Set<Integer> writeIndexes;
	private final int position;
	
	public InstructionLine(BaseOperation operation, int[] inputs, int position) {
		this.operation = operation;
		this.inputs = inputs;
		this.position = position;
		this.readIndexes = operation.getReadValues(this);
		this.writeIndexes = operation.getWriteValues(this);
	}
	
	public Set<Integer> getReadIndexes() {
		return readIndexes;
	}
	
	public Set<Integer> getWriteIndexes() {
		return writeIndexes;
	}
	
	public BaseOperation getOperation() {
		return operation;
	}
	
	public int[] getInputs() {
		return inputs;
	}
	
	public int getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return position + " " + operation.name() + " "
				+ StreamEx.of(readIndexes).joining(", ", "(", ")") + " -> "
				+ StreamEx.of(writeIndexes).joining(", ", "(", ")");
	}
}
