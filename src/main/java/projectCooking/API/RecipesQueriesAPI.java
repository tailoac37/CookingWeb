package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Service.RecipesQuriesServcie;

@RestController
public class RecipesQueriesAPI {
	@Autowired
	private RecipesQuriesServcie service  ; 
	@GetMapping("/api/recipes/search")
	public List<RecipesDTO> resultSearch( @RequestParam(value = "title" , required=false)String title ,
			@RequestParam(value = "category" , required=false)String category,
			@RequestParam(value = "difficulty" , required=false)Recipe.DifficultyLevel difficulty,
			@RequestParam(value = "tags" , required=false)String tags)
	{
		return service.getRecipesSearch(title, category, difficulty, tags) ; 
	}
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
}
