package fr.sma.adventofcode.resolve.day24;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * parse the inputs and run the simulation.
 * special care must be given to the ordering of the step of the battle, and of the unit turn.
 */
@Component
public class Day24Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day24Ex1");
		
		String values = dataFetcher.fetch(24).trim();
		
		InfectionBattle infectionBattle = InfectionBattle.build(values);
		
		Set<Group> winnigGroups = infectionBattle.doBattle(0);
		
		logger.info(winnigGroups.iterator().next().getType().name() + " win with " + StreamEx.of(winnigGroups).mapToInt(Group::getNb).sum() + " units");
	}
}