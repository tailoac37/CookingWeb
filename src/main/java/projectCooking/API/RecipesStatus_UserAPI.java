package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Service.RecipesStatus_User_Service;

@RestController
public class RecipesStatus_UserAPI {
	@Autowired
	private RecipesStatus_User_Service service ; 
	@GetMapping("/api/user/recipes/myRecipes")
	public List<RecipesDTO> getMyRecipesByStatus(@RequestHeader("Authorization") String auth , @RequestParam(value="status" , required=false) Recipe.RecipeStatus status)
	{
		String token = auth.replace("Bearer ","")  ; 
		return service.getMyRecipesByStatus(token, status) ; 
	}
}
