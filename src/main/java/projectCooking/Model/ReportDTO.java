package projectCooking.Model;

import java.time.LocalDateTime;
import projectCooking.Repository.Entity.Report.ReportReason;
import projectCooking.Repository.Entity.Report.ReportStatus;

public class ReportDTO {

    public enum ReportType {
        USER, RECIPE
    }

    private Integer reportId;
    private Integer reporterId;
    private String reporterName;
    private ReportType reportType;
    private Integer recipeId;
    private String recipeTitle;
    private Integer reportedUserId;
    private String reportedUserName;
    private ReportReason reason;
    private String description;
    private ReportStatus status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    // Constructors
    public ReportDTO() {
    }

    public ReportDTO(Integer reportId, Integer reporterId, String reporterName,
            ReportType reportType, Integer recipeId, String recipeTitle,
            Integer reportedUserId, String reportedUserName, ReportReason reason,
            String description, ReportStatus status, String adminNote,
            LocalDateTime createdAt, LocalDateTime resolvedAt) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reportType = reportType;
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.reportedUserId = reportedUserId;
        this.reportedUserName = reportedUserName;
        this.reason = reason;
        this.description = description;
        this.status = status;
        this.adminNote = adminNote;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    // Getters and Setters
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public Integer getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Integer reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public String getReportedUserName() {
        return reportedUserName;
    }

    public void setReportedUserName(String reportedUserName) {
        this.reportedUserName = reportedUserName;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
