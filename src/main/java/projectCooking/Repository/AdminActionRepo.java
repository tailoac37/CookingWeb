package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.AdminAction;

public interface AdminActionRepo extends JpaRepository<AdminAction, Integer> {
	
}
