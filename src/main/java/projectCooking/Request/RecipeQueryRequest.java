package projectCooking.Request;

import java.util.List;

import projectCooking.Repository.Entity.Recipe;

public class RecipeQueryRequest {
	private List<String>  category , title , ingredients  , tags ;
	private Integer prepTime , cookTime , servings ;
	private List<Recipe.DifficultyLevel> level ;
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public List<String> getTitle() {
		return title;
	}
	public void setTitle(List<String> title) {
		this.title = title;
	}
	public List<String> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	public Integer getPrepTime() {
		return prepTime;
	}
	public void setPrepTime(Integer prepTime) {
		this.prepTime = prepTime;
	}
	public Integer getCookTime() {
		return cookTime;
	}
	public void setCookTime(Integer cookTime) {
		this.cookTime = cookTime;
	}
	public Integer getServings() {
		return servings;
	}
	public void setServings(Integer servings) {
		this.servings = servings;
	}
	public List<Recipe.DifficultyLevel> getLevel() {
		return level;
	}
	public void setLevel(List<Recipe.DifficultyLevel> level) {
		this.level = level;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	} 
	
	
}
