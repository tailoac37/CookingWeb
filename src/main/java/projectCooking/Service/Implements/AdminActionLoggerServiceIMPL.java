package projectCooking.Service.Implements;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projectCooking.Repository.AdminActionRepo;
import projectCooking.Repository.NotificationRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.AdminAction;
import projectCooking.Repository.Entity.Notification;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Report;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.AdminActionLoggerService;
import projectCooking.Service.JWTService;

@Service
public class AdminActionLoggerServiceIMPL implements AdminActionLoggerService {

        @Autowired
        private AdminActionRepo adminActionRepo;

        @Autowired
        private NotificationRepo notificationRepo;

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private JWTService jwtService;

        @Override
        @Transactional
        public void logAction(User admin, AdminAction.ActionType actionType, User targetUser,
                        Recipe recipe, Report report, String reason, String adminNote) {
                // Save admin action
                AdminAction action = new AdminAction();
                action.setAdmin(admin);
                action.setActionType(actionType);
                action.setTargetUser(targetUser);
                action.setRecipe(recipe);
                action.setRelatedReport(report);
                action.setReason(reason);
                action.setAdminNote(adminNote);
                adminActionRepo.save(action);

                // Notify all other admins
                notifyAllAdmins(admin, actionType, targetUser, recipe, reason);
        }

        @Override
        @Transactional
        public void logUserStatusChange(String token, User targetUser, Boolean newStatus, String reason) {
                Integer adminId = jwtService.extractUserId(token);
                User admin = userRepo.findById(adminId)
                                .orElseThrow(() -> new RuntimeException("Admin not found"));

                String statusText = newStatus ? "kích hoạt" : "vô hiệu hóa";
                String message = String.format("Quản trị viên %s đã %s người dùng %s",
                                admin.getUserName(), statusText, targetUser.getUserName());

                logAction(admin, AdminAction.ActionType.USER_STATUS_CHANGE, targetUser,
                                null, null, reason, message);

                // Notify the affected user
                notifyTargetUser(targetUser, "Trạng thái tài khoản thay đổi",
                                String.format("Tài khoản của bạn đã bị %s bởi quản trị viên. Lý do: %s",
                                                statusText, reason != null ? reason : "Không có"));
        }

        @Override
        @Transactional
        public void logUserRoleChange(String token, User targetUser, User.Role newRole) {
                Integer adminId = jwtService.extractUserId(token);
                User admin = userRepo.findById(adminId)
                                .orElseThrow(() -> new RuntimeException("Admin not found"));

                String message = String.format("Quản trị viên %s đã thay đổi vai trò của người dùng %s thành %s",
                                admin.getUserName(), targetUser.getUserName(), newRole);

                logAction(admin, AdminAction.ActionType.USER_ROLE_CHANGE, targetUser,
                                null, null, null, message);
        }

        @Override
        @Transactional
        public void logUserDeletion(String token, User targetUser) {
                Integer adminId = jwtService.extractUserId(token);
                User admin = userRepo.findById(adminId)
                                .orElseThrow(() -> new RuntimeException("Admin not found"));

                String message = String.format("Quản trị viên %s đã vô hiệu hóa người dùng %s",
                                admin.getUserName(), targetUser.getUserName());

                logAction(admin, AdminAction.ActionType.USER_DELETE, targetUser,
                                null, null, null, message);

                // Notify the affected user
                notifyTargetUser(targetUser, "Tài khoản bị vô hiệu hóa",
                                "Tài khoản của bạn đã bị vô hiệu hóa bởi quản trị viên. Vui lòng liên hệ hỗ trợ để biết thêm chi tiết.");
        }

        @Override
        @Transactional
        public void logRecipeApproval(String token, Recipe recipe, String adminNote) {
                Integer adminId = jwtService.extractUserId(token);
                User admin = userRepo.findById(adminId)
                                .orElseThrow(() -> new RuntimeException("Admin not found"));

                logAction(admin, AdminAction.ActionType.APPROVE, null,
                                recipe, null, null, adminNote);

                // Notify the recipe owner with recipe ID
                notifyTargetUser(recipe.getUser(), "Công thức đã được duyệt",
                                String.format("Công thức '%s' của bạn đã được duyệt bởi quản trị viên!",
                                                recipe.getTitle()),
                                recipe);
        }

        @Override
        @Transactional
        public void logRecipeRejection(String token, Recipe recipe, String reason) {
                Integer adminId = jwtService.extractUserId(token);
                User admin = userRepo.findById(adminId)
                                .orElseThrow(() -> new RuntimeException("Admin not found"));

                String message = String.format("Quản trị viên %s đã từ chối công thức: %s",
                                admin.getUserName(), recipe.getTitle());

                logAction(admin, AdminAction.ActionType.REJECT, null,
                                recipe, null, reason, message);

                // Notify the recipe owner with recipe ID
                notifyTargetUser(recipe.getUser(), "Công thức bị từ chối",
                                String.format("Công thức '%s' của bạn đã bị từ chối. Lý do: %s",
                                                recipe.getTitle(), reason != null ? reason : "Không có"),
                                recipe);
        }

