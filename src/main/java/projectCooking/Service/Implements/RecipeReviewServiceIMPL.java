package projectCooking.Service.Implements;

import java.io.IOException;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.Arrays;
import java.util.List;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Mapper.RecipeReviewMapper;
import projectCooking.Model.RecipeReviewDTO;
import projectCooking.Repository.RecipeReviewRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.RecipeReview;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.RecipeReviewRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.NotificationService;
import projectCooking.Service.RecipeReviewService;
import projectCooking.Service.CloudinaryService.CloudinaryService;
@Service
public class RecipeReviewServiceIMPL implements RecipeReviewService {
	@Autowired
	Cloudinary cloudinary ; 
	@Autowired
	RecipeReviewRepo reviewRepo  ;
	@Autowired
	RecipesRepo recipeRepo ; 
	@Autowired
	JWTService jwt ; 
	@Autowired
	UserRepo userRepo ; 
	@Autowired
	ModelMapper model ; 
	@Autowired
	NotificationService notifService ; 
	@Autowired
	RecipeReviewMapper mapper ; 
	@Autowired
	CloudinaryService cloudinaryService  ; 
	@Override
	public String sendReview(String token, Integer Id, RecipeReviewRequest review , List<MultipartFile> images ) throws IOException {
		Recipe recipe = recipeRepo.findById(Id).orElse(null) ; 
		if(recipe== null )
		{
			return "bai viet nay khong ton tai "  ; 
		}
		String userName = jwt.extractUserName(token)  ; 
		User user = userRepo.findByUserName(userName)  ; 
		if(user == null )
		{
			return " nguoi dung nay khong ton tai " ; 
		}
		if(recipe.getUser().getUserName().equals(user.getUserName()))
		{
			return "Ban la nguoi tao ra bai viet nen khong co quyen danh gia san pham cua ban"  ; 
		}
		RecipeReview reviewData = model.map(review , RecipeReview.class)  ; 
		reviewData.setUser(user);
		reviewData.setRecipe(recipe);
		StringBuilder listImage = new StringBuilder() ; 
		for(MultipartFile image : images)
		{
			Map uploadImage = cloudinary.uploader().upload(image.getBytes(),ObjectUtils.emptyMap() )  ; 
			String imageURL = (String) uploadImage.get("secure_url") ; 
			if(listImage.length() > 0) {
		        listImage.append(","); 
		    }
		    
			listImage.append(imageURL) ;
		}
		
		reviewData.setUserImages(listImage.toString());
		reviewRepo.save(reviewData) ; 
		notifService.RatingNotification(user, recipe.getUser(), recipe, reviewData.getReviewContent());
		return "done";
	}
	@Override
	public List<RecipeReviewDTO> getReview(String token, Integer Id) {
		Recipe recipe = recipeRepo.findById(Id).orElse(null)  ; 
		if(recipe == null) 
		{
			throw new DulicateUserException("Bai viet nay khong ton tai , vui long chon lai bai viet")  ; 
		}
		List<RecipeReview> reviewList = recipe.getRatings()  ; 
		List<RecipeReviewDTO> reviewDTO = mapper.toResponseList(reviewList, token);
		return reviewDTO; 
	}
	@Override
	public String delReview(String token, Integer Id, String reason) {
		String userName = jwt.extractUserName(token) ; 
		User user = userRepo.findByUserName(userName)  ; 
		if(user==null)
		{
			return "nguoi dung khong tim thay"  ; 
		}
		RecipeReview review = reviewRepo.findById(Id).orElse(null)  ; 
		if(review == null) 
		{
			return "danh gia nay da bi xoa truoc roi " ;
		}
		if(!review.getUser().getUserName().equals(userName) && !review.getRecipe().getUser().getUserName().equals(userName))
		{
			return "ban khong quyen de xoa"  ; 
		}
		List<String> images = Arrays.asList(review.getUserImages().split(","));
		for(String image : images)
		{
			cloudinaryService.deleteImageByUrl(image) ; 
		}
		
		if(review.getRecipe().getUser().getUserName().equals(userName))
		{
			notifService.delRatingNotification(user, review.getUser(),review.getRecipe(), reason);
		}
		reviewRepo.delete(review);
		return "done";
	}

}
