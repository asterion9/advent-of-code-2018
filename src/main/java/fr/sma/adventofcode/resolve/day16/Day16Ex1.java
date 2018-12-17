package fr.sma.adventofcode.resolve.day16;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day16Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern INSTRUCTION_TEST_PATTERN = Pattern.compile("Before: [(\\d+), (\\d+), (\\d+), (\\d+)]\n(\\d+) (\\d+) (\\d+) (\\d+)\nAfter:  [(\\d+), (\\d+),(\\d+), (\\d+)]");
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day16Ex1");
		
		String values = dataFetcher.fetch(16).trim();
		
	}
}