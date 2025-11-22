package projectCooking.Service;

import projectCooking.Model.AdminUserDTO;
import projectCooking.Model.UserListResponse;
import projectCooking.Model.UserStatisticsDTO;
import projectCooking.Request.UpdateUserRoleRequest;
import projectCooking.Request.UpdateUserStatusRequest;
import projectCooking.Request.UserFilterRequest;

public interface AdminUserService {

    /**
     * Get paginated list of users with filters
     */
    UserListResponse getUsers(String token, UserFilterRequest filter);

    /**
     * Get detailed information about a specific user
     */
    AdminUserDTO getUserDetails(String token, Integer userId);

    /**
     * Update user status (active/inactive)
     */
    String updateUserStatus(String token, Integer userId, UpdateUserStatusRequest request);

    /**
     * Update user role (USER/ADMIN)
     */
    String updateUserRole(String token, Integer userId, UpdateUserRoleRequest request);

    /**
     * Get user statistics
     */
    UserStatisticsDTO getUserStatistics(String token);
}
