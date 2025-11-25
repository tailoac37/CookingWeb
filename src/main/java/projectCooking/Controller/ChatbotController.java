package projectCooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projectCooking.Request.ChatRequest;
import projectCooking.Response.ChatResponse;
import projectCooking.Service.ChatbotService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    /**
     * Simple chat endpoint - send a single message
     * POST /api/chat
     * Body: { "message": "Cho tôi xem món ăn nào được yêu thích nhất?" }
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = extractToken(authHeader);
            ChatResponse response = chatbotService.chat(request.getMessage(), token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            ChatResponse errorResponse = new ChatResponse("Đã có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Chat with conversation history
     * POST /api/chat/conversation
     * Body: {
     * "message": "Còn món nào khác không?",
     * "history": [
     * { "role": "user", "content": "Cho tôi xem món ăn được yêu thích nhất?" },
     * { "role": "model", "content": "Đây là những món ăn được yêu thích nhất..." }
     * ]
     * }
     */
    @PostMapping("/conversation")
    public ResponseEntity<ChatResponse> chatWithHistory(
            @RequestBody ChatRequest request,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = extractToken(authHeader);
            ChatResponse response = chatbotService.chatWithHistory(request, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            ChatResponse errorResponse = new ChatResponse("Đã có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Chatbot service is running");
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
}
