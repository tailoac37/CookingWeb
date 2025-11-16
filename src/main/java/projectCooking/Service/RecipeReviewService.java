package projectCooking.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.RecipeReviewDTO;
import projectCooking.Request.RecipeReviewRequest;

public interface RecipeReviewService {
	 public String sendReview(String token , Integer Id , RecipeReviewRequest review , List<MultipartFile> images ) throws IOException ;
	 public List<RecipeReviewDTO> getReview(String token ,Integer Id ) ; 
	 public String delReview(String token , Integer Id , String reason)  ; 
}
