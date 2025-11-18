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
	User findByUserName(String userName)  ; 
	User findByEmail(String email) ; 
	@Query("SELECT u FROM User u WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :find, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :find, '%')) OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :find, '%'))")
	public List<User> searchUser(@Param("find") String find)  ;
	List<User> findByRole(User.Role role)  ; 
}	
