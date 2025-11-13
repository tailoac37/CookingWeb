package projectCooking.Model;

import java.time.LocalDate;

import projectCooking.Repository.Entity.Notification;

public class NotificationDTO {
    private Integer id;
    private String title;
    private String message;
    private String type;
    private LocalDate createdAt;
    private String senderName;
    private Integer recipeId;
    private boolean isRead =false ;
    public NotificationDTO(Notification notif) {
        this.id = notif.getNotificationId();
        this.title = notif.getTitle();
        this.message = notif.getMessage();
        this.type = notif.getType().name();
        this.createdAt = notif.getCreatedAt();
        this.senderName = notif.getRelatedUser() != null ? notif.getRelatedUser().getUserName() : null;
        this.recipeId = notif.getRelatedRecipe() != null ? notif.getRelatedRecipe().getRecipeId() : null;
        this.isRead = notif.getIsRead() ; 
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}
    
}
