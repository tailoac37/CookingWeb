package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class RecipesDetailsDTO {
	private Integer recipeId, prepTime, cookTime, viewCount, likeCount, userId, servings;
	private String username, category, title, description, difficultyLevel, imageUrl, avatarUrl;
	private List<String> ingredients;
	private List<String> nutrition;
	private LocalDate CreateAt, UpdateAt;
	private List<instructionsDTO> instructions;
	private List<String> tags;
	private List<CommentsDTO> commentsDTO;
	private boolean isLike = false;
	private boolean isChange = false;
	private boolean isFavorite = false;

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
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

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
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

	public List<instructionsDTO> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<instructionsDTO> instructions) {
		this.instructions = instructions;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public LocalDate getCreateAt() {
		return CreateAt;
	}

	public void setCreateAt(LocalDate createAt) {
		CreateAt = createAt;
	}

	public LocalDate getUpdateAt() {
		return UpdateAt;
	}

	public void setUpdateAt(LocalDate updateAt) {
		UpdateAt = updateAt;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<CommentsDTO> getCommentsDTO() {
		return commentsDTO;
	}

	public void setCommentsDTO(List<CommentsDTO> commentsDTO) {
		this.commentsDTO = commentsDTO;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getServings() {
		return servings;
	}

	public void setServings(Integer servings) {
		this.servings = servings;
	}

}
