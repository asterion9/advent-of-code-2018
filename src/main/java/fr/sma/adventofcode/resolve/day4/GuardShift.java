package fr.sma.adventofcode.resolve.day4;

import com.google.common.primitives.Bytes;
import one.util.streamex.StreamEx;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GuardShift {
	public static final Pattern PATTERN_NEW_GUARD = Pattern.compile("Guard #(\\d+) begins shift");
	
	private static final String PATTERN_FALL_ASLEEP = "falls asleep";
	
	private static final String PATTERN_WAKES_UP = "wakes up";
	
	private final int id;
	private final byte[] shiftSleep;
	
	public GuardShift(int id) {
		this.id = id;
		this.shiftSleep = new byte[60];
	}
	
	public int getTotalSleep() {
		return Bytes.asList(shiftSleep).stream().mapToInt(Byte::byteValue).sum();
	}
	
	public byte getMaxSleep() {
		return Collections.max(Bytes.asList(shiftSleep));
	}
	
	public int getMaxMinute() {
		return Bytes.indexOf(shiftSleep, getMaxSleep());
	}
	
	public static GuardShift build(List<GuardLine> lines) {
		Matcher m = PATTERN_NEW_GUARD.matcher(lines.get(0).getEvent());
		m.matches();
		GuardShift guardShift = new GuardShift(Integer.parseInt(m.group(1)));
		StreamEx.of(lines.subList(1, lines.size()))
				.groupRuns((start, end) -> start.getEvent().matches(PATTERN_FALL_ASLEEP) && end.getEvent().matches(PATTERN_WAKES_UP))
				.forEachOrdered(linePair -> {
					GuardLine start = linePair.get(0);
					GuardLine end = linePair.get(1);
					IntStream.range(start.getTime().getMinute(), end.getTime().getMinute())
							.forEachOrdered(t -> guardShift.getShiftSleep()[t]++);
				});
				
		return guardShift;
	}
	
	public GuardShift merge(GuardShift other) {
		IntStream.range(0, shiftSleep.length)
				.forEach(t -> shiftSleep[t] += other.getShiftSleep()[t]);
		return this;
	}
	
	
	public int getId() {
		return id;
	}
	
	public byte[] getShiftSleep() {
		return shiftSleep;
	}
	
	@Override
	public String toString() {
		return "GuardShift{" +
				"id=" + id +
				", shiftSleep=" + Bytes.asList(shiftSleep).stream().map(Object::toString).collect(Collectors.joining(",")) +
				'}';
	}
}
