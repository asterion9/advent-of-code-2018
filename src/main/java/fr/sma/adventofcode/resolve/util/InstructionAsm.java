package fr.sma.adventofcode.resolve.util;

import fr.sma.adventofcode.resolve.day16.Instruction;
import java.util.List;
import org.objectweb.asm.tree.AbstractInsnNode;

public enum InstructionAsm implements Instruction {
	
	ADDR(List.of()),
	ADDI(List.of()),
	MULR(List.of()),
	MULI(List.of()),
	BANR(List.of()),
	BANI(List.of()),
	BORR(List.of()),
	BORI(List.of()),
	SETR(List.of()),
	SETI(List.of()),
	GTIR(List.of()),
	GTRI(List.of()),
	GTRR(List.of()),
	EQIR(List.of()),
	EQRI(List.of()),
	EQRR(List.of());
	
	private final List<AbstractInsnNode> insns;
	
	InstructionAsm(List<AbstractInsnNode> insns) {
		this.insns = insns;
	}
	
	@Override
	public void execute(int[] register, int inputA, int inputB, int outputReg) {
	}
}
