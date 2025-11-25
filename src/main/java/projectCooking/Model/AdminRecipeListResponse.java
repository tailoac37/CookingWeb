package projectCooking.Model;

import java.util.List;

public class AdminRecipeListResponse {
    private List<RecipesDTO> recipes;
    private Integer totalRecipes;
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    public AdminRecipeListResponse() {
    }

    public AdminRecipeListResponse(List<RecipesDTO> recipes, Integer totalRecipes,
            Integer currentPage, Integer totalPages, Integer pageSize) {
        this.recipes = recipes;
        this.totalRecipes = totalRecipes;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }

    public List<RecipesDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipesDTO> recipes) {
        this.recipes = recipes;
    }

    public Integer getTotalRecipes() {
        return totalRecipes;
    }

    public void setTotalRecipes(Integer totalRecipes) {
        this.totalRecipes = totalRecipes;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
