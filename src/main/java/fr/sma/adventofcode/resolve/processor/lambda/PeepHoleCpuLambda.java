package fr.sma.adventofcode.resolve.processor.lambda;

import fr.sma.adventofcode.resolve.processor.Cpu;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PeepHoleCpuLambda implements Cpu {
	private static final int TARGET_DEPTH = 5;
	
	private int pointer;
	
	private final List<NamedInstruction> code;
	
	private Set<Shortcut> shortcuts = new HashSet<>();
	
	private Set<Shortcut> lastShortcuts = new HashSet<>();
	
	
	public PeepHoleCpuLambda(int pointerLoc, List<InstructionLine> code) {
		this.pointer = pointerLoc;
		this.code = StreamEx.of(code)
				.map(instructionLine -> LambdaInstructionBuilder.build(pointerLoc, instructionLine))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	@Override
	public int calculate(int[] register) {
		int i = register[pointer];
		int r = register[0];
		while (i >= 0 && i < code.size()) {
			applyStep(register);
			i = register[pointer];
			if (r != register[0]) {
				r = register[0];
				System.out.println(r);
			}
		}
		return register[0];
	}
	
	private void applyStep(int[] register) {
		Shortcut matchingShortcut = StreamEx.of(shortcuts)
				.filter(shortcut -> shortcut.appliesTo(register))
				.max(Comparator.comparing(Shortcut::getDepth))
				.orElseGet(() -> {
					NamedInstruction instruction = code.get(register[pointer]);
					int[] beforeState = Arrays.copyOf(register, register.length);
					instruction.execute(register);
					return new Shortcut(beforeState, instruction.getReadIndexes(), register, instruction.getWriteIndexes());
				});
		
		matchingShortcut.execute(register);
		
		lastShortcuts = StreamEx.of(lastShortcuts)
				.filter(shortcut -> shortcut.canExpandInto(matchingShortcut))
				.map(shortcut -> Shortcut.expandShortcut(shortcut, matchingShortcut))
				.filter(shortcut -> shortcut.getStartStateSize() < 5)
				.peek(shortcuts::add)
				.append(matchingShortcut)
				.collect(Collectors.toSet());
	}
}
