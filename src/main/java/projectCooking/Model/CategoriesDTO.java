package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class CategoriesDTO {
	private Integer categoryId   ; 
	private String name , description  , imageUrl ; 
	private LocalDate createdAt  ;
	private List<RecipesDTO> recipes ; 
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	public List<RecipesDTO> getRecipes() {
		return recipes;
	}
	public void setRecipes(List<RecipesDTO> recipes) {
		this.recipes = recipes;
	}
	
	
}
