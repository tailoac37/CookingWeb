package projectCooking.Service.Implements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.FavoriteRepo;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Favorite;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.FavoritesService;
import projectCooking.Service.JWTService;

@Service
public class FavoritesServiceImplements implements FavoritesService {
	@Autowired
	RecipesRepo recipeRepo;
	@Autowired
	JWTService jwt;
	@Autowired
	UserRepo userRepo;
	@Autowired
	FavoriteRepo favoriteRepo;
	@Autowired
	ModelMapper model;
	@Autowired
	LikeRepo likeRepo;

	@Override
	public String addFavorites(Integer Id, String token) {
		Recipe recipe = recipeRepo.findById(Id).orElse(null);
		if (recipe == null) {
			return " bai viet khong ton tai";
		}
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			return "nguoi dung khong ton tai, ban vui long dang nhap lai";
		}
		if (favoriteRepo.checkRecipeInFavorite(recipe.getRecipeId(), userName) == 0) {
			Favorite favorite = new Favorite();
			favorite.setUser(user);
			favorite.setRecipe(recipe);
			favorite.setCreatedAt(LocalDate.now());
			favoriteRepo.save(favorite);
			return "da them vao muc yeu thich";
		}

		return "ok";
	}

	@Override
	public String delFavorites(Integer Id, String token) {
		Recipe recipe = recipeRepo.findById(Id).orElse(null);
		if (recipe == null) {
			return " bai viet khong ton tai";
		}
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			return "nguoi dung khong ton tai, ban vui long dang nhap lai";
		}
		Favorite favorite = favoriteRepo.getFavoriteByRecipeAndUser(recipe.getRecipeId(), user.getUserId());
		if (favorite == null) {
			return " bai viet nay khong ton tai trong danh sach yeu thich ";
		}
		favoriteRepo.delete(favorite);
		return "done";
	}

	@Override
	public List<RecipesDTO> getFavorite(String token) {
		String userName = jwt.extractUserName(token);
		List<Recipe> recipes = favoriteRepo.getRecipeFavoriteByUser(userName);
		List<RecipesDTO> recipesListDTO = new ArrayList<>();
		for (Recipe recipe : recipes) {
			RecipesDTO recipesDTO = model.map(recipe, RecipesDTO.class);
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tags = recipe.getTags();
			Set<String> tagsDTO = new HashSet<>();
			for (Tags item : tags) {
				tagsDTO.add(item.getName());
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
			if (userName != null) {
				if (userName.equals(recipe.getUser().getUserName())) {
					recipesDTO.setChange(true);
				}
				if (likeRepo.getCheckLikeByUser(userName, recipe.getRecipeId()) != null) {
					recipesDTO.setLike(true);
				}
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;
	}

}
