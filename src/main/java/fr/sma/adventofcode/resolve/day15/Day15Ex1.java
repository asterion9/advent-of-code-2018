package fr.sma.adventofcode.resolve.day15;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * parse the input and build a representation of the field.
 * you need to pay attention to the ordering.
 * I use a recursive path finding algorithm for each target that build a tree of shortest route to the nearest target,
 * respecting the moving priority to ensure that the correct route is selected.
 */
@Component
public class Day15Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day15Ex1");
		
		String values = dataFetcher.fetch(15).trim();
		
		Area area = Area.buildArea(values, 3);
		System.out.println(area.drawMap());
		int turn = 0;
		while (!area.doTurn()){
			turn++;
			System.out.println("end of turn " + turn);
			//System.out.println(area.drawMap());
		}
		System.out.println(area.drawMap());
		System.out.println("outcome : " + Math.max(area.getHp(Element.Type.ELF), area.getHp(Element.Type.GOBELIN)) * turn);
	}
}