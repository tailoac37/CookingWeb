package projectCooking.Service;

import projectCooking.Model.AdminRecipeListResponse;
import projectCooking.Request.RecipeApprovalRequest;
import projectCooking.Request.RecipeFilterRequest;

public interface AdminRecipeService {

    /**
     * Get recipes with filters and pagination
     */
    AdminRecipeListResponse getRecipes(String token, RecipeFilterRequest filter);

    /**
     * Approve a recipe
     */
    String approveRecipe(String token, Integer recipeId, RecipeApprovalRequest request);

    /**
     * Reject a recipe
     */
    String rejectRecipe(String token, Integer recipeId, RecipeApprovalRequest request);

}
