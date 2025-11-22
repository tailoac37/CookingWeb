package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Like;
import projectCooking.Repository.Entity.User;

public interface LikeRepo extends JpaRepository<Like, Integer> {
	@Query(value = "select count(*) from Like l where l.recipe.user.userName = :username")
	public long getTotalLikeByUser(@Param("username") String username);

	@Query(value = "select l from Like l where l.user.userName = :username AND l.recipe.recipeId = :Id")
	public Like getCheckLikeByUser(@Param("username") String username, @Param("Id") Integer Id);

	@Query("select l.user from Like l where l.recipe.recipeId =:id")
	public List<User> getListUserLike(@Param("id") Integer Id);

	@Query("select count(l) from Like l where l.user = :user")
	int countByUser(@Param("user") User user);
}
