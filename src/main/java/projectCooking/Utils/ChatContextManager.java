package projectCooking.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chat Context Manager
 * Maintains conversation history per user session
 */
public class ChatContextManager {

    // Session storage (userId -> context)
    private static final Map<String, ChatContext> sessions = new ConcurrentHashMap<>();

    // Session timeout (30 minutes)
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;

    /**
     * Get or create context for a user
     */
    public static ChatContext getContext(String userId) {
        // Clean expired sessions
        cleanExpiredSessions();

        return sessions.computeIfAbsent(userId, k -> new ChatContext(userId));
    }

    /**
     * Add message to context
     */
    public static void addMessage(String userId, String role, String content) {
        ChatContext context = getContext(userId);
        context.addMessage(role, content);
    }

    /**
     * Get last query from user
     */
    public static String getLastUserQuery(String userId) {
        ChatContext context = sessions.get(userId);
        if (context != null) {
            return context.getLastUserQuery();
        }
        return null;
    }

    /**
     * Get last intent detected
     */
    public static String getLastIntent(String userId) {
        ChatContext context = sessions.get(userId);
        if (context != null) {
            return context.getLastIntent();
        }
        return null;
    }

    /**
     * Set last intent
     */
    public static void setLastIntent(String userId, String intent) {
        ChatContext context = getContext(userId);
        context.setLastIntent(intent);
    }

    /**
     * Clear context for user
     */
    public static void clearContext(String userId) {
        sessions.remove(userId);
    }

    /**
     * Clean expired sessions
     */
    private static void cleanExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> (now - entry.getValue().getLastAccessTime()) > SESSION_TIMEOUT);
    }

    /**
     * Chat Context for a single user
     */
    public static class ChatContext {
        private final String userId;
        private final List<ChatMessage> messages;
        private String lastIntent;
        private long lastAccessTime;
        private Map<String, Object> contextData;

        public ChatContext(String userId) {
            this.userId = userId;
            this.messages = new ArrayList<>();
            this.contextData = new HashMap<>();
            this.lastAccessTime = System.currentTimeMillis();
        }

        public void addMessage(String role, String content) {
            messages.add(new ChatMessage(role, content, System.currentTimeMillis()));
            this.lastAccessTime = System.currentTimeMillis();

            // Keep only last 10 messages
            if (messages.size() > 10) {
                messages.remove(0);
            }
        }

        public String getLastUserQuery() {
            for (int i = messages.size() - 1; i >= 0; i--) {
                if ("user".equals(messages.get(i).role)) {
                    return messages.get(i).content;
                }
            }
            return null;
        }

        public List<ChatMessage> getMessages() {
            return new ArrayList<>(messages);
        }

        public String getLastIntent() {
            return lastIntent;
        }

        public void setLastIntent(String intent) {
            this.lastIntent = intent;
            this.lastAccessTime = System.currentTimeMillis();
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setContextData(String key, Object value) {
            contextData.put(key, value);
        }

        public Object getContextData(String key) {
            return contextData.get(key);
        }

        public String getUserId() {
            return userId;
        }
    }

    /**
     * Chat Message
     */
    public static class ChatMessage {
        private final String role; // "user" or "assistant"
        private final String content;
        private final long timestamp;

        public ChatMessage(String role, String content, long timestamp) {
            this.role = role;
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
