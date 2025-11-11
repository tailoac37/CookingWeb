package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.RecipeView;

public interface ViewRepo extends JpaRepository<RecipeView, Integer> {
	@Query(value= "select count(*) from RecipeView rv where rv.recipe.user.userName =:username")
	public long totalViewByUser(@Param("username") String username)  ; 
}
