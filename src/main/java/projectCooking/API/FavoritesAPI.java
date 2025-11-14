package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.RecipesDTO;
import projectCooking.Service.FavoritesService;

@RestController
public class FavoritesAPI {
	@Autowired
	FavoritesService service ; 
	@PostMapping("/api/user/recipes/{id}/favorite")
	public String addFavorites(@PathVariable("id") Integer Id , @RequestHeader("Authorization") String auth) 
	{
		String token = auth.replace("Bearer", "") ; 
		return service.addFavorites(Id, token) ; 
	}
	@DeleteMapping("/api/user/recipes/{id}/favorite")
	public String delFavorites(@PathVariable("id") Integer Id , @RequestHeader("Authorization") String auth) 
	{
		String token = auth.replace("Bearer", "") ; 
		return service.delFavorites(Id, token)  ; 
	}
	@GetMapping("/api/user/recipes/favorite")
	public List<RecipesDTO> getFavorite(@RequestHeader("Authorization") String auth)  
	{
		String token = auth.replace("Bearer", "") ; 
		return service.getFavorite(token) ; 
	}
}
