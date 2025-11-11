package projectCooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projectCooking.Repository.Entity.Follow;

public interface FollowRepo extends JpaRepository<Follow, Integer> {
	@Query(value ="select count(*) from Follow f where f.follower.userName = :username")
	public long getCountUserFollowing(@Param("username") String username)  ; 
	@Query(value ="select count(*) from Follow f where f.following.userName = :username")
	public long getCountUserFollower(@Param("username") String username)  ; 
	@Query(value="select count(*) from Follow f where f.following.userName = :myUserName AND f.follower.userName = :userNameOther")
	public int checkFollwer(@Param("myUserName") String myUserName , @Param("userNameOther") String userNameOther)  ; 
	@Query(value="select count(*) from Follow f where f.following.userName = :userNameOther AND f.follower.userName = :myUserName")
	public int checkFollwing(@Param("myUserName") String myUserName , @Param("userNameOther") String userNameOther)  ;
}
