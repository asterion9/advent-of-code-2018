package fr.sma.adventofcode.resolve.day16;

public interface Instruction {
	void execute(int[] register, int inputA, int inputB, int outputReg);
}
