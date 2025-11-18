package projectCooking.Service;

import java.util.List;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Request.RecipeQueryRequest;

public interface RecipesQuriesServcie {
	public List<RecipesDTO> getRecipesSearch(String title , String category , Recipe.DifficultyLevel difficulty , String tags)  ; 
	public List<RecipesDTO> popular() ; 
	public List<RecipesDTO> trending()  ; 
	public List<RecipesDTO> searchRecipes(String token ,RecipeQueryRequest request) ; 
}
