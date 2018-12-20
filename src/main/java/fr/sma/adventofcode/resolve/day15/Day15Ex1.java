package fr.sma.adventofcode.resolve.day15;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Day15Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day15Ex1");
		
		String values = "#######\n" +
				"#E..EG#\n" +
				"#.#G.E#\n" +
				"#E.##E#\n" +
				"#G..#.#\n" +
				"#..E#.#\n" +
				"#######";//dataFetcher.fetch(15).trim();
		
		Area area = Area.buildArea(values);
		System.out.println(area.drawMap());
		int turn = -1;
		int hpElf;
		int hpGob;
		do {
			turn++;
			System.out.println("start of turn " + turn);
			Set<Unit> deadUnit = area.doTurn();
			System.out.println(StreamEx.of(deadUnit).map(unit -> "one " + unit.getType()).joining(", ", "victims : ", "."));
			System.out.println(area.drawMap());
			hpElf = area.getHp(Element.Type.ELF);
			hpGob = area.getHp(Element.Type.GOBELIN);
			System.out.println("elf hp : " + hpElf + ", gobelin hp : " + hpGob);
		}while(hpElf > 0 && hpGob > 0);
		System.out.println("outcome : " + Math.max(hpElf, hpGob) * turn);
	}
}