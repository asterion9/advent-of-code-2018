package fr.sma.adventofcode.resolve.day24;

import one.util.streamex.StreamEx;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Day24Ex1Test {
	
	@Test
	public void testTargetChooseComparator() {
		Group g1 = new Group(2, 1, 1, "", Set.of(), Set.of(), 2, Group.TYPE.IMMUNITY, 1);
		Group g2 = new Group(2, 1, 1, "", Set.of(), Set.of(), 1, Group.TYPE.IMMUNITY, 2);
		Group g3 = new Group(1, 1, 1, "", Set.of(), Set.of(), 2, Group.TYPE.IMMUNITY, 3);
		Group g4 = new Group(1, 1, 1, "", Set.of(), Set.of(), 1, Group.TYPE.IMMUNITY, 4);
		
		List<Group> orderGroup = Stream.of(g1, g2, g3, g4).sorted(Group.targetChooseComparator).collect(Collectors.toList());
		
		assertThat(orderGroup).containsExactly(g1, g2, g3, g4);
	}
	
	@Test
	public void testTargetAttackComparator() {
		Group attacker = new Group(1, 1, 1, "", Set.of(), Set.of(), 2, Group.TYPE.INFECTION, 1);
		
		Group g1 = new Group(2, 1, 1, "", Set.of(), Set.of(), 2, Group.TYPE.IMMUNITY, 1);
		Group g2 = new Group(2, 1, 1, "", Set.of(), Set.of(), 1, Group.TYPE.IMMUNITY, 2);
		Group g3 = new Group(1, 1, 1, "", Set.of(), Set.of(), 2, Group.TYPE.IMMUNITY, 3);
		Group g4 = new Group(1, 1, 1, "", Set.of(), Set.of(), 1, Group.TYPE.IMMUNITY, 4);
		List<Group> orderGroup = StreamEx.of(g1, g2, g3, g4).reverseSorted(attacker.targetAttackComparator).collect(Collectors.toList());
		
		assertThat(orderGroup).containsExactly(g1, g2, g3, g4);
	}
}