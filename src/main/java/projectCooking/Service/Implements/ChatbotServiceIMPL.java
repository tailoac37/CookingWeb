package projectCooking.Service.Implements;

import java.util.*;
import java.util.stream.Collectors;
import java.text.Normalizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Request.ChatRequest;
import projectCooking.Response.ChatResponse;
import projectCooking.Service.ChatbotService;
import projectCooking.Service.JWTService;

@Service
public class ChatbotServiceIMPL implements ChatbotService {

    @Autowired
    private RecipesRepo recipeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwt;

    @Autowired
    private LikeRepo likeRepo;

    @Override
    public ChatResponse chat(String message, String token) {
        ChatRequest request = new ChatRequest();
        request.setMessage(message);
        request.setHistory(new ArrayList<>());
        return chatWithHistory(request, token);
    }

    @Override
    public ChatResponse chatWithHistory(ChatRequest request, String token) {
        String message = normalize(request.getMessage());

        // 👤 Lấy thông tin user từ token
        String username = null;
        try {
            if (token != null && !token.isEmpty()) {
                username = jwt.extractUserName(token);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Không thể lấy username: " + e.getMessage());
        }

        // Phân tích câu hỏi và xác định intent
        ChatResponse response = analyzeAndRespond(message, token, username);

        return response;
    }

    /**
     * Normalize text: remove accents, lowercase, trim
     * Giúp chatbot hiểu cả khi user gõ không dấu
     */
    private String normalize(String text) {
        if (text == null)
            return "";

        // Convert to lowercase
        text = text.toLowerCase().trim();

        // Remove Vietnamese accents
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");

        // Normalize common variations
        text = text.replaceAll("đ", "d");
        text = text.replaceAll("Đ", "d");

        return text;
    }

    private ChatResponse analyzeAndRespond(String message, String token, String username) {
        // 1. Check for popular/liked recipes
        if (containsAny(message, "yeu thich", "like", "pho bien", "nhieu like", "duoc thich", "ua chuong",
                "noi tieng")) {
            List<RecipesDTO> recipes = getPopularRecipes(token);
            if (!recipes.isEmpty()) {
                return new ChatResponse(
                        "Đây là những món ăn được yêu thích nhất trên hệ thống:",
                        recipes,
                        "getPopularRecipes");
            }
            return new ChatResponse("Hiện tại chưa có món ăn nào được yêu thích.");
        }

        // 2. Check for trending/hot/viewed recipes
        if (containsAny(message, "hot", "trending", "nong", "xem nhieu", "nhieu view", "dang hot", "pho bien nhat",
                "thinh hanh", "xu huong")) {
            List<RecipesDTO> recipes = getTrendingRecipes(token);
            if (!recipes.isEmpty()) {
                return new ChatResponse(
                        "Đây là những món ăn đang hot nhất (nhiều lượt xem):",
                        recipes,
                        "getTrendingRecipes");
            }
            return new ChatResponse("Hiện tại chưa có món ăn nào đang hot.");
        }

        // 3. Check for ingredient-based search
        if (containsAny(message, "nguyen lieu", "co", "dung", "ingredient", "lam tu", "chua", "thanh phan")) {
            List<String> ingredients = extractIngredients(message);
            if (!ingredients.isEmpty()) {
                List<RecipesDTO> recipes = searchRecipesByIngredients(ingredients, token);
                if (!recipes.isEmpty()) {
                    return new ChatResponse(
                            "Tôi đã tìm thấy các món ăn phù hợp với nguyên liệu: " + String.join(", ", ingredients),
                            recipes,
                            "searchRecipesByIngredients");
                }
                return new ChatResponse("Xin lỗi, tôi không tìm thấy món ăn nào với nguyên liệu đó.");
            }
        }

        // 4. Check for servings-based query - ✅ NEW!
        if (containsAny(message, "nguoi an", "nguoi", "khau phan", "phan an", "cho", "serving")) {
            // Extract number of servings
            Integer servings = extractServings(message);
            if (servings != null && servings > 0) {
                // For now, suggest popular recipes with a note about servings
                List<RecipesDTO> recipes = getPopularRecipes(token);
                if (!recipes.isEmpty()) {
                    return new ChatResponse(
                            "Đây là những món phổ biến (bạn có thể điều chỉnh khẩu phần cho " + servings + " người):",
                            recipes,
                            "getPopularRecipes");
                }
            }
        }

        // 5. Check for recipe name search
        if (containsAny(message, "tim", "mon", "cong thuc", "recipe", "lam", "nau", "tim kiem", "search", "tra",
                "tra cuu")) {
            String recipeName = extractRecipeName(message);
            if (!recipeName.isEmpty()) {
                List<RecipesDTO> recipes = searchRecipesByTitle(recipeName, token);
                if (!recipes.isEmpty()) {
                    return new ChatResponse(
                            "Kết quả tìm kiếm cho '" + recipeName + "':",
                            recipes,
                            "searchRecipesByTitle");
                }
                return new ChatResponse("Xin lỗi, tôi không tìm thấy món ăn nào có tên '" + recipeName + "'.");
            }
        }

        // 5. Greeting - ✅ Personalized
        if (containsAny(message, "xin chao", "hello", "hi", "chao", "hey", "halo", "alo")) {
            String greeting = username != null && !username.isEmpty()
                    ? "Xin chào " + username + "! 👋"
                    : "Xin chào! 👋";

            return new ChatResponse(
                    greeting + " Tôi là trợ lý AI cho ứng dụng nấu ăn. Tôi có thể giúp bạn:\n" +
                            "- Tìm món ăn được yêu thích nhất\n" +
                            "- Tìm món ăn đang hot\n" +
                            "- Tìm món ăn theo nguyên liệu\n" +
                            "- Tìm công thức nấu ăn theo tên\n\n" +
                            "Bạn muốn tìm món gì hôm nay?");
        }

        // 6. Help
        if (containsAny(message, "giup", "help", "huong dan", "lam gi", "co the", "tro giup", "ho tro")) {
            return new ChatResponse(
                    "Tôi có thể giúp bạn:\n\n" +
                            "🔥 Tìm món hot: \"Cho tôi xem món nào đang hot?\"\n" +
                            "❤️ Món yêu thích: \"Món nào được yêu thích nhất?\"\n" +
                            "🥘 Tìm theo nguyên liệu: \"Tìm món có gà và khoai tây\"\n" +
                            "🔍 Tìm theo tên: \"Tìm món phở\"\n\n" +
                            "Hãy thử hỏi tôi nhé!");
        }

        // Default response - ✅ Personalized
        String defaultIntro = username != null && !username.isEmpty()
                ? "Xin lỗi " + username + ", tôi chưa hiểu câu hỏi của bạn. "
                : "Xin lỗi, tôi chưa hiểu câu hỏi của bạn. ";

        return new ChatResponse(
                defaultIntro + "Bạn có thể hỏi tôi về:\n" +
                        "- Món ăn được yêu thích nhất\n" +
                        "- Món ăn đang hot\n" +
                        "- Tìm món ăn theo nguyên liệu\n" +
                        "- Tìm công thức theo tên món\n\n" +
                        "Hoặc gõ 'giúp' để xem hướng dẫn chi tiết.");
    }

    /**
     * Check if normalized text contains any of the normalized keywords
     */
    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private List<String> extractIngredients(String message) {
        List<String> ingredients = new ArrayList<>();
        String normalized = normalize(message);

        // ✅ Stop words - những từ KHÔNG phải nguyên liệu
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "toi", "minh", "em", "anh", "ban", "chung", "ho", "la", "cua", "ma", "de", "cho",
                "voi", "hay", "nao", "gi", "sao", "the", "nhung", "va", "hoac", "neu", "thi"));

