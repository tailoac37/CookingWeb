package projectCooking.Service.Implements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.RecipesDTO;
import projectCooking.Model.UserDTO;
import projectCooking.Model.UserOtherDTO;
import projectCooking.Model.UserProfileDTO;
import projectCooking.Repository.FavoriteRepo;
import projectCooking.Repository.FollowRepo;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.ViewRepo;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.UserRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.UserProfileService;

@Service
public class UserProfileService_Implements implements UserProfileService {
	@Autowired
	private UserRepo userRepo  ; 
	@Autowired
	private JWTService jwt ; 
	@Autowired
	private FavoriteRepo favoriteRepo ; 
	@Autowired
	private RecipesRepo recipesRepo ; 
	@Autowired
	private ModelMapper model ;
	@Autowired
	private FollowRepo followRepo; 
	@Autowired
	private LikeRepo likeRepo ; 
	@Autowired
	private ViewRepo viewRepo ;
	private Cloudinary cloudinary ;
	private BCryptPasswordEncoder bcry ; 
	@Override
	public UserProfileDTO GetProfile(String token) {
		String userName = jwt.extractUserName(token)  ; 
		User userDataBase = userRepo.findByUsername(userName)  ; 
		if(userDataBase==null)
		{
			throw new DulicateUserException("Token da het han , ban phai dang nhap lai")  ;
		}
		UserProfileDTO userDTO = model.map(userDataBase, UserProfileDTO.class)  ; 
		List<Recipe> favorite = favoriteRepo.getRecipeFavoriteByUser(userName)  ; 
		List<RecipesDTO> favoriteDTOList = new ArrayList<>()  ; 
		for(Recipe item : favorite)
		{
			RecipesDTO favoriteDTO = model.map(item, RecipesDTO.class)  ; 
			favoriteDTO.setUsername(item.getUser().getUsername());
			favoriteDTO.setAvatar_url(item.getUser().getAvatarUrl());
			favoriteDTO.setCategory(item.getCategory().getName());
			favoriteDTOList.add(favoriteDTO)  ; 
		}
		userDTO.setFavorites(favoriteDTOList);
		userDTO.setCreatedAt(userDataBase.getCreatedAt().toLocalDate());
		userDTO.setUpdateAt(userDataBase.getUpdatedAt().toLocalDate())  ; 
		userDTO.setTotalRecipes(recipesRepo.getCountRecipeByUser(userDataBase.getUserId()));
		userDTO.setFollowerCount(followRepo.getCountUserFollower(userName));
		userDTO.setFollowingcount(followRepo.getCountUserFollowing(userName));
		List<Recipe> myRecipeDataBase = userDataBase.getRecipes() ; 
		List<RecipesDTO> myRecipeDTOList = new ArrayList<>()  ; 
		for(Recipe item : myRecipeDataBase)
		{
			RecipesDTO myRecipeDTO = model.map(item, RecipesDTO.class)  ; 
			myRecipeDTO.setUsername(item.getUser().getUsername());
			myRecipeDTO.setAvatar_url(item.getUser().getAvatarUrl());
			myRecipeDTO.setCategory(item.getCategory().getName());
			myRecipeDTOList.add(myRecipeDTO)  ; 
		}
		userDTO.setMyRecipe(myRecipeDTOList);
		userDTO.setTotalLike(likeRepo.getTotalLikeByUser(userName));
		userDTO.setTotalView(viewRepo.totalViewByUser(userName));
		userDTO.setPendingRecipes(recipesRepo.getCountRecipeByUserAndStatus(userDataBase.getUserId(), Recipe.RecipeStatus.PENDING));
		userDTO.setApprovedRecipes(recipesRepo.getCountRecipeByUserAndStatus(userDataBase.getUserId(), Recipe.RecipeStatus.APPROVED));
		userDTO.setRejectedRecipes(recipesRepo.getCountRecipeByUserAndStatus(userDataBase.getUserId(), Recipe.RecipeStatus.REJECTED));
		
		return userDTO;
	}
	@Override
	public String UpdateUser(String token, UserRequest userRequest , MultipartFile image ) throws IOException {
		String userName = jwt.extractUserName(token)  ; 
		User userDataBase = userRepo.findByUsername(userName) ; 
		if(userDataBase==null)
		{
			throw new DulicateUserException("Token da het han , xin vui long dang nhap lai !!")  ; 
		}
		if(image !=null)
		{
			Map uploadResult = cloudinary.uploader().upload(image.getBytes(),ObjectUtils.emptyMap() )  ; 
			String avatarUrl = (String) uploadResult.get("secure_url") ; 
			userDataBase.setAvatarUrl(avatarUrl);
		}
		if(userRequest.getBio() !=null)
		{
			userDataBase.setBio(userRequest.getBio());
		}
		if(userRequest.getEmail()!=null)
		{
			userDataBase.setEmail(userRequest.getBio());
		}
		if(userRequest.getFullname() !=null)
		{
			userDataBase.setFullName(userRequest.getFullname());
		}
		if(userRequest.getUsername()!=null)
		{
			userDataBase.setUsername(userRequest.getUsername());
		}
		if(userRequest.getPasswordHash()!=null)
		{
			userDataBase.setPasswordHash(bcry.encode(userRequest.getPasswordHash()));
		}
		userRepo.save(userDataBase)  ; 
		return "da thay doi";
	}
	@Override
	public UserOtherDTO getProfileUserOther( Integer Id , String token ) {
		User userDataBase = userRepo.findById(Id).orElse(null) ;
		if(userDataBase == null)
		{
			throw new DulicateUserException("khong co nguoi dung nay , vui long tim kiem lai !!!") ; 
		}
		
		String userName = userDataBase.getUsername()  ; 
		
		UserOtherDTO userDTO = model.map(userDataBase, UserOtherDTO.class)  ; 
		userDTO.setCreatedAt(userDataBase.getCreatedAt().toLocalDate());
		userDTO.setUpdateAt(userDataBase.getUpdatedAt().toLocalDate())  ; 
		userDTO.setTotalRecipes(recipesRepo.getCountRecipeByUser(userDataBase.getUserId()));
		userDTO.setFollowerCount(followRepo.getCountUserFollower(userName));
		userDTO.setFollowingcount(followRepo.getCountUserFollowing(userName));
		List<Recipe> myRecipeDataBase = userDataBase.getRecipes() ; 
		List<RecipesDTO> myRecipeDTOList = new ArrayList<>()  ; 
		for(Recipe item : myRecipeDataBase)
		{
			RecipesDTO myRecipeDTO = model.map(item, RecipesDTO.class)  ; 
			myRecipeDTO.setUsername(item.getUser().getUsername());
			myRecipeDTO.setAvatar_url(item.getUser().getAvatarUrl());
			myRecipeDTO.setCategory(item.getCategory().getName());
			myRecipeDTOList.add(myRecipeDTO)  ; 
		}
		userDTO.setMyRecipe(myRecipeDTOList);
		userDTO.setTotalLike(likeRepo.getTotalLikeByUser(userName));
		userDTO.setTotalView(viewRepo.totalViewByUser(userName));
		if(token != null)
		{
			String myUserName = jwt.extractUserName(token) ; 
			if(followRepo.checkFollwer(myUserName, userName) >0 ) 
			{
				userDTO.setIs_follower(true);
			}
			if(followRepo.checkFollwing(myUserName, userName) >0 ) 
			{
				userDTO.setIs_follwing(true);
			}
			
		}
		return userDTO; 
	}
	@Override
	public List<UserDTO> resultSearch(String find) {
		// TODO Auto-generated method stub
		List<User> result= userRepo.searchUser(find); 
		List<UserDTO> userDTOList  = new ArrayList<>()  ; 
		for(User user : result) 
		{
			UserDTO userDTO = model.map(user , UserDTO.class)  ; 
			userDTOList.add(userDTO)  ; 
		}
		return userDTOList ; 
	}

}
