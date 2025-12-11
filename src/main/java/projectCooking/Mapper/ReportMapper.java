package projectCooking.Mapper;

import projectCooking.Repository.Entity.Report;

import org.springframework.stereotype.Component;

import projectCooking.Model.ReportDTO;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.ReportRequest;

@Component
public class ReportMapper {

    /**
     * Convert Report entity to ReportDTO
     */
    public static ReportDTO toDTO(Report report) {
        if (report == null) {
            return null;
        }

        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setReason(report.getReason());
        dto.setDescription(report.getDescription());
        dto.setStatus(report.getStatus());
        dto.setAdminNote(report.getAdminNote());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setResolvedAt(report.getResolvedAt());

        // Map reporter information
        if (report.getReporter() != null) {
            dto.setReporterId(report.getReporter().getUserId());
            dto.setReporterName(report.getReporter().getFullName()); // ✅ Use fullName instead of userName
        }

        // Map recipe information (if reporting a recipe)
        if (report.getRecipe() != null) {
            dto.setRecipeId(report.getRecipe().getRecipeId());
            dto.setRecipeTitle(report.getRecipe().getTitle());
            dto.setReportType(ReportDTO.ReportType.RECIPE);
        }

        // Map reported user information (if reporting a user)
        if (report.getReportedUser() != null) {
            dto.setReportedUserId(report.getReportedUser().getUserId());
            dto.setReportedUserName(report.getReportedUser().getFullName()); // ✅ Use fullName
            dto.setReportType(ReportDTO.ReportType.USER);
        }

        return dto;
    }

    /**
     * Convert ReportRequest to Report entity
     * Note: You need to fetch User, Recipe entities from repositories
     */
    public Report toEntity(ReportRequest request, User reporter,
            Recipe recipe, User reportedUser) {
        if (request == null) {
            return null;
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setRecipe(recipe);
        report.setReportedUser(reportedUser);
        report.setReason(request.getReason());
        report.setDescription(request.getDescription());

        return report;
    }

    /**
     * Update existing Report entity with ReportRequest data
     */
    public static void updateEntityFromRequest(Report report, ReportRequest request,
            User reporter, Recipe recipe, User reportedUser) {
        if (report == null || request == null) {
            return;
        }

        if (reporter != null) {
            report.setReporter(reporter);
        }
        if (recipe != null) {
            report.setRecipe(recipe);
        }
        if (reportedUser != null) {
            report.setReportedUser(reportedUser);
        }
        report.setReason(request.getReason());
        report.setDescription(request.getDescription());
    }
}