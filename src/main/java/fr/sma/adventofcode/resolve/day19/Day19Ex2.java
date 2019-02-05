package fr.sma.adventofcode.resolve.day19;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.Cpu;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import fr.sma.adventofcode.resolve.processor.lambda.CpuLambda;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * unfortunately, compiling the instructions to bytecode didn't solve the problem
 * (even if it was way faster than the virtual machine of day 16)
 * I then tried to optimize the bytecode by replacing instructions that modified the pointer register with goto
 * doing so increased the speed of the code, but still not enough to compute the solution.
 * by decompiling the produced bytecode, it is possible to identify and understand the instructions purpose.
 * it is, actually, a very inefficient factorisation algorithm. By rewriting the code with an optimized algrithm,
 * I can solve this day exercise.
 */
@Component
public class Day19Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day19Ex2");
		
		String values = dataFetcher.fetch(19).trim();
		
		int pointerLoc = Cpu.readPointer(values);
		/*List<InstructionLine> code = Cpu.readCode(values);
		
		Cpu cpu = CpuAsmBuilder.buildDynamic(pointerLoc, code);
		
		int result = cpu.calculate(1, 0, 0, 0, 0, 0);
		
		System.out.println("result = " + result);*/
		List<InstructionLine> code = StreamEx.of(Cpu.readCode(values))
				.map(iline -> {
					if(iline.getWriteIndexes().contains(0)) {
						if(iline.getReadIndexes().contains(0)) {
							return new InstructionLine(BaseOperation.SETI, new int[] {36, 0, pointerLoc}, iline.getPosition());
						}
					}
					return iline;
				}).collect(Collectors.toList());
		
		Cpu cpu = new CpuLambda(pointerLoc, code);
		long[] register = new long[] {1, 0, 0, 0, 0, 0};
		long result = cpu.calculate(register);
		long number = register[3];
		
		long sum = LongStreamEx.range(1, (int) Math.ceil(Math.pow(number, 0.5)))
				.filter(i -> number % i == 0)
				.flatMap(i -> LongStreamEx.of(i, number / i))
				.sum();
		
		System.out.println("sum = " + sum);
	}
}