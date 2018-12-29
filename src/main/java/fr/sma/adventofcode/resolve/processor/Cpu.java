package fr.sma.adventofcode.resolve.processor;

import fr.sma.adventofcode.resolve.processor.lambda.InstructionSetLambda;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Cpu {
	default int calculate(int... register) {
		return 0;
	}
	
	default int calculate(int a, int b, int c, int d, int e, int f) {
		return 0;
	}
	
	static int readPointer(String values) {
		final Pattern IP_LINE = Pattern.compile("#ip (\\d+)");
		
		Matcher ipMatcher = IP_LINE.matcher(values.split("\n")[0]);
		ipMatcher.matches();
		String ipReg = ipMatcher.group(1);
		return Integer.parseInt(ipReg);
	}
	
	static List<int[]> readCode(String values) {
		final Pattern INSTRUCTION_LINE = Pattern.compile("([a-z]{4}) (\\d+) (\\d+) (\\d+)");
		
		return StreamEx.split(values, "\n")
				.map(INSTRUCTION_LINE::matcher)
				.filter(Matcher::matches)
				.map(m -> new int[]{
						InstructionSetLambda.valueOf(m.group(1).toUpperCase()).ordinal(),
						Integer.parseInt(m.group(2)),
						Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4))
				}).collect(Collectors.toCollection(ArrayList::new));
	}
}
