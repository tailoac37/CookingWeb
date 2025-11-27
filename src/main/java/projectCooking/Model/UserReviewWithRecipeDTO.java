package projectCooking.Model;

public class UserReviewWithRecipeDTO {

    // Review information
    private RecipeReviewDTO review;

    // Recipe basic information
    private Integer recipeId;
    private String recipeTitle;
    private String recipeImageUrl;
    private String recipeDescription;
    private String category;
    private String difficultyLevel;
    private Integer prepTime;
    private Integer cookTime;

    // Constructors
    public UserReviewWithRecipeDTO() {
    }

    public UserReviewWithRecipeDTO(RecipeReviewDTO review, Integer recipeId, String recipeTitle,
            String recipeImageUrl, String recipeDescription, String category,
            String difficultyLevel, Integer prepTime, Integer cookTime) {
        this.review = review;
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeImageUrl = recipeImageUrl;
        this.recipeDescription = recipeDescription;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    // Getters and Setters
    public RecipeReviewDTO getReview() {
        return review;
    }

    public void setReview(RecipeReviewDTO review) {
        this.review = review;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
