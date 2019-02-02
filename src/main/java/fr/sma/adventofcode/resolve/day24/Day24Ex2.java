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
		
		String values = dataFetcher.fetch(24).trim();
		
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