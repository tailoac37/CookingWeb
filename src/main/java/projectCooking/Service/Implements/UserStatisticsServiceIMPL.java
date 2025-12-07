package projectCooking.Service.Implements;

import projectCooking.Service.UserStatisticsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Model.CategoryPerformance;
import projectCooking.Model.CategoryStatisticsResponse;
import projectCooking.Model.EngagementStatisticsResponse;
import projectCooking.Model.GrowthStatisticsResponse;
import projectCooking.Model.RecipeStatisticsResponse;
import projectCooking.Model.RecipeSummary;
import projectCooking.Model.SocialStatisticsResponse;
import projectCooking.Model.TimelineStatisticsResponse;
import projectCooking.Model.TopRecipesResponse;
import projectCooking.Model.UserStatisticsResponse;
import projectCooking.Model.UserSummary;
import projectCooking.Repository.*;
import projectCooking.Repository.Entity.*;

@Service
public class UserStatisticsServiceIMPL implements UserStatisticsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RecipesRepo recipesRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private ViewRepo viewRepo;

    @Autowired
    private FollowRepo followRepo;

    @Autowired
    private FavoriteRepo favoriteRepo;

    public UserStatisticsResponse getOverviewStatistics(Integer Id) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        UserStatisticsResponse response = new UserStatisticsResponse();

        // Thống kê cơ bản
        response.setTotalRecipes(recipesRepo.getCountRecipeByUser(user.getUserId()));
        response.setApprovedRecipes(
                recipesRepo.getCountRecipeByUserAndStatus(user.getUserId(), Recipe.RecipeStatus.APPROVED));
        response.setPendingRecipes(
                recipesRepo.getCountRecipeByUserAndStatus(user.getUserId(), Recipe.RecipeStatus.PENDING));
        response.setRejectedRecipes(
                recipesRepo.getCountRecipeByUserAndStatus(user.getUserId(), Recipe.RecipeStatus.REJECTED));

        // Engagement
        response.setTotalLikes(likeRepo.getTotalLikeByUser(username));
        response.setTotalViews(recipesRepo.totalViewByUser(username));
        response.setTotalFollowers(followRepo.getCountUserFollower(username));
        response.setTotalFollowing(followRepo.getCountUserFollowing(username));

        // Tính engagement rate
        if (response.getTotalViews() > 0) {
            double engagementRate = ((double) response.getTotalLikes() / response.getTotalViews()) * 100;
            response.setEngagementRate(Math.round(engagementRate * 100.0) / 100.0);
        }

        return response;
    }

    /**
     * Thống kê chi tiết về recipes
     */
    public RecipeStatisticsResponse getRecipeStatistics(Integer Id) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        List<Recipe> recipes = recipesRepo.getRecipesByUser(username);

        RecipeStatisticsResponse response = new RecipeStatisticsResponse();
        response.setTotalRecipes(recipes.size());

        // Thống kê theo status
        Map<String, Long> statusBreakdown = recipes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getStatus() == null ? "UNKNOWN" : r.getStatus().name(),
                        Collectors.counting()));
        response.setStatusBreakdown(statusBreakdown);

        // Thống kê theo difficulty level
        Map<String, Long> difficultyBreakdown = recipes.stream()
                .filter(r -> r.getDifficultyLevel() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getDifficultyLevel() == null ? "UNKNOWN" : r.getDifficultyLevel().name(),
                        Collectors.counting()));
        response.setDifficultyBreakdown(difficultyBreakdown);

        // Average metrics cho approved recipes
        List<Recipe> approvedRecipes = recipes.stream()
                .filter(r -> r.getStatus() == Recipe.RecipeStatus.APPROVED)
                .collect(Collectors.toList());

        if (!approvedRecipes.isEmpty()) {
            double avgViews = approvedRecipes.stream().mapToInt(Recipe::getViewCount).average().orElse(0);
            double avgLikes = approvedRecipes.stream().mapToInt(Recipe::getLikeCount).average().orElse(0);

            response.setAverageViews(Math.round(avgViews * 100.0) / 100.0);
            response.setAverageLikes(Math.round(avgLikes * 100.0) / 100.0);
        }

        return response;
    }

    /**
     * Thống kê theo timeline
     */
    public TimelineStatisticsResponse getTimelineStatistics(Integer Id, int days) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        List<Recipe> recipes = recipesRepo.getRecipesByUser(username);

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);

        TimelineStatisticsResponse response = new TimelineStatisticsResponse();
        response.setPeriodDays(days);

        // Recipes created trong period
        List<Recipe> recentRecipes = recipes.stream()
                .filter(r -> r.getCreatedAt().isAfter(cutoffDate))
                .collect(Collectors.toList());
        response.setRecipesCreated(recentRecipes.size());

        // Group by date
        Map<String, Long> dailyRecipes = recentRecipes.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt() == null ? "Unknown" : r.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()));
        response.setDailyRecipeCount(dailyRecipes);

        // Likes và views trong period - simplified calculation
        int totalNewLikes = 0;
        int totalNewViews = 0;
        for (Recipe recipe : recentRecipes) {
            totalNewLikes += recipe.getLikeCount();
            totalNewViews += recipe.getViewCount();
        }
        response.setTotalNewLikes(totalNewLikes);
        response.setTotalNewViews(totalNewViews);

        return response;
    }

    /**
     * Top recipes của user
     */
    public TopRecipesResponse getTopRecipes(Integer Id, String sortBy, int limit) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        List<Recipe> recipes = recipesRepo.getRecipesByStatusAndUser(Recipe.RecipeStatus.APPROVED, username);

        List<Recipe> sortedRecipes;
        switch (sortBy.toLowerCase()) {
            case "likes":
                sortedRecipes = recipes.stream()
                        .sorted(Comparator.comparing(Recipe::getLikeCount).reversed())
                        .limit(limit)
                        .collect(Collectors.toList());
                break;
            case "comments":
                sortedRecipes = recipes.stream()
                        .sorted(Comparator.comparing(r -> r.getComments().size(), Comparator.reverseOrder()))
                        .limit(limit)
                        .collect(Collectors.toList());
                break;
            default: // views
                sortedRecipes = recipes.stream()
                        .sorted(Comparator.comparing(Recipe::getViewCount).reversed())
                        .limit(limit)
                        .collect(Collectors.toList());
                break;
        }

        TopRecipesResponse response = new TopRecipesResponse();
        response.setSortBy(sortBy);
        response.setLimit(limit);

        List<RecipeSummary> summaries = sortedRecipes.stream()
                .map(r -> {
                    RecipeSummary summary = new RecipeSummary();
                    summary.setRecipeId(r.getRecipeId());
                    summary.setTitle(r.getTitle());
                    summary.setImageUrl(r.getImageUrl());
                    summary.setViews(r.getViewCount());
                    summary.setLikes(r.getLikeCount());
                    summary.setComments(r.getComments().size());
                    summary.setCreatedAt(r.getCreatedAt());
                    return summary;
                })
                .collect(Collectors.toList());

        response.setRecipes(summaries);
        return response;
    }

    /**
     * Thống kê engagement
     */
    public EngagementStatisticsResponse getEngagementStatistics(Integer Id) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        List<Recipe> recipes = recipesRepo.getRecipesByStatusAndUser(Recipe.RecipeStatus.APPROVED, username);

        EngagementStatisticsResponse response = new EngagementStatisticsResponse();

        int totalLikes = recipes.stream().mapToInt(Recipe::getLikeCount).sum();
        int totalViews = recipes.stream().mapToInt(Recipe::getViewCount).sum();
        int totalComments = recipes.stream().mapToInt(r -> r.getComments().size()).sum();

        response.setTotalLikes(totalLikes);
        response.setTotalViews(totalViews);
        response.setTotalComments(totalComments);

        // Engagement rate
        if (totalViews > 0) {
            double likeRate = ((double) totalLikes / totalViews) * 100;
            double commentRate = ((double) totalComments / totalViews) * 100;
            response.setLikeRate(Math.round(likeRate * 100.0) / 100.0);
            response.setCommentRate(Math.round(commentRate * 100.0) / 100.0);
        }

        // Distribution of engagement per recipe
        Map<String, Integer> engagementDistribution = new HashMap<>();
        for (Recipe recipe : recipes) {
            int totalEngagement = recipe.getLikeCount() + recipe.getComments().size();
            if (totalEngagement < 10) {
                engagementDistribution.merge("0-10", 1, Integer::sum);
            } else if (totalEngagement < 50) {
                engagementDistribution.merge("10-50", 1, Integer::sum);
            } else if (totalEngagement < 100) {
                engagementDistribution.merge("50-100", 1, Integer::sum);
            } else {
                engagementDistribution.merge("100+", 1, Integer::sum);
            }
        }
        response.setEngagementDistribution(engagementDistribution);

        return response;
    }

    /**
     * Thống kê categories
     */
    public CategoryStatisticsResponse getCategoryStatistics(Integer Id) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        List<Recipe> recipes = recipesRepo.getRecipesByUser(username);

        CategoryStatisticsResponse response = new CategoryStatisticsResponse();

        // Count by category
        Map<String, Long> categoryCount = recipes.stream()
                .filter(r -> r.getCategory() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getCategory().getName() == null ? "Uncategorized" : r.getCategory().getName(),
                        Collectors.counting()));
        response.setCategoryBreakdown(categoryCount);

        // Performance by category
        Map<String, CategoryPerformance> categoryPerformance = recipes.stream()
                .filter(r -> r.getCategory() != null && r.getStatus() == Recipe.RecipeStatus.APPROVED)
                .collect(Collectors.groupingBy(
                        r -> r.getCategory().getName() == null ? "Uncategorized" : r.getCategory().getName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    CategoryPerformance perf = new CategoryPerformance();
                                    perf.setRecipeCount(list.size());
                                    perf.setTotalViews(list.stream().mapToInt(Recipe::getViewCount).sum());
                                    perf.setTotalLikes(list.stream().mapToInt(Recipe::getLikeCount).sum());
                                    perf.setAverageViews(
                                            list.stream().mapToInt(Recipe::getViewCount).average().orElse(0));
                                    perf.setAverageLikes(
                                            list.stream().mapToInt(Recipe::getLikeCount).average().orElse(0));
                                    return perf;
                                })));
        response.setCategoryPerformance(categoryPerformance);

        return response;
    }

    /**
     * Thống kê social (followers/following)
     */
    public SocialStatisticsResponse getSocialStatistics(Integer Id) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();

        SocialStatisticsResponse response = new SocialStatisticsResponse();
        response.setFollowerCount(followRepo.getCountUserFollower(username));
        response.setFollowingCount(followRepo.getCountUserFollowing(username));

        // Get recent followers
        List<User> followers = followRepo.getFollower(user.getUserId());
        List<UserSummary> recentFollowers = followers.stream()
                .limit(10)
                .map(f -> {
                    UserSummary summary = new UserSummary();
                    summary.setUserId(f.getUserId());
                    summary.setUsername(f.getUserName());
                    summary.setFullName(f.getFullName());
                    summary.setAvatarUrl(f.getAvatarUrl());
                    return summary;
                })
                .collect(Collectors.toList());
        response.setRecentFollowers(recentFollowers);

        return response;
    }

    /**
     * Thống kê growth
     */
    public GrowthStatisticsResponse getGrowthStatistics(Integer Id, int days) {
        User user = userRepo.findById(Id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String username = user.getUserName();
        List<Recipe> allRecipes = recipesRepo.getRecipesByUser(username);

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);

        GrowthStatisticsResponse response = new GrowthStatisticsResponse();
        response.setPeriodDays(days);

        // New recipes
        long newRecipes = allRecipes.stream()
                .filter(r -> r.getCreatedAt().isAfter(cutoffDate))
                .count();
        response.setNewRecipes((int) newRecipes);

        // Calculate growth rates (simplified - comparing with previous period)
        LocalDateTime previousPeriodStart = cutoffDate.minusDays(days);
        long previousPeriodRecipes = allRecipes.stream()
                .filter(r -> r.getCreatedAt().isAfter(previousPeriodStart) && r.getCreatedAt().isBefore(cutoffDate))
                .count();

        if (previousPeriodRecipes > 0) {
            double recipeGrowthRate = ((double) (newRecipes - previousPeriodRecipes) / previousPeriodRecipes) * 100;
            response.setRecipeGrowthRate(Math.round(recipeGrowthRate * 100.0) / 100.0);
        }

        return response;
    }
}
