package ie.atu.sw;

public class Runner {
	// Will store the user response to the menu as a character.  Default is a space.
	static char userResponse = ' ';

	// This variable will contain the menu response to the user input.
	static String menuFeedback = "";

	// This variable will contain the filepath to the embedding.
	static String embeddingFilePath = "/home/filofej/IdeaProjects/Similarity Search with Word Embeddings/word-embeddings.txt";

	// This variable will contain the filepath to the output file.  Default location is set, but is mutable.
	static String outputFilePath = "./out.txt";

	// This variable will store the text which user wants to be used for similarity search.
	static String textInput = "";

	// This is a collection of booleans for tracking different things within
	static boolean running = true;
	static boolean snowMenu = true;

	private static final int MAXIMUM_OUTPUT_ENTRIES = 30000;


	public static void main(String[] args) throws Exception {
		// Instantiate a WordEmbeddingSystem.
		WordEmbeddingSystem weSystem = new WordEmbeddingSystem();

		String uiResponse = "";
		while (running) {
			// Show Main Menu if needed.
			if (snowMenu) mainMenuScreen();

			mainMenuOptions(uiResponse);

			// Get response to the menu prompt, then handle it. Parse it as a character,
			// which will always take the first letter of a string.
			userResponse = (char) System.in.read();

			switch (userResponse) {
				case '1':	// For Specify Embedding file.
					System.out.print("Enter the directory for an Embedding File now.");
					System.out.println();
					// Is true if embedding was loaded without issues.
					boolean s1 = weSystem.loadEmbeddings(System.console().readLine());

					// Alter User Interface Response.
					if (s1) uiResponse = "Word Embeddings were successfully loaded.";
					else uiResponse = "An error has occurred loading word embeddings.";

					break;
				case '2': 	// For Output Directory.
					System.out.println("Enter the directory for an Output File out.txt (or press Enter for the default location: ./)");
					boolean s2 = weSystem.specifyOutputFilepath(System.console().readLine());

					if (s2) uiResponse = "The output path provided is valid: " + weSystem.getOutputPath();
					else uiResponse = "The output path provided is invalid.";

					break;
				case '3':	// For Similarity Search
					// Allow to initiate similarity search when previous two steps were completed.
					if (weSystem.isReadyToDoSimilaritySearch()) {
						// Will store user input for total matches to be outputted into a file.
						int totalBestMatches = 0;
						int totalWorstMatches = 0;
						// First while loop that ensures that there will be an output.
						while (totalBestMatches == 0 && totalWorstMatches == 0) {

							System.out.println("Enter the number of best matches you want to see: ");
							String input = System.console().readLine();
							totalBestMatches = handleConsoleInput(input, true);

							System.out.println("Enter the number of worst matches you want to see: ");
							input = System.console().readLine();
							totalWorstMatches = handleConsoleInput(input, true);

							// If both matches counts are 0, force user to repeat.
							if (totalBestMatches == 0 && totalWorstMatches == 0) {
								System.out.println("Cannot output nothing.  Please choose again");
							}

						}
						// Will store the choice for the similarity search algorithms to be used.
						int choice = 0;

						// Boolean used to keep while loop active until user chooses search to use
						boolean searchAlgorithmSelected = false;
						while (searchAlgorithmSelected == false) {
							System.out.println("Enter the number for similarity search you want to use: ");
							System.out.println("1 - Cosine Similarity Search");
							System.out.println("2 - Euclidean Similarity Search");
							System.out.println("3 - Both Similarity Searches");

							String input = System.console().readLine();
							choice = handleConsoleInput(input, false);

							if (choice == 1 || choice == 2  || choice == 3){
								searchAlgorithmSelected = true;
							}
						}

						System.out.println("Please enter the text.");
						if (choice == 1) {
							weSystem.performWordSimilaritySearch(
								System.console().readLine(),
								totalBestMatches, totalWorstMatches,
								WordEmbeddingSystem.SEARCH_MODE.COSINE);
						}
						else if (choice == 2) {
							weSystem.performWordSimilaritySearch(
									System.console().readLine(),
									totalBestMatches, totalWorstMatches,
									WordEmbeddingSystem.SEARCH_MODE.EUCLIDEAN);
						}
						else {
							weSystem.performWordSimilaritySearch(
									System.console().readLine(),
									totalBestMatches, totalWorstMatches,
									WordEmbeddingSystem.SEARCH_MODE.BOTH);
						}
						uiResponse = "Similarity search was completed. The output is located at: " + weSystem.getOutputPath() + "";
					}
					else {
						uiResponse = "You either did not select embeddings, or did not specify the output path.";
					}

					break;
				case '4':
					System.out.print("Goodbye!");
					running = false;
					break;
				default:
					System.out.print("Wrong Response. Please try again.");
					break;
			}

			// Used in normal console application to give the feeling of a menu changing rather than a
			// continuous chain of console commands.
			System.out.print("\033[H\033[2J");
		}
	}

	// This method is used to handle numerical input
	private static int handleConsoleInput(String input, boolean isForOutput) {
		int result = -1;

		// Checks that output is a number.
		if (input.matches("[0-9]+") ){
			result = Integer.parseInt(input);
		}
		// If not a number, assume 0.
		else {
			System.out.println("You did not enter a positive number.  Assumed 0.");
			result = 0;
		}

		// Check that the total is less than total words in embedding - 1.
		// If so, set total to the highest possible value.
		if (isForOutput){
			if (result >= MAXIMUM_OUTPUT_ENTRIES) {
				result = MAXIMUM_OUTPUT_ENTRIES;
				System.out.println("The total was set to most logical maximum: " + result);
			}
		}
		return result;
	}





	// Encapsulate the main menu into a separate method, for ease of reading of the main menu
	static void mainMenuScreen() {
		//You should put the following code into a menu or Menu class
		System.out.println(ConsoleColour.WHITE);
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*          Similarity Search with Word Embeddings          *");
		System.out.println("*                                                          *");
		System.out.println("*            Implementation by Mihass Konopelko            *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Embedding File");
		System.out.println("(2) Specify an Output File (default: ./out.txt)");
		System.out.println("(3) Enter a Word or Text");
		System.out.println("(4) Quit");
	}

	static void mainMenuOptions(String uiResonse) {
		//Output a menu of options and solicit text from the user
		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.println();
		System.out.print(uiResonse);
		System.out.println();
		System.out.print("Select Option [1-4]>");
		System.out.println();
	}








	

}