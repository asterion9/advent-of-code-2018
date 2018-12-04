package fr.sma.adventofcode.resolve.day2;

import one.util.streamex.StreamEx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class Day02Ex2Test {
	
	@Test
	public void distance() {
		int distance = (int) StreamEx.of("aac".chars().iterator()).zipWith(StreamEx.of("caa".chars().iterator()), Object::equals)
				.peek(System.out::println)
				.filter(aBoolean -> !aBoolean)
				.count();
		
		System.out.println(distance);
	}
}