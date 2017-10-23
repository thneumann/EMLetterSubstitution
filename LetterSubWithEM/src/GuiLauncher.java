import java.awt.EventQueue;

import ui.AppGui;
import ui.UiController;


public class GuiLauncher {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("Please give one input argument, which contains the path to the input file!");
			System.out.println("The Input file must contain the ciphertext in the first line.");
			System.out.println("From the second line on the language model must be given with all possible letter combinations. The letters are excluded from the ciphertext.");
			System.out.println("The notation is as follows for P(A|B)=0.4 please write 'A B 0.4'. To consider the start, end and space please use 'START' 'END' '_'.");
			System.out.println("Note: The space character is always included automatically and it is supposed that the space is always substituted by the space. Please don't forget to give the probabilities for letters after a space occured");
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UiController controller= new UiController(args[0]);
					AppGui window = new AppGui(controller);
					window.showWindow();
					controller.windowInitialized();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public GuiLauncher(){
		
	}
	
}
