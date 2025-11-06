package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.RecipeImage;

public interface RecipeImageRepo extends JpaRepository<RecipeImage, Long> {
	
}
