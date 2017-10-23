package inputReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.WrongInputFormatException;

public class TextAndModelInputReader {

	private String ciphertext;
	private Map<String, Map<String, Double>> lm2gram = new HashMap<String, Map<String, Double>>();
	private List<String> letters = new ArrayList<String>();
	
	public TextAndModelInputReader(String path) throws FileNotFoundException, WrongInputFormatException{
		File inputFile = new File(path);
		if(!inputFile.exists()){
			throw new FileNotFoundException("File Not Found!");
		}
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = br.readLine();
			ciphertext = line.replace(" ", "_"); // first line is ciphertext
			while((line = br.readLine())!= null){
				String [] split = line.split(" ");
				if(!lm2gram.containsKey(split[0].toLowerCase())){
					lm2gram.put(split[0].toLowerCase(), new HashMap<String, Double>());
				}
				if(lm2gram.get(split[0].toLowerCase()).containsKey(split[1].toLowerCase())){
					throw new WrongInputFormatException("P("+split[0]+"|"+split[1]+") exists twice in the input file");
				}
				lm2gram.get(split[0].toLowerCase()).put(split[1].toLowerCase(), Double.parseDouble(split[2]));
			}
			br.close();			
		}catch(IOException e){
			System.out.println("IO Problem");
			return;
		}catch(NumberFormatException e){
			throw new WrongInputFormatException("The third value of the language model must be a double");
		}catch(IndexOutOfBoundsException e){
			throw new WrongInputFormatException("The language model must definitions must contain 2 spaces per line");
		}
		//get all letters and validate completeness of 2gram
		String ciphertextLetters [] = ciphertext.split("");
		for (int i = 0; i < ciphertextLetters.length; i++) {
			String letter = ciphertextLetters[i].toLowerCase();
			if(!letters.contains(letter)){
				letters.add(letter);
			}
		}
		
		//now validate the lm, each letter must have an entry which maps again to all letters
		for (int i = 0; i < letters.size(); i++) {
			if(lm2gram.containsKey(letters.get(i))){
				for (int j = 0; j < letters.size(); j++) {
					if(!lm2gram.get(letters.get(i)).containsKey(letters.get(j))){
						throw new WrongInputFormatException("P(" + letters.get(i) + "|" + letters.get(j) +") is missing in the LM");
						}
				}
			}else{
				throw new WrongInputFormatException("Letter " + letters.get(i) +" is missing in the LM");
			}
		}
		//TODO check START and END
		
		//no exception thrown? LM is valid
	}

	public String getCiphertext() {
		return ciphertext;
	}


	public Map<String, Map<String, Double>> getLm2gram() {
		return lm2gram;
	}


	public List<String> getLetters() {
		return letters;
	}

	
	
}
