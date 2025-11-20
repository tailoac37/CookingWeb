package projectCooking.Model;

import java.util.Map;

public class RecipeStatisticsResponse {
	private int totalRecipes;
    private Map<String, Long> statusBreakdown;
    private Map<String, Long> difficultyBreakdown;
    private double averageViews;
    private double averageLikes;
	public int getTotalRecipes() {
		return totalRecipes;
	}
	public void setTotalRecipes(int totalRecipes) {
		this.totalRecipes = totalRecipes;
	}
	public Map<String, Long> getStatusBreakdown() {
		return statusBreakdown;
	}
	public void setStatusBreakdown(Map<String, Long> statusBreakdown) {
		this.statusBreakdown = statusBreakdown;
	}
	public Map<String, Long> getDifficultyBreakdown() {
		return difficultyBreakdown;
	}
	public void setDifficultyBreakdown(Map<String, Long> difficultyBreakdown) {
		this.difficultyBreakdown = difficultyBreakdown;
	}
	public double getAverageViews() {
		return averageViews;
	}
	public void setAverageViews(double averageViews) {
		this.averageViews = averageViews;
	}
	public double getAverageLikes() {
		return averageLikes;
	}
	public void setAverageLikes(double averageLikes) {
		this.averageLikes = averageLikes;
	}
    
}
