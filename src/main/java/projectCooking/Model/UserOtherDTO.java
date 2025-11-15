package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class UserOtherDTO {
	private Integer userId  ; 
	private long totalRecipes , followerCount , followingCount , totalLike , totalView ;  
	private LocalDate createdAt, updateAt  ; 
	private String userName , email , avatarUrl , bio , fullName  ; 
	private List<RecipesDTO>  myRecipe ;
	private boolean isFollowing = false  , isFollower = false ; 
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public long getTotalRecipes() {
		return totalRecipes;
	}
	public void setTotalRecipes(long totalRecipes) {
		this.totalRecipes = totalRecipes;
	}
	public long getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(long followerCount) {
		this.followerCount = followerCount;
	}
	public long getFollowingCount() {
		return followingCount;
	}
	public void setFollowingCount(long followingcount) {
		this.followingCount = followingcount;
	}
	public long getTotalLike() {
		return totalLike;
	}
	public void setTotalLike(long totalLike) {
		this.totalLike = totalLike;
	}
	public long getTotalView() {
		return totalView;
	}
	public void setTotalView(long totalView) {
		this.totalView = totalView;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDate getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(LocalDate updateAt) {
		this.updateAt = updateAt;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<RecipesDTO> getMyRecipe() {
		return myRecipe;
	}
	public void setMyRecipe(List<RecipesDTO> myRecipe) {
		this.myRecipe = myRecipe;
	}
	public boolean isFollowing() {
		return isFollowing;
	}
	public void setFollowing(boolean isFollwing) {
		this.isFollowing = isFollwing;
	}
	public boolean isFollower() {
		return isFollower;
	}
	public void setFollower(boolean isFollower) {
		this.isFollower = isFollower;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}

	
	
}
