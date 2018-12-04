package fr.sma.adventofcode.resolve;


import fr.sma.adventofcode.resolve.day1.Day01Ex2;
import one.util.streamex.StreamEx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DataPartialSumSeqTest {
	
	@Test
	public void testSum() {
		String values = "+1\n-2\n+3\n-4\n+5\n-6";
		Day01Ex2.DataPartialSumSeq dataPartialSumSeq = new Day01Ex2.DataPartialSumSeq(values);
		List<Integer> partialSum = StreamEx.of(dataPartialSumSeq).limit(9).collect(Collectors.toList());
		assertThat(partialSum).containsExactly(1, -1, 2, -2, 3, -3, -2, -4, -1);
	}
	
}