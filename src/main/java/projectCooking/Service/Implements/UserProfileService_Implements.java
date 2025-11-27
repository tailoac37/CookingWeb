package projectCooking.Service.Implements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.RecipeReviewDTO;
import projectCooking.Model.RecipesDTO;
import projectCooking.Model.UserDTO;
import projectCooking.Model.UserOtherDTO;
import projectCooking.Model.UserProfileDTO;
import projectCooking.Model.UserReviewWithRecipeDTO;
import projectCooking.Repository.FavoriteRepo;
import projectCooking.Repository.FollowRepo;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipeReviewRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.ViewRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.RecipeReview;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.UserRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.UserProfileService;
import projectCooking.Service.CloudinaryService.CloudinaryService;

@Service
public class UserProfileService_Implements implements UserProfileService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JWTService jwt;
	@Autowired
	private FavoriteRepo favoriteRepo;
	@Autowired
	private RecipesRepo recipesRepo;
	@Autowired
	private ModelMapper model;
	@Autowired
	private FollowRepo followRepo;
	@Autowired
	private LikeRepo likeRepo;
	@Autowired
	private ViewRepo viewRepo;
	@Autowired
	private Cloudinary cloudinary;
	@Autowired
	private CloudinaryService cloudinaryService;
	@Autowired
	private RecipeReviewRepo recipeReviewRepo;
	private BCryptPasswordEncoder bcry;

	@Override
	public UserProfileDTO GetProfile(String token) {
		String userName = jwt.extractUserName(token);
		User userDataBase = userRepo.findByUserName(userName);
		if (userDataBase == null) {
			throw new DulicateUserException("Token da het han , ban phai dang nhap lai");
		}
		UserProfileDTO userDTO = model.map(userDataBase, UserProfileDTO.class);
		List<Recipe> favorite = favoriteRepo.getRecipeFavoriteByUser(userName);
		List<RecipesDTO> favoriteDTOList = new ArrayList<>();
		for (Recipe item : favorite) {
			RecipesDTO favoriteDTO = model.map(item, RecipesDTO.class);
			favoriteDTO.setUserName(item.getUser().getUserName());
			favoriteDTO.setAvatarUrl(item.getUser().getAvatarUrl());
			favoriteDTO.setCategory(item.getCategory().getName());
			favoriteDTOList.add(favoriteDTO);
		}
		userDTO.setFavorites(favoriteDTOList);
		userDTO.setCreatedAt(userDataBase.getCreatedAt().toLocalDate());
		userDTO.setUpdateAt(userDataBase.getUpdatedAt().toLocalDate());
		userDTO.setTotalRecipes(recipesRepo.getCountRecipeByUser(userDataBase.getUserId()));
		userDTO.setFollowerCount(followRepo.getCountUserFollower(userName));
		userDTO.setFollowingcount(followRepo.getCountUserFollowing(userName));
		List<Recipe> myRecipeDataBase = userDataBase.getRecipes();
		List<RecipesDTO> myRecipeDTOList = new ArrayList<>();
		for (Recipe item : myRecipeDataBase) {
			RecipesDTO myRecipeDTO = model.map(item, RecipesDTO.class);
			myRecipeDTO.setUserName(item.getUser().getUserName());
			myRecipeDTO.setAvatarUrl(item.getUser().getAvatarUrl());
			myRecipeDTO.setCategory(item.getCategory().getName());
			Set<Tags> tags = item.getTags();
			Set<String> tagsDTO = new HashSet<>();
			for (Tags itemTag : tags) {
				tagsDTO.add(itemTag.getName());
			}
			myRecipeDTO.setTags(tagsDTO);
			myRecipeDTO.setUpdateAt(item.getUpdatedAt().toLocalDate());
			myRecipeDTO.setCreateAt(item.getCreatedAt().toLocalDate());
			myRecipeDTOList.add(myRecipeDTO);
		}
		userDTO.setMyRecipe(myRecipeDTOList);
		userDTO.setTotalLike(likeRepo.getTotalLikeByUser(userName));
		userDTO.setTotalView(viewRepo.totalViewByUser(userName));
		userDTO.setPendingRecipes(
				recipesRepo.getCountRecipeByUserAndStatus(userDataBase.getUserId(), Recipe.RecipeStatus.PENDING));
		userDTO.setApprovedRecipes(
				recipesRepo.getCountRecipeByUserAndStatus(userDataBase.getUserId(), Recipe.RecipeStatus.APPROVED));
		userDTO.setRejectedRecipes(
				recipesRepo.getCountRecipeByUserAndStatus(userDataBase.getUserId(), Recipe.RecipeStatus.REJECTED));

		return userDTO;
	}

	@Override
	public String UpdateUser(String token, UserRequest userRequest, MultipartFile image) throws IOException {

		System.out.println("========== UPDATE USER DEBUG ==========");
		System.out.println("1. UserRequest: " + userRequest);
		System.out.println("2. Image: " + (image != null ? "NOT NULL" : "NULL"));

		if (image != null) {
			System.out.println("   - Filename: " + image.getOriginalFilename());
			System.out.println("   - Size: " + image.getSize() + " bytes");
			System.out.println("   - Content-Type: " + image.getContentType());
			System.out.println("   - Is Empty: " + image.isEmpty());
		}

		System.out.println("3. Cloudinary config:");
		System.out.println("   - Cloud name: " + cloudinary.config.cloudName);
		System.out.println("   - API Key: " + cloudinary.config.apiKey);
		System.out.println("   - API Secret exists: " + (cloudinary.config.apiSecret != null));
		System.out.println("=======================================");

		String userName = jwt.extractUserName(token);
		User userDataBase = userRepo.findByUserName(userName);

		if (userDataBase == null) {
			throw new DulicateUserException("Token da het han, xin vui long dang nhap lai!!");
		}

		// Upload anh
		if (image != null && !image.isEmpty()) {

			try {
				System.out.println(">>> [1] Preparing to upload image...");

				byte[] imageBytes = image.getBytes();
				System.out.println(">>> [2] Image bytes length: " + imageBytes.length);

				Map<String, Object> uploadParams = ObjectUtils.asMap(
						"folder", "user_avatars",
						"resource_type", "auto");

				Map uploadResult = cloudinary.uploader().upload(imageBytes, uploadParams);

				if (uploadResult == null) {
					throw new RuntimeException("Upload result is NULL!");
				}

				String secureUrl = (String) uploadResult.get("secure_url");
				String regularUrl = (String) uploadResult.get("url");

				String avatarUrl = null;
				if (secureUrl != null && !secureUrl.isEmpty()) {
					avatarUrl = secureUrl;

				} else if (regularUrl != null && !regularUrl.isEmpty()) {
					avatarUrl = regularUrl;

				} else {

					throw new RuntimeException("khong nha duoc anh Cloudinary. Upload result: " + uploadResult);
				}

				if (userDataBase.getAvatarUrl() != null) {
					cloudinaryService.deleteImageByUrl(userDataBase.getAvatarUrl());
				}

				userDataBase.setAvatarUrl(avatarUrl);

				System.out.println(">>> [14] Avatar URL set successfully!");

			} catch (Exception e) {
				System.err.println("========== UPLOAD ERROR ==========");
				System.err.println("Error at some step during upload");
				System.err.println("Error message: " + e.getMessage());
				System.err.println("Error class: " + e.getClass().getName());
				System.err.println("Stack trace:");
				e.printStackTrace();
				System.err.println("==================================");

				throw new IOException("khong the upload anh: " + e.getMessage(), e);
			}
		} else {
			System.out.println(">>> No image to upload (null or empty)");
		}

		// cap nhat anh
		System.out.println(">>> Updating user information...");

		if (userRequest != null) {
			if (userRequest.getBio() != null) {
				userDataBase.setBio(userRequest.getBio());
			}
			if (userRequest.getEmail() != null) {
				userDataBase.setEmail(userRequest.getEmail());
			}
			if (userRequest.getFullName() != null) {

				userDataBase.setFullName(userRequest.getFullName());
			}
			if (userRequest.getUserName() != null) {

				userDataBase.setUserName(userRequest.getUserName());
			}
			if (userRequest.getPasswordHash() != null) {

				userDataBase.setPasswordHash(bcry.encode(userRequest.getPasswordHash()));
			}
		}

		System.out.println(">>> Saving user to database...");
		userRepo.save(userDataBase);

		System.out.println(">>> User saved successfully!");
		System.out.println("========== UPDATE COMPLETE ==========");
		String jwtToken = jwt.getToken(userDataBase);
		return jwtToken;
	}

	@Override
	public UserOtherDTO getProfileUserOther(Integer Id, String token) {
		User userDataBase = userRepo.findById(Id).orElse(null);
		if (userDataBase == null) {
			throw new DulicateUserException("khong co nguoi dung nay, vui long tim kiem lai !!!");
		}

		String userName = userDataBase.getUserName();
		model.typeMap(User.class, UserOtherDTO.class)
				.addMappings(mapper -> {
					mapper.skip(UserOtherDTO::setFollower);
					mapper.skip(UserOtherDTO::setFollowing);
				});

		UserOtherDTO userDTO = model.map(userDataBase, UserOtherDTO.class);
		userDTO.setCreatedAt(userDataBase.getCreatedAt().toLocalDate());
		userDTO.setUpdateAt(userDataBase.getUpdatedAt().toLocalDate());
		userDTO.setTotalRecipes(recipesRepo.getCountRecipeByUser(userDataBase.getUserId()));
		userDTO.setFollowerCount(followRepo.getCountUserFollower(userName));
		userDTO.setFollowingCount(followRepo.getCountUserFollowing(userName));

		List<Recipe> myRecipeDataBase = userDataBase.getRecipes();
		List<RecipesDTO> myRecipeDTOList = new ArrayList<>();

		for (Recipe item : myRecipeDataBase) {
			RecipesDTO myRecipeDTO = model.map(item, RecipesDTO.class);
			myRecipeDTO.setUserName(item.getUser().getUserName());
			myRecipeDTO.setAvatarUrl(item.getUser().getAvatarUrl());
			myRecipeDTO.setCategory(item.getCategory().getName());

			Set<Tags> tags = item.getTags();
			Set<String> tagsDTO = new HashSet<>();
			for (Tags itemTag : tags) {
				tagsDTO.add(itemTag.getName());
			}
			myRecipeDTO.setTags(tagsDTO);
			myRecipeDTO.setUpdateAt(item.getUpdatedAt().toLocalDate());
			myRecipeDTO.setCreateAt(item.getCreatedAt().toLocalDate());
			myRecipeDTOList.add(myRecipeDTO);
		}

		userDTO.setMyRecipe(myRecipeDTOList);
		userDTO.setTotalLike(likeRepo.getTotalLikeByUser(userName));
		userDTO.setTotalView(viewRepo.totalViewByUser(userName));

		if (token != null) {
			String myUserName = jwt.extractUserName(token);
			if (followRepo.checkFollwer(myUserName, userName) > 0) {
				userDTO.setFollower(true);
			}
			if (followRepo.checkFollwing(myUserName, userName) > 0) {
				userDTO.setFollowing(true);
			}
		}

		return userDTO;
	}

	@Override
	public List<UserDTO> resultSearch(String find) {
		// TODO Auto-generated method stub
		List<User> result = userRepo.searchUser(find);
		List<UserDTO> userDTOList = new ArrayList<>();
		for (User user : result) {
			UserDTO userDTO = model.map(user, UserDTO.class);
			userDTOList.add(userDTO);
		}
		return userDTOList;
	}

	@Override
	public List<UserReviewWithRecipeDTO> getUserReviews(String token) {
		String userName = jwt.extractUserName(token);
		User userDataBase = userRepo.findByUserName(userName);

		if (userDataBase == null) {
			throw new DulicateUserException("Token đã hết hạn, vui lòng đăng nhập lại!");
		}

		List<RecipeReview> reviews = recipeReviewRepo.findByUserId(userDataBase.getUserId());
		List<UserReviewWithRecipeDTO> result = new ArrayList<>();

		for (RecipeReview review : reviews) {
			UserReviewWithRecipeDTO dto = new UserReviewWithRecipeDTO();

			// Map review information
			RecipeReviewDTO reviewDTO = model.map(review, RecipeReviewDTO.class);
			reviewDTO.setUserId(review.getUser().getUserId());
			reviewDTO.setUserName(review.getUser().getUserName());
			reviewDTO.setUserAvatar(review.getUser().getAvatarUrl());
			reviewDTO.setRecipeId(review.getRecipe().getRecipeId());
			reviewDTO.setRecipeName(review.getRecipe().getTitle());
			reviewDTO.setCreatedAt(review.getCreatedAt());
			reviewDTO.setUpdatedAt(review.getUpdatedAt());
			reviewDTO.setActualCookingTime(review.getActualCookingTime());
			// Parse user images if exists
			if (review.getUserImages() != null && !review.getUserImages().isEmpty()) {
				List<String> imageList = new ArrayList<>();
				String[] images = review.getUserImages().split(",");
				for (String img : images) {
					imageList.add(img.trim());
				}
				reviewDTO.setUserImages(imageList);
			}

			// Set change and delete flags (user can modify their own reviews)
			reviewDTO.setChange(true);
			reviewDTO.setDelete(true);

			dto.setReview(reviewDTO);

			// Map recipe information
			Recipe recipe = review.getRecipe();
			dto.setRecipeId(recipe.getRecipeId());
			dto.setRecipeTitle(recipe.getTitle());
			dto.setRecipeImageUrl(recipe.getImageUrl());
			dto.setRecipeDescription(recipe.getDescription());
			dto.setCategory(recipe.getCategory() != null ? recipe.getCategory().getName() : null);
			dto.setDifficultyLevel(recipe.getDifficultyLevel() != null ? recipe.getDifficultyLevel().toString() : null);
			dto.setPrepTime(recipe.getPrepTime());
			dto.setCookTime(recipe.getCookTime());

			result.add(dto);
		}

		return result;
	}

}
