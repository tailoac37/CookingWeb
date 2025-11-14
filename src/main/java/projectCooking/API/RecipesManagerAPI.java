package projectCooking.API;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.RecipesDTO;
import projectCooking.Model.RecipesDetailsDTO;
import projectCooking.Request.RecipeRequest;
import projectCooking.Service.RecipesManagerService;

@RestController
public class RecipesManagerAPI {
	@Autowired
	private RecipesManagerService service   ; 
	@PostMapping("/api/user/recipes")
	public String createRecipes(@RequestHeader("Authorization") String auth , @RequestPart("recipes") RecipeRequest recipes , @RequestPart("image_primary") MultipartFile imagePrimary , @RequestPart("image") List<MultipartFile> image) throws IOException
	{
		String token = auth .replace("Bearer ", "")  ; 
		return service.createRecipes(token, recipes, imagePrimary, image) ; 
	}
	@GetMapping("/api/recipes/{id}")
	public RecipesDetailsDTO getRecipes(@PathVariable("id") Integer id , @RequestHeader(value = "Authorization" , required=false)String auth) 
	{
		String token =null ; 
		if(auth!=null)
		{
			 token = auth.replace("Bearer ", "")  ; 
		}
		
		return service.getRecipes(id ,token) ; 
	}
	@PutMapping("/api/user/recipes/{id}")
	public String updateRecipes(@RequestHeader("Authorization") String auth ,@RequestPart("recipes") RecipeRequest recipesUpdate , @RequestPart(value = "image_primary", required =false) MultipartFile image_primary , @RequestPart(value ="image" , required =false) List<MultipartFile> image , @PathVariable("id") Integer Id  ) throws IOException
	{
		String token = auth.replace("Bearer ", "")  ; 
		return service.updateRecipes(token, recipesUpdate, image_primary, image, Id) ;
	}
	@DeleteMapping("/api/user/recipes/{id}") 
	public String deleteRecipes(@RequestHeader("Authorization") String auth , @PathVariable("id") Integer Id)  
	{
		String token = auth.replace("Bearer ", "")  ; 
		return service.deleteRecipes(token, Id)  ;
	}
	@GetMapping("/api/recipes")
	public List<RecipesDTO> getListRecipes(@RequestHeader(value = "Authorization" , required = false) String auth)
	{
		String token =null ; 
		if(auth!=null)
		{
			 token = auth.replace("Bearer ", "")  ; 
		}
		
		return service.getListRecipes(token) ; 
	}
}
