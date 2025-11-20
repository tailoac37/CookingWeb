package projectCooking.Model;

import java.util.Map;

public class EngagementStatisticsResponse {
    private int totalLikes;
    private int totalViews;
    private int totalComments;
    private double likeRate;
    private double commentRate;
    private Map<String, Integer> engagementDistribution;
    
    public int getTotalLikes() { return totalLikes; }
    public void setTotalLikes(int totalLikes) { this.totalLikes = totalLikes; }
    
    public int getTotalViews() { return totalViews; }
    public void setTotalViews(int totalViews) { this.totalViews = totalViews; }
    
    public int getTotalComments() { return totalComments; }
    public void setTotalComments(int totalComments) { this.totalComments = totalComments; }
    
    public double getLikeRate() { return likeRate; }
    public void setLikeRate(double likeRate) { this.likeRate = likeRate; }
    
    public double getCommentRate() { return commentRate; }
    public void setCommentRate(double commentRate) { this.commentRate = commentRate; }
    
    public Map<String, Integer> getEngagementDistribution() { return engagementDistribution; }
    public void setEngagementDistribution(Map<String, Integer> engagementDistribution) { 
        this.engagementDistribution = engagementDistribution; 
    }
}
