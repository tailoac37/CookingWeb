package projectCooking.Service.Implements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;

import projectCooking.Model.FoodAIReponse;
import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Service.AIService;
import projectCooking.Service.JWTService;

@Service
public class AIServiceIMPL implements AIService {
	@Autowired
	private RecipesRepo recipeRepo  ; 
	@Autowired
	private ModelMapper model ; 
	@Autowired
	private JWTService jwt ; 
	@Autowired
	private UserRepo userRepo ; 
	@Autowired
	private LikeRepo likeRepo ; 
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public FoodAIReponse detectIngredients(MultipartFile file ) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

       
        ResponseEntity<String> response = restTemplate.exchange(
                "https://taihoangvan-ai-imagefood.hf.space/detect",
                HttpMethod.POST,
                request,
                String.class
        );

        System.out.println("=== RAW AI RESPONSE ===");
        System.out.println(response.getBody());

        
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.readValue(response.getBody(), FoodAIReponse.class);
    }

    @Override
    public List<RecipesDTO> searchByIngredients(List<String> ingredients, String token) {

       
        List<Recipe> all = recipeRepo.findAllApproved();

        List<RecipeMatch> matches = new ArrayList<>();

 
        for (Recipe recipe : all) {
            String ingDb = recipe.getIngredients().toLowerCase();

            int score = 0;

            for (String ing : ingredients) {
                if (ingDb.contains(ing.toLowerCase().trim())) {
                    score++;
                }
            }

            if (score > 0) {
                matches.add(new RecipeMatch(recipe, score));
            }
        }

   
        matches.sort((a, b) -> Integer.compare(b.score, a.score));

 
        List<RecipesDTO> result = new ArrayList<>();

        for (RecipeMatch match : matches) {
            Recipe recipe = match.recipe;

            RecipesDTO dto = model.map(recipe, RecipesDTO.class);

        

            dto.setAvatarUrl(recipe.getUser().getAvatarUrl());
            dto.setUserName(recipe.getUser().getUserName());
            dto.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
            dto.setCreateAt(recipe.getCreatedAt().toLocalDate());

            dto.setCategory(recipe.getCategory().getName());

            // Tags
            Set<String> tagNames = recipe.getTags()
                                         .stream()
                                         .map(Tags::getName)
                                         .collect(Collectors.toSet());
            dto.setTags(tagNames);

            // Ingredients -> List<String>
            dto.setIngredients(
                Arrays.stream(recipe.getIngredients().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList())
            );

            // Like + Change flag
            if (token != null) {
                String userName = jwt.extractUserName(token);

                if (userName != null) {
                    if (userName.equals(recipe.getUser().getUserName())) {
                        dto.setChange(true);
                    }

                    if (likeRepo.getCheckLikeByUser(userName, recipe.getRecipeId()) != null) {
                        dto.setLike(true);
                    }
                }
            }

            result.add(dto);
        }

        return result;
    }


    private static class RecipeMatch {
        Recipe recipe;
        int score;

        RecipeMatch(Recipe r, int s) {
            this.recipe = r;
            this.score = s;
        }
    }

}
