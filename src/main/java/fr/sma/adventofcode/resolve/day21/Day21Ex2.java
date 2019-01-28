package fr.sma.adventofcode.resolve.day21;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Day21Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day21Ex2");
		
		String values = dataFetcher.fetch(21).trim();
		
		Day21Cpu cpu = Day21Cpu.build(values);
		
		long n = 0;
		Set<Long> visitedNumbers = new HashSet<Long>();
		
		while(true) {
			long npp = cpu.nextNumber(n);
			if(visitedNumbers.contains(npp)) {
				System.out.println("n = " + n);
				return;
			}
			visitedNumbers.add(npp);
			n = npp;
			if(visitedNumbers.size() % 1000 == 0) {
				System.out.println("iteration " + visitedNumbers.size());
			}
		}
	}
}