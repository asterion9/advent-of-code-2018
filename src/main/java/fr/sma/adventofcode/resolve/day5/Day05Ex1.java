package fr.sma.adventofcode.resolve.day5;

import fr.sma.adventofcode.resolve.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day05Ex1 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day05Ex1");
		
		String values = dataFetcher.fetch(5);
		
		ArrayList<String> polymer = new ArrayList<>(Arrays.asList(values.split("")));
		
		int index = 0;
		while(index < polymer.size() - 1) {
			//String line = (index > 0 ? polymer.get(index -1): ".") + polymer.get(index) + polymer.get(index +1)+ (index polymer.get(index +2);
			if(doReact(polymer.get(index), polymer.get(index + 1))) {
				//logger.debug(line + " match !");
				polymer.remove(index);
				polymer.remove(index);
				if(index > 0) {
					index--;
				}
			} else {
				//logger.debug(line);
				index++;
			}
		}
		
		System.out.println(polymer.size());
	}
	
	private boolean doReact(String left, String right) {
		return left.toUpperCase().equals(right.toUpperCase()) && !left.equals(right);
	}
}