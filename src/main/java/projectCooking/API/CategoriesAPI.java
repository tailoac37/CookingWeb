package projectCooking.API;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.CategoriesDTO;
import projectCooking.Model.RecipesDTO;
import projectCooking.Request.CategoriesRequest;
import projectCooking.Service.CategoriesService;

@RestController
public class CategoriesAPI {
	@Autowired
	private CategoriesService service;

	@GetMapping("/api/categories")
	public List<CategoriesDTO> getListCategories() {
		return service.getListCategories();
	}

	@GetMapping("/api/categories/{id}")
	public CategoriesDTO getListRecipesByCategories(
			@RequestHeader(value = "Authorization", required = false) String auth, @PathVariable("id") Integer Id) {
		String token = null;
		if (token != null) {
			token = auth.replace("Bearer", "");
		}

		return service.getListRecipeByCategories(token, Id);
	}

	@PostMapping("/api/admin/categories")
	public String addCategories(@RequestHeader("Authorization") String auth, @RequestPart("image") MultipartFile image,
			@RequestPart("categories") CategoriesRequest categories) throws IOException {
		String token = auth.replace("Bearer", "");
		return service.addCategories(token, image, categories);
	}

	@PatchMapping("/api/admin/categories/{id}")
	public String updateCategories(@RequestHeader("Authorization") String auth,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "categories", required = false) CategoriesRequest categories,
			@PathVariable("id") Integer Id) throws IOException {
		String token = auth.replace("Bearer", "");
		return service.updateCategories(token, image, categories, Id);
	}

	@PatchMapping("/api/admin/categories/{id}/deactivate")
	public String deactivateCategory(@RequestHeader("Authorization") String auth, @PathVariable("id") Integer Id) {
		String token = auth.replace("Bearer ", "").trim();
		return service.deactivateCategory(token, Id);
	}

}
