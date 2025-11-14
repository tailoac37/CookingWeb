package projectCooking.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Integer> {
	List<Notification> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
	@Modifying
	@Transactional
	@Query("DELETE FROM Notification n WHERE n.relatedRecipe IN (SELECT r FROM Recipe r WHERE r.recipeId = :recipeId)")
	void deleteNotificationsByRecipeId(@Param("recipeId") Integer recipeId);
	
	List<Notification> findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(Integer userId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Notification n WHERE n.user IN (SELECT u FROM User u WHERE u.userId = :userId)")
	void deleteNotificationsByUserId(@Param("userId") Integer userId);
}
