package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.Categories;

public interface CategoryRepo extends JpaRepository<Categories,Long>{
	Categories findByName(String name)  ; 
}
