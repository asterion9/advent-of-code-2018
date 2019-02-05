package fr.sma.adventofcode.resolve.day21;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * again, a code exercise. Following the same process than day 19, I build the byte code then decompile it.
 * I can see that the produced code is a kind of inefficient pseudo-random number generator
 * that runs until the input number (reg0 value) is found.
 * so the shortest execution is for finding the first generated value.
 */
@Component
public class Day21Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day21Ex1");
		
		String values = dataFetcher.fetch(21).trim();
		
		Day21Cpu cpu = Day21Cpu.build(values);
		
		System.out.println(cpu.nextNumber(0));
	}
}