package projectCooking.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import projectCooking.Model.NotificationDTO;
import projectCooking.Repository.NotificationRepo;
import projectCooking.Repository.Entity.Notification;
import projectCooking.Repository.Entity.Notification.NotificationType;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.User;

@Service

public class NotificationService {

    private final NotificationRepo notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public NotificationService(NotificationRepo notificationRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }
    public void sendLikeNotification(User sender, User receiver, Recipe recipe) {
        if (sender.getUserId().equals(receiver.getUserId())) return;

        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.LIKE);
        notif.setTitle("Bài viết của bạn được thích!");
        notif.setMessage(sender.getFullName() + " đã thích bài viết của bạn.");
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif) ; 
        notificationRepository.save(notif);
        System.out.println("📨 [NOTIF] Send to: " + receiver.getUserName());
        messagingTemplate.convertAndSendToUser(
            receiver.getUserName(), 
            "/queue/notifications",
            notifDTO
        );
    }
}
