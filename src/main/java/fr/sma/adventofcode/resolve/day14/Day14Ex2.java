package fr.sma.adventofcode.resolve.day14;

import com.google.common.primitives.Bytes;
import fr.sma.adventofcode.resolve.ExSolution;
import fr.sma.adventofcode.resolve.util.DataFetcher;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ex1 solution can't work for performance reason, we use a byte array instead to do basically the same thing.
 */
@Component
public class Day14Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day14Ex2");
		String targetPattern = dataFetcher.fetch(14).trim();
		
		byte[] recipes = new byte[1_000_000];
		System.arraycopy(parseString("37"), 0, recipes, 0, 2);
		
		byte[] targetBytePatttern = new byte[targetPattern.length()];
		System.arraycopy(parseString(targetPattern), 0, targetBytePatttern, 0, targetPattern.length());
		
		int index1 = 0, index2 = 1;
		int recipeSize = 2;
		while (recipeSize < 8 || compareLastTwo(recipes, targetBytePatttern, recipeSize) == -1) {
			if(recipes[index1] + recipes[index2] >= 10) {
				recipes[recipeSize] = 1;
				recipeSize++;
			}
			recipes[recipeSize] = (byte) ((recipes[index1] + recipes[index2]) % 10);
			recipeSize++;
			index1 = (index1 + recipes[index1] + 1) % recipeSize;
			index2 = (index2 + recipes[index2] + 1) % recipeSize;
			recipes = Bytes.ensureCapacity(recipes, recipeSize + 2, 1000000);
		}
		
		System.out.println(compareLastTwo(recipes, targetBytePatttern, recipeSize));
	}
	
	private byte[] parseString(String s) {
		return StreamEx.split(s, "")
				.mapToInt(Integer::valueOf)
				.toByteArray();
	}
	
	private int compareLastTwo(byte[] source, byte[] pattern, int sourceSize) {
		if(Arrays.compare(source, sourceSize-pattern.length, sourceSize, pattern, 0, pattern.length) == 0) {
			return sourceSize-pattern.length;
		} else if(Arrays.compare(source, sourceSize-pattern.length-1, sourceSize-1, pattern, 0, pattern.length) == 0) {
			return sourceSize-pattern.length-1;
		}
		return -1;
	}
}