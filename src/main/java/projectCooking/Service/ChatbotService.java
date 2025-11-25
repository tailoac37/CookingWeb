package projectCooking.Service;

import projectCooking.Request.ChatRequest;
import projectCooking.Response.ChatResponse;

public interface ChatbotService {
    /**
     * Send a single message to the chatbot
     * 
     * @param message User's question
     * @param token   JWT token for authentication
     * @return ChatResponse with AI's answer and optional recipe data
     */
    ChatResponse chat(String message, String token);

    /**
     * Send a message with conversation history
     * 
     * @param request ChatRequest containing message and history
     * @param token   JWT token for authentication
     * @return ChatResponse with AI's answer and optional recipe data
     */
    ChatResponse chatWithHistory(ChatRequest request, String token);
}