        // Common Vietnamese ingredients (normalized - no accents)
        Map<String, String> ingredientMap = new HashMap<>();

        // 🍗 Thịt (Meat)
        ingredientMap.put("thit ga", "thịt gà");
        ingredientMap.put("thit bo", "thịt bò");
        ingredientMap.put("thit heo", "thịt heo");
        ingredientMap.put("thit lon", "thịt lợn");
        ingredientMap.put("thit", "thịt"); // ✅ Thêm "thịt" riêng
        ingredientMap.put("ga", "gà");
        ingredientMap.put("bo", "bò");
        ingredientMap.put("heo", "heo");

        // 🐟 Hải sản (Seafood)
        ingredientMap.put("ca", "cá");
        ingredientMap.put("tom", "tôm");
        ingredientMap.put("muc", "mực");

        // 🥬 Rau củ (Vegetables)
        ingredientMap.put("khoai tay", "khoai tây");
        ingredientMap.put("ca chua", "c chua");
        ingredientMap.put("ca rot", "cà rốt");
        ingredientMap.put("bap cai", "bắp cải");
        ingredientMap.put("su hao", "su hào");
        ingredientMap.put("hanh", "hành");
        ingredientMap.put("ot", "ớt");
        ingredientMap.put("rau", "rau");
        ingredientMap.put("nam", "nấm");
        ingredientMap.put("dau", "đậu");

