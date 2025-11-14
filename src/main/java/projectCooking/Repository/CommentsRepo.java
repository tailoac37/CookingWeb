package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Comment;

public interface CommentsRepo extends JpaRepository<Comment, Integer> {
	 @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId")
	  List<Comment> findByParentCommentId(@Param("parentCommentId") Integer parentCommentId);
}
