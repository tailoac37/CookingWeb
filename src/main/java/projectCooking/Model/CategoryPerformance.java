package projectCooking.Model;

public class CategoryPerformance {
    private int recipeCount;
    private int totalViews;
    private int totalLikes;
    private double averageViews;
    private double averageLikes;
    
    public int getRecipeCount() { return recipeCount; }
    public void setRecipeCount(int recipeCount) { this.recipeCount = recipeCount; }
    
    public int getTotalViews() { return totalViews; }
    public void setTotalViews(int totalViews) { this.totalViews = totalViews; }
    
    public int getTotalLikes() { return totalLikes; }
    public void setTotalLikes(int totalLikes) { this.totalLikes = totalLikes; }
    
    public double getAverageViews() { return averageViews; }
    public void setAverageViews(double averageViews) { this.averageViews = averageViews; }
    
    public double getAverageLikes() { return averageLikes; }
    public void setAverageLikes(double averageLikes) { this.averageLikes = averageLikes; }
}