package projectCooking.Utils;

import java.util.*;

/**
 * Smart Response Template Manager
 * Provides varied and natural responses instead of repetitive ones
 */
public class ResponseTemplateManager {

    private static final Random random = new Random();

    // Template groups for different scenarios
    private static final Map<String, List<String>> templates = new HashMap<>();

    static {
        // Popular recipes responses
        templates.put("POPULAR_SUCCESS", Arrays.asList(
                "⭐ Đây là Top {count} món ăn được yêu thích nhất:",
                "💖 {count} món ăn HOT nhất mọi người đang thích:",
                "🌟 Top {count} món được yêu thích:",
                "❤️ Những món ăn được cộng đồng yêu thích nhất ({count} món):"));

        templates.put("POPULAR_EMPTY", Arrays.asList(
                "Hmm, hiện chưa có món nào được yêu thích đặc biệt.",
                "Chưa có món được like nhiều lắm. Bạn thử xem món hot nhé!",
                "Danh sách món yêu thích đang trống. Thử hỏi món đang hot nhé!"));

        // Trending recipes responses
        templates.put("TRENDING_SUCCESS", Arrays.asList(
                "🔥 Top {count} món ăn đang HOT nhất:",
                "🌶️ {count} món đang được quan tâm nhiều nhất:",
                "📈 Món ăn trending ({count} món):",
                "🔥 Đây là những gì đang hot ({count} món):"));

        templates.put("TRENDING_EMPTY", Arrays.asList(
                "Chưa có món nào đang hot đặc biệt.",
                "Danh sách trending đang trống. Thử xem món được yêu thích nhất nhé!",
                "Hmm, hiện tại chưa có món nào hot."));

        // Difficulty responses
        templates.put("DIFFICULTY_SUCCESS", Arrays.asList(
                "📊 Món ăn mức độ {difficulty} ({count} món):",
                "👨‍🍳 {count} món {difficulty} dành cho bạn:",
                "✨ Đây là các món {difficulty} ({count} món):",
                "🎯 Tìm thấy {count} món ăn {difficulty}:"));

        // Time-based responses
        templates.put("TIME_SUCCESS", Arrays.asList(
                "⏱️ Món nấu trong {time} phút ({count} món):",
                "⚡ {count} món có thể xong trong {time} phút:",
                "🕐 Các món nấu nhanh ({time} phút, {count} món):",
                "⏰ Nhanh gọn trong {time} phút ({count} món):"));

        // Servings responses
        templates.put("SERVINGS_SUCCESS", Arrays.asList(
                "👨‍👩‍👧‍👦 Món cho {servings} người ({count} món):",
                "🍽️ {count} món phù hợp cho {servings} người:",
                "👥 Các món ăn cho {servings} người ({count} món):",
                "🍴 {count} món phù hợp với {servings} người ăn:"));

        // Ingredients responses
        templates.put("INGREDIENTS_SUCCESS", Arrays.asList(
                "🥘 Tìm thấy {count} món với {ingredients}:",
                "👨‍🍳 {count} món có {ingredients}:",
                "🍳 Các món dùng {ingredients} ({count} món):",
                "✨ {count} món ăn từ {ingredients}:"));

        // Search responses
        templates.put("SEARCH_SUCCESS", Arrays.asList(
                "🔍 Kết quả cho '{query}' ({count} món):",
                "🔎 Tìm thấy {count} món '{query}':",
                "📝 {count} món phù hợp với '{query}':",
                "✅ Kết quả tìm kiếm ({count} món):"));

        templates.put("SEARCH_EMPTY", Arrays.asList(
                "😅 Không tìm thấy món '{query}'. Thử món khác nhé!",
                "🤔 '{query}' không có trong danh sách. Bạn có thể xem món hot không?",
                "❌ Không có kết quả cho '{query}'. Thử tìm theo nguyên liệu nhé!"));

        // Greeting responses
        templates.put("GREETING", Arrays.asList(
                "{time} Tôi là trợ lý nấu ăn AI! 🍳 Tôi có thể giúp gì cho bạn?",
                "{time} Chào bạn! 🌟 Tìm món gì ngon hôm nay không?",
                "{time} Hey! 👋 Sẵn sàng khám phá món ăn mới chưa?",
                "{time} Xin chào! 😊 Hôm nay nấu gì thế?"));

        // Thank you responses
        templates.put("THANK_YOU", Arrays.asList(
                "Không có gì! Vui được giúp bạn! 😊",
                "Luôn sẵn sàng! Cần gì cứ hỏi nhé! 🌟",
                "Rất vui! Nấu ăn vui vẻ nhé! 👨‍🍳",
                "Hehe không sao! Chúc bạn nấu ngon! 🍴"));

        // Goodbye responses
        templates.put("GOODBYE", Arrays.asList(
                "Tạm biệt! Chúc nấu ngon! 👋🍽️",
                "Bye bye! Hẹn gặp lại bạn! 😊",
                "Chào tạm biệt! Nấu ăn vui vẻ nhé! 🌟",
                "See you! Chúc thành công! 👨‍🍳"));

        // Default responses
        templates.put("DEFAULT", Arrays.asList(
                "🤔 Hmm, tôi chưa hiểu rõ. Bạn có thể hỏi cụ thể hơn không?",
                "😅 Tôi chưa nắm được ý bạn. Thử hỏi theo cách khác nhé!",
                "🙏 Xin lỗi, câu hỏi hơi khó hiểu. Gõ 'giúp' để xem hướng dẫn nhé!",
                "💭 Ơ, can you ask differently? Hoặc gõ 'giúp' để xem examples!"));
    }

    /**
     * Get a random template from a category
     */
    public static String getTemplate(String category) {
        List<String> categoryTemplates = templates.get(category);
        if (categoryTemplates == null || categoryTemplates.isEmpty()) {
            return "";
        }
        return categoryTemplates.get(random.nextInt(categoryTemplates.size()));
    }

    /**
     * Fill template with parameters
     */
    public static String fillTemplate(String template, Map<String, String> params) {
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    /**
     * Get random response for success with count
     */
    public static String getSuccessResponse(String category, int count, Map<String, String> params) {
        String template = getTemplate(category + "_SUCCESS");
        params.put("count", String.valueOf(count));
        return fillTemplate(template, params);
    }

    /**
     * Get random empty response
     */
    public static String getEmptyResponse(String category) {
        return getTemplate(category + "_EMPTY");
    }
}
