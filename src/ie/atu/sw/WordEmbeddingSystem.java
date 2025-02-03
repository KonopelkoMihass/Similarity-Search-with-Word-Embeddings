package ie.atu.sw;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class WordEmbeddingSystem {
    // A constant for the total number of words.
    private final int WORDS_COUNT = 59602;

    // A constant for the total number of features per word in embedding.
    private final int FEATURES_COUNT = 50;

    // An array that stores all the words from embedding.
    private String[] words = new String[WORDS_COUNT];

    // A double array that stores all the features for each word in embedding.
    private double[][] embeddings = new double[WORDS_COUNT][FEATURES_COUNT];

    // A filepath for the output text which will contain the results of similarity search.
    private String outputFilePath = "./out.txt";

    // Condition checks for loaded embeddings and output selection.  Both must be true to allow similarity search.
    private boolean wordEmbeddingsLoaded = true;
    private boolean outputSelected = true;

    // An instance of the Cosine Similarity Search class.
    private CosineSimilaritySearch cosineSearch;

    // An instance of the Euclidean Similarity Search class.
    private EuclideanSimilaritySearch euclideanSearch;

    enum SEARCH_MODE {
        COSINE ,
        EUCLIDEAN ,
        BOTH ,
    }



    // A constructor.
    public WordEmbeddingSystem() {
        cosineSearch = new CosineSimilaritySearch();
        euclideanSearch = new EuclideanSimilaritySearch();
    }

    // This method will load embeddings from the filepath.
    public boolean loadEmbeddings(String filepath) {
        boolean result = false;
        try {
            // First, get the file in the supplied filepath.
            File f = new File(filepath);

            // Second, read the file using a Scanner,
            Scanner myReader = new Scanner(f);

            // Used as an iterator for managing arrays,
            int i = 0;

            // We loop until there are no lines remaining.
            while (myReader.hasNextLine()) {
                // After some steps, update the progress meter.
                printProgress(i + 1, WORDS_COUNT);

                // Get the new line of data.
                String line = myReader.nextLine();

                // Split it into segments, using comma as a separator.
                String[] partitions = line.split(",");

                // In each case, the first element of partitions will be a word.  It is saved into a words array.
                words[i] = partitions[0];

                // Then the first element is removed from partitions using Array.copyOfRange method.
                partitions = Arrays.copyOfRange(partitions, 1, partitions.length);

                // Check that it contains precisely 50 numerical values
                if (partitions.length != 50) {
                    System.out.println();
                    System.out.print(words[i] + " does not contain 50 embeddings, but " + partitions.length);
                    return result;
                }

                // Afterward, the following line of code is used to convert array of strings into an array of doubles.
                // There is a list of methods and what they do:
                // - Arrays.stream() turns an array into a Sequential Stream (as to preserve order of elements).
                // - Stream.mapToDouble(Double::parseDouble) converts each element in the stream into a double, parsing
                // them fully from a string.
                // - Stream.toArray() converts the stream to an array.
                // Note: Same operation could be done using a for loop and casting, but curiosity drove me to use this
                // approach + it is still relatively readable and not too overcomplicated.
                embeddings[i] = Arrays.stream(partitions).mapToDouble(Double::parseDouble).toArray();

                // Increment counter,
                i++;
            }
            result = true;
        }
        // Catch statement for when file was not found.
        catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: An error occurred while parsing the file.");
        }
        // Catch statement for when the number contains non-numeric character.
        catch (NumberFormatException e){
            System.out.println("NumberFormat Exception: The numerical data contains a non-numeric character: " +
                    // this will command will capture the end of a string from a message that contains the location of the error.
                    e.getMessage().substring(18, e.getMessage().length() ));
        }

        // If result is true, meaning the embedding was loaded in without errors, set wordEmbeddingsLoaded to true.
        if (result == true){
            wordEmbeddingsLoaded = true;
        }

        return result;
    }

    // This method is checking wherever a specified filepath exists and can be used for output file.
    public boolean specifyOutputFilepath(String filepath) {
        boolean result = false;

        // Try method to detect any error possible.
        try {
            // If the length of a parameter is 0, it means that a default filepath is to be used.
            if (filepath.length() == 0) {
                result = true;
                outputSelected = true;
            }
            // Otherwise, test if it is a valid directory.
            else {
                // Initialize a new file with a parameter filepath.
                File f = new File(filepath);

                // If it is a directory, then set this as a new path for the output file.
                if (f.isDirectory()) {
                    result = true;
                    outputFilePath = filepath + outputFilePath.substring(2, outputFilePath.length()); // removes './'
                    outputSelected = true;
                }
                // Else it is not a directory.
                else {
                    result = false;
                }
            }
        }
        // In case if something occurs, it will be caught here.
        catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // Gets Output Path.
    public String getOutputPath(){
        return outputFilePath;
    }

    // Checks if the embeddings were loaded and output filepath was selected.
    public boolean isReadyToDoSimilaritySearch() {
        return wordEmbeddingsLoaded == true && outputSelected == true;
    }

    // This method will find an ID of a word entered by user.
    private int getWordID (String word) {
        // Find ID of the word
        int wordID = -1;
        for (int i = 0; i < WORDS_COUNT; i++) {
            if (words[i].equals(word)) {
                wordID = i;
                // Break the loop since the word is found.
                break;
            }
        }

        return wordID;
    }

    // This method performs the similarity search based on parameters submitted.
    // Parameters:
    // - String userInput: a user input to be searched.
    // - int totalBestMatches: the number of total best matches to store in results.
    // - int totalWorstMatches the number of total worst matches to store in results.
    // - SEARCH_MODE mode: dictates which search to use - Euclidean, Cosine or both.
    public void performWordSimilaritySearch(String userInput, int totalBestMatches, int totalWorstMatches, SEARCH_MODE mode) throws IOException {
        // This array will store the split version of the user inout (in case if it contains multiple words and non-alphabetical characters.
        String[] wordsToSearch;

        // Split user input using a regex command \W, which will split the user input on whitespaces, commas, periods and other symbols
        wordsToSearch = userInput.split("\\W+");

        // Iterate through the loop to change words to lowercase and remove any numerical values added.
        for (int i = 0; i < wordsToSearch.length; i ++) {
            wordsToSearch[i] = wordsToSearch[i].toLowerCase(Locale.ROOT);
            wordsToSearch[i] = wordsToSearch[i].replaceAll("[^a-zA-Z]", "");
        }

        // Will store the text which will be saved in out.txt
        String resultText = "";

        // Loop through each searched word.
        for (int i = 0; i < wordsToSearch.length; i ++) {
            // After some steps, update the progress meter.
            printProgress(i + 1, wordsToSearch.length);

            // First, get the ID which corresponds to the words and embeddings arrays.
            int wordID = getWordID(wordsToSearch[i]);

            // If wordID is -1, it means the word does not exist in the embeddinds and words catalogues.
            // Causes can include a grammatical error or by being plural.
            // A message will be left in an output text, and the next word will be searched.
            if (wordID == -1) {
                resultText += "No similarity search was performed on a word " + wordsToSearch[i] + "\n\n\n";
                continue;
            }

            // If Statements for different similarity searches.  Depending on the mode chosen, a specific search
            // algorithm will be used (or even both).
            // Each algorithm will output a result, which is the transcribed into a string and appended to a resultText.
            if (mode == SEARCH_MODE.COSINE) {
                SimilaritySearchResults results = cosineSearch.run(wordID,embeddings,totalBestMatches,totalWorstMatches);
                resultText += transcribeResults(wordsToSearch[i], results, mode);
            } else if (mode == SEARCH_MODE.EUCLIDEAN) {
                SimilaritySearchResults results = euclideanSearch.run(wordID,embeddings,totalBestMatches,totalWorstMatches);
                resultText += transcribeResults(wordsToSearch[i], results, mode);
            } else if (mode == SEARCH_MODE.BOTH) {
                SimilaritySearchResults cosResults = cosineSearch.run(wordID,embeddings,totalBestMatches,totalWorstMatches);
                SimilaritySearchResults euclResults = euclideanSearch.run(wordID,embeddings,totalBestMatches,totalWorstMatches);
                resultText += transcribeResults(wordsToSearch[i], cosResults, SEARCH_MODE.COSINE);
                resultText += "\n\n";
                resultText += transcribeResults(wordsToSearch[i], euclResults, SEARCH_MODE.EUCLIDEAN);
            }

            // Add extra lines to separate each similarly searched word.
            resultText += "\n\n\n";


        }

        // At the end, write the result text to the file.
        writeToFile(resultText);
    }

    // This method will take in results and extract data from it into a string variable, which it returns at the end.
    private String transcribeResults(String targetWord, SimilaritySearchResults results, SEARCH_MODE mode) {
        String text = "The following are the results of ";
        // Alters text based on the mode.
        switch (mode) {
            case EUCLIDEAN:
                text += "an Euclidean Similarity search for a word " + targetWord + ":";
                break;
            case COSINE:
                text += "a Cosine Similarity search for a word " + targetWord + ":";
                break;
        }

        // Gets an array of best matches ids.
        int [] bestMatchesIDs = results.getBestMatchesIDs();

        // If the length the array is 0, then there is no need to write a section with best matches.
        if (bestMatchesIDs.length > 0) {
            text += "\nBest Matches:";
            double [] bestMatchesScores = results.getBestMatchesScores();
            for (int i = 0; i < bestMatchesIDs.length; i++) {
                text +=  "\n" + String.format( "%-6s  %-18s  [%.9f]",  (i+1) + ". ", words[bestMatchesIDs[i]], bestMatchesScores[i] );
            }
        }
        // Extra line break.
        text += "\n";

        // Gets an array of worst matches ids.
        int [] worstMatchesIDs = results.getWorstMatchesIDs();

        // If the length the array is 0, then there is no need to write a section with worst matches.
        if (worstMatchesIDs.length > 0) {
            text += "\nWorst Matches:";
            double [] worstMatchesScores = results.getWorstMatchesScores();
            for (int i = 0; i < worstMatchesIDs.length; i++) {
                text +=  "\n" + String.format( "%-6s  %-18s  [%.9f]",  (i+1) + ". ", words[worstMatchesIDs[i]], worstMatchesScores[i] );
            }
        }

        // Return text.
        return text;
    }


    // This method creates a file with the text as its content.
    private void writeToFile(String text) throws IOException {
        File file = new File(outputFilePath);

        // If file doesn't exists, then create it.
        if (!file.exists()) {
            file.createNewFile();
        }
        // Create File Writer and open the output file.
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

        // Create a buffer into which the text will be written.
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        // Write into file.
        bufferedWriter.write(text);

        // Close the buffer.
        bufferedWriter.close();
    }

    // Returns total number of entries.
    public int getTotalEntriesInEmbedding() {
        return WORDS_COUNT;
    }



    /*
     *  Terminal Progress Meter
     *  -----------------------
     *  You might find the progress meter below useful. The progress effect
     *  works best if you call this method from inside a loop and do not call
     *  System.out.println(....) until the progress meter is finished.
     *
     *  Please note the following carefully:
     *
     *  1) The progress meter will NOT work in the Eclipse console, but will
     *     work on Windows (DOS), Mac and Linux terminals.
     *
     *  2) The meter works by using the line feed character "\r" to return to
     *     the start of the current line and writes out the updated progress
     *     over the existing information. If you output any text between
     *     calling this method, i.e. System.out.println(....), then the next
     *     call to the progress meter will output the status to the next line.
     *
     *  3) If the variable size is greater than the terminal width, a new line
     *     escape character "\n" will be automatically added and the meter won't
     *     work properly.
     *
     *
     */
    public void printProgress(int index, int total) {
        if (index > total) return;	//Out of range
        int size = 50; 				//Must be less than console width
        char done = '█';			//Change to whatever you like.
        char todo = '░';			//Change to whatever you like.

        //Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;

        /*
         * A StringBuilder should be used for string concatenation inside a
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append((i < completeLen) ? done : todo);
        }

        /*
         * The line feed escape character "\r" returns the cursor to the
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");

        //Once the meter reaches its max, move to a new line.
        if (done == total) System.out.println("\n");
    }







}