        @Override
        @Transactional
        public void logRecipeDeletion(String token, Recipe recipe, String reason) {
                Integer adminId = jwtService.extractUserId(token);
                User admin = userRepo.findById(adminId)
                                .orElseThrow(() -> new RuntimeException("Admin not found"));

                String message = String.format("Quản trị viên %s đã xóa công thức: %s",
                                admin.getUserName(), recipe.getTitle());

                logAction(admin, AdminAction.ActionType.DELETE, null,
                                recipe, null, reason, message);

                // Notify the recipe owner with recipe ID
                notifyTargetUser(recipe.getUser(), "Công thức bị xóa",
                                String.format("Công thức '%s' của bạn đã bị xóa bởi quản trị viên. Lý do: %s",
                                                recipe.getTitle(), reason != null ? reason : "Không có"),
                                recipe);
        }

        /**
         * Send notification to all admins except the one who performed the action
         */
        private void notifyAllAdmins(User performingAdmin, AdminAction.ActionType actionType,
                        User targetUser, Recipe recipe, String reason) {
                // Get all admin users
                List<User> allAdmins = userRepo.findByRole(User.Role.ADMIN);

                for (User admin : allAdmins) {
                        // Don't notify the admin who performed the action
                        if (admin.getUserId().equals(performingAdmin.getUserId())) {
                                continue;
                        }

                        // Create notification
                        Notification notification = new Notification();
                        notification.setUser(admin);
                        notification.setType(Notification.NotificationType.ADMIN_MESSAGE);
                        notification.setTitle(getNotificationTitle(actionType));
                        notification.setMessage(getNotificationMessage(performingAdmin, actionType, targetUser, recipe,
                                        reason));
                        notification.setRelatedUser(targetUser);
                        notification.setRelatedRecipe(recipe);
                        notification.setIsRead(false);
                        notification.setCreatedAt(LocalDate.now());

                        notificationRepo.save(notification);
                }
        }

        /**
         * Send notification to the target user (without recipe)
         */
        private void notifyTargetUser(User targetUser, String title, String message) {
                notifyTargetUser(targetUser, title, message, null);
        }

        /**
         * Send notification to the target user with related recipe
         */
        private void notifyTargetUser(User targetUser, String title, String message, Recipe recipe) {
                Notification notification = new Notification();
                notification.setUser(targetUser);
                notification.setType(Notification.NotificationType.ADMIN_MESSAGE);
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setRelatedRecipe(recipe);
                notification.setIsRead(false);
                notification.setCreatedAt(LocalDate.now());

                notificationRepo.save(notification);
        }

        private String getNotificationTitle(AdminAction.ActionType actionType) {
                switch (actionType) {
                        case USER_STATUS_CHANGE:
                                return "Thay đổi trạng thái người dùng";
                        case USER_ROLE_CHANGE:
                                return "Thay đổi vai trò người dùng";
                        case USER_DELETE:
                                return "Người dùng bị vô hiệu hóa";
                        case APPROVE:
                                return "Công thức đã được duyệt";
                        case REJECT:
                                return "Công thức bị từ chối";
                        case DELETE:
                                return "Nội dung bị xóa";
                        default:
                                return "Hành động quản trị";
                }
        }

        private String getNotificationMessage(User performingAdmin, AdminAction.ActionType actionType,
                        User targetUser, Recipe recipe, String reason) {
                String adminName = performingAdmin.getUserName();

                switch (actionType) {
                        case USER_STATUS_CHANGE:
                                return String.format("Quản trị viên %s đã thay đổi trạng thái người dùng %s. Lý do: %s",
                                                adminName, targetUser.getUserName(),
                                                reason != null ? reason : "Không có");

                        case USER_ROLE_CHANGE:
                                return String.format("Quản trị viên %s đã thay đổi vai trò của người dùng %s thành %s",
                                                adminName, targetUser.getUserName(), targetUser.getRole());

                        case USER_DELETE:
                                return String.format("Quản trị viên %s đã vô hiệu hóa người dùng %s",
                                                adminName, targetUser.getUserName());

                        case APPROVE:
                                return String.format("Quản trị viên %s đã duyệt công thức: %s",
                                                adminName, recipe != null ? recipe.getTitle() : "Không xác định");

                        case REJECT:
                                return String.format("Quản trị viên %s đã từ chối công thức: %s. Lý do: %s",
                                                adminName, recipe != null ? recipe.getTitle() : "Không xác định",
                                                reason != null ? reason : "Không có");

                        case DELETE:
                                String target = recipe != null ? "công thức: " + recipe.getTitle()
                                                : targetUser != null ? "người dùng: " + targetUser.getUserName()
                                                                : "nội dung";
                                return String.format("Quản trị viên %s đã xóa %s", adminName, target);

                        default:
                                return String.format("Quản trị viên %s đã thực hiện một hành động", adminName);
                }
        }
}
