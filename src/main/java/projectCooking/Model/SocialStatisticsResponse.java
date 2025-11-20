package projectCooking.Model;

import java.util.List;

public class SocialStatisticsResponse {
    private long followerCount;
    private long followingCount;
    private List<UserSummary> recentFollowers;
    
    public long getFollowerCount() { return followerCount; }
    public void setFollowerCount(long followerCount) { this.followerCount = followerCount; }
    
    public long getFollowingCount() { return followingCount; }
    public void setFollowingCount(long followingCount) { this.followingCount = followingCount; }
    
    public List<UserSummary> getRecentFollowers() { return recentFollowers; }
    public void setRecentFollowers(List<UserSummary> recentFollowers) { 
        this.recentFollowers = recentFollowers; 
    }
}
