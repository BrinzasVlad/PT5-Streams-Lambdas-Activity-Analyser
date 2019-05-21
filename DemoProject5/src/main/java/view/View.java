package view;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

public class View {
	private JFrame frame;
	
	private JButton btnDistinctDays;
	private JButton btnTotalOccurrences;
	private JButton btnDailyOccurrences;
	private JButton btnTotalTime;
	private JButton btnActivityDurations;
	private JButton btnShortInstances;
	
	private JTextArea labelResult; //We use a JTextArea as a label, simply because it works.
	
	/**
	 * Creates a new View and initializes its components
	 */
	public View() {
		frame = new JFrame("Lambda Expressions and String Processing");
		init();
	}
	
	private void init() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		frame.setContentPane(mainPanel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		mainPanel.add(buttonsPanel);
		
		btnDistinctDays = new JButton("Distinct Days");
		buttonsPanel.add(btnDistinctDays);
		btnTotalOccurrences = new JButton("Count Activities");
		buttonsPanel.add(btnTotalOccurrences);
		btnDailyOccurrences = new JButton("Daily Activities");
		buttonsPanel.add(btnDailyOccurrences);
		btnActivityDurations = new JButton("Activity Durations");
		buttonsPanel.add(btnActivityDurations);
		btnTotalTime = new JButton("Total Times");
		buttonsPanel.add(btnTotalTime);
		btnShortInstances = new JButton("Short Instances");
		buttonsPanel.add(btnShortInstances);
		
		JSeparator sep = new JSeparator();
		mainPanel.add(sep);
		
		String instructions = "Welcome!\n" +
							"Please click one of the buttons above to proceed.\n";
		labelResult = new JTextArea(instructions); //We make a JTextArea that looks a lot like
		labelResult.setEditable(false); //a JLabel, but multi-line
		labelResult.setCursor(null);  
		labelResult.setOpaque(false);  
		labelResult.setFocusable(false);  
		labelResult.setFont(UIManager.getFont("Label.font"));      
		labelResult.setWrapStyleWord(true);
		labelResult.setLineWrap(true);
		labelResult.revalidate();
		JScrollPane resultsScrollPane = new JScrollPane(labelResult,
										ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
										ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		resultsScrollPane.setPreferredSize(new Dimension(0, 250));
		resultsScrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		mainPanel.add(resultsScrollPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	//Outsource ActionListeners for the view buttons, so they can be handled in the controller
	public void addListenerDistinctDays(ActionListener e) {btnDistinctDays.addActionListener(e);}
	public void addListenerTotalOccurrences(ActionListener e) {btnTotalOccurrences.addActionListener(e);}
	public void addListenerDailyOccurrences(ActionListener e) {btnDailyOccurrences.addActionListener(e);}
	public void addListenerTotalTime(ActionListener e) {btnTotalTime.addActionListener(e);}
	public void addListenerActivityDurations(ActionListener e) {btnActivityDurations.addActionListener(e);}
	public void addListenerShortInstances(ActionListener e) {btnShortInstances.addActionListener(e);}

	/**
	 * Set the text to be displayed as a result on the GUI
	 * @param str - the text to be displayed
	 */
	public void setResultsText(String str) {
		labelResult.setText(str);
		labelResult.setCaretPosition(0); //To counter the auto-scrolling behaviour of the above 
		labelResult.revalidate();
		frame.repaint();
	}
}
