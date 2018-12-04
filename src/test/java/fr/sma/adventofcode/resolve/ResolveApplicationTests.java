package fr.sma.adventofcode.resolve;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class ResolveApplicationTests {
	private static final Pattern PATTERN = Pattern.compile("#\\d+ @ (\\d+),(\\d+): (\\d+)x(\\d+)");

	
	@Test
	public void contextLoads() {
		Matcher matcher = PATTERN.matcher("#1 @ 335,861: 14x10");
		matcher.matches();
		System.out.println(matcher.group(4));
		
	}

}
