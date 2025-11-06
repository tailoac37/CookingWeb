package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Favorite;
import projectCooking.Repository.Entity.Recipe;

public interface FavoriteRepo extends JpaRepository<Favorite, Long>{
	@Query(value = "select  f.recipe from Favorite f  where f.user.username = :username") 
	public List<Recipe> getRecipeFavoriteByUser(@Param("username") String username)   ; 
}
