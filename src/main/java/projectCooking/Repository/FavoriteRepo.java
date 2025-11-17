package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Favorite;
import projectCooking.Repository.Entity.Recipe;

public interface FavoriteRepo extends JpaRepository<Favorite, Long>{
	@Query(value = "select  f.recipe from Favorite f  where f.user.userName = :username") 
	public List<Recipe> getRecipeFavoriteByUser(@Param("username") String username)   ; 
	@Query("select  count(*) from Favorite f  where f.recipe.recipeId = :recipeId and f.user.userName= :userName")
	public int checkRecipeInFavorite(@Param("recipeId") Integer recipeId , @Param("userName") String userName) ; 
	
	
	@Query("select  f from Favorite f  where f.recipe.recipeId = :recipeId and f.user.userId = :userId")
	public Favorite getFavoriteByRecipeAndUser(@Param("recipeId") Integer recipeId, @Param("userId") Integer userId) ; 
}
