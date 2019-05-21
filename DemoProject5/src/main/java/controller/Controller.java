package controller;

import model.MonitoredDataManager;
import view.View;

public class Controller {
	
	public static final String FILENAME = "Activities.txt";
	
	private MonitoredDataManager manager;
	private View view;

	public static void main(String[] args) {
		@SuppressWarnings("unused") //Not really unused, since its view makes a JFrame appear
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
		
		view.addListenerDistinctDays(e -> {
			String result = "There are " + manager.countDistinctDays() +
							" distinct days in the data.\n";
			view.setResultsText(result);
		});
		
		view.addListenerTotalOccurrences(e -> {
			String result = manager.textForOccurrencesPerActivity();
			view.setResultsText(result);
		});
		
		view.addListenerDailyOccurrences(e -> {
			String result = manager.textForDailyOccurrencesPerActivity();
			view.setResultsText(result);
		});
		
		view.addListenerTotalTime(e -> {
			String result = manager.textForTotalDurationFiltered();
			view.setResultsText(result);
		});
		
		view.addListenerActivityDurations(e -> {
			String result = manager.textForLinesWithDuration();
			view.setResultsText(result);
		});
		
		view.addListenerShortInstances(e -> {
			String result = manager.textForSelectShortActivityNames();
			view.setResultsText(result);
		});
	}
}
