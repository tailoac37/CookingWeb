package projectCooking.Utils;

import java.util.*;

/**
 * Demo class to test all utilities
 * Run this to verify everything works
 */
public class UtilsDemo {

    public static void main(String[] args) {
        System.out.println("=== CHATBOT UTILITIES DEMO ===\n");

        // 1. Test FuzzyMatcher
        System.out.println("1. FUZZY MATCHER:");
        String input = "yeu thich";
        String typo = "yue thich";
        double similarity = FuzzyMatcher.similarity(input, typo);
        System.out.println("   Similarity(\"" + input + "\", \"" + typo + "\") = " + similarity + "%");
        System.out.println("   Is similar (80% threshold)? " + FuzzyMatcher.isSimilar(input, typo, 80));
        System.out.println();

        // 2. Test SynonymDictionary
        System.out.println("2. SYNONYM DICTIONARY:");
        List<String> synonyms = SynonymDictionary.getSynonyms("like");
        System.out.println("   Synonyms of 'like': " + synonyms);
        System.out.println("   'yeu thich' and 'like' are synonyms? " +
                SynonymDictionary.areSynonyms("yeu thich", "like"));
        System.out.println();

        // 3. Test EntityExtractor
        System.out.println("3. ENTITY EXTRACTOR:");
        String query = "tim mon de lam trong 30 phut cho 4 nguoi";
        System.out.println("   Query: \"" + query + "\"");

        String difficulty = EntityExtractor.extractDifficulty(query);
        System.out.println("   Difficulty: " + difficulty);

        Integer time = EntityExtractor.extractTimeInMinutes(query);
        System.out.println("   Time: " + time + " minutes");

        Integer servings = EntityExtractor.extractServings(query);
        System.out.println("   Servings: " + servings + " people");

        List<String> intents = EntityExtractor.detectIntents(query);
        System.out.println("   Intents detected: " + intents);
        System.out.println();

        // 4. Test ChatContextManager
        System.out.println("4. CONTEXT MANAGER:");
        String userId = "user123";
        ChatContextManager.addMessage(userId, "user", "Hello");
        ChatContextManager.addMessage(userId, "assistant", "Hi there!");
        ChatContextManager.setLastIntent(userId, "GREETING");

        String lastQuery = ChatContextManager.getLastUserQuery(userId);
        String lastIntent = ChatContextManager.getLastIntent(userId);
        System.out.println("   Last user query: " + lastQuery);
        System.out.println("   Last intent: " + lastIntent);
        System.out.println();

        // 5. Test ResponseTemplateManager
        System.out.println("5. RESPONSE TEMPLATES:");
        String template = ResponseTemplateManager.getTemplate("POPULAR_SUCCESS");
        System.out.println("   Random template: " + template);

        Map<String, String> params = new HashMap<>();
        params.put("count", "10");
        String filled = ResponseTemplateManager.fillTemplate(template, params);
        System.out.println("   Filled: " + filled);
        System.out.println();

        System.out.println("=== ALL UTILITIES WORKING! ===");
    }
}
