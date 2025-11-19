package projectCooking.API;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.FoodAIReponse;
import projectCooking.Model.RecipesDTO;
import projectCooking.Service.AIService;

@RestController
@RequestMapping(
    value = "/api/find",
    produces = MediaType.APPLICATION_JSON_VALUE  
)
public class AIAPI {

    @Autowired
    private AIService aiService;

    @PostMapping(
        value = "/AI",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE   
    )
    public ResponseEntity<List<RecipesDTO>> detectFood(@RequestHeader(value ="Authorization" , required =false) String auth ,@RequestParam("file") MultipartFile file) {
        try {
        	
            String token = null ; 
            if(auth !=null)
            {
            	token = auth.replace("Bearer", "") ; 
            }

            FoodAIReponse response = aiService.detectIngredients(file);
            String ingredients = response.getData().getIngredients_vi()  ; 
            List<String> list = Arrays.asList(ingredients.split(","))
            	    .stream()
            	    .map(String::trim)
            	    .collect(Collectors.toList());
            return ResponseEntity.ok(aiService.searchByIngredients(list , token));   
        } 
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
