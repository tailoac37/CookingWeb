package projectCooking.Model;

import java.time.LocalDateTime;
import java.util.List;

public class RecipeReviewDTO {

    private Integer reviewId;
    private Integer recipeId;
    private String recipeName;
    private Integer userId;
    private String userName;
    private String userAvatar;
    // private Integer rating;
    private String title;
    private String reviewContent;
    private List<String> userImages;
    // private Boolean followedRecipeExactly;
    // private String modifications;
    // private String difficultyLevel;
    private Integer actualCookingTime;
    // private Boolean wouldMakeAgain;
    // private Integer helpfulCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isChange = false, isDelete = false;

    // Constructors
    public RecipeReviewDTO() {
    }

    public RecipeReviewDTO(Integer reviewId, Integer recipeId, String recipeName,
            Integer userId, String userName, Integer rating,
            String reviewContent, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.userId = userId;
        this.userName = userName;
        // this.rating = rating;
        this.reviewContent = reviewContent;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    // public Integer getRating() {
    // return rating;
    // }
    //
    // public void setRating(Integer rating) {
    // this.rating = rating;
    // }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public List<String> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<String> userImages) {
        this.userImages = userImages;
    }

    // public Boolean getFollowedRecipeExactly() {
    // return followedRecipeExactly;
    // }
    //
    // public void setFollowedRecipeExactly(Boolean followedRecipeExactly) {
    // this.followedRecipeExactly = followedRecipeExactly;
    // }
    //
    // public String getModifications() {
    // return modifications;
    // }
    //
    // public void setModifications(String modifications) {
    // this.modifications = modifications;
    // }
    //
    // public String getDifficultyLevel() {
    // return difficultyLevel;
    // }
    //
    // public void setDifficultyLevel(String difficultyLevel) {
    // this.difficultyLevel = difficultyLevel;
    // }

    public Integer getActualCookingTime() {
        return actualCookingTime;
    }

    public void setActualCookingTime(Integer actualCookingTime) {
        this.actualCookingTime = actualCookingTime;
    }

    // public Boolean getWouldMakeAgain() {
    // return wouldMakeAgain;
    // }
    //
    // public void setWouldMakeAgain(Boolean wouldMakeAgain) {
    // this.wouldMakeAgain = wouldMakeAgain;
    // }
    //
    // public Integer getHelpfulCount() {
    // return helpfulCount;
    // }
    //
    // public void setHelpfulCount(Integer helpfulCount) {
    // this.helpfulCount = helpfulCount;
    // }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

}
