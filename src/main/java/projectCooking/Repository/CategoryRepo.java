package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Categories;
import projectCooking.Repository.Entity.Recipe;

public interface CategoryRepo extends JpaRepository<Categories, Integer> {
	Categories findFirstByName(String name);

	@Query("select r from Categories c join c.recipes r where c.categoryId =:id and r.status ='APPROVED'")
	List<Recipe> getListRecipeByCategories(@Param("id") Integer id);

}
