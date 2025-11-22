package projectCooking.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projectCooking.Model.AdminActionListResponse;
import projectCooking.Request.AdminActionFilterRequest;
import projectCooking.Service.AdminActivityService;

@RestController
@RequestMapping("/api/admin/activity")
@CrossOrigin(origins = "*")
public class AdminActivityAPI {

    @Autowired
    private AdminActivityService adminActivityService;

    /**
     * Get all admin activity history with filtering
     * GET /api/admin/activity
     */
    @GetMapping
    public ResponseEntity<?> getAdminActivityHistory(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) Integer adminId,
            @RequestParam(required = false) Integer targetUserId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        try {
            // Extract token from Authorization header
            String token = auth.replace("Bearer ", "").trim();

            // Build filter request
            AdminActionFilterRequest filter = new AdminActionFilterRequest();
            filter.setActionType(actionType);
            filter.setAdminId(adminId);
            filter.setTargetUserId(targetUserId);
            filter.setSearchTerm(searchTerm);
            filter.setPage(page);
            filter.setSize(size);
            filter.setSortBy(sortBy);
            filter.setSortDirection(sortDirection);

            AdminActionListResponse response = adminActivityService.getAdminActivityHistory(token, filter);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get activity history for a specific admin
     * GET /api/admin/activity/{adminId}
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<?> getAdminActivityByAdmin(
            @RequestHeader("Authorization") String auth,
            @PathVariable Integer adminId,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        try {
            // Extract token from Authorization header
            String token = auth.replace("Bearer ", "").trim();

            // Build filter request
            AdminActionFilterRequest filter = new AdminActionFilterRequest();
            filter.setActionType(actionType);
            filter.setSearchTerm(searchTerm);
            filter.setPage(page);
            filter.setSize(size);
            filter.setSortBy(sortBy);
            filter.setSortDirection(sortDirection);

            AdminActionListResponse response = adminActivityService.getAdminActivityByAdmin(token, adminId, filter);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Get activity statistics
     * GET /api/admin/activity/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getActivityStats(@RequestHeader("Authorization") String auth) {
        try {
            // Extract token from Authorization header
            String token = auth.replace("Bearer ", "").trim();

            // Get all actions without pagination
            AdminActionFilterRequest filter = new AdminActionFilterRequest();
            filter.setSize(Integer.MAX_VALUE);

            AdminActionListResponse response = adminActivityService.getAdminActivityHistory(token, filter);

            // Build simple stats
            return ResponseEntity.ok(new Object() {
                public final int totalActions = response.getTotalActions();
                public final String message = "Total admin actions recorded";
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
