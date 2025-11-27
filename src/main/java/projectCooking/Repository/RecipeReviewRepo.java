package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.RecipeReview;

public interface RecipeReviewRepo extends JpaRepository<RecipeReview, Integer> {

    @Query("SELECT r FROM RecipeReview r WHERE r.user.userId = :userId ORDER BY r.createdAt DESC")
    List<RecipeReview> findByUserId(@Param("userId") Integer userId);

}
