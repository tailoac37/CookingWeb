package projectCooking.Model;

import java.util.Map;

public class TimelineStatisticsResponse {
	private int periodDays;
    private int recipesCreated;
    private int totalNewLikes;
    private int totalNewViews;
    private Map<String, Long> dailyRecipeCount;
	public int getPeriodDays() {
		return periodDays;
	}
	public void setPeriodDays(int periodDays) {
		this.periodDays = periodDays;
	}
	public int getRecipesCreated() {
		return recipesCreated;
	}
	public void setRecipesCreated(int recipesCreated) {
		this.recipesCreated = recipesCreated;
	}
	public int getTotalNewLikes() {
		return totalNewLikes;
	}
	public void setTotalNewLikes(int totalNewLikes) {
		this.totalNewLikes = totalNewLikes;
	}
	public int getTotalNewViews() {
		return totalNewViews;
	}
	public void setTotalNewViews(int totalNewViews) {
		this.totalNewViews = totalNewViews;
	}
	public Map<String, Long> getDailyRecipeCount() {
		return dailyRecipeCount;
	}
	public void setDailyRecipeCount(Map<String, Long> dailyRecipeCount) {
		this.dailyRecipeCount = dailyRecipeCount;
	}
    
}
