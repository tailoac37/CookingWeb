package projectCooking.Request;

import java.util.List;

public class RecipeRequest {
	private String title, description, difficultyLevel;
	private Integer prepTime, cookTime, servings;
	private CategoriesRequest category;
	private List<String> ingredients;
	private List<String> nutrition;
	private List<InstructionRequest> instructions;
	private List<String> tags;

	public CategoriesRequest getCategory() {
		return category;
	}

	public void setCategory(CategoriesRequest category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public List<String> getNutrition() {
		return nutrition;
	}

	public void setNutrition(List<String> nutrition) {
		this.nutrition = nutrition;
	}

	public List<InstructionRequest> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<InstructionRequest> instructions) {
		this.instructions = instructions;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
