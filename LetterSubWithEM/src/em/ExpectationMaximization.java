package em;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.Plaintext;
import objects.SubstitutionTable;

public class ExpectationMaximization {

	private SubstitutionTable st;
	private Map<String, Map<String, Double>> lm;
	private String ciphertext;
	private List<String> letters;
	
	private List<Plaintext> allPossiblePlaintexts;
	
	private Plaintext currentBestPlaintext;

	public ExpectationMaximization(SubstitutionTable st,
			Map<String, Map<String, Double>> lm, String ciphertext,
			List<String> letters) {
		this.st = st;
		this.lm = lm;
		this.ciphertext = ciphertext;
		this.letters = letters;
		// calculate all conditional probabilities from the substitution table
		this.allPossiblePlaintexts = getAllPossiblePlaintexts();
	}

	public void doNextIteration() {
		currentBestPlaintext = null;
		//for each plaintext calculate P(p) and P(c|p)
		double probabilityForAllPlaintexts = 0.0;
		for (Plaintext plaintext : allPossiblePlaintexts) {
			plaintext.updateSubTableProbability(st);
			double subTableProb = plaintext.getSubTableProbability();
			double lmProb = plaintext.getLMProbability();
			probabilityForAllPlaintexts += subTableProb*lmProb;
			//check if this is the current best result
			if(currentBestPlaintext != null){
				if(currentBestPlaintext.getLMProbability()*currentBestPlaintext.getSubTableProbability() < subTableProb*lmProb){
					currentBestPlaintext = plaintext;
				}
			}else{
				currentBestPlaintext = plaintext;
			}
		}
		
		double [][] newSubTable = st.getEmptySubTable();
		for (Plaintext plaintext : allPossiblePlaintexts) {
			double plaintextProbability = (plaintext.getLMProbability()*plaintext.getSubTableProbability())/probabilityForAllPlaintexts;
			String plain = plaintext.getPlaintext();
			for (int i = 0; i < plain.length(); i++) {
				newSubTable[letters.indexOf(plain.charAt(i)+"")][i] += plaintextProbability;
			}
		}
		
		//normalize new sub table
		for (int i = 0; i < newSubTable[0].length; i++) {
			double columnSum = 0;
			//get the sum
			for (int j = 0; j < newSubTable.length; j++) {
				columnSum += newSubTable[j][i];
			}
			//do the normalization
			for (int j = 0; j < newSubTable.length; j++) {
				newSubTable[j][i] = newSubTable[j][i]/columnSum;
			}
		}
		
		st.setSubTable(newSubTable);
		System.out.println("The current best plaintext is: " + currentBestPlaintext.getPlaintext());
		System.out.println("The probability is: " + currentBestPlaintext.getLMProbability()*currentBestPlaintext.getSubTableProbability());
		System.out.println("");
		System.out.println("---------------------------");
		System.out.println("");
		System.out.println("New substitution table:");
		System.out.println(st);
	}

	private List<Plaintext> getAllPossiblePlaintexts() {
		// remove _ as it is not replaced from a copy of the list
		List<String> letterscopy = copyList(letters);
		letterscopy.remove("_");

		List<Plaintext> texts = new ArrayList<Plaintext>();
		getAllPossiblePlaintexts(ciphertext, texts, letterscopy,
				new StringBuilder());

		return texts;
	}

	public List<Plaintext> getTop(int top){
		allPossiblePlaintexts.sort(null);
		return allPossiblePlaintexts.subList(0, top);
	}
	
	private void getAllPossiblePlaintexts(String ciphertext,
			List<Plaintext> texts, List<String> letters, StringBuilder string) {
		if (ciphertext.length() == 0) {
			texts.add(new Plaintext(string.toString(), lm));
			return;
		}
		if (ciphertext.charAt(0) == '_') {
			string.append("_");
			getAllPossiblePlaintexts(ciphertext.substring(1), texts, letters,
					string);
		} else {

			StringBuilder copy = new StringBuilder(string);
			for (int j = 0; j < letters.size(); j++) {
				copy.append(letters.get(j));
				getAllPossiblePlaintexts(ciphertext.substring(1), texts,
						letters, copy);
				copy = new StringBuilder(string);
			}

		}
	}

	private List<String> copyList(List<String> listInput) {
		List<String> listOutput = new ArrayList<String>();
		for (int i = 0; i < listInput.size(); i++) {
			listOutput.add(listInput.get(i));
		}
		return listOutput;
	}

}
