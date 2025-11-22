package projectCooking.Service;

import projectCooking.Model.AdminActionListResponse;
import projectCooking.Request.AdminActionFilterRequest;

public interface AdminActivityService {

    /**
     * Get admin activity history with filtering and pagination
     * 
     * @param token  Admin JWT token
     * @param filter Filter criteria
     * @return Paginated list of admin actions
     */
    AdminActionListResponse getAdminActivityHistory(String token, AdminActionFilterRequest filter);

    /**
     * Get activity history for a specific admin
     * 
     * @param token   Admin JWT token
     * @param adminId ID of the admin to get history for
     * @param filter  Filter criteria
     * @return Paginated list of admin actions
     */
    AdminActionListResponse getAdminActivityByAdmin(String token, Integer adminId, AdminActionFilterRequest filter);
}
