package projectCooking.Utils;

import java.util.*;

/**
 * Vietnamese Synonym Dictionary for Cooking Domain
 * Helps chatbot understand different ways to express the same intent
 */
public class SynonymDictionary {

    private static final Map<String, List<String>> synonyms = new HashMap<>();

    static {
        // Popular/Like related
        synonyms.put("yeu thich", Arrays.asList("ua chuong", "thich", "like", "pho bien", "noi tieng"));

        // Hot/Trending related
        synonyms.put("hot", Arrays.asList("nong", "trending", "dang len", "thinh hanh", "xu huong"));

        // Search/Find related
        synonyms.put("tim", Arrays.asList("tim kiem", "search", "tra cuu", "hoi", "xem"));

        // Recipe/Dish related
        synonyms.put("mon", Arrays.asList("mon an", "cong thuc", "dish", "recipe", "bua an"));

        // Cooking related
        synonyms.put("nau", Arrays.asList("lam", "che bien", "cook", "prepare", "tao ra"));

        // Difficulty related
        synonyms.put("de", Arrays.asList("de lam", "don gian", "easy", "gian don", "nhe nhang"));
        synonyms.put("kho", Arrays.asList("kho lam", "phuc tap", "hard", "difficulty", "kho khan"));
        synonyms.put("trung binh", Arrays.asList("vua phai", "medium", "binh thuong"));

        // Time related
        synonyms.put("nhanh", Arrays.asList("nhanh chong", "gap", "toc do", "quickly", "fast"));
        synonyms.put("cham", Arrays.asList("lau", "dai", "slowly", "long"));

        // Ingredients
        synonyms.put("nguyen lieu", Arrays.asList("ingredient", "thanh phan", "chat lieu", "ngay lieu"));

        // Help related
        synonyms.put("giup", Arrays.asList("giup do", "tro giup", "ho tro", "help", "support"));

        // Question words
        synonyms.put("gi", Arrays.asList("nao", "what", "which", "cai nao"));

        // Quality
        synonyms.put("ngon", Arrays.asList("thom", "hap dan", "delicious", "tasty", "ngon mieng"));

        // Quantity
        synonyms.put("nhieu", Arrays.asList("dong", "rat nhieu", "many", "much", "lots"));
        synonyms.put("it", Arrays.asList("vai", "few", "little", "khong nhieu"));
    }

    /**
     * Get all synonyms for a word
     */
    public static List<String> getSynonyms(String word) {
        word = word.toLowerCase().trim();

        // Check if word is a key
        if (synonyms.containsKey(word)) {
            return new ArrayList<>(synonyms.get(word));
        }

        // Check if word is in any synonym list
        for (Map.Entry<String, List<String>> entry : synonyms.entrySet()) {
            if (entry.getValue().contains(word)) {
                List<String> result = new ArrayList<>();
                result.add(entry.getKey());
                result.addAll(entry.getValue());
                result.remove(word); // Remove the word itself
                return result;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Check if two words are synonyms
     */
    public static boolean areSynonyms(String word1, String word2) {
        word1 = word1.toLowerCase().trim();
        word2 = word2.toLowerCase().trim();

        if (word1.equals(word2)) {
            return true;
        }

        List<String> syns1 = getSynonyms(word1);
        return syns1.contains(word2);
    }

    /**
     * Expand a phrase with all possible synonyms
     * Used for better keyword matching
     */
    public static List<String> expandWithSynonyms(String phrase) {
        Set<String> expanded = new HashSet<>();
        expanded.add(phrase);

        String[] words = phrase.toLowerCase().split("\\s+");
        for (String word : words) {
            List<String> syns = getSynonyms(word);
            for (String syn : syns) {
                String newPhrase = phrase.replace(word, syn);
                expanded.add(newPhrase);
            }
        }

        return new ArrayList<>(expanded);
    }

    /**
     * Find if any synonym of keywords exists in text
     */
    public static boolean containsAnySynonym(String text, String... keywords) {
        text = text.toLowerCase();

        for (String keyword : keywords) {
            // Check exact match
            if (text.contains(keyword.toLowerCase())) {
                return true;
            }

            // Check synonyms
            List<String> syns = getSynonyms(keyword);
            for (String syn : syns) {
                if (text.contains(syn.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}
