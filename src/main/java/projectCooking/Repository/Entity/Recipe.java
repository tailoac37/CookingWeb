package projectCooking.Repository.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "recipes")
public class Recipe {
	// Updated nutrition field

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_id")
	private Integer recipeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Categories category;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String ingredients;

	@Column(columnDefinition = "TEXT")
	private String nutrition;

	@Column(name = "prep_time")
	private Integer prepTime; // minutes

	@Column(name = "cook_time")
	private Integer cookTime; // minutes

	private Integer servings;

	@Enumerated(EnumType.STRING)
	@Column(name = "difficulty_level")
	private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

	@Column(name = "image_url")
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private RecipeStatus status = RecipeStatus.PENDING;

	@Column(name = "admin_note", columnDefinition = "TEXT")
	private String adminNote;

	@Column(name = "view_count")
	private Integer viewCount = 0;

	@Column(name = "like_count")
	private Integer likeCount = 0;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// Relationships
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeImage> images = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tags> tags = new HashSet<>();

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Like> likes = new ArrayList<>();

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RecipeReview> ratings = new ArrayList<>();

	public enum DifficultyLevel {
		EASY, MEDIUM, HARD
	}

	public enum RecipeStatus {
		DRAFT, PENDING, APPROVED, REJECTED
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Categories getCategory() {
		return category;
	}

	public void setCategory(Categories category) {
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

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getNutrition() {
		return nutrition;
	}

	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}

	// public String getInstructions() {
	// return instructions;
	// }
	//
	// public void setInstructions(String instructions) {
	// this.instructions = instructions;
	// }

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

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public RecipeStatus getStatus() {
		return status;
	}

	public void setStatus(RecipeStatus status) {
		this.status = status;
	}

	public String getAdminNote() {
		return adminNote;
	}

	public void setAdminNote(String adminNote) {
		this.adminNote = adminNote;
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

	public List<RecipeImage> getImages() {
		return images;
	}

	public void setImages(List<RecipeImage> images) {
		this.images = images;
	}

	public Set<Tags> getTags() {
		return tags;
	}

	public void setTags(Set<Tags> tags) {
		this.tags = tags;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<RecipeReview> getRatings() {
		return ratings;
	}

	public void setRatings(List<RecipeReview> ratings) {
		this.ratings = ratings;
	}

}
