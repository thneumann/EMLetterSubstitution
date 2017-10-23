package objects;

import java.text.DecimalFormat;
import java.util.List;

public class SubstitutionTable {

	private double [][] subTable;
	private List<String> letters;
	private String ciphertext;
	
	public SubstitutionTable(String ciphertext, List<String> letters){
		subTable = new double[letters.size()][ciphertext.length()];
		this.letters = letters;
		this.ciphertext = ciphertext;
		//initialize with standard values 1/letters.size except for space (0 or 1)
		for (int i = 0; i < subTable.length; i++) {
			for (int j = 0; j < subTable[i].length; j++) {
				// i iterates over letters, j iterates over ciphertext
				if(letters.get(i).equals("_") && (ciphertext.charAt(j)+"").equals("_")){
					subTable[i][j]=1.;
				}else if(letters.get(i).equals("_") || (ciphertext.charAt(j)+"").equals("_")){
					subTable[i][j]=0;
				}else{
					//both are letters and no space
					subTable[i][j]=1./(letters.size()-1); // -1 for the space
				}
			}
		}
	}
	
	public double[][] getSubTable(){
		return cloneArray(this.subTable);
	}
	
	public void setSubTable(double [][] subTable){
		this.subTable = subTable;
	}
	
	public double getProbability(String plaintextLetter, int column){
		return subTable[letters.indexOf(plaintextLetter)][column];
	}
	
	public double[][] getEmptySubTable(){
		return new double[subTable.length][subTable[0].length];
		
		
		//return newSub;
	
	}
	
	
	private double[][] cloneArray(double[][] src) {
	    int length = src.length;
	    double [][] target = new double[length][src[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
	    }
	    return target;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		DecimalFormat decimalFormat = new DecimalFormat("0.0000");
		sb.append("\t");
		for (int i = 0; i < ciphertext.length(); i++) {
			sb.append(String.format("%-7s%-5s", "", ciphertext.charAt(i)+""));
		}
		sb.append("\n");
		for (int i = 0; i < subTable.length; i++) {
			sb.append(letters.get(i) + " |\t ");
			for (int j = 0; j < subTable[i].length; j++) {
				sb.append(decimalFormat.format(subTable[i][j])+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
