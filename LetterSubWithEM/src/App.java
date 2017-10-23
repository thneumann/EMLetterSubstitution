import java.io.FileNotFoundException;

import objects.SubstitutionTable;
import em.ExpectationMaximization;
import exceptions.WrongInputFormatException;
import inputReader.TextAndModelInputReader;


public class App {

	public static void main(String[] args) {
		
		if(args.length != 1){
			System.out.println("Please give one input argument, which contains the path to the input file!");
			System.out.println("The Input file must contain the ciphertext in the first line.");
			System.out.println("From the second line on the language model must be given with all possible letter combinations. The letters are excluded from the ciphertext.");
			System.out.println("The notation is as follows for P(A|B)=0.4 please write 'A B 0.4'. To consider the start, end and space please use 'START' 'END' '_'.");
			System.out.println("Note: The space character is always included automatically and it is supposed that the space is always substituted by the space. Please don't forget to give the probabilities for letters after a space occured");
			return;
		}
		TextAndModelInputReader tamir = null;
		try {
			tamir = new TextAndModelInputReader(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			return;
		} catch (WrongInputFormatException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		SubstitutionTable st = new SubstitutionTable(tamir.getCiphertext(), tamir.getLetters());
		ExpectationMaximization em = new ExpectationMaximization(st, tamir.getLm2gram(), tamir.getCiphertext(), tamir.getLetters());
		for (int i = 0; i < 20; i++) {
			em.doNextIteration();
		}
		
	}
	
}
