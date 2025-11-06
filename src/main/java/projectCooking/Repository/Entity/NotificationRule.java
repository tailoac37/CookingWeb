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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notification_rules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "trigger_user_id", "notification_type"})
})
public class NotificationRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer ruleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_user_id", nullable = false)
    private User triggerUser;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private Notification.NotificationType notificationType;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getTriggerUser() {
		return triggerUser;
	}

	public void setTriggerUser(User triggerUser) {
		this.triggerUser = triggerUser;
	}

	public Notification.NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Notification.NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
}
