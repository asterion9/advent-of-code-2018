package fr.sma.adventofcode.resolve.day1;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Day01Ex2 implements ExSolution {
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day01Ex2");
		DataPartialSumSeq dataPartialSumSeq = new DataPartialSumSeq(dataFetcher.fetch(1));
		
		StreamEx.of(dataPartialSumSeq)
				.distinct(2).limit(1_000_000).findFirst()
				.ifPresent(System.out::println);
	}
	
	public static class DataPartialSumSeq implements Iterator<Integer> {
		private int index;
		private final List<Integer> dataList;
		private int curSum;
		
		public DataPartialSumSeq(String data) {
			dataList = Arrays.stream(data.split("\n")).map(Integer::valueOf).collect(Collectors.toList());
			index = 0;
			curSum = 0;
		}
		
		@Override
		public boolean hasNext() {
			return true;
		}
		
		@Override
		public Integer next() {
			curSum += dataList.get(index);
			index = (index + 1) % dataList.size();
			return curSum;
		}
	}
}