        // 🧄 Gia vị (Spices) - ✅ Cẩn thận với "tỏi"
        // Chỉ match "tỏi" khi có từ "toi" và KHÔNG phải trong context "tôi có"
        if (normalized.contains("toi") && !normalized.matches(".*\\btoi\\s+(co|dang|muon|can|se)\\b.*")) {
            ingredientMap.put("toi", "tỏi");
        }
        ingredientMap.put("gung", "gừng");
        ingredientMap.put("sa", "sả");

        // 🥚 Khác (Others)
        ingredientMap.put("trung", "trứng");
        ingredientMap.put("sua", "sữa");
        ingredientMap.put("pho mai", "phô mai");
        ingredientMap.put("cheese", "cheese");
        ingredientMap.put("gao", "gạo");
        ingredientMap.put("bun", "bún");
        ingredientMap.put("pho", "phở");
        ingredientMap.put("mi", "mì");
        ingredientMap.put("banh mi", "bánh mì");

        // ✅ Improved matching: Check multi-word first, then single words
        // Sort by length (longest first) to match "thịt gà" before "gà"
        List<Map.Entry<String, String>> sortedEntries = new ArrayList<>(ingredientMap.entrySet());
        sortedEntries.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));

        for (Map.Entry<String, String> entry : sortedEntries) {
            String key = entry.getKey();
            String value = entry.getValue();

            // ✅ Check if ingredient is in the message
            if (normalized.contains(key)) {
                // ✅ Avoid stop words (e.g., "toi" in "tôi có gà")
                String[] keyWords = key.split("\\s+");
                boolean isStopWord = false;

                for (String word : keyWords) {
                    if (stopWords.contains(word)) {
                        // Check context - if it's in a "toi co..." pattern, skip
                        if (normalized.matches(".*\\b" + word + "\\s+(co|dang|muon|can|se|la|cua)\\b.*")) {
                            isStopWord = true;
                            break;
                        }
                    }
                }

                if (!isStopWord && !ingredients.contains(value)) {
                    ingredients.add(value);
                }
            }
        }

        return ingredients;
    }

    private String extractRecipeName(String message) {
        // ✅ Pattern 1: "tên là [món]" - highest priority
        if (message.contains("ten la")) {
            String[] parts = message.split("ten la", 2);
            if (parts.length > 1) {
                return cleanRecipeName(parts[1].trim());
            }
        }

        // ✅ Pattern 2: "tìm món [tên]"
        if (message.matches(".*\\btim\\s+mon\\b.*")) {
            String cleaned = message.replaceFirst(".*\\btim\\s+mon\\b\\s*", "");
            return cleanRecipeName(cleaned);
        }

        // ✅ Pattern 3: "tôi muốn [món]"
        if (message.matches(".*\\btoi\\s+muon\\b.*")) {
            String cleaned = message.replaceFirst(".*\\btoi\\s+muon\\b\\s*(tim|nau|lam|mon|an)?\\s*", "");
            cleaned = cleaned.replaceFirst("\\s*ten\\s+la\\s*", " ");
            return cleanRecipeName(cleaned);
        }

        // Default: remove stop words
        String cleaned = message
                .replaceAll("\\btim\\b", "")
                .replaceAll("\\bmon\\b", "")
                .replaceAll("\\bcong\\s+thuc\\b", "")
                .replaceAll("\\blam\\b", "")
                .replaceAll("\\bnau\\b", "")
                .replaceAll("\\bcho\\s+toi\\b", "")
                .replaceAll("\\s+", " ")
                .trim();

        return cleanRecipeName(cleaned);
    }

    private String cleanRecipeName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        name = name.replaceAll("^[\\s,.:;?!]+|[\\s,.:;?!]+$", "");

        if (name.length() < 2) {
            return "";
        }

        name = name.replaceAll("\\s+(khong|nao|gi|co)\\s*$", "");
        name = name.trim();

        return name.length() >= 2 ? name : "";
    }

    /**
     * Extract number of servings from message
     * e.g., "món cho 3 người" -> 3
     */
    private Integer extractServings(String message) {
        // Pattern: số + người/khẩu phần
        // Examples: "3 người", "cho 5 người ăn", "2 khẩu phần"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*(nguoi|khau\\s*phan|phan)");
        java.util.regex.Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    // Query functions
    private List<RecipesDTO> getPopularRecipes(String token) {
        List<Recipe> recipes = recipeRepo.popular();
        if (recipes.isEmpty()) {
            recipes = recipeRepo.findAllApproved().stream()
                    .sorted((a, b) -> Integer.compare(b.getLikeCount(), a.getLikeCount()))
                    .limit(3)
                    .collect(Collectors.toList());
        }
        return convertToDTO(recipes, token);
    }

    private List<RecipesDTO> getTrendingRecipes(String token) {
        List<Recipe> recipes = recipeRepo.trending();
        if (recipes.isEmpty()) {
            recipes = recipeRepo.findAllApproved().stream()
                    .sorted((a, b) -> Integer.compare(b.getViewCount(), a.getViewCount()))
                    .limit(3)
                    .collect(Collectors.toList());
        }
        // Giới hạn 3 công thức
        return convertToDTO(recipes.stream().limit(3).collect(Collectors.toList()), token);
    }

    private List<RecipesDTO> searchRecipesByIngredients(List<String> ingredients, String token) {
        List<Recipe> allRecipes = recipeRepo.findAllApproved();
        List<RecipeMatch> matches = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            String ingDb = normalize(recipe.getIngredients());
            int score = 0;

            for (String ing : ingredients) {
                String normalizedIng = normalize(ing);
                if (ingDb.contains(normalizedIng)) {
                    score++;
                }
            }

            if (score > 0) {
                matches.add(new RecipeMatch(recipe, score));
            }
        }

        matches.sort((a, b) -> Integer.compare(b.score, a.score));

        // Giới hạn 3 công thức
        List<Recipe> sortedRecipes = matches.stream()
                .map(m -> m.recipe)
                .limit(3)
                .collect(Collectors.toList());

        return convertToDTO(sortedRecipes, token);
    }

    private List<RecipesDTO> searchRecipesByTitle(String title, String token) {
        List<Recipe> recipes = recipeRepo.searchRecipes(title, null, null, null);
        if (recipes.isEmpty()) {
            // Fallback with normalized search
            String normalizedTitle = normalize(title);
            recipes = recipeRepo.findAllApproved().stream()
                    .filter(r -> normalize(r.getTitle()).contains(normalizedTitle))
                    .limit(3)
                    .collect(Collectors.toList());
        }
        // Giới hạn 3 công thức
        return convertToDTO(recipes.stream().limit(3).collect(Collectors.toList()), token);
    }

    private List<RecipesDTO> convertToDTO(List<Recipe> recipes, String token) {
        List<RecipesDTO> result = new ArrayList<>();

        for (Recipe recipe : recipes) {
            RecipesDTO dto = modelMapper.map(recipe, RecipesDTO.class);

            dto.setAvatarUrl(recipe.getUser().getAvatarUrl());
            dto.setUserName(recipe.getUser().getUserName());
            dto.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
            dto.setCreateAt(recipe.getCreatedAt().toLocalDate());

            if (recipe.getCategory() != null) {
                dto.setCategory(recipe.getCategory().getName());
            }

            // Tags
            Set<String> tagNames = recipe.getTags()
                    .stream()
                    .map(Tags::getName)
                    .collect(Collectors.toSet());
            dto.setTags(tagNames);

            // Ingredients
            dto.setIngredients(
                    Arrays.stream(recipe.getIngredients().split(","))
                            .map(String::trim)
                            .collect(Collectors.toList()));

            // Like + Change flag
            if (token != null) {
                String userName = jwt.extractUserName(token);
                if (userName != null) {
                    if (userName.equals(recipe.getUser().getUserName())) {
                        dto.setChange(true);
                    }
                    if (likeRepo.getCheckLikeByUser(userName, recipe.getRecipeId()) != null) {
                        dto.setLike(true);
                    }
                }
            }

            result.add(dto);
        }

        return result;
    }

    private static class RecipeMatch {
        Recipe recipe;
        int score;

        RecipeMatch(Recipe r, int s) {
            this.recipe = r;
            this.score = s;
        }
    }
}
