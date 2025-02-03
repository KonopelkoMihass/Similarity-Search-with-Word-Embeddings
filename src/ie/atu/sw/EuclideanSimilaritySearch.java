package ie.atu.sw;

// This class implements the Euclidean similarity search, as well as encapsulates logic necessary to perform it.
public class EuclideanSimilaritySearch {

    // Empty constructor.
    public EuclideanSimilaritySearch() { }

    // The main method of this class, which performs the search.
    // Parameters:
    // - int wordID: an id of a word which match the words and embeddings arrays.
    // - double[][] embeddings: a "reference" to the embeddings from the WordEmbeddingSystem.  Not modified.
    // - int totalBestMatches: the number of total best matches to store in results.
    // - int totalWorstMatches the number of total worst matches to store in results.
    // In the end it will return a SimilaritySearchResults variable with results of the search.
    public SimilaritySearchResults run(int wordID, double[][] embeddings, int totalBestMatches, int totalWorstMatches) {
        // Instance of the SimilaritySearchResults, which will store the result of a search.
        SimilaritySearchResults results = new SimilaritySearchResults(
                totalBestMatches,
                totalWorstMatches,
                SimilaritySearchResults.SIMILARITY_SEARCH.EUCLIDEAN // Specified to ensure correct calculations.
        );

        // Get embedding for target word.
        double[] wordEmbedding = embeddings[wordID];

        // Get the total amount of embeddings.
        int totalEmbeddings = embeddings.length;

        // Get the total number of features per embedding.
        int totalFeatures = wordEmbedding.length;

        // Loop through embeddings
        for (int i = 0; i < totalEmbeddings; i++) {
            // Skip the similarity search for itself
            if (i == wordID) {
                continue;
            }

            // Retrieve embedding at index i.
            double [] wordEmbeddings = embeddings[i];

            // Will store the distance between target embedding and iterated embedding. Later it will be square-rooted.
            double unsquareRootedDistance = 0;

            // Loop through features in both embeddings and perform Euclidean calculation.
            for (int j = 0; j < totalFeatures; j++) {
                unsquareRootedDistance += (wordEmbeddings[j] - wordEmbedding[j]) * (wordEmbeddings[j] - wordEmbedding[j]);
            }

            // Get the distance by square-rooting the unsquareRootedDistance.
            double distance = Math.sqrt(unsquareRootedDistance);

            // Update best/worst matches at index i.
            results.updateResults(i, distance);
        }
        return results;
    }



}
