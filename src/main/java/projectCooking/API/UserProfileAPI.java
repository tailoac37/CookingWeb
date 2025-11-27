package projectCooking.API;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.RecipesDTO;
import projectCooking.Model.UserDTO;
import projectCooking.Model.UserOtherDTO;
import projectCooking.Model.UserProfileDTO;
import projectCooking.Model.UserReviewWithRecipeDTO;
import projectCooking.Request.UserRequest;
import projectCooking.Service.UserProfileService;

@RestController
public class UserProfileAPI {
	@Autowired
	private UserProfileService service;

	// lay thong tin cua minh
	@GetMapping("/api/user/me")
	public UserProfileDTO getUser(@RequestHeader("Authorization") String auth) {
		String token = auth.replace("Bearer ", "").trim();
		return service.GetProfile(token);
	}

	@PutMapping("/api/user/me")
	public String updateUser(@RequestHeader("Authorization") String auth,
			@RequestPart("UserInfor") UserRequest userRequest,
			@RequestPart(value = "avatar", required = false) MultipartFile image) throws IOException {
		String token = auth.replace("Bearer ", "").trim();
		return service.UpdateUser(token, userRequest, image);
	}

	@GetMapping("/api/getUser/{id}")
	public UserOtherDTO getUserOther(@PathVariable("id") Integer Id,
			@RequestHeader(value = "Authorization", required = false) String auth) {
		String token = null;
		if (auth != null) {
			token = auth.replace("Bearer ", "");
		}
		return service.getProfileUserOther(Id, token);
	}

	@GetMapping("/api/getUser/search")
	public List<UserDTO> resultSearch(@RequestParam("find") String find) {
		return service.resultSearch(find);
	}

	@GetMapping("/api/user/me/reviews")
	public List<UserReviewWithRecipeDTO> getUserReviews(@RequestHeader("Authorization") String auth) {
		String token = auth.replace("Bearer ", "").trim();
		return service.getUserReviews(token);
	}
}
