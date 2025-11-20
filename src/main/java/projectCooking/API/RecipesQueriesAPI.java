package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.FoodAIReponse;
import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Request.RecipeQueryRequest;
import projectCooking.Service.AIService;
import projectCooking.Service.RecipesQuriesServcie;

@RestController
public class RecipesQueriesAPI {
	@Autowired
	private RecipesQuriesServcie service  ; 
	@Autowired
	private AIService aiService ; 
	@GetMapping("/api/recipes/popular")
	public List<RecipesDTO> popular()
	{
		return service.popular()  ; 
	}
	@GetMapping("/api/recipes/trending")
	public List<RecipesDTO> trending()  
	{
		return service.trending() ; 
	}
	@PostMapping("/api/recipes/find")
	public List<RecipesDTO> searchRecipes(@RequestHeader(value="Authorization" , required = false) String auth, @RequestBody RecipeQueryRequest recipesRequest  )
	{
		String token = null  ; 
		if(auth != null)
		{
			token = auth.replace("Bearer", "") ; 
		}
		return service.searchRecipes(token, recipesRequest) ; 
	}
	
	
}
