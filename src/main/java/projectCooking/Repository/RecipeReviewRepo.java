package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.RecipeReview;

public interface RecipeReviewRepo extends JpaRepository<RecipeReview, Integer>{

}
