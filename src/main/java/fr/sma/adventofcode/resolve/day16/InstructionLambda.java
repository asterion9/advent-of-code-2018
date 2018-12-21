package fr.sma.adventofcode.resolve.day16;

public enum InstructionLambda implements Instruction {
	
	ADDR((r, a, b, c) -> r[c] = r[a] + r[b]),
	ADDI((r, a, b, c) -> r[c] = r[a] + b),
	MULR((r, a, b, c) -> r[c] = r[a] * r[b]),
	MULI((r, a, b, c) -> r[c] = r[a] * b),
	BANR((r, a, b, c) -> r[c] = r[a] & r[b]),
	BANI((r, a, b, c) -> r[c] = r[a] & b),
	BORR((r, a, b, c) -> r[c] = r[a] | r[b]),
	BORI((r, a, b, c) -> r[c] = r[a] | b),
	SETR((r, a, b, c) -> r[c] = r[a]),
	SETI((r, a, b, c) -> r[c] = a),
	GTIR((r, a, b, c) -> r[c] = a > r[b] ? 1 : 0),
	GTRI((r, a, b, c) -> r[c] = r[a] > b ? 1 : 0),
	GTRR((r, a, b, c) -> r[c] = r[a] > r[b] ? 1 : 0),
	EQIR((r, a, b, c) -> r[c] = a == r[b] ? 1 : 0),
	EQRI((r, a, b, c) -> r[c] = r[a] == b ? 1 : 0),
	EQRR((r, a, b, c) -> r[c] = r[a] == r[b] ? 1 : 0);
	
	InstructionLambda(Instruction instruction) {
		this.instruction = instruction;
	}
	
	private final Instruction instruction;
	
	@Override
	public void execute(int[] register, int inputA, int inputB, int outputReg) {
		instruction.execute(register, inputA, inputB, outputReg);
	}
}
