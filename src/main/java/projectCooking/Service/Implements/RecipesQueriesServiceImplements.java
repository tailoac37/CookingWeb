package projectCooking.Service.Implements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Repository.Specification.RecipeSpecification;
import projectCooking.Request.RecipeQueryRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.RecipesManagerService;
import projectCooking.Service.RecipesQuriesServcie;

@Service
public class RecipesQueriesServiceImplements implements RecipesQuriesServcie {
	@Autowired
	private ModelMapper model;
	@Autowired
	private RecipesRepo recipeRepo;
	@Autowired
	private LikeRepo likeRepo;
	@Autowired
	private JWTService jwt;

	@Override
	public List<RecipesDTO> getRecipesSearch(String title, String category, Recipe.DifficultyLevel difficulty,
			String tags) {
		List<Recipe> recipes = recipeRepo.searchRecipes(title, category, difficulty, tags);
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
					Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(Collectors.toList()));
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;
	}

	@Override
	public List<RecipesDTO> popular() {
		List<Recipe> recipes = recipeRepo.popular();
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
					Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(Collectors.toList()));
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;
	}

	@Override
	public List<RecipesDTO> trending() {
		List<Recipe> recipes = recipeRepo.trending();
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
					Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(Collectors.toList()));
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;

	}

	@Override
	public List<RecipesDTO> searchRecipes(String token, RecipeQueryRequest request) {
		Specification<Recipe> spec = RecipeSpecification.filterRecipes(request);
		List<Recipe> recipes = recipeRepo.findAll(spec);
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
					Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(Collectors.toList()));
			}
			if (token != null) {
				String userName = jwt.extractUserName(token);
				if (userName != null) {
					if (userName.equals(recipe.getUser().getUserName())) {
						recipesDTO.setChange(true);
					}
					if (likeRepo.getCheckLikeByUser(userName, recipe.getRecipeId()) != null) {
						recipesDTO.setLike(true);
					}
				}
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;
	}

}
