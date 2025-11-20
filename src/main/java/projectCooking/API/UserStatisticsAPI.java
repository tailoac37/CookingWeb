package projectCooking.API;




import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import projectCooking.Model.UserStatisticsResponse;
import projectCooking.Service.JWTService;
import projectCooking.Service.UserStatisticsService;

@RestController
@RequestMapping("/api/user/statistics")
@CrossOrigin(origins = "*")
public class UserStatisticsAPI {
    
    @Autowired
    private UserStatisticsService statisticsService;
    
    @Autowired
    private JWTService jwtService;
    
    /**
     * Lấy thống kê tổng quan của user hiện tại
     */
    @GetMapping("/overview")
    public ResponseEntity<UserStatisticsResponse> getOverviewStatistics(HttpServletRequest request) {
        Integer userId = extractUserIdFromRequest(request);
        UserStatisticsResponse stats = statisticsService.getOverviewStatistics(userId);
        return ResponseEntity.ok(stats);
    }
    
    // Helper method để extract userId từ token
    private Integer extractUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtService.extractUserId(token);
        }
        throw new RuntimeException("Token not found");
    }
    
    /**
     * Lấy thống kê chi tiết về recipes
     */
    @GetMapping("/recipes")
    public ResponseEntity<?> getRecipeStatistics(HttpServletRequest request) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getRecipeStatistics(userId));
    }
    
    /**
     * Lấy thống kê theo thời gian (7 ngày, 30 ngày, 12 tháng)
     */
    @GetMapping("/timeline")
    public ResponseEntity<?> getTimelineStatistics(
            HttpServletRequest request,
            @RequestParam(defaultValue = "30") int days) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getTimelineStatistics(userId, days));
    }
    
    /**
     * Lấy top recipes của user (theo views, likes, comments)
     */
    @GetMapping("/top-recipes")
    public ResponseEntity<?> getTopRecipes(
            HttpServletRequest request,
            @RequestParam(defaultValue = "views") String sortBy,
            @RequestParam(defaultValue = "5") int limit) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getTopRecipes(userId, sortBy, limit));
    }
    
    /**
     * Lấy thống kê về engagement (likes, comments, favorites, views)
     */
    @GetMapping("/engagement")
    public ResponseEntity<?> getEngagementStatistics(HttpServletRequest request) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getEngagementStatistics(userId));
    }
    
    /**
     * Lấy thống kê về categories
     */
    @GetMapping("/categories")
    public ResponseEntity<?> getCategoryStatistics(HttpServletRequest request) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getCategoryStatistics(userId));
    }
    
    /**
     * Lấy thống kê về followers/following
     */
    @GetMapping("/social")
    public ResponseEntity<?> getSocialStatistics(HttpServletRequest request) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getSocialStatistics(userId));
    }
    
    /**
     * Lấy thống kê growth (tăng trưởng theo thời gian)
     */
    @GetMapping("/growth")
    public ResponseEntity<?> getGrowthStatistics(
            HttpServletRequest request,
            @RequestParam(defaultValue = "30") int days) {
        Integer userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getGrowthStatistics(userId, days));
    }
}
