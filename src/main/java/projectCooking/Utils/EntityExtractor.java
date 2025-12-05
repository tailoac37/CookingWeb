package projectCooking.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enhanced Entity Extractor
 * Extracts structured information from natural language queries
 */
public class EntityExtractor {

    // Vietnamese dish names
    private static final Set<String> DISH_NAMES = new HashSet<>(Arrays.asList(
            "pho", "bun", "banh mi", "com", "chao", "sup", "canh", "goi", "nem", "cha gio",
            "bun cha", "bun bo", "bun rieu", "mi quang", "cao lau", "hu tieu",
            "banh xeo", "banh cuon", "banh beo", "banh khot", "che", "xoi"));

    // Common ingredients (expanded)
    private static final Map<String, String> INGREDIENTS_MAP = new HashMap<>();
    static {
        // Proteins
        INGREDIENTS_MAP.put("ga", "gà");
        INGREDIENTS_MAP.put("thit ga", "gà");
        INGREDIENTS_MAP.put("chicken", "gà");

        INGREDIENTS_MAP.put("bo", "bò");
        INGREDIENTS_MAP.put("thit bo", "bò");
        INGREDIENTS_MAP.put("beef", "bò");

        INGREDIENTS_MAP.put("heo", "heo");
        INGREDIENTS_MAP.put("lon", "heo");
        INGREDIENTS_MAP.put("thit heo", "heo");
        INGREDIENTS_MAP.put("pork", "heo");

        INGREDIENTS_MAP.put("ca", "cá");
        INGREDIENTS_MAP.put("fish", "cá");

        INGREDIENTS_MAP.put("tom", "tôm");
        INGREDIENTS_MAP.put("shrimp", "tôm");

        INGREDIENTS_MAP.put("muc", "mực");
        INGREDIENTS_MAP.put("squid", "mực");

        // Vegetables
        INGREDIENTS_MAP.put("khoai tay", "khoai tây");
        INGREDIENTS_MAP.put("potato", "khoai tây");

        INGREDIENTS_MAP.put("ca chua", "cà chua");
        INGREDIENTS_MAP.put("tomato", "cà chua");

        INGREDIENTS_MAP.put("hanh", "hành");
        INGREDIENTS_MAP.put("onion", "hành");

        INGREDIENTS_MAP.put("toi", "tỏi");
        INGREDIENTS_MAP.put("garlic", "tỏi");

        INGREDIENTS_MAP.put("ot", "ớt");
        INGREDIENTS_MAP.put("chili", "ớt");

        INGREDIENTS_MAP.put("ca rot", "cà rốt");
        INGREDIENTS_MAP.put("carrot", "cà rốt");

        INGREDIENTS_MAP.put("bap cai", "bắp cải");
        INGREDIENTS_MAP.put("cabbage", "bắp cải");

        INGREDIENTS_MAP.put("rau", "rau");
        INGREDIENTS_MAP.put("vegetable", "rau");

        // Others
        INGREDIENTS_MAP.put("trung", "trứng");
        INGREDIENTS_MAP.put("egg", "trứng");

        INGREDIENTS_MAP.put("sua", "sữa");
        INGREDIENTS_MAP.put("milk", "sữa");

        INGREDIENTS_MAP.put("pho mai", "phô mai");
        INGREDIENTS_MAP.put("cheese", "phô mai");

        INGREDIENTS_MAP.put("nam", "nấm");
        INGREDIENTS_MAP.put("mushroom", "nấm");

        INGREDIENTS_MAP.put("dau", "đậu");
        INGREDIENTS_MAP.put("bean", "đậu");
    }

    /**
     * Extract ingredients from normalized message
     */
    public static List<String> extractIngredients(String normalizedMessage) {
        Set<String> ingredients = new HashSet<>();

        // Check for multi-word ingredients first
        for (Map.Entry<String, String> entry : INGREDIENTS_MAP.entrySet()) {
            if (normalizedMessage.contains(entry.getKey())) {
                ingredients.add(entry.getValue());
            }
        }

        return new ArrayList<>(ingredients);
    }

    /**
     * Extract time in minutes from message
     * Examples: "30 phut", "1 gio", "1.5 gio"
     */
    public static Integer extractTimeInMinutes(String message) {
        // Pattern for number + unit
        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(phut|gio|minute|hour|min|hr)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            double number = Double.parseDouble(matcher.group(1));
            String unit = matcher.group(2);

            // Convert to minutes
            if (unit.contains("gio") || unit.contains("hour") || unit.equals("hr")) {
                return (int) (number * 60);
            } else {
                return (int) number;
            }
        }

        // Check for keywords
        if (message.contains("nhanh") || message.contains("gap")) {
            return 30;
        }

        return null;
    }

    /**
     * Extract number of servings
     */
    public static Integer extractServings(String message) {
        // Pattern: number + (nguoi|phan|serving)
        Pattern pattern = Pattern.compile("(\\d+)\\s*(nguoi|phan|serving|people)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        // Pattern: cho + number
        pattern = Pattern.compile("cho\\s+(\\d+)");
        matcher = pattern.matcher(message);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return null;
    }

    /**
     * Extract difficulty level
     */
    public static String extractDifficulty(String message) {
        if (FuzzyMatcher.isSimilar(message, "de lam", 70) ||
                message.contains("de") || message.contains("easy") || message.contains("don gian")) {
            return "EASY";
        }

        if (FuzzyMatcher.isSimilar(message, "kho lam", 70) ||
                message.contains("kho") || message.contains("hard") || message.contains("phuc tap")) {
            return "HARD";
        }

        if (message.contains("trung binh") || message.contains("medium") || message.contains("vua phai")) {
            return "MEDIUM";
        }

        return null;
    }

    /**
     * Extract dish name from query
     */
    public static String extractDishName(String normalizedMessage) {
        // Remove common query words
        String cleaned = normalizedMessage
                .replaceAll("tim|mon|cong thuc|recipe|lam|nau|cho toi|xem|co|khong|gi|nao|tim kiem|search|muon|an", "")
                .trim();

        // Check if any known dish name matches
        for (String dish : DISH_NAMES) {
            if (cleaned.contains(dish)) {
                return dish;
            }
        }

        // Return cleaned query if long enough
        if (cleaned.length() > 2) {
            return cleaned;
        }

        return null;
    }

    /**
     * Detect all intents in a message
     */
    public static List<String> detectIntents(String normalizedMessage) {
        List<String> intents = new ArrayList<>();

        // Check each intent
        if (SynonymDictionary.containsAnySynonym(normalizedMessage, "yeu thich", "like", "pho bien")) {
            intents.add("POPULAR");
        }

        if (SynonymDictionary.containsAnySynonym(normalizedMessage, "hot", "trending", "nong")) {
            intents.add("TRENDING");
        }

        if (extractDifficulty(normalizedMessage) != null) {
            intents.add("DIFFICULTY");
        }

        if (extractTimeInMinutes(normalizedMessage) != null) {
            intents.add("COOKING_TIME");
        }

        if (extractServings(normalizedMessage) != null) {
            intents.add("SERVINGS");
        }

        if (!extractIngredients(normalizedMessage).isEmpty()) {
            intents.add("INGREDIENTS");
        }

        if (SynonymDictionary.containsAnySynonym(normalizedMessage, "tim", "search", "cong thuc")) {
            intents.add("SEARCH");
        }

        return intents;
    }
}
