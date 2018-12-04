package fr.sma.adventofcode.resolve.day4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GuardLine {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private final LocalDateTime time;
	private final String event;
	
	public GuardLine(LocalDateTime time, String event) {
		this.time = time;
		this.event = event;
	}
	
	public static GuardLine parse(String time, String event) {
		LocalDateTime parsedTime = LocalDateTime.parse(time, formatter);
		return new GuardLine(parsedTime, event);
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	
	public String getEvent() {
		return event;
	}
	
	@Override
	public String toString() {
		return "GuardLine{" +
				"time=" + time +
				", event='" + event + '\'' +
				'}';
	}
}
