package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.MonitoredData;

public class MonitoredDataManager {
	
	private List<MonitoredData> activities;
	
	/**
	 * Creates a new MonitoredDataManager object and loads it with
	 * the data from the filename file.
	 * <p>
	 * The data is expected to have the format
	 * START  END  NAME for each activity, each line
	 * representing one instance of an activity.
	 * These three fields should be separated by two tabs (\t) each.
	 * <p>
	 * START and END are expected to follow the yyyy-MM-dd HH:mm:ss format.
	 * 
	 * @param
	 */
	public MonitoredDataManager(String filename) {
		activities = new ArrayList<MonitoredData>();
		
		//Read the activities from the file and print them out
		try(Stream<String> stream = Files.lines(Paths.get(filename))) {
			stream.forEach(e -> {
				String[] elems = e.split("\t\t");
				activities.add(new MonitoredData(elems[2].trim(), elems[0].trim(), elems[1].trim()));
			});
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Counts the number of distinct days over which the activities extend
	 * @return A number of days
	 */
	public long countDistinctDays() {
		List<LocalDate> dates = new ArrayList<LocalDate>();
		activities.forEach(activ -> {
			dates.add(activ.getStart().toLocalDate());
			dates.add(activ.getEnd().toLocalDate());
		});
		
		return dates.stream().distinct().count();
	}
	
	/**
	 * For each activity, computes the number of entries across all days
	 * @return An map correlating each activity to its number of entries
	 */
	public Map<String, Integer> countOccurencesPerActivity() {		
		return activities.stream()
		.collect(Collectors.groupingBy(MonitoredData::getName, Collectors.summingInt(e -> 1)));
	}
	
	/**
	 * Presents the results of countOccurencesPerActivity() in an easily-printable String format
	 * @return A string representing the results
	 */
	public String textForOccurrencesPerActivity() {
		Map<String, Integer> map = countOccurencesPerActivity();
		StringBuilder output = new StringBuilder();
		
		map.forEach((str, integ) -> {
			output.append( str + " appears " + integ + " times." + "\n" );
		});
		
		return output.toString();
	}
	
	/**
	 * For each day in the list of activities, computes the number of occurrences
	 * for every activity. Note that days are treated as days in the year (1 to 365),
	 * which avoids duplicates in situations like 3 Mar and 3 Apr, but can lead to
	 * errors in cases such as 13 Mar 1998 vs. 13 Mar 2005 (counted as same day).
	 * @return A map of integers representing the day of the year, correlated to
	 * a map of activity names to number of occurrences.
	 */
	public Map<Integer, Map<String, Integer>> countDailyOccurrencesPerActivity() {
		return activities.stream()
		.collect(Collectors.groupingBy(
				(MonitoredData data) -> {return data.getStart().getDayOfYear();}, //We use DayOfYear, since we might cover several months of activity in Activities.txt
				Collectors.groupingBy(
						MonitoredData::getName,
						Collectors.summingInt(e -> 1))));
	}
	
	/**
	 * Presents the results of countDailyOccurrencesPerActivity()
	 * in an easily-printable String format
	 * @return A string representing the results
	 */
	public String textForDailyOccurrencesPerActivity() {
		Map<Integer, Map<String, Integer>> map = countDailyOccurrencesPerActivity();
		StringBuilder output = new StringBuilder();
		
		map.forEach((integ, submap) -> {
			output.append( "\nIn day " + integ + " we have:" + "\n" );
			submap.forEach((str, integer) -> {
				output.append(str + " appears " + integer + " times." + "\n");
			});
		});
		
		return output.toString().substring(1); //Skip the leading newline
	}
	
	/**
	 * For each MonitoredData instance, computes the duration.
	 * Then, returns a string that contains all the activities, but also
	 * displays the above-mentioned durations.
	 * @return A string representing the results.
	 */
	public String textForLinesWithDuration() {
		StringBuilder output = new StringBuilder();
		
		activities.forEach(act -> {
			output.append(act.toString());
			output.append("        duration: ");
			output.append(durationToDaysHoursMinutesSeconds(act.getDuration()));
			output.append("\n");
		});
		
		return output.toString();
	}
	
	/**
	 * Computes the total duration across all days for each activity.
	 * @return A map of names to durations.
	 */
	public Map<String, Duration> totalDuration() {
		return activities.stream()
		.collect(
				Collectors.toMap(MonitoredData::getName,
								MonitoredData::getDuration,
								Duration::plus)
		);
	}
	
	/**
	 * Presents the results of totalDuration() in an easily-printable String format.
	 * @return A string representing the results.
	 */
	public String textForTotalDurationFiltered() {
		Map<String, Duration> map = totalDuration();
		StringBuilder output = new StringBuilder();
		
		map.forEach((str, dur) -> {
			output.append(str + " total duration: ");
			output.append(durationToDaysHoursMinutesSeconds(dur));
			output.append("\n");
		});
		
		return output.toString();
	}
	
	/**
	 * Generates a list of the activities that have over 90% of
	 * their occurrences represented by activities of 5 minutes or less.
	 * @return A list of strings representing activity names.
	 */
	private List<String> selectShortActivityNames() {
		Map<String, Integer> occurrences = activities.stream()
		.collect(Collectors.groupingBy(MonitoredData::getName, Collectors.summingInt(e->1)));
		
		Map<String, Integer> underFiveMinutes = activities.stream()
		.filter(act -> {return act.getDuration().minusMinutes(5).isNegative();}) //Accept only activities of under 5 minutes
		.collect(Collectors.groupingBy(MonitoredData::getName, Collectors.summingInt(e->1)));
		
		return underFiveMinutes.entrySet().stream()
		.filter(elem -> {
			return elem.getValue().intValue()  >=  0.9 * occurrences.get(elem.getKey());
		})
		.map(elem -> {return elem.getKey();})
		.collect(Collectors.toList());
		
	}
	
	/**
	 * Presents the results of selectShortActivityNames() in an easily-printable String format
	 * @return A string representing the results
	 */
	public String textForSelectShortActivityNames() {
		List<String> list = selectShortActivityNames();
		StringBuilder output = new StringBuilder();
		
		output.append("Activities with 90% of instances below 5 minutes:\n");
		list.forEach(str -> output.append("- " + str + "\n"));
		
		return output.toString();
	}
	
	/**
	 * Converts a Duration object to a human-readable format that
	 * shows the time elapsed in days - hours - minutes - seconds.
	 * <br>
	 * Output will omit days if the duration is less than one day,
	 * will omit hours if the duration is less than one hour and will
	 * omit minutes is the duration is less than one minute.
	 * @param d - the Duration to convert
	 * @return A String representation of the given Duration object
	 */
	private String durationToDaysHoursMinutesSeconds(Duration d) {
		StringBuilder result = new StringBuilder();
		
		if(d.toDays() > 0) result.append( d.toDays() + " days, " );
		if(d.toHours() > 0) result.append( (d.toHours() % 24) + " hours, " );
		if(d.toMinutes() > 0) result.append( (d.toMinutes() % 60) + " minutes, " );
		result.append( (d.getSeconds() % 60) + " seconds.");
		
		return result.toString();
	}
}
