package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class UserProfileDTO {
	private Integer userId  ; 
	private long totalRecipes , followerCount , followingcount , totalLike , totalView , pendingRecipes , approvedRecipes , rejectedRecipes  ; 
	private LocalDate createdAt, updateAt  ; 
	private String role, userName , email , avatarUrl , bio , fullName  ; 
	private boolean isActive ; 
	private List<RecipesDTO> Favorites ;
	private List<RecipesDTO>  MyRecipe ; 
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
	public long getPendingRecipes() {
		return pendingRecipes;
	}
	public void setPendingRecipes(long pendingRecipes) {
		this.pendingRecipes = pendingRecipes;
	}
	public long getApprovedRecipes() {
		return approvedRecipes;
	}
	public void setApprovedRecipes(long approvedRecipes) {
		this.approvedRecipes = approvedRecipes;
	}
	public long getRejectedRecipes() {
		return rejectedRecipes;
	}
	public void setRejectedRecipes(long rejectedRecipes) {
		this.rejectedRecipes = rejectedRecipes;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public List<RecipesDTO> getFavorites() {
		return Favorites;
	}
	public void setFavorites(List<RecipesDTO> favorites) {
		Favorites = favorites;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public List<RecipesDTO> getMyRecipe() {
		return MyRecipe;
	}
	public void setMyRecipe(List<RecipesDTO> myRecipe) {
		MyRecipe = myRecipe;
	} 
	
	
}
