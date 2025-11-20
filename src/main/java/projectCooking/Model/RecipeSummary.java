package projectCooking.Model;

import java.time.LocalDateTime;

public class RecipeSummary {
    private Integer recipeId;
    private String title;
    private String imageUrl;
    private int views;
    private int likes;
    private int comments;
    private LocalDateTime createdAt;
    
    public Integer getRecipeId() { return recipeId; }
    public void setRecipeId(Integer recipeId) { this.recipeId = recipeId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    
    public int getComments() { return comments; }
    public void setComments(int comments) { this.comments = comments; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}