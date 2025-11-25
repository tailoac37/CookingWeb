package projectCooking.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projectCooking.Repository.Entity.Recipe;

public interface RecipesRepo extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {

        @Query(value = "select count(*) from Recipe r where r.user.id = :id ")
        public long getCountRecipeByUser(@Param("id") Integer id);

        @Query(value = "select count(*) from Recipe r where r.user.id = :id and r.status =:status")
        public long getCountRecipeByUserAndStatus(@Param("id") Integer id, @Param("status") Recipe.RecipeStatus status);

        @Query(value = "select r from Recipe r where r.status =:status")
        public List<Recipe> getListRecipes(@Param("status") Recipe.RecipeStatus status);

        @Query("SELECT DISTINCT r FROM Recipe r " +
                        "LEFT JOIN r.tags t " +
                        "WHERE (:title IS NULL OR :title = '' OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
                        +
                        "(:category IS NULL OR :category = '' OR r.category.name = :category) AND " +
                        "(:difficulty IS NULL OR r.difficultyLevel = :difficulty) AND " +
                        "(:tags IS NULL OR :tags = '' OR t.name = :tags) AND " +
                        "r.status ='APPROVED'")
        List<Recipe> searchRecipes(@Param("title") String title,
                        @Param("category") String category,
                        @Param("difficulty") Recipe.DifficultyLevel difficulty,
                        @Param("tags") String tags);

        @Query("select r from Recipe r where r.status='APPROVED' ORDER BY r.likeCount DESC")
        public List<Recipe> popular();

        @Query("select r from Recipe r where r.status='APPROVED' ORDER BY r.viewCount DESC")
        public List<Recipe> trending();

        @Query("select r from Recipe r where r.status =:status AND r.user.userName =:username")
        public List<Recipe> getRecipesByStatusAndUser(@Param("status") Recipe.RecipeStatus status,
                        @Param("username") String username);

        @Query("select r from Recipe r where r.user.userName =:username")
        public List<Recipe> getRecipesByUser(@Param("username") String username);

        @Query("SELECT r FROM Recipe r WHERE r.status = 'APPROVED'")
        List<Recipe> findAllApproved();

        @Query("select coalesce(sum(r.viewCount), 0) from Recipe r where r.user.userName = :username")
        public long totalViewByUser(@Param("username") String username);

        @Query("select count(r) from Recipe r where r.user = :user")
        int countByUser(@Param("user") projectCooking.Repository.Entity.User user);
}