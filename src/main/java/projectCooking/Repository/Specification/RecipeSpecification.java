package projectCooking.Repository.Specification;

import org.springframework.data.jpa.domain.Specification;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Categories;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Request.RecipeQueryRequest;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification {

    public static Specification<Recipe> filterRecipes(RecipeQueryRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter by APPROVED status
            predicates.add(criteriaBuilder.equal(root.get("status"), Recipe.RecipeStatus.APPROVED));

            // Filter by categories (multiple values with OR)
            if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                Join<Recipe, Categories> categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(categoryJoin.get("name").in(request.getCategory()));
            }

            // Filter by titles (LIKE search with OR - search any title matches)
            if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                List<Predicate> titlePredicates = new ArrayList<>();
                for (String title : request.getTitle()) {
                    if (title != null && !title.trim().isEmpty()) {
                        titlePredicates.add(
                            criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + title.toLowerCase().trim() + "%"
                            )
                        );
                    }
                }
                if (!titlePredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(titlePredicates.toArray(new Predicate[0])));
                }
            }

            // Filter by ingredients (LIKE search with OR - recipe contains any of the ingredients)
            if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
                List<Predicate> ingredientPredicates = new ArrayList<>();
                for (String ingredient : request.getIngredients()) {
                    if (ingredient != null && !ingredient.trim().isEmpty()) {
                        ingredientPredicates.add(
                            criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("ingredients")),
                                "%" + ingredient.toLowerCase().trim() + "%"
                            )
                        );
                    }
                }
                if (!ingredientPredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(ingredientPredicates.toArray(new Predicate[0])));
                }
            }

            // Filter by tags (multiple values with OR)
            if (request.getTags() != null && !request.getTags().isEmpty()) {
                Join<Recipe, Tags> tagsJoin = root.join("tags", JoinType.LEFT);
                predicates.add(tagsJoin.get("name").in(request.getTags()));
            }

            // Filter by prepTime (less than or equal)
            if (request.getPrepTime() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("prepTime"), request.getPrepTime())
                );
            }

            // Filter by cookTime (less than or equal)
            if (request.getCookTime() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(root.get("cookTime"), request.getCookTime())
                );
            }

            // Filter by servings (greater than or equal)
            if (request.getServings() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("servings"), request.getServings())
                );
            }

            // Filter by difficulty levels (multiple values with OR)
            if (request.getLevel() != null && !request.getLevel().isEmpty()) {
                predicates.add(root.get("difficultyLevel").in(request.getLevel()));
            }

            // Ensure DISTINCT results (important when joining with collections like tags)
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
