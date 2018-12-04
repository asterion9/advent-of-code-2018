package fr.sma.adventofcode.resolve.day1;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class Day01Ex1 implements ExSolution {
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day01Ex1");
		
		String data = dataFetcher.fetch(1);
		
		int sum = Stream.of(data.split("\n"))
				.mapToInt(Integer::parseInt)
				.sum();
		
		System.out.println("sum = " + sum);
	}
}
