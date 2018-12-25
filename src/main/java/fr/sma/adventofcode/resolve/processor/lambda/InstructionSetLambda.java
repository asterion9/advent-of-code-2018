package fr.sma.adventofcode.resolve.processor.lambda;

public enum InstructionSetLambda {
	
	ADDR((a, b, c) -> r -> r[c] = r[a] + r[b]),
	ADDI((a, b, c) -> r -> r[c] = r[a] + b),
	MULR((a, b, c) -> r -> r[c] = r[a] * r[b]),
	MULI((a, b, c) -> r -> r[c] = r[a] * b),
	BANR((a, b, c) -> r -> r[c] = r[a] & r[b]),
	BANI((a, b, c) -> r -> r[c] = r[a] & b),
	BORR((a, b, c) -> r -> r[c] = r[a] | r[b]),
	BORI((a, b, c) -> r -> r[c] = r[a] | b),
	SETR((a, b, c) -> r -> r[c] = r[a]),
	SETI((a, b, c) -> r -> r[c] = a),
	GTIR((a, b, c) -> r -> r[c] = a > r[b] ? 1 : 0),
	GTRI((a, b, c) -> r -> r[c] = r[a] > b ? 1 : 0),
	GTRR((a, b, c) -> r -> r[c] = r[a] > r[b] ? 1 : 0),
	EQIR((a, b, c) -> r -> r[c] = a == r[b] ? 1 : 0),
	EQRI((a, b, c) -> r -> r[c] = r[a] == b ? 1 : 0),
	EQRR((a, b, c) -> r -> r[c] = r[a] == r[b] ? 1 : 0);
	
	InstructionSetLambda(InstructionProviderLambda builder) {
		this.builder = builder;
	}
	
	private final InstructionProviderLambda builder;
	
	public Instruction build(int a, int b, int c) {
		return builder.build(a, b, c);
	}
}
