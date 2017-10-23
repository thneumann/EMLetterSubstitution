package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.SubstitutionTable;

public class EmTable extends JPanel {

	/**
	 * Create the panel.
	 */
	public EmTable(SubstitutionTable st, List<String> letters, String ciphertext) {

		int rows = st.getSubTable().length;
		int columns = st.getSubTable()[0].length;

		setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		int[] colWidhts = new int[columns];
		int[] rowWidths = new int[rows];
		for (int i = 0; i < colWidhts.length; i++) {
			colWidhts[i] = 40;
		}
		gridBagLayout.columnWidths = colWidhts;
		gridBagLayout.rowHeights = rowWidths;
		double colWeights[] = new double[columns + 1];
		colWeights[columns] = Double.MIN_VALUE;
		double rowWeights[] = new double[rows + 1];
		rowWeights[rows] = Double.MIN_VALUE;
		gridBagLayout.columnWeights = colWeights;
		gridBagLayout.rowWeights = rowWeights;

		setLayout(gridBagLayout);

		// JLabel[][] table = new JLabel[rows][columns];

		for (int i = 0; i < rows+2; i++) {
			for (int j = 0; j < columns; j++) {
				GridBagConstraints gbc_lblEmpty = new GridBagConstraints();
				gbc_lblEmpty.gridx = j;
				gbc_lblEmpty.gridy = i;
				if (i == 0) {
					JLabel lblEmpty = new JLabel(ciphertext.charAt(j) + "");
					add(lblEmpty, gbc_lblEmpty);
				}else if(i ==1){
					JLabel lblEmpty = new JLabel("-");
					add(lblEmpty, gbc_lblEmpty);
				}else{
					JLabel lblEmpty = new JLabel(letters.get(i-2) + "");
					float v = (float)st.getSubTable()[i-2][j];
					lblEmpty.setForeground(new Color(1-v, 1-v, 1-v));
					add(lblEmpty, gbc_lblEmpty);
				}
				
			}
		}
	}
}
