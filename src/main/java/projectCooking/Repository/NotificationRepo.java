package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Integer> {
	List<Notification> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
