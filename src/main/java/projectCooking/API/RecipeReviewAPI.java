package projectCooking.API;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.RecipeReviewDTO;
import projectCooking.Request.RecipeReviewRequest;
import projectCooking.Service.RecipeReviewService;

@RestController
public class RecipeReviewAPI {
	@Autowired
	RecipeReviewService service ; 
	@PostMapping("/api/user/review/{id}")
	public String sendReview(@RequestHeader("Authorization") String auth , @PathVariable("id") Integer Id ,  @RequestPart("review") RecipeReviewRequest review  , @RequestPart("images") List<MultipartFile> image ) throws IOException 
	{
		String token = auth.replace("Bearer ", "") ;
		return service.sendReview(token, Id, review, image) ;
	}
	@GetMapping("/api/review/{id}")
	public List<RecipeReviewDTO> getReviewByRecipe(@RequestHeader(value="Authorization" , required = false)  String auth , @PathVariable("id") Integer Id )
	{
		String token = null ; 
		if(auth!=null)
		{
			token = auth.replace("Bearer ", "")  ; 
		}
		
		return service.getReview(token, Id)  ; 
	}
	@DeleteMapping("/api/user/review/{id}")
	public String delReview(@RequestHeader("Authorization") String auth , @PathVariable("id") Integer Id , @RequestBody String reason)
	{
		String token = auth.replace("Bearer ", "") ;
		return service.delReview(token, Id, reason) ; 
	}
}
