package fr.sma.adventofcode.resolve.day13;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import java.util.Optional;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day13Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day13Ex1");
		
		String values = dataFetcher.fetch(13).trim();
		
		TrackSystem trackSystem = TrackSystem.buildTrackSystem(values);
		
		TrackSystem.Chariot collidingChariot = StreamEx.generate(() -> trackSystem)
				.map(TrackSystem::moveAll)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst().get();
		
		System.out.println(collidingChariot.getX() + "," + collidingChariot.getY());
	}
	
	
}