package fr.sma.adventofcode.resolve.day16;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * I built a "virtual machine" that can compile the code in a series of lambdas, then run it onto the register.
 * I the use a tester to compile each line with each possible instruction
 * and test whether this instruction is compatible with the expected output.
 */
@Component
public class Day16Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(Before: \\[\\d+, \\d+, \\d+, \\d+]\n\\d+ \\d+ \\d+ \\d+\nAfter:  \\[\\d+, \\d+, \\d+, \\d+])+");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day16Ex1");
		
		String values = dataFetcher.fetch(16).trim();
		
		Matcher instructionMatcher = INSTRUCTION_PATTERN.matcher(values);
		
		long nbOpeMatchThreeOrMore = StreamEx.of(instructionMatcher.results())
				.map(MatchResult::group)
				.map(InstructionTester::build)
				.mapToInt(it -> (int) StreamEx.of(EnumSet.allOf(BaseOperation.class))
						.filter(it::matchInstruction)
						.count())
				.filter(i -> i >= 3)
				.count();
		
		System.out.println("nbOpeMatchThreeOrMore = " + nbOpeMatchThreeOrMore);
	}
	
}