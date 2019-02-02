package fr.sma.adventofcode.resolve.day24;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Day24Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day24Ex2");
		
		String values = /*"Immune System:\n" +
				"17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2\n" +
				"989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3\n" +
				"\n" +
				"Infection:\n" +
				"801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1\n" +
				"4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4";*/
		dataFetcher.fetch(24).trim();
		
		InfectionBattle infectionBattle = InfectionBattle.build(values);
		
		Set<Group> winningGroup;
		int boost = 0;
		do {
			winningGroup = infectionBattle.doBattle(boost);
			logger.info(boost + " boost, " + winningGroup.iterator().next().getType().name() + " win with " + StreamEx.of(winningGroup).mapToInt(Group::getNb).sum() + " units");
			boost++;
		}while (winningGroup.iterator().next().getType() != Group.TYPE.IMMUNITY);
		
		
	}
}