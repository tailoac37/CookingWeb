package projectCooking.Service;

import projectCooking.Repository.Entity.AdminAction;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Report;
import projectCooking.Repository.Entity.User;

public interface AdminActionLoggerService {

    /**
     * Log admin action and notify all other admins
     */
    void logAction(User admin, AdminAction.ActionType actionType, User targetUser,
            Recipe recipe, Report report, String reason, String adminNote);

    /**
     * Log user status change
     */
    void logUserStatusChange(String token, User targetUser, Boolean newStatus, String reason);

    /**
     * Log user role change
     */
    void logUserRoleChange(String token, User targetUser, User.Role newRole);

    /**
     * Log user deletion
     */
    void logUserDeletion(String token, User targetUser);

    /**
     * Log recipe approval
     */
    void logRecipeApproval(String token, Recipe recipe, String adminNote);

    /**
     * Log recipe rejection
     */
    void logRecipeRejection(String token, Recipe recipe, String reason);

    /**
     * Log recipe deletion
     */
    void logRecipeDeletion(String token, Recipe recipe, String reason);
}
