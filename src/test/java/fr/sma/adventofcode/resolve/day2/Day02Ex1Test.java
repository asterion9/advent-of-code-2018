package fr.sma.adventofcode.resolve.day2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
public class Day02Ex1Test {
	
	private Day02Ex1 day02Ex1 = new Day02Ex1();
	
	@Test
	public void testCountBoth() {
		String value = "aabbccc";
		
		testCount(value, 1, 1);
	}
	
	@Test
	public void testCountDouble() {
		String value = "aabbcccc";
		
		testCount(value, 1, 0);
	}
	
	@Test
	public void testCountTriple() {
		String value = "abcdcdcdd";
		
		testCount(value, 0, 1);
	}
	
	private void testCount(String value, int expectedDouble, int expectedTriple) {
		AtomicInteger nbDouble = new AtomicInteger();
		AtomicInteger nbTriple = new AtomicInteger();
		
		day02Ex1.countMultiple(value, nbDouble, nbTriple);
		
		assertThat(nbDouble.get(), is(expectedDouble));
		assertThat(nbTriple.get(), is(expectedTriple));
	}
}