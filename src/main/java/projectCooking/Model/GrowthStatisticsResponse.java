package projectCooking.Model;

public class GrowthStatisticsResponse {
    private int periodDays;
    private int newRecipes;
    private double recipeGrowthRate;
    
    public int getPeriodDays() { return periodDays; }
    public void setPeriodDays(int periodDays) { this.periodDays = periodDays; }
    
    public int getNewRecipes() { return newRecipes; }
    public void setNewRecipes(int newRecipes) { this.newRecipes = newRecipes; }
    
    public double getRecipeGrowthRate() { return recipeGrowthRate; }
    public void setRecipeGrowthRate(double recipeGrowthRate) { this.recipeGrowthRate = recipeGrowthRate; }
}
