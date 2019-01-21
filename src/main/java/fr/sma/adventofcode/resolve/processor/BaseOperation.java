package fr.sma.adventofcode.resolve.processor;

import one.util.streamex.IntStreamEx;

import java.util.Set;
import java.util.function.Function;

public enum BaseOperation {
	ADDR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2])),
	ADDI(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0), iline -> Set.of(iline.getInputs()[2])),
	MULR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2])),
	MULI(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0), iline -> Set.of(iline.getInputs()[2])),
	BANR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2])),
	BANI(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0), iline -> Set.of(iline.getInputs()[2])),
	BORR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2])),
	BORI(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0), iline -> Set.of(iline.getInputs()[2])),
	SETR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2])),
	SETI(iline -> Set.of(), iline -> Set.of(iline.getInputs()[2])),
	GTIR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 1), iline -> Set.of(iline.getInputs()[2])),
	GTRI(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0), iline -> Set.of(iline.getInputs()[2])),
	GTRR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2])),
	EQIR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 1), iline -> Set.of(iline.getInputs()[2])),
	EQRI(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0), iline -> Set.of(iline.getInputs()[2])),
	EQRR(iline -> BaseOperation.setOfIndexes(iline.getInputs(), 0, 1), iline -> Set.of(iline.getInputs()[2]));
	
	
	BaseOperation(Function<InstructionLine, Set<Integer>> readValuesProducer, Function<InstructionLine, Set<Integer>> writeValuesProducer) {
		this.readValuesProducer = readValuesProducer;
		this.writeValuesProducer = writeValuesProducer;
	}
	
	private final Function<InstructionLine, Set<Integer>> readValuesProducer;
	private final Function<InstructionLine, Set<Integer>> writeValuesProducer;
	
	public static Set<Integer> setOfIndexes(int[] inputs, int... indexes) {
		return IntStreamEx.of(indexes).map(i -> inputs[i]).boxed().toSet();
	}
	
	public Set<Integer> getReadValues(InstructionLine instructionLine) {
		return readValuesProducer.apply(instructionLine);
	}
	
	public Set<Integer> getWriteValues(InstructionLine instructionLine) {
		return writeValuesProducer.apply(instructionLine);
	}
}