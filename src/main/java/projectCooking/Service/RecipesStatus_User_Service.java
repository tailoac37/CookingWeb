package projectCooking.Service;

import java.util.List;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.Entity.Recipe;

public interface RecipesStatus_User_Service {
	public List<RecipesDTO> getMyRecipes(String token) ;
	public List<RecipesDTO> getMyRecipesByStatus(String token, Recipe.RecipeStatus status)  ;  
}
