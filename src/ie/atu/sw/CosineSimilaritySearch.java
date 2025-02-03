package ie.atu.sw;

// This class implements the DOT + Cosine similarity search, as well as encapsulates logic necessary to perform it.
public class CosineSimilaritySearch {

    // Empty constructor.
    public CosineSimilaritySearch() { }

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
                SimilaritySearchResults.SIMILARITY_SEARCH.COSINE // Specified to ensure correct calculations.
        );

        // Get embedding for target word.
        double[] wordEmbedding = embeddings[wordID];

        // Get the total amount of embeddings.
        int totalEmbeddings = embeddings.length;

        // Pre-calculate unsquare-rooted distance for the target word embedding.
        // Done so that it is not calculated during each iteration of the loop.
        double targetWordUnsquaredRootedDist = calculateUnsquaredRootedDistance(wordEmbedding);

        // Loop through embeddings
        for (int i = 0; i < totalEmbeddings; i++) {
            // Skip the similarity search for itself.
            if (i == wordID) {
                continue;
            }

            // Retrieve embedding at index i.
            double[] testEmbedding = embeddings[i];

            // Calculate Dot Product.
            double dotProduct = calculateDotOfTwoVectors(wordEmbedding, testEmbedding);

            // Calculate Cosine distance.
            double distance = Math.sqrt(targetWordUnsquaredRootedDist * calculateUnsquaredRootedDistance(testEmbedding));

            // Update best/worst matches at index i.
            results.updateResults(i,dotProduct / distance);
        }
        // Returns results.
        return results;
    }


    // This method calculates Dot product of two vectors.
    private double calculateDotOfTwoVectors(double[] v1, double[] v2) {
        // The size of the vectors (it is same for v1 and v2).
        int size = v1.length;

        // Stores the result of operation.
        double result = 0;

        // Loop through features of two embeddings, perform multiplication of parallel features and addition to the result.
        for (int i = 0; i < size; i++) {
            result += (v1[i] * v2[i]);
        }
        // Returns results.
        return result;
    }

    // This method performs the squaring of distance components for Cosine Distance.  Afterward, it would be square rooted.
    private double calculateUnsquaredRootedDistance(double[] v) {
        // The size of the vector v.
        int size = v.length;

        // Stores the result of operation.
        double result = 0;

        // Loop through features of embedding, perform double multiplication of each feature and addition to the result.
        for (int i = 0; i < size; i++) {
            result += (v[i] * v[i]);
        }
        // Returns results.
        return result;
    }

}
