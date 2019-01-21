package fr.sma.adventofcode.resolve.processor.lambda;

import java.util.Set;

public class NamedInstruction implements Instruction {
	
	private final Instruction instruction;
	private final Set<Integer> readIndexes;
	private final Set<Integer> writeIndexes;
	
	public NamedInstruction(Instruction instruction, Set<Integer> readIndexes, Set<Integer> writeIndexes) {
		this.instruction = instruction;
		this.readIndexes = readIndexes;
		this.writeIndexes = writeIndexes;
	}
	
	public Set<Integer> getReadIndexes() {
		return readIndexes;
	}
	
	public Set<Integer> getWriteIndexes() {
		return writeIndexes;
	}
	
	@Override
	public void execute(int[] register) {
		instruction.execute(register);
	}
}
