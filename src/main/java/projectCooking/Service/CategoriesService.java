package projectCooking.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.CategoriesDTO;
import projectCooking.Model.RecipesDTO;
import projectCooking.Request.CategoriesRequest;

public interface CategoriesService {
	public List<CategoriesDTO> getListCategories();

	public CategoriesDTO getListRecipeByCategories(String token, Integer Id);

	public String addCategories(String token, MultipartFile image, CategoriesRequest categories) throws IOException;

	public String updateCategories(String token, MultipartFile image, CategoriesRequest categories, Integer Id)
			throws IOException;

	public String deactivateCategory(String token, Integer Id);
}
