package ui;

import inputReader.TextAndModelInputReader;

import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JDialog;

import objects.Plaintext;
import objects.SubstitutionTable;
import em.ExpectationMaximization;
import exceptions.WrongInputFormatException;

public class UiController {

	private AppGui appGui;
	private TextAndModelInputReader tamir;
	private SubstitutionTable subTable;
	private ExpectationMaximization em;
	
	public UiController(String pathToInput){
		tamir = null;
		try {
			tamir = new TextAndModelInputReader(pathToInput);
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			return;
		} catch (WrongInputFormatException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		subTable = new SubstitutionTable(tamir.getCiphertext(), tamir.getLetters());
		em = new ExpectationMaximization(subTable, tamir.getLm2gram(), tamir.getCiphertext(), tamir.getLetters());
	}

	public void windowInitialized(){
		appGui.updateEmPanel(subTable, tamir.getLetters(), tamir.getCiphertext());
	}
	
	public AppGui getAppGui() {
		return appGui;
	}

	public void setAppGui(AppGui appGui) {
		this.appGui = appGui;
	}
	
	public void showErrorMessage(String error){
		ErrorMessage dialog = new ErrorMessage(error);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		appGui.enableButtons(true);
	}
	
	public void showContentInPopup(String text){
		ShowContent dialog = new ShowContent(text);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
	
	public void showTop10(){
		Runnable sortListInBackground = new Runnable() {
			
			@Override
			public void run() {
				List<Plaintext> top10 = em.getTop(10);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < top10.size(); i++) {
					sb.append((i+1) + ".\t " + top10.get(i).getPlaintext() + " \t " + top10.get(i).getLMProbability()*top10.get(i).getSubTableProbability() + "\n");
				}
				showContentInPopup(sb.toString());
			}
		};
		
		
		Thread backgroundThread = new Thread(sortListInBackground);
		backgroundThread.start();
	}
	
	public void showCurrentSubTable(){
		showContentInPopup(subTable.toString());
	}
	
	public void executeEm(int iterations){
		Runnable background = new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < iterations; i++) {
					em.doNextIteration();
					appGui.updateEmPanel(subTable, tamir.getLetters(), tamir.getCiphertext());
				}
				appGui.enableButtons(true);
			}
		};
		
		Thread backgroundThread = new Thread(background);
		backgroundThread.start();
		
	}
	
}
