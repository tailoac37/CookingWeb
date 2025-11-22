package projectCooking.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Follow;
import projectCooking.Repository.Entity.User;

public interface FollowRepo extends JpaRepository<Follow, Integer> {
	@Query(value = "select count(*) from Follow f where f.follower.userName = :username")
	public long getCountUserFollowing(@Param("username") String username);

	@Query(value = "select count(*) from Follow f where f.following.userName = :username")
	public long getCountUserFollower(@Param("username") String username);

	@Query(value = "select count(*) from Follow f where f.following.userName = :myUserName AND f.follower.userName = :userNameOther")
	public int checkFollwer(@Param("myUserName") String myUserName, @Param("userNameOther") String userNameOther);

	@Query(value = "select count(*) from Follow f where f.following.userName = :userNameOther AND f.follower.userName = :myUserName")
	public int checkFollwing(@Param("myUserName") String myUserName, @Param("userNameOther") String userNameOther);

	@Modifying
	@Transactional
	@Query("DELETE FROM Follow f WHERE f.following.userId = :followingId AND f.follower.userId = :followerId")
	void deleteFollowing(@Param("followerId") Integer followerId, @Param("followingId") Integer followingId);

	@Query(value = "select f.follower from Follow f where f.following.userId = :userId")
	public List<User> getFollower(@Param("userId") Integer userId);

	@Query(value = "select f.following from Follow f where f.follower.userId = :userId")
	public List<User> getFollowing(@Param("userId") Integer userId);

	@Query("select count(f) from Follow f where f.following = :user")
	int countByFollowing(@Param("user") User user);

	@Query("select count(f) from Follow f where f.follower = :user")
	int countByFollower(@Param("user") User user);
}
