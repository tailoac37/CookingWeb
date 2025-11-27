package projectCooking.Repository.Entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table

public class RecipeImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private long imageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id", nullable = false)
	private Recipe recipe;

	@Column(name = "image_url", nullable = true)
	private String imageUrl;

	@Column(name = "is_primary")
	private Boolean isPrimary = false;
	@Column(name = "created_at", updatable = false)
	private LocalDate createdAt;
	@Column(name = "instructions", nullable = false, columnDefinition = "TEXT")
	private String instructions;

	public long getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

}
