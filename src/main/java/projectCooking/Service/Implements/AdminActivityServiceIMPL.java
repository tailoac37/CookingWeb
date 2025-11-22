package projectCooking.Service.Implements;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Model.AdminActionDTO;
import projectCooking.Model.AdminActionListResponse;
import projectCooking.Repository.AdminActionRepo;
import projectCooking.Repository.Entity.AdminAction;
import projectCooking.Request.AdminActionFilterRequest;
import projectCooking.Service.AdminActivityService;
import projectCooking.Service.JWTService;

@Service
public class AdminActivityServiceIMPL implements AdminActivityService {

    @Autowired
    private AdminActionRepo adminActionRepo;

    @Autowired
    private JWTService jwtService;

    @Override
    public AdminActionListResponse getAdminActivityHistory(String token, AdminActionFilterRequest filter) {
        // Verify admin role
        verifyAdminRole(token);

        final AdminActionFilterRequest finalFilter = (filter == null) ? new AdminActionFilterRequest() : filter;

        // Get all admin actions
        List<AdminAction> allActions = adminActionRepo.findAll();

        // Apply filters
        List<AdminAction> filteredActions = applyFilters(allActions, finalFilter);

        // Sort
        filteredActions = sortActions(filteredActions, finalFilter);

        // Paginate
        return paginateAndConvert(filteredActions, finalFilter);
    }

    @Override
    public AdminActionListResponse getAdminActivityByAdmin(String token, Integer adminId,
            AdminActionFilterRequest filter) {
        // Verify admin role
        verifyAdminRole(token);

        final AdminActionFilterRequest finalFilter = (filter == null) ? new AdminActionFilterRequest() : filter;

        // Get actions by specific admin
        List<AdminAction> allActions = adminActionRepo.findAll().stream()
                .filter(action -> action.getAdmin() != null && action.getAdmin().getUserId().equals(adminId))
                .collect(Collectors.toList());

        // Apply filters
        List<AdminAction> filteredActions = applyFilters(allActions, finalFilter);

        // Sort
        filteredActions = sortActions(filteredActions, finalFilter);

        // Paginate
        return paginateAndConvert(filteredActions, finalFilter);
    }

    // Helper methods

    private List<AdminAction> applyFilters(List<AdminAction> actions, AdminActionFilterRequest filter) {
        return actions.stream()
                .filter(action -> {
                    if (action == null)
                        return false;

                    // Filter by action type
                    if (filter.getActionType() != null && !filter.getActionType().isEmpty()) {
                        if (action.getActionType() == null
                                || !action.getActionType().toString().equalsIgnoreCase(filter.getActionType())) {
                            return false;
                        }
                    }

                    // Filter by admin ID
                    if (filter.getAdminId() != null) {
                        if (action.getAdmin() == null || !action.getAdmin().getUserId().equals(filter.getAdminId())) {
                            return false;
                        }
                    }

                    // Filter by target user ID
                    if (filter.getTargetUserId() != null) {
                        if (action.getTargetUser() == null ||
                                !action.getTargetUser().getUserId().equals(filter.getTargetUserId())) {
                            return false;
                        }
                    }

                    // Filter by date range
                    if (filter.getStartDate() != null) {
                        if (action.getCreatedAt() == null || action.getCreatedAt().isBefore(filter.getStartDate())) {
                            return false;
                        }
                    }

                    if (filter.getEndDate() != null) {
                        if (action.getCreatedAt() == null || action.getCreatedAt().isAfter(filter.getEndDate())) {
                            return false;
                        }
                    }

                    // Search in admin notes
                    if (filter.getSearchTerm() != null && !filter.getSearchTerm().isEmpty()) {
                        String searchLower = filter.getSearchTerm().toLowerCase();
                        String adminNote = action.getAdminNote() != null ? action.getAdminNote().toLowerCase() : "";
                        String reason = action.getReason() != null ? action.getReason().toLowerCase() : "";

                        if (!adminNote.contains(searchLower) && !reason.contains(searchLower)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<AdminAction> sortActions(List<AdminAction> actions, AdminActionFilterRequest filter) {
        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "createdAt";
        boolean ascending = "ASC".equalsIgnoreCase(filter.getSortDirection());

        return actions.stream()
                .sorted((a1, a2) -> {
                    int comparison = 0;

                    switch (sortBy) {
                        case "createdAt":
                            if (a1.getCreatedAt() == null)
                                comparison = -1;
                            else if (a2.getCreatedAt() == null)
                                comparison = 1;
                            else
                                comparison = a1.getCreatedAt().compareTo(a2.getCreatedAt());
                            break;
                        case "actionType":
                            String type1 = a1.getActionType() != null ? a1.getActionType().toString() : "";
                            String type2 = a2.getActionType() != null ? a2.getActionType().toString() : "";
                            comparison = type1.compareTo(type2);
                            break;
                        case "adminUserName":
                            String name1 = (a1.getAdmin() != null && a1.getAdmin().getUserName() != null)
                                    ? a1.getAdmin().getUserName()
                                    : "";
                            String name2 = (a2.getAdmin() != null && a2.getAdmin().getUserName() != null)
                                    ? a2.getAdmin().getUserName()
                                    : "";
                            comparison = name1.compareTo(name2);
                            break;
                        default:
                            if (a1.getCreatedAt() == null)
                                comparison = -1;
                            else if (a2.getCreatedAt() == null)
                                comparison = 1;
                            else
                                comparison = a1.getCreatedAt().compareTo(a2.getCreatedAt());
                    }

                    return ascending ? comparison : -comparison;
                })
                .collect(Collectors.toList());
    }

    private AdminActionListResponse paginateAndConvert(List<AdminAction> actions, AdminActionFilterRequest filter) {
        int totalActions = actions.size();
        int size = filter.getSize() > 0 ? filter.getSize() : 20;
        int totalPages = (int) Math.ceil((double) totalActions / size);
        int start = filter.getPage() * size;
        int end = Math.min(start + size, totalActions);

        // Get page subset
        List<AdminAction> pageActions;
        if (start >= totalActions) {
            pageActions = Collections.emptyList();
        } else {
            pageActions = actions.subList(start, end);
        }

        // Convert to DTOs
        List<AdminActionDTO> actionDTOs = pageActions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new AdminActionListResponse(actionDTOs, totalActions, filter.getPage(), totalPages, size);
    }

    private AdminActionDTO convertToDTO(AdminAction action) {
        AdminActionDTO dto = new AdminActionDTO();
        dto.setActionId(action.getActionId());
        dto.setActionType(action.getActionType() != null ? action.getActionType().toString() : "UNKNOWN");

        if (action.getAdmin() != null) {
            dto.setAdminUserName(action.getAdmin().getUserName());
            dto.setAdminEmail(action.getAdmin().getEmail());
        } else {
            dto.setAdminUserName("Unknown");
        }

        if (action.getTargetUser() != null) {
            dto.setTargetUserName(action.getTargetUser().getUserName());
            dto.setTargetUserEmail(action.getTargetUser().getEmail());
        }

        if (action.getRecipe() != null) {
            dto.setRecipeTitle(action.getRecipe().getTitle());
        }

        dto.setAdminNote(action.getAdminNote());
        dto.setCreatedAt(action.getCreatedAt());

        return dto;
    }

    private void verifyAdminRole(String token) {
        String role = jwtService.extractRole(token);
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied. Admin role required.");
        }
    }
}
