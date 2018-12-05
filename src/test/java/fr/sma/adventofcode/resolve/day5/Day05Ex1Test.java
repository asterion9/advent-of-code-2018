package fr.sma.adventofcode.resolve.day5;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class Day05Ex1Test {
	
	@Test
	public void polymerize() {
		Day05Ex1 day05Ex1 = new Day05Ex1();
		
		ArrayList<String> testPolymer = new ArrayList<>(Arrays.asList("dabAcCaCBAcCcaDAeEadAC".split("")));
		
		
		day05Ex1.polymerize(testPolymer);
		
		System.out.println(testPolymer);
	}
}