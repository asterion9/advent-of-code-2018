package fr.sma.adventofcode.resolve.day14;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day14Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day14Ex1");
		StringBuilder recipesBoard = new StringBuilder("37");
		int targetSize = Integer.parseInt(dataFetcher.fetch(14).trim());
		
		int index1 = 0, index2 = 1;
		int v1, v2;
		
		while (recipesBoard.length() < targetSize + 10) {
			v1 = recipesBoard.charAt(index1)-'0';
			v2 = recipesBoard.charAt(index2)-'0';
			recipesBoard.append(v1+v2);
			index1 = (index1 + v1 + 1) % recipesBoard.length();
			index2 = (index2 + v2 + 1) % recipesBoard.length();
		}
		
		System.out.println(recipesBoard.substring(recipesBoard.length()-10));
	}
	
}