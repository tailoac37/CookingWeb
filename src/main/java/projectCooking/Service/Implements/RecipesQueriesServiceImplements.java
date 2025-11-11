package projectCooking.Service.Implements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Service.RecipesManagerService;
import projectCooking.Service.RecipesQuriesServcie;
@Service
public class RecipesQueriesServiceImplements implements RecipesQuriesServcie {
	@Autowired
	private ModelMapper model ; 
	@Autowired
	private RecipesRepo recipeRepo ; 
	@Override
	public List<RecipesDTO> getRecipesSearch(String title , String category , Recipe.DifficultyLevel difficulty , String tags) {
		List<Recipe> recipes = recipeRepo.searchRecipes(title, category, difficulty, tags)  ; 
		List<RecipesDTO> recipesListDTO = new ArrayList<>()  ; 
		for(Recipe recipe : recipes)
		{
			RecipesDTO recipesDTO = model.map(recipe , RecipesDTO.class)  ; 
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tagsDataBase = recipe.getTags()  ; 
			Set<String> tagsDTO = new HashSet<>()  ; 
			for(Tags tag : tagsDataBase)  
			{
				tagsDTO.add(tag.getName())  ; 
			}
			recipesDTO.setTags(tagsDTO);
			recipesListDTO.add(recipesDTO)  ; 
 		}
		return recipesListDTO;
	}
	@Override
	public List<RecipesDTO> popular() {
		List<Recipe> recipes = recipeRepo.popular() ; 
		List<RecipesDTO> recipesListDTO = new ArrayList<>()  ; 
		for(Recipe recipe : recipes)
		{
			RecipesDTO recipesDTO = model.map(recipe , RecipesDTO.class)  ; 
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tagsDataBase = recipe.getTags()  ; 
			Set<String> tagsDTO = new HashSet<>()  ; 
			for(Tags tag : tagsDataBase)  
			{
				tagsDTO.add(tag.getName())  ; 
			}
			recipesDTO.setTags(tagsDTO);
			recipesListDTO.add(recipesDTO)  ; 
 		}
		return recipesListDTO;
	}
	@Override
	public List<RecipesDTO> trending() {
		List<Recipe> recipes = recipeRepo.trending()  ; 
		List<RecipesDTO> recipesListDTO = new ArrayList<>()  ; 
		for(Recipe recipe : recipes)
		{
			RecipesDTO recipesDTO = model.map(recipe , RecipesDTO.class)  ; 
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tagsDataBase = recipe.getTags()  ; 
			Set<String> tagsDTO = new HashSet<>()  ; 
			for(Tags tag : tagsDataBase)  
			{
				tagsDTO.add(tag.getName())  ; 
			}
			recipesDTO.setTags(tagsDTO);
			recipesListDTO.add(recipesDTO)  ; 
 		}
		return recipesListDTO;
		
	}

}
