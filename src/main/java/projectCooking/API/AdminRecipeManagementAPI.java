package projectCooking.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projectCooking.Model.AdminRecipeListResponse;
import projectCooking.Request.RecipeApprovalRequest;
import projectCooking.Request.RecipeFilterRequest;
import projectCooking.Service.AdminRecipeService;

@RestController
@RequestMapping("/api/admin/recipes")
public class AdminRecipeManagementAPI {

    @Autowired
    private AdminRecipeService adminRecipeService;

    /**
     * Get recipes with filters
     */
    @GetMapping
    public ResponseEntity<?> getRecipes(
            @RequestHeader("Authorization") String auth,
            @ModelAttribute RecipeFilterRequest filter) {
        try {
            // Extract token from Authorization header
            String token = auth.replace("Bearer ", "").trim();
            AdminRecipeListResponse response = adminRecipeService.getRecipes(token, filter);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Approve a recipe
     */
    @PostMapping("/{recipeId}/approve")
    public ResponseEntity<String> approveRecipe(
            @RequestHeader("Authorization") String auth,
            @PathVariable Integer recipeId,
            @RequestBody(required = false) RecipeApprovalRequest request) {
        try {
            // Extract token from Authorization header
            String token = auth.replace("Bearer ", "").trim();
            String result = adminRecipeService.approveRecipe(token, recipeId, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Reject a recipe
     */
    @PostMapping("/{recipeId}/reject")
    public ResponseEntity<String> rejectRecipe(
            @RequestHeader("Authorization") String auth,
            @PathVariable Integer recipeId,
            @RequestBody(required = false) RecipeApprovalRequest request) {
        try {
            // Extract token from Authorization header
            String token = auth.replace("Bearer ", "").trim();
            String result = adminRecipeService.rejectRecipe(token, recipeId, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
