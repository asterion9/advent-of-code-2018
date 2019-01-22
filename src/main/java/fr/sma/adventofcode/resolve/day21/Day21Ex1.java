package fr.sma.adventofcode.resolve.day21;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.processor.Cpu;
import fr.sma.adventofcode.resolve.processor.lambda.CpuLambda;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day21Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day21Ex1");
		
		String values = dataFetcher.fetch(21).trim();
		
		Cpu cpu = /*CpuAsmBuilder.buildDynamic */new CpuLambda/*new PeepHoleCpuLambda*/(Cpu.readPointer(values), Cpu.readCode(values));
		
		//Cpu cpu = new Day21CompiledAsmCpu();
		
		for (int i = 0; i < 1000; i++) {
			System.out.println(i + " => " + cpu.calculate(i, 0, 0, 0, 0, 0));
		}
	}
}