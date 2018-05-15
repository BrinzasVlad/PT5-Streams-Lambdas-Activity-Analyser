package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.MonitoredDataManager;
import view.View;

public class Controller {
	
	public static final String FILENAME = "Activities.txt";
	public static final String TOTAL_OCCUR_FILE = "TotalActivityOccurrences.txt";
	public static final String DAILY_OCCUR_FILE = "DailyActivityOccurrences.txt";
	public static final String LONG_DURATION_FILE = "ActivitiesWithTotalDurationOver10h.txt";
	public static final String SHORT_ACTIONS_FILE = "ActivitiesWithOver90PercentUnder5Minutes.txt";
	
	private MonitoredDataManager manager;
	private View view;

	public static void main(String[] args) {
		@SuppressWarnings("unused") //Not really unused, since its controller makes a JFrame appear
		Controller controller = new Controller();
	}
	
	/**
	 * Creates a new Controller object and initializes it
	 * with a MonitoredDataManager manager taking its data
	 * from "Activities.txt"
	 */
	public Controller() {
		this.manager = new MonitoredDataManager(FILENAME);
		this.view = new View();
		setListeners();
	}
	
	private void setListeners() {
		view.addListenerDistinctDays(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = "There are " + manager.countDistinctDays() +
								" distinct days in the data.\n";
				view.setResultsText(result);
			}
		});
		view.addListenerTotalOccurrences(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = manager.textForOccurencesPerActivity();
				manager.writeStringToFile(result, TOTAL_OCCUR_FILE);
				view.setResultsText(result);
			}
		});
		view.addListenerDailyOccurrences(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = manager.textForDailyOccurencesPerActivity();
				manager.writeStringToFile(result, DAILY_OCCUR_FILE);
				view.setResultsText(result);
			}
		});
		view.addListenerLongTotalTime(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = manager.textForTotalDurationFiltered();
				manager.writeStringToFile(result, LONG_DURATION_FILE);
				view.setResultsText(result);
			}
		});
		view.addListenerShortInstances(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = manager.textForSelectShortActivityNames();
				manager.writeStringToFile(result, SHORT_ACTIONS_FILE);
				view.setResultsText(result);
			}
		});
	}
}
