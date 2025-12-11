package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectCooking.Repository.Entity.AdminChatMessage;
import java.util.List;

@Repository
public interface AdminChatRepository extends JpaRepository<AdminChatMessage, Long> {
    // Lấy 50 tin nhắn mới nhất
    List<AdminChatMessage> findTop50ByOrderByCreatedAtDesc();
}
