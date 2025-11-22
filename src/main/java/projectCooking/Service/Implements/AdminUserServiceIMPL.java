package projectCooking.Service.Implements;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projectCooking.Model.AdminUserDTO;
import projectCooking.Model.UserListResponse;
import projectCooking.Model.UserStatisticsDTO;
import projectCooking.Repository.CommentsRepo;
import projectCooking.Repository.FollowRepo;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.UpdateUserRoleRequest;
import projectCooking.Request.UpdateUserStatusRequest;
import projectCooking.Request.UserFilterRequest;
import projectCooking.Service.AdminActionLoggerService;
import projectCooking.Service.AdminUserService;
import projectCooking.Service.JWTService;

@Service
public class AdminUserServiceIMPL implements AdminUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RecipesRepo recipesRepo;

    @Autowired
    private FollowRepo followRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private CommentsRepo commentsRepo;

    @Autowired
    private AdminActionLoggerService actionLogger;

    @Override
    public UserListResponse getUsers(String token, UserFilterRequest filter) {
        // Verify admin role
        verifyAdminRole(token);

        // Get all users
        List<User> allUsers = userRepo.findAll();

        // Apply filters
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> {
                    // Filter by role
                    if (filter.getRole() != null && !filter.getRole().isEmpty()) {
                        if (!user.getRole().toString().equalsIgnoreCase(filter.getRole())) {
                            return false;
                        }
                    }

                    // Filter by active status
                    if (filter.getIsActive() != null) {
                        if (!user.getIsActive().equals(filter.getIsActive())) {
                            return false;
                        }
                    }

                    // Filter by email verified
                    if (filter.getEmailVerified() != null) {
                        if (!user.getEmailVerified().equals(filter.getEmailVerified())) {
                            return false;
                        }
                    }

                    // Search term
                    if (filter.getSearchTerm() != null && !filter.getSearchTerm().isEmpty()) {
                        String searchLower = filter.getSearchTerm().toLowerCase();
                        boolean matches = (user.getUserName() != null
                                && user.getUserName().toLowerCase().contains(searchLower))
                                || (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchLower))
                                || (user.getFullName() != null
                                        && user.getFullName().toLowerCase().contains(searchLower));
                        if (!matches) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());

        // Calculate pagination
        int totalUsers = filteredUsers.size();
        int totalPages = (int) Math.ceil((double) totalUsers / filter.getSize());
        int start = filter.getPage() * filter.getSize();
        int end = Math.min(start + filter.getSize(), totalUsers);

        // Get page subset
        List<User> pageUsers = filteredUsers.subList(
                Math.min(start, totalUsers),
                Math.min(end, totalUsers));

        // Convert to DTOs
        List<AdminUserDTO> userDTOs = pageUsers.stream()
                .map(this::convertToAdminUserDTO)
                .collect(Collectors.toList());

        return new UserListResponse(userDTOs, totalUsers, filter.getPage(), totalPages, filter.getSize());
    }

    @Override
    public AdminUserDTO getUserDetails(String token, Integer userId) {
        // Verify admin role
        verifyAdminRole(token);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToAdminUserDTO(user);
    }

    @Override
    @Transactional
    public String updateUserStatus(String token, Integer userId, UpdateUserStatusRequest request) {
        // Verify admin role
        verifyAdminRole(token);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent admin from deactivating themselves
        Integer adminId = jwtService.extractUserId(token);
        if (userId.equals(adminId) && !request.getIsActive()) {
            throw new RuntimeException("You cannot deactivate your own account");
        }

        user.setIsActive(request.getIsActive());
        userRepo.save(user);

        // Log action and notify admins + target user
        actionLogger.logUserStatusChange(token, user, request.getIsActive(), request.getReason());

        String status = request.getIsActive() ? "activated" : "deactivated";
        return "User " + user.getUserName() + " has been " + status + " successfully";
    }

    @Override
    @Transactional
    public String updateUserRole(String token, Integer userId, UpdateUserRoleRequest request) {
        // Verify admin role
        verifyAdminRole(token);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent admin from changing their own role
        Integer adminId = jwtService.extractUserId(token);
        if (userId.equals(adminId)) {
            throw new RuntimeException("You cannot change your own role");
        }

        // Validate role
        User.Role newRole;
        try {
            newRole = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Must be USER or ADMIN");
        }

        user.setRole(newRole);
        userRepo.save(user);

        // Log action and notify admins
        actionLogger.logUserRoleChange(token, user, newRole);

        return "User " + user.getUserName() + " role has been updated to " + newRole;
    }

    @Override
    public UserStatisticsDTO getUserStatistics(String token) {
        // Verify admin role
        verifyAdminRole(token);

        UserStatisticsDTO stats = new UserStatisticsDTO();

        List<User> allUsers = userRepo.findAll();

        stats.setTotalUsers(allUsers.size());
        stats.setActiveUsers((int) allUsers.stream().filter(User::getIsActive).count());
        stats.setInactiveUsers((int) allUsers.stream().filter(u -> !u.getIsActive()).count());
        stats.setAdminUsers((int) allUsers.stream().filter(u -> u.getRole() == User.Role.ADMIN).count());
        stats.setRegularUsers((int) allUsers.stream().filter(u -> u.getRole() == User.Role.USER).count());
        stats.setVerifiedUsers((int) allUsers.stream().filter(User::getEmailVerified).count());
        stats.setUnverifiedUsers((int) allUsers.stream().filter(u -> !u.getEmailVerified()).count());

        // Users created today
        LocalDateTime startOfToday = LocalDateTime.now().with(LocalTime.MIN);
        stats.setNewUsersToday((int) allUsers.stream()
                .filter(u -> u.getCreatedAt().isAfter(startOfToday))
                .count());

        // Users created this month
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        stats.setNewUsersThisMonth((int) allUsers.stream()
                .filter(u -> u.getCreatedAt().isAfter(startOfMonth))
                .count());

        return stats;
    }

    // Helper methods

    private void verifyAdminRole(String token) {
        String role = jwtService.extractRole(token);
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied. Admin role required.");
        }
    }

    private AdminUserDTO convertToAdminUserDTO(User user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole().toString());
        dto.setIsActive(user.getIsActive());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // Add statistics
        dto.setTotalRecipes(recipesRepo.countByUser(user));
        dto.setTotalFollowers(followRepo.countByFollowing(user));
        dto.setTotalFollowing(followRepo.countByFollower(user));
        dto.setTotalLikes(likeRepo.countByUser(user));
        dto.setTotalComments(commentsRepo.countByUser(user));

        return dto;
    }
}
