package projectCooking.Repository.Entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "recipe_reviews")
public class RecipeReview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
   
//    @Column(name = "rating", nullable = false)
//    private Integer rating;
    
   
    @Column(name = "title", length = 200)
    private String title;
    
    
    @Column(name = "review_content", columnDefinition = "TEXT")
    private String reviewContent;
    
    
    @Column(name = "user_images", columnDefinition = "TEXT")
    private String userImages;
    
    
//    @Column(name = "followed_recipe_exactly")
//    private Boolean followedRecipeExactly;
    
    
//    @Column(name = "modifications", columnDefinition = "TEXT")
//    private String modifications;
//    
//    
//    @Column(name = "difficulty_level")
//    private String difficultyLevel; // EASY, MEDIUM, HARD
    
    
    @Column(name = "actual_cooking_time")
    private Integer actualCookingTime;
    
    
//    @Column(name = "would_make_again")
//    private Boolean wouldMakeAgain;
    
    
//    @Column(name = "helpful_count")
//    private Integer helpfulCount = 0;
    
    
//    @Column(name = "status", length = 20)
//    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
   
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
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

    public String getUserImages() {
        return userImages;
    }

    public void setUserImages(String userImages) {
        this.userImages = userImages;
    }

  
    public Integer getActualCookingTime() {
        return actualCookingTime;
    }

    public void setActualCookingTime(Integer actualCookingTime) {
        this.actualCookingTime = actualCookingTime;
    }

   
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
}