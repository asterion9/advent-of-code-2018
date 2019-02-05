package fr.sma.adventofcode.resolve.day13;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * same as ex 1, but run the simulation until all chariots collide
 */
@Component
public class Day13Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day13Ex2");
		
		String values = dataFetcher.fetch(13);
		
		TrackSystem trackSystem = TrackSystem.buildTrackSystem(values);
		
		TrackSystem.TrackSystemPainter painter = trackSystem.getPainter();
		
		TrackSystem.Chariot lastChariot = StreamEx.generate(() -> trackSystem)
				.peek(trackSystem1 -> painter.repaint())
				.peek(trackSystem1 -> {
					try {
						Thread.sleep(trackSystem1.getRemaingChariot().size());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				})
				.peek(TrackSystem::moveAll)
				.map(TrackSystem::getRemaingChariot)
				.filter(remaining -> remaining.size() < 2)
				.flatMap(Collection::stream)
				.findFirst().get();
		
		painter.repaint();
		
		System.out.println(lastChariot.getX() + "," + lastChariot.getY());
	}
}