package projectCooking.Model;

import java.util.List;

public class TopRecipesResponse {
    private String sortBy;
    private int limit;
    private List<RecipeSummary> recipes;
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
    
    public List<RecipeSummary> getRecipes() { return recipes; }
    public void setRecipes(List<RecipeSummary> recipes) { this.recipes = recipes; }
}
