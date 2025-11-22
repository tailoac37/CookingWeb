package projectCooking.API;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.AdminUserDTO;
import projectCooking.Model.UserListResponse;
import projectCooking.Model.UserStatisticsDTO;
import projectCooking.Request.UpdateUserRoleRequest;
import projectCooking.Request.UpdateUserStatusRequest;
import projectCooking.Request.UserFilterRequest;
import projectCooking.Service.AdminUserService;

@RestController
public class AdminUserManagementAPI {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * Get paginated list of users with filters
     * GET /api/admin/users
     */
    @GetMapping("/api/admin/users")
    public UserListResponse getUsers(
            @RequestHeader("Authorization") String auth,
            UserFilterRequest filter) {
        String token = auth.replace("Bearer ", "").trim();
        return adminUserService.getUsers(token, filter);
    }

    /**
     * Get detailed information about a specific user
     * GET /api/admin/users/{id}
     */
    @GetMapping("/api/admin/users/{id}")
    public AdminUserDTO getUserDetails(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") Integer userId) {
        String token = auth.replace("Bearer ", "").trim();
        return adminUserService.getUserDetails(token, userId);
    }

    /**
     * Update user status (active/inactive)
     * PATCH /api/admin/users/{id}/status
     */
    @PatchMapping("/api/admin/users/{id}/status")
    public String updateUserStatus(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") Integer userId,
            @RequestBody UpdateUserStatusRequest request) {
        String token = auth.replace("Bearer ", "").trim();
        return adminUserService.updateUserStatus(token, userId, request);
    }

    /**
     * Update user role (USER/ADMIN)
     * PATCH /api/admin/users/{id}/role
     */
    @PatchMapping("/api/admin/users/{id}/role")
    public String updateUserRole(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") Integer userId,
            @RequestBody UpdateUserRoleRequest request) {
        String token = auth.replace("Bearer ", "").trim();
        return adminUserService.updateUserRole(token, userId, request);
    }

    /**
     * Get user statistics
     * GET /api/admin/users/statistics
     */
    @GetMapping("/api/admin/users/statistics")
    public UserStatisticsDTO getUserStatistics(
            @RequestHeader("Authorization") String auth) {
        String token = auth.replace("Bearer ", "").trim();
        return adminUserService.getUserStatistics(token);
    }
}
