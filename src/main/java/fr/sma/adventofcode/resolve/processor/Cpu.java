package fr.sma.adventofcode.resolve.processor;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Cpu {
	default int calculate(int... register) {
		throw new IllegalStateException("you must implement this method");
	}
	
	default int calculate(int a, int b, int c, int d, int e, int f) {
		return calculate(new int[]{a, b, c, d, e, f});
	}
	
	static int readPointer(String values) {
		final Pattern IP_LINE = Pattern.compile("#ip (\\d+)");
		
		Matcher ipMatcher = IP_LINE.matcher(values.split("\n")[0]);
		ipMatcher.matches();
		String ipReg = ipMatcher.group(1);
		return Integer.parseInt(ipReg);
	}
	
	static List<InstructionLine> readCode(String values) {
		final Pattern INSTRUCTION_LINE = Pattern.compile("([a-zA-Z]{4}) (\\d+) (\\d+) (\\d+)");
		
		List<Matcher> matchers = StreamEx.split(values, "\n")
				.map(INSTRUCTION_LINE::matcher)
				.filter(Matcher::matches).collect(Collectors.toList());
		return EntryStream.of(matchers)
				.mapKeyValue((i, m) -> new InstructionLine(
						BaseOperation.valueOf(m.group(1).toUpperCase()),
						new int[]{
								Integer.parseInt(m.group(2)),
								Integer.parseInt(m.group(3)),
								Integer.parseInt(m.group(4))},
						i)).collect(Collectors.toCollection(ArrayList::new));
	}
}