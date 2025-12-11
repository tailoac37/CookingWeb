package projectCooking.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.NotificationDTO;
import projectCooking.Repository.NotificationRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Notification;
import projectCooking.Repository.Entity.Notification.NotificationType;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Report;
import projectCooking.Repository.Entity.User;

@Service

public class NotificationService {

    private final NotificationRepo notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    JWTService jwt;
    @Autowired
    UserRepo userRepo;

    @Autowired
    public NotificationService(NotificationRepo notificationRepository,
            SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendLikeNotification(User sender, User receiver, Recipe recipe) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;

        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.LIKE);
        notif.setTitle("Bài viết của bạn được thích!");
        notif.setMessage(sender.getFullName() + " đã thích bài viết của bạn.");
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        System.out.println("📨 [NOTIF] Send to: " + receiver.getUserName());
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void sendCommentsNotification(User sender, User receiver, Recipe recipe, String content) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.COMMENT);
        notif.setTitle("Bài viết của bạn được comments!");
        notif.setMessage(sender.getFullName() + " đã comment bài viết của bạn: " + content);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void ReplyCommentsNotification(User sender, User receiver, Recipe recipe, String content) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.COMMENT);
        notif.setTitle(sender.getFullName() + " đã phản hồi comment của bạn  ");
        notif.setMessage(sender.getFullName() + " đã trả lời comment của bạn: " + content);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void ViewRecipesNotification(User sender, User receiver, Recipe recipe) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.VIEW);
        notif.setTitle(sender.getFullName() + " đã xem bài viết của bạn  ");
        notif.setMessage(sender.getFullName() + "đã xem bài viết của bạn ");
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void FollowNotification(User sender, User receiver) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setType(NotificationType.FOLLOW);
        notif.setTitle(sender.getFullName() + " đã theo dõi bạn ");
        notif.setMessage(sender.getFullName() + "đã theo dõi bạn ");
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public List<NotificationDTO> getListNotification(String token) {
        String userName = jwt.extractUserName(token);
        User user = userRepo.findByUserName(userName);
        if (user == null) {
            throw new DulicateUserException("dang nhap lai di ");
        }
        List<Notification> notification = notificationRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId());
        List<NotificationDTO> notificationDTO = new ArrayList<>();
        for (Notification item : notification) {
            NotificationDTO DTO = new NotificationDTO(item);
            notificationDTO.add(DTO);
        }
        return notificationDTO;

    }

    public List<NotificationDTO> getListNotificationUnread(String token) {
        String userName = jwt.extractUserName(token);
        User user = userRepo.findByUserName(userName);
        if (user == null) {
            throw new DulicateUserException("dang nhap lai di ");
        }
        List<Notification> notification = notificationRepository
                .findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getUserId());
        List<NotificationDTO> notificationDTO = new ArrayList<>();
        for (Notification item : notification) {
            NotificationDTO DTO = new NotificationDTO(item);
            notificationDTO.add(DTO);
        }
        return notificationDTO;

    }

    public String changeStatusNotification(String token, Integer Id) {

        String userName = jwt.extractUserName(token);
        User user = userRepo.findByUserName(userName);
        if (user == null) {
            return "dang nhap lai di";
        }
        Notification notif = notificationRepository.findById(Id).orElse(null);
        if (notif == null) {
            return "bai viet nay co the da bi xoa roi ";
        }
        if (!notif.getUser().getUserName().equals(user.getUserName())) {
            return "day la thong bao cua nguoi khac , khong phai thong bao cua ban";
        }
        notif.setIsRead(true);
        notificationRepository.save(notif);
        return "done";
    }

    public String delNotificationById(String token, Integer Id) {
        String userName = jwt.extractUserName(token);
        User user = userRepo.findByUserName(userName);
        if (user == null) {
            return "dang nhap lai di";
        }
        Notification notif = notificationRepository.findById(Id).orElse(null);
        if (notif == null) {
            return "bai viet nay co the da bi xoa roi ";
        }
        if (!notif.getUser().getUserName().equals(user.getUserName())) {
            return "day la thong bao cua nguoi khac , khong phai thong bao cua ban";
        }
        notificationRepository.delete(notif);
        return "done ";
    }

    public String delAllNotification(String token) {
        String userName = jwt.extractUserName(token);
        User user = userRepo.findByUserName(userName);
        if (user == null) {
            return "dang nhap lai di";
        }
        notificationRepository.deleteNotificationsByUserId(user.getUserId());
        return "done";
    }

    public void RatingNotification(User sender, User receiver, Recipe recipe, String contents) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.RATE);
        notif.setTitle(sender.getFullName() + " đã đánh giá bài viết của bạn  ");
        notif.setMessage(sender.getFullName() + ":  " + contents);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void delRatingNotification(User sender, User receiver, Recipe recipe, String content) {
        if (sender.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        notif.setRelatedUser(sender);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.RATE);
        notif.setTitle("Chủ công thức: " + recipe.getTitle() + "đã xóa bài đánh giá của bạn ");
        notif.setMessage(sender.getFullName() + ": " + content);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void sendReportNotificationToAdmins(User reporter, Recipe recipe, User reportedUser, String reportReason) {

        List<User> admins = userRepo.findByRole(User.Role.ADMIN);

        String title = "Báo cáo mới từ " + reporter.getFullName();
        String message = "";

        if (recipe != null) {
            message = reporter.getFullName() + " đã báo cáo công thức: " + recipe.getTitle() +
                    " với lý do: " + reportReason;
        } else if (reportedUser != null) {
            message = reporter.getFullName() + " đã báo cáo người dùng: " + reportedUser.getFullName() +
                    " với lý do: " + reportReason;
        }

        for (User admin : admins) {
            Notification notif = new Notification();
            notif.setUser(admin);
            notif.setRelatedUser(reporter);

            if (recipe != null) {
                notif.setRelatedRecipe(recipe);
            }

            notif.setType(NotificationType.REPORT);
            notif.setTitle(title);
            notif.setMessage(message);
            notif.setCreatedAt(LocalDate.now());
            notif.setIsRead(false);

            NotificationDTO notifDTO = new NotificationDTO(notif);
            notificationRepository.save(notif);

            messagingTemplate.convertAndSendToUser(
                    admin.getUserName(),
                    "/queue/notifications",
                    notifDTO);
        }
    }

    public void AdminSendReporterNotification(User admin, User receiver, Recipe recipe, String content, User reported,
            String reason) {
        if (admin.getUserId().equals(receiver.getUserId()))
            return;
        Notification notif = new Notification();
        notif.setUser(receiver);
        String report = "";
        if (reported != null) {
            notif.setRelatedUser(reported);
            report = "Người dùng " + reported.getUserName();
        } else if (recipe != null) {
            notif.setRelatedRecipe(recipe);
            report = "Bài viết " + recipe.getTitle();
        }

        notif.setType(NotificationType.ADMIN_MESSAGE);
        notif.setTitle("ADMIN " + "Đã phản hồi báo cáo của bạn về " + report + " với lý do " + reason);
        notif.setMessage("ADMIN " + ": " + content);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                receiver.getUserName(),
                "/queue/notifications",
                notifDTO);
    }

    public void AdminSendReportedNotification(User admin, Recipe recipe, String content, User reported, String reason) {
        if (reported != null) {
            if (admin.getUserId().equals(reported.getUserId()))
                return;
        }

        Notification notif = new Notification();
        String userName = "";
        if (reported != null) {
            notif.setUser(reported);

            userName = reported.getUserName();
        } else if (recipe != null) {
            notif.setUser(recipe.getUser());
            userName = recipe.getUser().getUserName();
        }

        notif.setType(NotificationType.ADMIN_MESSAGE);
        notif.setTitle("ADMIN " + "đã cảnh cáo bạn về việc " + reason);
        notif.setMessage("ADMIN " + ": " + content);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);
        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);
        messagingTemplate.convertAndSendToUser(
                userName,
                "/queue/notifications",
                notifDTO);
    }

    public void sendResolveNotificationToAdmins(User solver, Recipe recipe, User reportedUser, String reportReason,
            Report report) {

        List<User> admins = userRepo.findByRole(User.Role.ADMIN);

        String title = "Báo cáo mới từ admin tên là:  " + solver.getFullName();
        String message = "";

        if (recipe != null) {
            message = solver.getFullName() + " đã xử lí báo cáo của bài viết:  " + recipe.getTitle() +
                    " với lý do: " + report.getAdminNote();
        } else if (reportedUser != null) {
            message = solver.getFullName() + " đã xử lí báo cáo của người dùng:  " + reportedUser.getFullName() +
                    " với lý do: " + report.getAdminNote();
        }

        for (User admin : admins) {
            if (solver.getUserId().equals(admin.getUserId()))
                continue;
            Notification notif = new Notification();
            notif.setUser(admin);
            notif.setRelatedUser(solver);

            if (recipe != null) {
                notif.setRelatedRecipe(recipe);
            }

            notif.setType(NotificationType.ADMIN_MESSAGE);
            notif.setTitle(title);
            notif.setMessage(message);
            notif.setCreatedAt(LocalDate.now());
            notif.setIsRead(false);

            NotificationDTO notifDTO = new NotificationDTO(notif);
            notificationRepository.save(notif);

            messagingTemplate.convertAndSendToUser(
                    admin.getUserName(),
                    "/queue/notifications",
                    notifDTO);
        }
    }

    public void sendChangeNotificationToAdmins(User reporter, String message) {

        List<User> admins = userRepo.findByRole(User.Role.ADMIN);

        String title = "Báo cáo mới từ " + reporter.getFullName();
        for (User admin : admins) {
            Notification notif = new Notification();
            notif.setUser(admin);
            notif.setRelatedUser(reporter);

            notif.setType(NotificationType.CATEGORIES);
            notif.setTitle(title);
            notif.setMessage(message);
            notif.setCreatedAt(LocalDate.now());
            notif.setIsRead(false);

            NotificationDTO notifDTO = new NotificationDTO(notif);
            notificationRepository.save(notif);

            messagingTemplate.convertAndSendToUser(
                    admin.getUserName(),
                    "/queue/notifications",
                    notifDTO);
        }
    }

    /**
     * Send admin notification for recipe approval/rejection
     */
    public void sendAdminRecipeNotification(User recipeOwner, Recipe recipe, String title, String message) {
        if (recipeOwner == null || recipe == null)
            return;

        Notification notif = new Notification();
        notif.setUser(recipeOwner);
        notif.setRelatedRecipe(recipe);
        notif.setType(NotificationType.ADMIN_MESSAGE);
        notif.setTitle(title);
        notif.setMessage(message);
        notif.setCreatedAt(LocalDate.now());
        notif.setIsRead(false);

        NotificationDTO notifDTO = new NotificationDTO(notif);
        notificationRepository.save(notif);

        System.out.println("📨 [ADMIN NOTIF] Send to: " + recipeOwner.getUserName());
        messagingTemplate.convertAndSendToUser(
                recipeOwner.getUserName(),
                "/queue/notifications",
                notifDTO);
    }
}
