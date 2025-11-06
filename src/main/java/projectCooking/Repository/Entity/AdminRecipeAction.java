package projectCooking.Repository.Entity;

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

@Entity
@Table(name = "admin_recipe_actions")
public class AdminRecipeAction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Integer actionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum ActionType {
        APPROVE, REJECT, DELETE
    }

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
    
}