package projectCooking.Response;

import java.util.List;

import projectCooking.Model.RecipesDTO;

public class ChatResponse {
    private String response;
    private List<RecipesDTO> recipes;
    private String functionCalled;

    public ChatResponse() {
    }

    public ChatResponse(String response) {
        this.response = response;
    }

    public ChatResponse(String response, List<RecipesDTO> recipes, String functionCalled) {
        this.response = response;
        this.recipes = recipes;
        this.functionCalled = functionCalled;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<RecipesDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipesDTO> recipes) {
        this.recipes = recipes;
    }

    public String getFunctionCalled() {
        return functionCalled;
    }

    public void setFunctionCalled(String functionCalled) {
        this.functionCalled = functionCalled;
    }
}
