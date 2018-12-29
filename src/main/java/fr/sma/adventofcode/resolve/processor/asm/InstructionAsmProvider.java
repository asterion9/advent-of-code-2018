package fr.sma.adventofcode.resolve.processor.asm;

import org.objectweb.asm.tree.InsnList;

public interface InstructionAsmProvider {
	InsnList build(int inputA, int inputB, int inputC);
}
