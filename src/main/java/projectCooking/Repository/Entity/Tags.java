package projectCooking.Repository.Entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
public class Tags {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;
    
    @Column(name ="name"  , unique = true, nullable = false, length = 50)
    private String name;
   
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;
    
    @ManyToMany(mappedBy = "tags")
    private Set<Recipe> recipes = new HashSet<>();

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public Set<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(Set<Recipe> recipes) {
		this.recipes = recipes;
	}
    
}
