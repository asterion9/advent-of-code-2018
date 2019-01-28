package fr.sma.adventofcode.resolve.day21;

import org.junit.Test;

public class CompiledCodeTest {
	
	@Test
	public void testCodeSimplification() {
		for (long i = 0; i <= 10000; i += 100) {
			long a = i / 256L;
			long b = 0;
			
			long t3 = 0;
			while(true) {
				long t5 = t3 + 1L;
				t5 *= 256L;
				if (t5 > i) {
					b = t3;
					break;
				}
				
				++t3;
			}
			
			System.out.println("i=" + i + ", a=" + a + ", b=" + b);
			assert a == b;
		}
	}
}
