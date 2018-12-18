package fr.sma.adventofcode.resolve.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

public class Processor {
	private final int[] register;
	
	private final ArrayList<InstructionSet> opCode;
	
	public Processor() {
		this(new int[4], new ArrayList<>(EnumSet.allOf(InstructionSet.class)));
	}
	
	public Processor(int[] register, ArrayList<InstructionSet> opCode) {
		this.opCode = opCode;
		this.register = register;
	}
	
	public void apply(Instruction operation, int a, int b, int c) {
		operation.execute(register, a, b, c);
	}
	
	
	public void apply(int[] opeLine) {
		Instruction i = opCode.get(opeLine[0]);
		this.apply(i, opeLine[1], opeLine[2], opeLine[3]);
	}
	
	public void resetReg(int[] newRegister) {
		System.arraycopy(register, 0, this.register, 0, 4);
	}
	
	public int[] getRegister() {
		return Arrays.copyOf(register, 4);
	}
}
