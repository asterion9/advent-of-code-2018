package fr.sma.adventofcode.resolve.day19;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.Cpu;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import fr.sma.adventofcode.resolve.processor.lambda.CpuLambda;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
		int[] register = new int[] {1, 0, 0, 0, 0, 0};
		int result = cpu.calculate(register);
		int number = register[3];
		
		int sum = IntStreamEx.range(1, (int) Math.ceil(Math.pow(number, 0.5)))
				.filter(i -> number % i == 0)
				.flatMap(i -> IntStreamEx.of(i, number / i))
				.sum();
		
		System.out.println("sum = " + sum);
	}
}