package projectCooking.Service.Implements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Recipe.RecipeStatus;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Service.JWTService;
import projectCooking.Service.RecipesStatus_User_Service;

@Service
public class RecipesStatusUserServiceImplements implements RecipesStatus_User_Service {
	@Autowired
	private JWTService jwt;
	@Autowired
	private RecipesRepo recipesRepo;
	@Autowired
	private ModelMapper model;

	@Override
	public List<RecipesDTO> getMyRecipes(String token) {
		String username = jwt.extractUserName(token);
		if (username == null) {
			throw new DulicateUserException("Ten nay khong ton tai , vui long dang nhap va thu lai");
		}
		List<Recipe> recipes = recipesRepo.getRecipesByUser(username);
		List<RecipesDTO> recipesListDTO = new ArrayList<>();
		for (Recipe recipe : recipes) {
			RecipesDTO recipesDTO = model.map(recipe, RecipesDTO.class);
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tagsDataBase = recipe.getTags();
			Set<String> tagsDTO = new HashSet<>();
			for (Tags tag : tagsDataBase) {
				tagsDTO.add(tag.getName());
			}
			recipesDTO.setTags(tagsDTO);
			recipesDTO.setIngredients(
					java.util.Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(java.util.stream.Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						java.util.Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(java.util.stream.Collectors.toList()));
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;

	}

	@Override
	public List<RecipesDTO> getMyRecipesByStatus(String token, RecipeStatus status) {
		String username = jwt.extractUserName(token);
		if (username == null) {
			throw new DulicateUserException("Ten nay khong ton tai , vui long dang nhap va thu lai");
		}
		List<Recipe> recipes = new ArrayList<>();
		if (status == null) {
			recipes = recipesRepo.getRecipesByUser(username);
		} else {
			recipes = recipesRepo.getRecipesByStatusAndUser(status, username);
		}

		List<RecipesDTO> recipesListDTO = new ArrayList<>();
		for (Recipe recipe : recipes) {
			RecipesDTO recipesDTO = model.map(recipe, RecipesDTO.class);
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tagsDataBase = recipe.getTags();
			Set<String> tagsDTO = new HashSet<>();
			for (Tags tag : tagsDataBase) {
				tagsDTO.add(tag.getName());
			}
			recipesDTO.setTags(tagsDTO);
			recipesDTO.setIngredients(
					java.util.Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(java.util.stream.Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						java.util.Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(java.util.stream.Collectors.toList()));
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;
	}

}
