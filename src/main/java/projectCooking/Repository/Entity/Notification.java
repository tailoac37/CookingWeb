package projectCooking.Repository.Entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notifications")

public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Integer notificationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "type", nullable = false)
	private NotificationType type;

	@Column(name = "title", nullable = false, length = 200)
	private String title;

	@Column(name = "message", columnDefinition = "TEXT")
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "related_user_id")
	private User relatedUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "related_recipe_id")
	private Recipe relatedRecipe;

	@Column(name = "is_read")
	private Boolean isRead = false;

	@Column(name = "created_at", updatable = false)
	private LocalDate createdAt;

	public enum NotificationType {
		NEW_RECIPE, LIKE, COMMENT, FOLLOW, ADMIN_MESSAGE, VIEW, RATE, REPORT, CATEGORIES
	}

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getRelatedUser() {
		return relatedUser;
	}

	public void setRelatedUser(User relatedUser) {
		this.relatedUser = relatedUser;
	}

	public Recipe getRelatedRecipe() {
		return relatedRecipe;
	}

	public void setRelatedRecipe(Recipe relatedRecipe) {
		this.relatedRecipe = relatedRecipe;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

}
