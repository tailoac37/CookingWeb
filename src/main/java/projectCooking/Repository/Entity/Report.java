package projectCooking.Repository.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
@Table(name = "reports")

public class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reportedUser;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name ="status")
    private ReportStatus status = ReportStatus.PENDING;
    
    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    
    public enum ReportReason {
        SPAM, INAPPROPRIATE, COPYRIGHT, FAKE, OTHER
    }
    
    public enum ReportStatus {
        PENDING, REVIEWED, RESOLVED, REJECTED
    }

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public User getReporter() {
		return reporter;
	}

	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public User getReportedUser() {
		return reportedUser;
	}

	public void setReportedUser(User reportedUser) {
		this.reportedUser = reportedUser;
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

