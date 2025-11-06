package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projectCooking.Model.UserDTO;
import projectCooking.Repository.Entity.User;
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	User findByUsername(String userName)  ; 
	User findByEmail(String email) ; 
	@Query("select u from User u where u.username = :find or u.email = :find or u.fullName = :find")
	public List<User> searchUser(@Param("find") String find)  ; 
}	
