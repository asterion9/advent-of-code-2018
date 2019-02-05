package fr.sma.adventofcode.resolve.day15;

import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * same as ex1 for runnign the fight,
 * we use a dichotomy search to locate the lowest value at which the battle issue change.
 */
@Component
public class Day15Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day15Ex2");
		
		String values = dataFetcher.fetch(15).trim();
		
		int imin = 4;
		int imax = 200;
		int lastValidOutcome = 0;
		do {
			Map.Entry<Integer, Integer> nbDeadElfOutcome = simulateBattle(values, (imax + imin) / 2);
			if(nbDeadElfOutcome.getKey() > 0) {
				imin = (imax + imin)/2 + 1;
			} else {
				lastValidOutcome = nbDeadElfOutcome.getValue();
				imax = (imax + imin)/2;
			}
		}while (imax != imin);
		System.out.println("outcome=" + lastValidOutcome);
	}
	
	private Map.Entry<Integer, Integer> simulateBattle(String values, int elfAp) {
		Area area = Area.buildArea(values, elfAp);
		int turn = 0;
		while (!area.doTurn()){
			turn++;
			//System.out.println("end of turn " + turn);
			//System.out.println(area.drawMap());
		}
		System.out.println(area.drawMap());
		int elfHp = area.getHp(Element.Type.ELF);
		int gobHp = area.getHp(Element.Type.GOBELIN);
		Element.Type winner = elfHp > gobHp ? Element.Type.ELF : Element.Type.GOBELIN;
		int outcome = Math.max(elfHp, gobHp) * turn;
		int nbDeadElf = area.getNbDead(Element.Type.ELF);
		System.out.println("winner : " + winner + ", outcome : " + outcome + ", ap : " + elfAp + ", nb dead elf : " + nbDeadElf);
		return Map.entry(nbDeadElf, outcome);
	}
}