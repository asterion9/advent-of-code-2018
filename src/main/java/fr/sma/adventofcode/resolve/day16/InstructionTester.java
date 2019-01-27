package fr.sma.adventofcode.resolve.day16;

import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import fr.sma.adventofcode.resolve.processor.lambda.LambdaInstructionBuilder;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionTester {
	public static final Pattern OPERATION_PATTERN = Pattern.compile("(\\d+,? \\d+,? \\d+,? \\d+)");

	private long[] beforeReg;
	private int[] instruction;
	private long[] afterReg;
	
	public InstructionTester(long[] beforeReg, int[] instruction, long[] afterReg) {
		this.beforeReg = beforeReg;
		this.instruction = instruction;
		this.afterReg = afterReg;
	}
	
	public static InstructionTester build(String testString) {
		Matcher matcher = OPERATION_PATTERN.matcher(testString);
		matcher.find();
		long[] before = StreamEx.split(matcher.group(), ", ")
				.mapToLong(Long::valueOf)
				.toArray();
		matcher.find();
		int[] instruction = StreamEx.split(matcher.group(), " ")
				.mapToInt(Integer::valueOf)
				.toArray();
		matcher.find();
		long[] after = StreamEx.split(matcher.group(), ", ")
				.mapToLong(Long::valueOf)
				.toArray();
		
		return new InstructionTester(before, instruction, after);
	}
	
	public boolean matchInstruction(BaseOperation operation) {
		long[] before = Arrays.copyOf(beforeReg, 4);
		LambdaInstructionBuilder.build(new InstructionLine(operation, Arrays.copyOfRange(instruction, 1, instruction.length),0)).execute(before);
		return Arrays.equals(before, afterReg);
	}
	
	public long[] getBeforeReg() {
		return beforeReg;
	}
	
	public long[] getAfterReg() {
		return afterReg;
	}
	
	public int[] getInstruction() {
		return instruction;
	}
}
