package projectCooking.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.RecipesDTO;
import projectCooking.Model.RecipesDetailsDTO;
import projectCooking.Request.RecipeRequest;

public interface RecipesManagerService {
	public String createRecipes(String token , RecipeRequest recipes , MultipartFile imagePrimary , List<MultipartFile> image) throws IOException ;
	public RecipesDetailsDTO getRecipes(Integer id , String token)  ; 
	public String updateRecipes(String token , RecipeRequest recipesUpdate , MultipartFile image_primary , List<MultipartFile> image , Integer Id) throws IOException   ; 
	public String deleteRecipes(String token , Integer Id)  ; 
	public List<RecipesDTO> getListRecipes(String token)  ; 
}
