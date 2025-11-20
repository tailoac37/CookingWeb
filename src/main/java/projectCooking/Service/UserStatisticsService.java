package projectCooking.Service;

import projectCooking.Model.CategoryStatisticsResponse;
import projectCooking.Model.EngagementStatisticsResponse;
import projectCooking.Model.GrowthStatisticsResponse;
import projectCooking.Model.RecipeStatisticsResponse;
import projectCooking.Model.SocialStatisticsResponse;
import projectCooking.Model.TimelineStatisticsResponse;
import projectCooking.Model.UserStatisticsResponse;
import projectCooking.Model.TopRecipesResponse ; 

public interface UserStatisticsService {
	public UserStatisticsResponse getOverviewStatistics(Integer Id) ; 
	public RecipeStatisticsResponse getRecipeStatistics(Integer Id)  ;
	public TimelineStatisticsResponse getTimelineStatistics(Integer Id, int days) ; 
	public TopRecipesResponse getTopRecipes(Integer Id, String sortBy, int limit) ; 
	public EngagementStatisticsResponse getEngagementStatistics(Integer Id)  ; 
	public CategoryStatisticsResponse getCategoryStatistics(Integer Id)  ; 
	public SocialStatisticsResponse getSocialStatistics(Integer Id)  ; 
	public GrowthStatisticsResponse getGrowthStatistics(Integer Id, int days) ; 
} 
