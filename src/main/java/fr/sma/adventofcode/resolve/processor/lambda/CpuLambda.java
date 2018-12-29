package fr.sma.adventofcode.resolve.processor.lambda;

import fr.sma.adventofcode.resolve.processor.Cpu;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class CpuLambda implements Cpu {
	private int pointer;
	
	private final List<Instruction> code;
	
	public CpuLambda(int pointerLoc, List<int[]> code) {
		this(pointerLoc, new ArrayList<>(EnumSet.allOf(InstructionSetLambda.class)), code);
	}
	
	public CpuLambda(int pointerLoc, List<InstructionSetLambda> opCode, List<int[]> code) {
		this.pointer = pointerLoc;
		this.code = StreamEx.of(code)
				.map(line -> opCode.get(line[0]).build(line[1],line[2],line[3]))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	@Override
	public int calculate(int a, int b, int c, int d, int e, int f) {
		return calculate(new int[] {a, b, c, d, e, f});
	}
	
	@Override
	public int calculate(int[] register) {
		int i = register[pointer];
		int r = register[0];
		while (i >= 0 && i < code.size()) {
			code.get(i).execute(register);
			register[pointer]++;
			i = register[pointer];
			if (r != register[0]) {
				r = register[0];
				System.out.println(r);
			}
		}
		return register[0];
	}
}