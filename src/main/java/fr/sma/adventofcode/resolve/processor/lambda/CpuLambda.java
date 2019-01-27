package fr.sma.adventofcode.resolve.processor.lambda;

import fr.sma.adventofcode.resolve.processor.Cpu;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CpuLambda implements Cpu {
	private int pointer;
	
	private final List<Instruction> code;
	
	
	public CpuLambda(int pointerLoc, List<InstructionLine> code) {
		this.pointer = pointerLoc;
		this.code = StreamEx.of(code)
				.map(instructionLine -> LambdaInstructionBuilder.build(pointerLoc, instructionLine))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	@Override
	public long calculate(long[] register) {
		long i = register[pointer];
		long r = register[0];
		while (i >= 0 && i < code.size()) {
			code.get((int) i).execute(register);
			i = register[pointer];
			if (r != register[0]) {
				r = register[0];
				System.out.println(r);
			}
		}
		return register[0];
	}
}