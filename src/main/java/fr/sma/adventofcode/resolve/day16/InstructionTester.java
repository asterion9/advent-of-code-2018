package fr.sma.adventofcode.resolve.day16;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import one.util.streamex.StreamEx;

public class InstructionTester {
	public static final Pattern OPERATION_PATTERN = Pattern.compile("(\\d+,? \\d+,? \\d+,? \\d+)");

	private int[] beforeReg;
	private int[] instrutction;
	private int[] afterReg;
	
	public InstructionTester(int[] beforeReg, int[] instruction, int[] afterReg) {
		this.beforeReg = beforeReg;
		this.instrutction = instruction;
		this.afterReg = afterReg;
	}
	
	public static InstructionTester build(String testString) {
		Matcher matcher = OPERATION_PATTERN.matcher(testString);
		matcher.find();
		int[] before = StreamEx.split(matcher.group(), ", ")
				.mapToInt(Integer::valueOf)
				.toArray();
		matcher.find();
		int[] instruction = StreamEx.split(matcher.group(), " ")
				.mapToInt(Integer::valueOf)
				.toArray();
		matcher.find();
		int[] after = StreamEx.split(matcher.group(), ", ")
				.mapToInt(Integer::valueOf)
				.toArray();
		
		return new InstructionTester(before, instruction, after);
	}
	
	public boolean matchInstruction(Instruction i) {
		int[] before = Arrays.copyOf(beforeReg, 4);
		i.execute(before, instrutction[1], instrutction[2], instrutction[3]);
		return Arrays.equals(before, afterReg);
	}
	
	public int[] getBeforeReg() {
		return beforeReg;
	}
	
	public int[] getInstrutction() {
		return instrutction;
	}
	
	public int[] getAfterReg() {
		return afterReg;
	}
}
