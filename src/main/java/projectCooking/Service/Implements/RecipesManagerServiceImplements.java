package projectCooking.Service.Implements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.CommentsDTO;
import projectCooking.Model.RecipesDTO;
import projectCooking.Model.RecipesDetailsDTO;
import projectCooking.Model.instructionsDTO;
import projectCooking.Repository.CategoryRepo;
import projectCooking.Repository.CommentsRepo;
import projectCooking.Repository.FavoriteRepo;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.NotificationRepo;
import projectCooking.Repository.RecipeImageRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.TagsRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Categories;
import projectCooking.Repository.Entity.Comment;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.RecipeImage;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.RecipeRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.RecipesManagerService;
import projectCooking.Service.CloudinaryService.CloudinaryService;
@Service
public class RecipesManagerServiceImplements implements  RecipesManagerService {
	@Autowired
	private RecipesRepo recipeRepo ; 
	@Autowired
	private ModelMapper model ; 
	@Autowired
	private CategoryRepo categoriesRepo  ; 
	@Autowired
	private TagsRepo tagsRepo ; 
	@Autowired
	private Cloudinary cloudinary ; 
	@Autowired
	private RecipeImageRepo imageRepo ;
	@Autowired
	private JWTService jwt ; 
	@Autowired
	private UserRepo userRepo ; 
	@Autowired
	private CloudinaryService cloudinaryService;  
	@Autowired
	private LikeRepo likeRepo ; 
	@Autowired
	private CommentsRepo commentsRepo ; 
	@Autowired
	private NotificationRepo notifRepo ; 
	@Autowired
	private FavoriteRepo favoRepo ; 
	@Override
	public String createRecipes(String token,RecipeRequest recipes, MultipartFile imagePrimary, List<MultipartFile> image) throws IOException {
		Recipe recipeDataBase  = model.map(recipes, Recipe.class) ; 
		Set<Tags> tagsList = new HashSet<>();
		String userName = jwt.extractUserName(token) ; 
		User userDataBase =  userRepo.findByUserName(userName)  ; 
		if(userDataBase == null)
		{
			throw new DulicateUserException("Dang nhap lai  , co loi voi tai khoan cua ban !!!" )  ; 
		}
		recipeDataBase.setUser(userDataBase);
		Categories categories = categoriesRepo.findByName(recipes.getCategory().getName()) ;
		if(categories == null)  
		{
			categories = new Categories() ; 
			categories.setCreatedAt(LocalDate.now());
			categories.setDescription(recipes.getCategory().getDesciption());
			categories.setName(recipes.getCategory().getName());
			categoriesRepo.save(categories)  ; 
		}
		StringBuilder  Ingredients = new StringBuilder()  ; 
		for(String item : recipes.getIngredients())
		{
			Ingredients.append(item)  ; 
		}
		recipeDataBase.setIngredients(Ingredients.toString());
		for (String item : recipes.getTags())
		{
			Tags tags = tagsRepo.findByName(item)  ; 
			if(tags==null)
			{
				tags = new Tags()  ; 
				tags.setCreatedAt(LocalDate.now());
				tags.setName(item);
				tagsRepo.save(tags)  ; 
			}
			tagsList.add(tags) ; 
			
		}
		Map uploadImage = cloudinary.uploader().upload(imagePrimary.getBytes(),ObjectUtils.emptyMap() )  ; 
		String imageURLPrimary = (String) uploadImage.get("secure_url") ; 
		recipeDataBase.setImageUrl(imageURLPrimary);
		recipeDataBase.setCategory(categories);
		recipeDataBase.setTags(tagsList); 
		recipeRepo.save(recipeDataBase)  ; 
		int i = 0 ; 
		for(MultipartFile file : image)
		{
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(),ObjectUtils.emptyMap() )  ; 
			String imageURL = (String) uploadResult.get("secure_url") ; 
			RecipeImage imageDataBase = new RecipeImage()  ; 
			imageDataBase.setCreatedAt(LocalDate.now());
			imageDataBase.setImageUrl(imageURL);
			imageDataBase.setInstructions(recipes.getInstructions().get(i));
			imageDataBase.setRecipe(recipeDataBase);
			imageRepo.save(imageDataBase)  ; 
			i ++ ; 
		} 
		return "done";
	}
	@Override
	public RecipesDetailsDTO getRecipes(Integer id , String token ) {
		Recipe recipes = recipeRepo.findById(id).orElse(null) ;
		if(recipes== null)
		{
			throw new DulicateUserException("bai viet nay khong ton tai , vui long thu lai sau !!!")  ; 
			
		}
		
		RecipesDetailsDTO recipesDTO = model.map(recipes,RecipesDetailsDTO.class)  ; 
		recipesDTO.setUsername(recipes.getUser().getUserName());
		recipesDTO.setAvatarUrl(recipes.getUser().getAvatarUrl()); 
		recipesDTO.setCategory(recipes.getCategory().getName());
		if(token!= null)
		{
			String userName = jwt.extractUserName(token)  ; 
			if(userName !=null)
			{
				if(userName.equals(recipes.getUser().getUserName()))
				{
					recipesDTO.setChange(true);
				}
				if(likeRepo.getCheckLikeByUser(userName, recipes.getRecipeId()) !=null)
				{
					recipesDTO.setLike(true);
				}
				if(favoRepo.checkRecipeInFavorite(id ,userName) >0 )
				{
					recipesDTO.setFavorite(true);
				}
			}
			
		}
			
		List<RecipeImage> imageDataBase = recipes.getImages() ; 
		List<instructionsDTO> instructions = new ArrayList()  ; 
		for (RecipeImage image : imageDataBase)
		{
			instructionsDTO instruction = model.map(image, instructionsDTO.class)  ; 
			instructions.add(instruction)  ; 
 		}
		recipesDTO.setInstructions(instructions);
		recipesDTO.setUpdateAt(recipes.getUpdatedAt().toLocalDate());
		recipesDTO.setCreateAt(recipes.getCreatedAt().toLocalDate());
		Set<Tags> TagsDataBase = recipes.getTags() ; 
		List<String> tagsListDTO = new ArrayList()  ; 
		for (Tags tags : TagsDataBase)
		{
			String tagsDTO = tags.getName(); 
			tagsListDTO.add(tagsDTO)  ; 
 		}
		recipesDTO.setTags(tagsListDTO);
		List<Comment> commentsList = recipes.getComments();
		List<CommentsDTO> commentsDTOList = new ArrayList<>();

		for(Comment comments : commentsList) {
		 
		    if(comments.getParentComment() == null) {
		        CommentsDTO commentsDTO = model.map(comments, CommentsDTO.class);
		        commentsDTO.setAvatarUrl(comments.getUser().getAvatarUrl());
		        commentsDTO.setUserName(comments.getUser().getUserName());
		        commentsDTO.setUpdateAt(comments.getUpdatedAt());
		        commentsDTO.setCreateAt(comments.getCreatedAt());
		        commentsDTO.setParentComment(null); // 
		        commentsDTO.setUserId(comments.getUser().getUserId());
		       
		        List<CommentsDTO> repliesDTOList = new ArrayList<>();
		        for(Comment reply : commentsList) {
		            if(reply.getParentComment() != null && 
		               reply.getParentComment().getCommentId().equals(comments.getCommentId())) {
		                
		                CommentsDTO replyDTO = model.map(reply, CommentsDTO.class);
		                replyDTO.setAvatarUrl(reply.getUser().getAvatarUrl());
		                replyDTO.setUserName(reply.getUser().getUserName());
		                replyDTO.setUpdateAt(reply.getUpdatedAt());
		                replyDTO.setCreateAt(reply.getCreatedAt());
		                replyDTO.setParentCommentId(comments.getCommentId());
		                replyDTO.setUserId(reply.getUser().getUserId());
		                
		                replyDTO.setParentComment(null);
		                replyDTO.setReplies(new ArrayList<>());
		                
		                repliesDTOList.add(replyDTO);
		            }
		        }
		        
		        commentsDTO.setReplies(repliesDTOList);
		        commentsDTOList.add(commentsDTO);
		    }
		
		
		}
		recipesDTO.setCommentsDTO(commentsDTOList);
		recipesDTO.setIngredients(
			    Arrays.stream(recipes.getIngredients().split(","))
			          .map(String::trim)
			          .collect(Collectors.toList())
			);
		return recipesDTO;
	}
	@Override
	public String updateRecipes(String token, RecipeRequest recipesUpdate, MultipartFile image_primary,
			List<MultipartFile> image , Integer Id) throws IOException {
		String userName = jwt.extractUserName(token)  ; 
		Recipe recipesDataBase = recipeRepo.findById(Id).orElse(null)  ; 
		if(recipesDataBase==null)
		{
			throw new DulicateUserException("khong tim thay bai viet nay !!!")   ; 
		}
		if(!recipesDataBase.getUser().getUserName().equals(userName))
		{
			throw new DulicateUserException("Day khong phai la bai viet cua ban , ban khong co quyen de chinh sua bai viet nay !!!!")  ; 
		}
		recipesDataBase = model.map(recipesUpdate, Recipe.class) ; 
		if(image_primary!= null)  
		{
			cloudinaryService.deleteImageByUrl(recipesDataBase.getImageUrl()) ;
			Map uploadResult = cloudinary.uploader().upload(image_primary.getBytes(),ObjectUtils.emptyMap() )  ; 
			String imageURL = (String) uploadResult.get("secure_url") ; 
			recipesDataBase.setImageUrl(imageURL);
		}
		if(image!=null)
		{
			List<RecipeImage> imageDataBaseList = recipesDataBase.getImages()  ; 
			int n = image.size()  ;
			for(RecipeImage imageItem : imageDataBaseList)
			{
				cloudinaryService.deleteImageByUrl(imageItem.getImageUrl()) ; 
			}
			for(int i = 0 ;i < n ; i++)  
			{
				Map uploadResult = cloudinary.uploader().upload(image_primary.getBytes(),ObjectUtils.emptyMap() )  ; 
				String imageURL = (String) uploadResult.get("secure_url") ; 
				imageDataBaseList.get(i).setImageUrl(imageURL)  ; 
				imageDataBaseList.get(i).setInstructions(recipesUpdate.getInstructions().get(i)) ; 
				imageRepo.save(imageDataBaseList.get(i))  ; 
			}
			
			
		}
		recipesDataBase.setUser(userRepo.findByUserName(userName))  ; 
		List<String> tagsListDTO = recipesUpdate.getTags() ; 
		Set<Tags> tagsDataBase = new HashSet<>() ; 
		for(String tagsDTO  : tagsListDTO) 
		{
			Tags tags = tagsRepo.findByName(tagsDTO)  ; 
			tagsDataBase.add(tags) ; 
			 
		}
		
		recipesDataBase.setTags(tagsDataBase);
		Categories categories = categoriesRepo.findByName(recipesUpdate.getCategory().getName())  ; 
		recipesDataBase.setCategory(categories);
		recipesDataBase.setRecipeId(Id);
		notifRepo.deleteNotificationsByRecipeId(recipesDataBase.getRecipeId());
		recipeRepo.save(recipesDataBase) ; 
		
		return "Da cap nhat thanh cong";
	}
	@Override
	public String deleteRecipes(String token, Integer Id) {
		String userName = jwt.extractUserName(token) ; 
		User userDataBase = userRepo.findByUserName(userName)  ; 
		Recipe recipes = recipeRepo.findById(Id).orElse(null)  ; 
		if(recipes ==null)
		{
			throw new DulicateUserException("Bai viet nay khong ton tai hoac da  duoc xoa truoc do nhung chua kip load !!")  ; 
		}
		if(!userName.equals(recipes.getUser().getUserName()) && !userDataBase.getRole().equals("ADMIN"))
		{
			throw new DulicateUserException("Ban khong co quyen xoa bai viet cua nguoi khac , chi co ADMIN hoac nguoi tao bai viet nay moi co the lam duoc dieu do")  ; 
			
 		}
		cloudinaryService.deleteImageByUrl(recipes.getImageUrl())  ; 
		for (RecipeImage image : recipes.getImages())
		{
			
			cloudinaryService.deleteImageByUrl(image.getImageUrl()) ;
			imageRepo.delete(image);
		}
		notifRepo.deleteNotificationsByRecipeId(recipes.getRecipeId());
		recipeRepo.delete(recipes);
		
		return "done";
	}
	@Override
	public List<RecipesDTO> getListRecipes(String token) {
		List<Recipe> recipes = recipeRepo.getListRecipes(Recipe.RecipeStatus.APPROVED) ; 
		List<RecipesDTO> recipesListDTO = new ArrayList<>()  ; 
		for(Recipe recipe : recipes)
		{
			RecipesDTO recipesDTO = model.map(recipe, RecipesDTO.class)  ; 
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tags = recipe.getTags()  ; 
			Set<String> tagsDTO  = new HashSet<>()  ; 
			for(Tags item : tags)
			{
				tagsDTO.add(item.getName()) ; 
			}
			
			recipesDTO.setTags(tagsDTO);
			recipesDTO.setIngredients(
				    Arrays.stream(recipe.getIngredients().split(","))
				          .map(String::trim)
				          .collect(Collectors.toList())
				);
			if(token!= null)
			{
				String userName = jwt.extractUserName(token)  ; 
				if(userName !=null)
				{
					if(userName.equals(recipe.getUser().getUserName()))
					{
						recipesDTO.setChange(true);
					}
					if(likeRepo.getCheckLikeByUser(userName, recipe.getRecipeId()) !=null)
					{
						recipesDTO.setLike(true);
					}
				}
			}
			recipesListDTO.add(recipesDTO) ; 
		}
		return recipesListDTO;
	}

}
