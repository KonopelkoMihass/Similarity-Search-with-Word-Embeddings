package ie.atu.sw;

import java.util.Arrays;

// This class contains the embeddings and ids of words which are the best and worst matches.
public class SimilaritySearchResults {
    // Will contain similarity scores for best and worst matches.
    private double [] bestMatchesSimilarity;
    private double [] worstMatchesSimilarity;

    // Will contain similarity IDs for best and worst matches.
    private int [] bestMatchesIDs;
    private int [] worstMatchesIDs;

    // An enum which will dictate how the best and worst entries are detected and saved.
    enum SIMILARITY_SEARCH {
        COSINE,
        EUCLIDEAN,
    }

    private SIMILARITY_SEARCH chosen_search;


    // Overloaded constructor
    // - int totalBestMatches: the number of total best matches to store in results.
    // - int totalWorstMatches the number of total worst matches to store in results.
    // - SIMILARITY_SEARCH option: which search algorithm was used.
    public SimilaritySearchResults(int totalBestMatches, int totalWorstMatches, SIMILARITY_SEARCH option) {

        // Initialize variables.
        chosen_search = option;
        bestMatchesSimilarity = new double[totalBestMatches];
        bestMatchesIDs = new int[totalBestMatches];
        worstMatchesSimilarity = new double[totalWorstMatches];
        worstMatchesIDs = new int[totalWorstMatches];

        // Depending on a search used, set specific default values to fill best and worst matches arrays.
        switch (chosen_search) {
            case COSINE:
                fillResultArrays(-2.0, 2.0);
                break;
            case EUCLIDEAN:
                fillResultArrays(9999.0, -9999.0);
                break;
        }
    }

    // This method fills best/worst matches scores with specified in parameters values.
    private void fillResultArrays(double forBestMathes, double forWorstMatches) {
        int totalBestMatches = bestMatchesSimilarity.length;
        int totalWorstMatches = worstMatchesSimilarity.length;

        for (int i = 0; i < totalBestMatches; i ++) {
            bestMatchesSimilarity[i] = forBestMathes;
            bestMatchesIDs[i] = -1;
        }
        for (int i = 0; i < totalWorstMatches; i ++) {
            worstMatchesSimilarity[i] = forWorstMatches;
            worstMatchesIDs[i] = -1;
        }
    }

    // A collection of get methods for best/worst matches ids and scores.
    public int[] getBestMatchesIDs() {
        return bestMatchesIDs;
    }

    public int[] getWorstMatchesIDs() {
        return worstMatchesIDs;
    }

    public double[] getBestMatchesScores() {
        return bestMatchesSimilarity;
    }

    public double[] getWorstMatchesScores() {
        return worstMatchesSimilarity;
    }

    // This method is used in search algorithms to pass in candidate id and similarity score.
    public void updateResults(int id, double score) {
        updateBestSimilarityScoresAndID(id, score);
        updateWorstSimilarityScoresAndID (id, score);
    }

    // These sister methods will take in score and id of a word and attempt to add to the array of similarity scores.
    private void updateBestSimilarityScoresAndID(int id, double score) {
        // If search used is Cosine Similarity.
        if (chosen_search == SIMILARITY_SEARCH.COSINE) {
            // Loop through all the best matches scores.
            for (int i = 0; i < bestMatchesSimilarity.length; i++) {
                // If a candidate word's score is greater than what is stored in an array.
                if (bestMatchesSimilarity[i] < score) {
                    // Take all the values from index i to the second last character and shift them by 1.
                    insertAndShiftArray(bestMatchesIDs, bestMatchesSimilarity,id,score,i);
                    break;
                }
            }
        }
        // If search used is Euclidean Similarity.
        else if (chosen_search == SIMILARITY_SEARCH.EUCLIDEAN) {
            // Loop through all the best matches scores.
            for (int i = 0; i < bestMatchesSimilarity.length; i++) {
                // If a candidate word's score is smaller than what is stored in an array.
                if (bestMatchesSimilarity[i] > score) {
                    // Take all the values from index i to the second last character and shift them by 1.
                    insertAndShiftArray(bestMatchesIDs, bestMatchesSimilarity,id,score,i);
                    break;
                }
            }
        }
    }
    private void updateWorstSimilarityScoresAndID (int id, double score) {
        // If search used is Cosine Similarity.
        if (chosen_search == SIMILARITY_SEARCH.COSINE) {
            // Loop through all the best matches scores.
            for (int i = 0; i < worstMatchesSimilarity.length; i++) {
                // If a candidate word's score is less than what is stored in an array.
                if (worstMatchesSimilarity[i] > score) {
                    // Take all the values from index i to the second last character and shift them by 1.
                    insertAndShiftArray(worstMatchesIDs, worstMatchesSimilarity,id,score,i);
                    break;
                }
            }
        }
        // If search used is Euclidean Similarity.
        else if (chosen_search == SIMILARITY_SEARCH.EUCLIDEAN) {
            // Loop through all the best matches scores.
            for (int i = 0; i < worstMatchesSimilarity.length; i++) {
                // If a candidate word's score is greater than what is stored in an array.
                if (worstMatchesSimilarity[i] < score) {
                    // Take all the values from index i to the second last character and shift them by 1.
                    insertAndShiftArray(worstMatchesIDs, worstMatchesSimilarity,id,score,i);
                    break;
                }
            }
        }
    }


    // This method is used to split an array at index and until the second-last element, then shift these elements by +1.
    // A new score and id will be inserted at index location.
    // - int[] idArr: array of ids.
    // - double[] scoreArr: array of similarity scores.
    // - int id: candidate id to insert.
    // - double score: candidate score to insert.
    // - int index: the index in idAr rand scoreArr where to insert candidate data.
    private void insertAndShiftArray(int[] idArr, double[] scoreArr, int id, double score, int index) {
        // Test if the element is last in the array.
        if (index + 1 == idArr.length) {
            scoreArr[index] = score;
            idArr[index] = id;
        }
        // If not.
        else {
            // Create sub-arrays for both indecies and scores.  It is important to preserve order.
            double[] subArrayScores = Arrays.copyOfRange(scoreArr, index, scoreArr.length -2);
            int[] subArrayIds = Arrays.copyOfRange(idArr, index, idArr.length -2);

            // Insert new value at index.
            scoreArr[index] = score;
            idArr[index] = id;

            // Insert spliced arrays back, but at i+1;
            for (int j = 0; j < subArrayIds.length; j++) {
                scoreArr[index+1+j] = subArrayScores[j];
                idArr[index+1+j] = subArrayIds[j];
            }
        }
    }
}
