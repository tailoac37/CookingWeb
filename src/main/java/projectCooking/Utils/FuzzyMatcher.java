package projectCooking.Utils;

/**
 * Fuzzy String Matching using Levenshtein Distance
 * Helps chatbot understand typos and spelling mistakes
 */
public class FuzzyMatcher {

    /**
     * Calculate Levenshtein distance between two strings
     * Returns number of edits needed to transform s1 to s2
     */
    public static int levenshteinDistance(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return Integer.MAX_VALUE;
        }

        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int len1 = s1.length();
        int len2 = s2.length();

        // Create DP table
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Initialize base cases
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // Fill DP table
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            Math.min(dp[i - 1][j], dp[i][j - 1]),
                            dp[i - 1][j - 1]);
                }
            }
        }

        return dp[len1][len2];
    }

    /**
     * Calculate similarity percentage (0-100)
     */
    public static double similarity(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0.0;
        }

        if (s1.equals(s2)) {
            return 100.0;
        }

        int distance = levenshteinDistance(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());

        if (maxLen == 0) {
            return 100.0;
        }

        return (1.0 - (double) distance / maxLen) * 100.0;
    }

    /**
     * Check if two strings are similar based on threshold
     * 
     * @param threshold - similarity percentage (0-100)
     */
    public static boolean isSimilar(String s1, String s2, double threshold) {
        return similarity(s1, s2) >= threshold;
    }

    /**
     * Find best matching string from a list
     * Returns null if no match above threshold
     */
    public static String findBestMatch(String target, String[] candidates, double threshold) {
        String bestMatch = null;
        double bestSimilarity = 0.0;

        for (String candidate : candidates) {
            double sim = similarity(target, candidate);
            if (sim > bestSimilarity && sim >= threshold) {
                bestSimilarity = sim;
                bestMatch = candidate;
            }
        }

        return bestMatch;
    }
}
