package fr.sma.adventofcode.resolve.day3;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@SpringBootTest
public class Day03Ex1Test {
	
	@Test
	public void claimFabric() {
		List<byte[]> fabric = Stream.generate(() -> new byte[10]).limit(10).collect(Collectors.toCollection(ArrayList::new));
		Day03Ex1.FabricClaim claim1, claim2, claim3;
		claim1 = new Day03Ex1.FabricClaim(2, 2, 4, 4);
		claim2 = new Day03Ex1.FabricClaim(4, 4, 4, 4);
		claim3 = new Day03Ex1.FabricClaim(4, 4, 4, 4);
		
		Day03Ex1 day03Ex1 =  new Day03Ex1();
		
		assertThat(day03Ex1.claimFabric(fabric, claim1), is(0));
		assertThat(day03Ex1.claimFabric(fabric, claim2), is(4));
		assertThat(day03Ex1.claimFabric(fabric, claim3), is(12));
		
	}
}