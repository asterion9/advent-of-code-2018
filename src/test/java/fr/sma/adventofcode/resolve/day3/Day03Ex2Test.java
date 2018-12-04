package fr.sma.adventofcode.resolve.day3;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class Day03Ex2Test {
	
	@Test
	public void claimFabric() {
		List<short[]> fabric = Stream.generate(() -> new short[10]).limit(10).collect(Collectors.toCollection(ArrayList::new));
		Day03Ex2.FabricClaim claim1, claim2, claim3, claim4;
		claim1 = new Day03Ex2.FabricClaim((short)1,2, 2, 4, 4);
		claim2 = new Day03Ex2.FabricClaim((short)2, 4, 4, 4, 4);
		claim3 = new Day03Ex2.FabricClaim((short)3, 3, 3, 4, 4);
		claim4 = new Day03Ex2.FabricClaim((short)4, 0, 0, 2, 2);
		
		Day03Ex2 day03Ex2 =  new Day03Ex2();
		
		Assert.assertThat(day03Ex2.claimFabric(fabric, claim1), Matchers.empty());
		Assert.assertThat(day03Ex2.claimFabric(fabric, claim2), Matchers.containsInAnyOrder((short)1, (short)2));
		Assert.assertThat(day03Ex2.claimFabric(fabric, claim3), Matchers.containsInAnyOrder((short)1, (short)2, (short)3));
		Assert.assertThat(day03Ex2.claimFabric(fabric, claim4), Matchers.empty());
		
	}
}