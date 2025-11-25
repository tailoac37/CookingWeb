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

        // Phân tích câu hỏi và xác định intent
        ChatResponse response = analyzeAndRespond(message, token);

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

    private ChatResponse analyzeAndRespond(String message, String token) {
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

        // 4. Check for recipe name search
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

        // 5. Greeting
        if (containsAny(message, "xin chao", "hello", "hi", "chao", "hey", "halo", "alo")) {
            return new ChatResponse(
                    "Xin chào! Tôi là trợ lý AI cho ứng dụng nấu ăn. Tôi có thể giúp bạn:\n" +
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

        // Default response
        return new ChatResponse(
                "Xin lỗi, tôi chưa hiểu câu hỏi của bạn. Bạn có thể hỏi tôi về:\n" +
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

        // Common Vietnamese ingredients (normalized - no accents)
        Map<String, String> ingredientMap = new HashMap<>();
        ingredientMap.put("ga", "gà");
        ingredientMap.put("thit bo", "thịt bò");
        ingredientMap.put("bo", "bò");
        ingredientMap.put("heo", "heo");
        ingredientMap.put("thit heo", "thịt heo");
        ingredientMap.put("ca", "cá");
        ingredientMap.put("tom", "tôm");
        ingredientMap.put("muc", "mực");
        ingredientMap.put("khoai tay", "khoai tây");
        ingredientMap.put("ca chua", "cà chua");
        ingredientMap.put("hanh", "hành");
        ingredientMap.put("toi", "tỏi");
        ingredientMap.put("ot", "ớt");
        ingredientMap.put("rau", "rau");
        ingredientMap.put("trung", "trứng");
        ingredientMap.put("sua", "sữa");
        ingredientMap.put("bo", "bơ");
        ingredientMap.put("pho mai", "phô mai");
        ingredientMap.put("cheese", "cheese");
        ingredientMap.put("gao", "gạo");
        ingredientMap.put("bun", "bún");
        ingredientMap.put("pho", "phở");
        ingredientMap.put("mi", "mì");
        ingredientMap.put("banh mi", "bánh mì");
        ingredientMap.put("nam", "nấm");
        ingredientMap.put("dau", "đậu");
        ingredientMap.put("ca rot", "cà rốt");
        ingredientMap.put("bap cai", "bắp cải");
        ingredientMap.put("su hao", "su hào");

        for (Map.Entry<String, String> entry : ingredientMap.entrySet()) {
            if (message.contains(entry.getKey())) {
                ingredients.add(entry.getValue());
            }
        }

        return ingredients;
    }

    private String extractRecipeName(String message) {
        // Remove common question words (normalized)
        String cleaned = message
                .replaceAll("tim|mon|cong thuc|recipe|lam|nau|cho toi|xem|co|khong|gi|nao|tim kiem|search", "")
                .trim();

        // If still has content, use it as recipe name
        if (cleaned.length() > 2) {
            return cleaned;
        }

        return "";
    }

    // Query functions
    private List<RecipesDTO> getPopularRecipes(String token) {
        List<Recipe> recipes = recipeRepo.popular();
        if (recipes.isEmpty()) {
            recipes = recipeRepo.findAllApproved().stream()
                    .sorted((a, b) -> Integer.compare(b.getLikeCount(), a.getLikeCount()))
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return convertToDTO(recipes, token);
    }

    private List<RecipesDTO> getTrendingRecipes(String token) {
        List<Recipe> recipes = recipeRepo.trending();
        if (recipes.isEmpty()) {
            recipes = recipeRepo.findAllApproved().stream()
                    .sorted((a, b) -> Integer.compare(b.getViewCount(), a.getViewCount()))
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return convertToDTO(recipes, token);
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

        List<Recipe> sortedRecipes = matches.stream()
                .map(m -> m.recipe)
                .limit(10)
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
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return convertToDTO(recipes, token);
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
