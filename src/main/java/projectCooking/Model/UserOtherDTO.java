package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class UserOtherDTO {
	private Integer userId  ; 
	private long totalRecipes , followerCount , followingcount , totalLike , totalView ;  
	private LocalDate createdAt, updateAt  ; 
	private String username , email , avatarUrl , bio , fullname  ; 
	private List<RecipesDTO>  MyRecipe ;
	private boolean is_follwing = false  , is_follower = false ; 
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
	public long getFollowingcount() {
		return followingcount;
	}
	public void setFollowingcount(long followingcount) {
		this.followingcount = followingcount;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public List<RecipesDTO> getMyRecipe() {
		return MyRecipe;
	}
	public void setMyRecipe(List<RecipesDTO> myRecipe) {
		MyRecipe = myRecipe;
	}
	public boolean isIs_follwing() {
		return is_follwing;
	}
	public void setIs_follwing(boolean is_follwing) {
		this.is_follwing = is_follwing;
	}
	public boolean isIs_follower() {
		return is_follower;
	}
	public void setIs_follower(boolean is_follower) {
		this.is_follower = is_follower;
	} 
	
	
}
