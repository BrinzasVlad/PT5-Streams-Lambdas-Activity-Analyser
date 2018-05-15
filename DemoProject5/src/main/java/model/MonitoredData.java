package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonitoredData {
	private String name;
	private LocalDateTime start;
	private LocalDateTime end;
	
	/**
	 * Creates a new MonitoredData object with the given data.
	 * <b>start</b> and <b>end</b> Strings must follow the following format:
	 * yyyy-MM-dd HH:mm:ss
	 * @param name - the name of the activity
	 * @param start - the date and time when the activity begins
	 * @param end - the date and time when the activity ends
	 */
	public MonitoredData(String name, String start, String end) {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.name = name;
		this.start = LocalDateTime.parse(start, f);
		this.end = LocalDateTime.parse(end, f);
	}
	
	@Override
	public String toString() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return this.name + "     " + start.format(f) + "     " + end.format(f);
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}
	
	/**
	 * Computes and returns the duration between the beginning and end of the data.
	 * @return A Duration object.
	 */
	public Duration getDuration() {
		return Duration.between(start, end).abs();
	}
}
