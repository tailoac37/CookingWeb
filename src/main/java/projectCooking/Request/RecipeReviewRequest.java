package projectCooking.Request;



import java.util.List;





public class RecipeReviewRequest {
    
  
    
 

//    private Integer rating;
    
   
    private String title;
    
    private String reviewContent;
    
    
    
//    private Boolean followedRecipeExactly;
    
//    private String modifications;
    
//    private String difficultyLevel; // EASY, MEDIUM, HARD
    

    private Integer actualCookingTime;
    
//    private Boolean wouldMakeAgain;

    // Constructors
    public RecipeReviewRequest() {
    }

    public RecipeReviewRequest(Integer recipeId, Integer rating, String title, String reviewContent) {
//        this.rating = rating;
        this.title = title;
        this.reviewContent = reviewContent;
    }

    // Getters and Setters
//    public Integer getRating() {
//        return rating;
//    }
//
//    public void setRating(Integer rating) {
//        this.rating = rating;
//    }

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

  

//    public Boolean getFollowedRecipeExactly() {
//        return followedRecipeExactly;
//    }
//
//    public void setFollowedRecipeExactly(Boolean followedRecipeExactly) {
//        this.followedRecipeExactly = followedRecipeExactly;
//    }

//    public String getModifications() {
//        return modifications;
//    }
//
//    public void setModifications(String modifications) {
//        this.modifications = modifications;
//    }
//
//    public String getDifficultyLevel() {
//        return difficultyLevel;
//    }
//
//    public void setDifficultyLevel(String difficultyLevel) {
//        this.difficultyLevel = difficultyLevel;
//    }

    public Integer getActualCookingTime() {
        return actualCookingTime;
    }

    public void setActualCookingTime(Integer actualCookingTime) {
        this.actualCookingTime = actualCookingTime;
    }

//    public Boolean getWouldMakeAgain() {
//        return wouldMakeAgain;
//    }
//
//    public void setWouldMakeAgain(Boolean wouldMakeAgain) {
//        this.wouldMakeAgain = wouldMakeAgain;
//    }
}