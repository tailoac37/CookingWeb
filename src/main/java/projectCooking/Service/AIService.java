package projectCooking.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.FoodAIReponse;
import projectCooking.Model.RecipesDTO;

public interface AIService {
	FoodAIReponse detectIngredients(MultipartFile file) throws IOException;
	public List<RecipesDTO> searchByIngredients(List<String> ingredients , String token)  ; 
}
