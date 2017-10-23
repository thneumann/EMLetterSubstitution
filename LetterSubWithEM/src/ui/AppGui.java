package ui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;

import objects.SubstitutionTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AppGui {

	private JFrame frame;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JPanel panel_1;
	private UiController uiController;
	private JTextField textField;
	private JButton btnNewButton;
	private JButton btnShowTop;
	private JButton btnShowCurrentSubtable;
	/**
	 * Create the application.
	 */
	public AppGui(UiController uc) {
		this.uiController = uc;
		this.uiController.setAppGui(this);
		initialize();
	}
	
	public void showWindow(){
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 900, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{884, 0};
		gridBagLayout.rowHeights = new int[]{542, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblNewLabel = new JLabel("Solving Letter Substitution with EM");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		btnShowCurrentSubtable = new JButton("Show current SubTable");
		btnShowCurrentSubtable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.showCurrentSubTable();
			}
		});
		GridBagConstraints gbc_btnShowCurrentSubtable = new GridBagConstraints();
		gbc_btnShowCurrentSubtable.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowCurrentSubtable.gridx = 3;
		gbc_btnShowCurrentSubtable.gridy = 1;
		panel.add(btnShowCurrentSubtable, gbc_btnShowCurrentSubtable);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 2;
		panel.add(panel_1, gbc_panel_1);
		
		btnShowTop = new JButton("Show top 10");
		btnShowTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uiController.showTop10();
			}
		});
		GridBagConstraints gbc_btnShowTop = new GridBagConstraints();
		gbc_btnShowTop.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowTop.gridx = 3;
		gbc_btnShowTop.gridy = 2;
		panel.add(btnShowTop, gbc_btnShowTop);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 3;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnNewButton = new JButton("Do x iterations");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enableButtons(false);
				try{
					int iterations = Integer.parseInt(textField.getText());
					uiController.executeEm(iterations);
				}catch(NullPointerException e){
					uiController.showErrorMessage("Please insert something into the text field.");
				}catch(NumberFormatException e){
					uiController.showErrorMessage("Please insert an Integer.");
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 3;
		panel.add(btnNewButton, gbc_btnNewButton);
		
	}
	
	public void enableButtons(boolean enabled){
		btnNewButton.setEnabled(enabled);
		btnShowTop.setEnabled(enabled);
		btnShowCurrentSubtable.setEnabled(enabled);
	}

	public void updateEmPanel(SubstitutionTable st, List<String> letters, String ciphertext){
		panel.remove(panel_1);
		panel_1 = new EmTable(st, letters, ciphertext);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 2;
		panel.add(panel_1, gbc_panel_1);
		panel.revalidate();
		
	}
	
}
