package projectCooking.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.RecipesDTO;
import projectCooking.Model.UserDTO;
import projectCooking.Model.UserOtherDTO;
import projectCooking.Model.UserProfileDTO;
import projectCooking.Model.UserReviewWithRecipeDTO;
import projectCooking.Request.UserRequest;

public interface UserProfileService {
	public UserProfileDTO GetProfile(String token);

	public String UpdateUser(String token, UserRequest userRequest, MultipartFile image) throws IOException;

	public UserOtherDTO getProfileUserOther(Integer Id, String token);

	public List<UserDTO> resultSearch(String find);

	public List<UserReviewWithRecipeDTO> getUserReviews(String token);
}
