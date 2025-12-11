package projectCooking.API;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import projectCooking.Model.AdminChatMessageDTO;
import projectCooking.Repository.Entity.User;
import projectCooking.Repository.Entity.AdminChatMessage;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.AdminChatRepository;
import projectCooking.Service.CloudinaryService.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Controller
public class AdminChatController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminChatRepository adminChatRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private Cloudinary cloudinary;

    // WebSocket: Gửi tin nhắn
    @MessageMapping("/admin/chat")
    @SendTo("/topic/admin-chat")
    public AdminChatMessageDTO sendMessage(@Payload AdminChatMessageDTO chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        try {
            if (headerAccessor.getUser() != null) {
                String username = headerAccessor.getUser().getName();

                if (username != null) {
                    User user = userRepo.findByUserName(username);
                    if (user != null) {
                        chatMessage.setSenderUsername(user.getUserName());
                        chatMessage.setSenderFullName(user.getFullName());
                        chatMessage.setSenderAvatarUrl(user.getAvatarUrl());
                        chatMessage.setTimestamp(LocalDateTime.now());

                        // ✅ LƯU VÀO DB
                        AdminChatMessage entity = new AdminChatMessage();
                        entity.setContent(chatMessage.getContent());
                        entity.setSender(user);
                        entity.setType(chatMessage.getType() != null ? chatMessage.getType() : "CHAT");

                        // Reply info
                        if (chatMessage.getReplyToId() != null) {
                            entity.setReplyToId(chatMessage.getReplyToId());
                            entity.setReplyToName(chatMessage.getReplyToName());
                            entity.setReplyToText(chatMessage.getReplyToText());
                        }

                        AdminChatMessage saved = adminChatRepo.save(entity);
                        chatMessage.setId(saved.getId());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error in AdminChatController: " + e.getMessage());
        }

        return chatMessage;
    }

    // WebSocket: Join (không cần lưu DB)
    @MessageMapping("/admin/chat.addUser")
    @SendTo("/topic/admin-chat")
    public AdminChatMessageDTO addUser(@Payload AdminChatMessageDTO chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getUser() != null) {
            String username = headerAccessor.getUser().getName();
            User user = userRepo.findByUserName(username);
            if (user != null) {
                chatMessage.setSenderUsername(user.getUserName());
                chatMessage.setSenderFullName(user.getFullName());
                chatMessage.setSenderAvatarUrl(user.getAvatarUrl());
                chatMessage.setTimestamp(LocalDateTime.now());
                chatMessage.setType("JOIN");
            }
        }
        return chatMessage;
    }

    // ✅ API HTTP: Lấy lịch sử chat (50 tin mới nhất)
    @GetMapping("/api/admin/chat/history")
    @ResponseBody
    public List<AdminChatMessageDTO> getChatHistory() {
        List<AdminChatMessage> entities = adminChatRepo.findTop50ByOrderByCreatedAtDesc();

        // Reverse để tin cũ nhất lên đầu (cho đúng thứ tự chat)
        Collections.reverse(entities);

        return entities.stream().map(e -> {
            AdminChatMessageDTO dto = new AdminChatMessageDTO();
            dto.setId(e.getId());
            dto.setContent(e.getContent());
            dto.setTimestamp(e.getCreatedAt());
            dto.setType(e.getType());

            // Map reply
            dto.setReplyToId(e.getReplyToId());
            dto.setReplyToName(e.getReplyToName());
            dto.setReplyToText(e.getReplyToText());

            if (e.getSender() != null) {
                dto.setSenderUsername(e.getSender().getUserName());
                dto.setSenderFullName(e.getSender().getFullName());
                dto.setSenderAvatarUrl(e.getSender().getAvatarUrl());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // ✅ API Upload Image
    @PostMapping("/api/admin/chat/upload")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            return ResponseEntity.ok(Collections.singletonMap("url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
