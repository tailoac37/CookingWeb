package projectCooking.Model;

public class UserStatisticsResponse {
	    private long totalRecipes;
	    private long approvedRecipes;
	    private long pendingRecipes;
	    private long rejectedRecipes;
	    private long totalLikes;
	    private long totalViews;
	    private long totalFollowers;
	    private long totalFollowing;
	    private double engagementRate;
		public long getTotalRecipes() {
			return totalRecipes;
		}
		public void setTotalRecipes(long totalRecipes) {
			this.totalRecipes = totalRecipes;
		}
		public long getApprovedRecipes() {
			return approvedRecipes;
		}
		public void setApprovedRecipes(long approvedRecipes) {
			this.approvedRecipes = approvedRecipes;
		}
		public long getPendingRecipes() {
			return pendingRecipes;
		}
		public void setPendingRecipes(long pendingRecipes) {
			this.pendingRecipes = pendingRecipes;
		}
		public long getRejectedRecipes() {
			return rejectedRecipes;
		}
		public void setRejectedRecipes(long rejectedRecipes) {
			this.rejectedRecipes = rejectedRecipes;
		}
		public long getTotalLikes() {
			return totalLikes;
		}
		public void setTotalLikes(long totalLikes) {
			this.totalLikes = totalLikes;
		}
		public long getTotalViews() {
			return totalViews;
		}
		public void setTotalViews(long totalViews) {
			this.totalViews = totalViews;
		}
		public long getTotalFollowers() {
			return totalFollowers;
		}
		public void setTotalFollowers(long totalFollowers) {
			this.totalFollowers = totalFollowers;
		}
		public long getTotalFollowing() {
			return totalFollowing;
		}
		public void setTotalFollowing(long totalFollowing) {
			this.totalFollowing = totalFollowing;
		}
		public double getEngagementRate() {
			return engagementRate;
		}
		public void setEngagementRate(double engagementRate) {
			this.engagementRate = engagementRate;
		}
	    
}
