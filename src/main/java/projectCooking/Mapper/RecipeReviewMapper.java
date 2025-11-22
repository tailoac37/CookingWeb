package projectCooking.Mapper;


import projectCooking.Model.RecipeReviewDTO;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.RecipeReview;
import projectCooking.Service.JWTService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecipeReviewMapper {
    @Autowired
    JWTService jwt ;
    @Autowired
    UserRepo userRepo ; 
    public  RecipeReviewDTO toResponse(RecipeReview entity , String token ) {
        if (entity == null) {
            return null;
        }
        
        RecipeReviewDTO response = new RecipeReviewDTO();
        response.setReviewId(entity.getReviewId());
        response.setRecipeId(entity.getRecipe().getRecipeId());
        response.setRecipeName(entity.getRecipe().getTitle());
        response.setUserId(entity.getUser().getUserId());
        response.setUserName(entity.getUser().getUserName());
        response.setUserAvatar(entity.getUser().getAvatarUrl());
//        response.setRating(entity.getRating());
        response.setTitle(entity.getTitle());
        response.setReviewContent(entity.getReviewContent());
        if(token !=null)
        {
        	String userName =  jwt.extractUserName(token)  ; 
        	if( userName.equals(entity.getUser().getUserName())) 
        	{
        		response.setDelete(true);
        		response.setChange(true);
        	}
        	if(userName.equals(entity.getRecipe().getUser().getUserName()))
        	{
        		response.setDelete(true);
        	}
        	
        }
        // Convert comma-separated string to List
        if (entity.getUserImages() != null && !entity.getUserImages().isEmpty()) {
            response.setUserImages(Arrays.asList(entity.getUserImages().split(",")));
        } else {
            response.setUserImages(Collections.emptyList());
        }
        
//        response.setFollowedRecipeExactly(entity.getFollowedRecipeExactly());
//        response.setModifications(entity.getModifications());
//        response.setDifficultyLevel(entity.getDifficultyLevel());
//        response.setActualCookingTime(entity.getActualCookingTime());
//        response.setWouldMakeAgain(entity.getWouldMakeAgain());
//        response.setHelpfulCount(entity.getHelpfulCount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        
        return response;
    }
    
    

    public  List<RecipeReviewDTO> toResponseList(List<RecipeReview> entities, String token) {
        return entities.stream()
                .map(entity -> toResponse(entity, token)) 
                .collect(Collectors.toList());
    }
}
