package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.Tags;

public interface TagsRepo extends JpaRepository<Tags, Long> {
	Tags findFirstByName(String name);
}
