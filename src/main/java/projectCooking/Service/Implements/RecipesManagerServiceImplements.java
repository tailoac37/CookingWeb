package projectCooking.Service.Implements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import projectCooking.Request.InstructionRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.RecipesManagerService;
import projectCooking.Service.CloudinaryService.CloudinaryService;

@Service
public class RecipesManagerServiceImplements implements RecipesManagerService {
	@Autowired
	private RecipesRepo recipeRepo;
	@Autowired
	private ModelMapper model;
	@Autowired
	private CategoryRepo categoriesRepo;
	@Autowired
	private TagsRepo tagsRepo;
	@Autowired
	private Cloudinary cloudinary;
	@Autowired
	private RecipeImageRepo imageRepo;
	@Autowired
	private JWTService jwt;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CloudinaryService cloudinaryService;
	@Autowired
	private LikeRepo likeRepo;
	@Autowired
	private CommentsRepo commentsRepo;
	@Autowired
	private NotificationRepo notifRepo;
	@Autowired
	private FavoriteRepo favoRepo;

	@Override
	public String createRecipes(String token, RecipeRequest recipes, MultipartFile imagePrimary,
			List<MultipartFile> image) throws IOException {
		Recipe recipeDataBase = model.map(recipes, Recipe.class);

		// Fix difficultyLevel mapping - Convert String to Enum
		if (recipes.getDifficultyLevel() != null) {
			try {
				Recipe.DifficultyLevel difficulty = Recipe.DifficultyLevel.valueOf(
						recipes.getDifficultyLevel().toUpperCase());
				recipeDataBase.setDifficultyLevel(difficulty);
			} catch (IllegalArgumentException e) {
				recipeDataBase.setDifficultyLevel(Recipe.DifficultyLevel.MEDIUM);
			}
		}

		Set<Tags> tagsList = new HashSet<>();
		String userName = jwt.extractUserName(token);
		User userDataBase = userRepo.findByUserName(userName);
		if (userDataBase == null) {
			throw new DulicateUserException("Dang nhap lai  , co loi voi tai khoan cua ban !!!");
		}
		recipeDataBase.setUser(userDataBase);
		Categories categories = categoriesRepo.findFirstByName(recipes.getCategory().getName());
		if (categories == null) {
			categories = new Categories();
			categories.setCreatedAt(LocalDate.now());
			categories.setDescription(recipes.getCategory().getDescription());
			categories.setName(recipes.getCategory().getName());
			categoriesRepo.save(categories);
		}
		String ingredientsString = String.join("@", recipes.getIngredients());
		recipeDataBase.setIngredients(ingredientsString);
		if (recipes.getNutrition() != null) {
			String nutritionString = String.join("@", recipes.getNutrition());
			recipeDataBase.setNutrition(nutritionString);
		}
		for (String item : recipes.getTags()) {
			Tags tags = tagsRepo.findFirstByName(item);
			if (tags == null) {
				tags = new Tags();
				tags.setCreatedAt(LocalDate.now());
				tags.setName(item);
				tagsRepo.save(tags);
			}
			tagsList.add(tags);

		}
		Map uploadImage = cloudinary.uploader().upload(imagePrimary.getBytes(), ObjectUtils.emptyMap());
		String imageURLPrimary = (String) uploadImage.get("secure_url");
		recipeDataBase.setImageUrl(imageURLPrimary);
		recipeDataBase.setCategory(categories);
		recipeDataBase.setTags(tagsList);
		recipeRepo.save(recipeDataBase);
		int imageIndex = 0;
		for (InstructionRequest instructionReq : recipes.getInstructions()) {
			RecipeImage imageDataBase = new RecipeImage();
			imageDataBase.setCreatedAt(LocalDate.now());
			imageDataBase.setInstructions(instructionReq.getInstruction());
			imageDataBase.setRecipe(recipeDataBase);

			if (instructionReq.getImage() != null && instructionReq.getImage() && image != null
					&& imageIndex < image.size()) {
				Map uploadResult = cloudinary.uploader().upload(image.get(imageIndex).getBytes(),
						ObjectUtils.emptyMap());
				String imageURL = (String) uploadResult.get("secure_url");
				imageDataBase.setImageUrl(imageURL);
				imageIndex++;
			} else {
				imageDataBase.setImageUrl("");
			}
			imageRepo.save(imageDataBase);
		}
		return "done";
	}

	@Override
	public RecipesDetailsDTO getRecipes(Integer id, String token) {
		Recipe recipes = recipeRepo.findById(id).orElse(null);
		if (recipes == null) {
			throw new DulicateUserException("bai viet nay khong ton tai , vui long thu lai sau !!!");

		}

		RecipesDetailsDTO recipesDTO = model.map(recipes, RecipesDetailsDTO.class);
		if (recipes.getUser() != null) {
			recipesDTO.setUserId(recipes.getUser().getUserId());
			recipesDTO.setUsername(recipes.getUser().getUserName());
			recipesDTO.setAvatarUrl(recipes.getUser().getAvatarUrl());
		}
		if (recipes.getCategory() != null) {
			recipesDTO.setCategory(recipes.getCategory().getName());
		} else {
			recipesDTO.setCategory("Chưa phân loại");
		}
		if (token != null) {
			String userName = jwt.extractUserName(token);
			if (userName != null && recipes.getUser() != null) {
				if (userName.equals(recipes.getUser().getUserName())) {
					recipesDTO.setChange(true);
				}
				if (likeRepo.getCheckLikeByUser(userName, recipes.getRecipeId()) != null) {
					recipesDTO.setLike(true);
				}
				if (favoRepo.checkRecipeInFavorite(id, userName) > 0) {
					recipesDTO.setFavorite(true);
				}
			}

		}

		List<RecipeImage> imageDataBase = recipes.getImages();
		List<instructionsDTO> instructions = new ArrayList();
		for (RecipeImage image : imageDataBase) {
			instructionsDTO instruction = model.map(image, instructionsDTO.class);
			if ("".equals(instruction.getImageUrl())) {
				instruction.setImageUrl(null);
			}
			instructions.add(instruction);
		}
		recipesDTO.setInstructions(instructions);
		recipesDTO.setUpdateAt(recipes.getUpdatedAt().toLocalDate());
		recipesDTO.setCreateAt(recipes.getCreatedAt().toLocalDate());
		Set<Tags> TagsDataBase = recipes.getTags();
		List<String> tagsListDTO = new ArrayList();
		for (Tags tags : TagsDataBase) {
			String tagsDTO = tags.getName();
			tagsListDTO.add(tagsDTO);
		}
		recipesDTO.setTags(tagsListDTO);
		List<Comment> commentsList = recipes.getComments();
		List<CommentsDTO> commentsDTOList = new ArrayList<>();

		for (Comment comments : commentsList) {

			if (comments.getParentComment() == null) {
				CommentsDTO commentsDTO = model.map(comments, CommentsDTO.class);
				commentsDTO.setAvatarUrl(comments.getUser().getAvatarUrl());
				commentsDTO.setUserName(comments.getUser().getUserName());
				commentsDTO.setUpdateAt(comments.getUpdatedAt());
				commentsDTO.setCreateAt(comments.getCreatedAt());
				commentsDTO.setParentComment(null); //
				commentsDTO.setUserId(comments.getUser().getUserId());

				List<CommentsDTO> repliesDTOList = new ArrayList<>();
				for (Comment reply : commentsList) {
					if (reply.getParentComment() != null &&
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
		if (recipes.getIngredients() != null && !recipes.getIngredients().isEmpty()) {
			recipesDTO.setIngredients(
					Arrays.stream(recipes.getIngredients().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
		} else {
			recipesDTO.setIngredients(new ArrayList<>());
		}
		if (recipes.getNutrition() != null) {
			recipesDTO.setNutrition(
					Arrays.stream(recipes.getNutrition().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
		}
		return recipesDTO;
	}

	@Override
	public String updateRecipes(String token, RecipeRequest recipesUpdate, MultipartFile image_primary,
			List<MultipartFile> image, Integer Id) throws IOException {
		String userName = jwt.extractUserName(token);
		Recipe recipesDataBase = recipeRepo.findById(Id).orElse(null);
		if (recipesDataBase == null) {
			throw new DulicateUserException("khong tim thay bai viet nay !!!");
		}
		if (!recipesDataBase.getUser().getUserName().equals(userName)) {
			throw new DulicateUserException(
					"Day khong phai la bai viet cua ban , ban khong co quyen de chinh sua bai viet nay !!!!");
		}

		// Update individual fields instead of overwriting the entire object
		// This preserves recipeId, user, createdAt, and other relationships
		recipesDataBase.setTitle(recipesUpdate.getTitle());
		recipesDataBase.setDescription(recipesUpdate.getDescription());
		recipesDataBase.setCookTime(recipesUpdate.getCookTime());
		recipesDataBase.setPrepTime(recipesUpdate.getPrepTime());
		recipesDataBase.setServings(recipesUpdate.getServings());

		// Convert String difficultyLevel to Enum
		if (recipesUpdate.getDifficultyLevel() != null) {
			try {
				Recipe.DifficultyLevel difficulty = Recipe.DifficultyLevel.valueOf(
						recipesUpdate.getDifficultyLevel().toUpperCase());
				recipesDataBase.setDifficultyLevel(difficulty);
			} catch (IllegalArgumentException e) {
				// If invalid, keep the current value or set default
				recipesDataBase.setDifficultyLevel(Recipe.DifficultyLevel.MEDIUM);
			}
		}

		// Manually update ingredients to ensure correct delimiter
		if (recipesUpdate.getIngredients() != null) {
			String ingredientsString = String.join("@", recipesUpdate.getIngredients());
			recipesDataBase.setIngredients(ingredientsString);
		}
		if (recipesUpdate.getNutrition() != null) {
			String nutritionString = String.join("@", recipesUpdate.getNutrition());
			recipesDataBase.setNutrition(nutritionString);
		}

		// Update primary image
		if (image_primary != null) {
			cloudinaryService.deleteImageByUrl(recipesDataBase.getImageUrl());
			Map<String, Object> uploadResult = cloudinary.uploader().upload(image_primary.getBytes(),
					ObjectUtils.emptyMap());
			String imageURL = (String) uploadResult.get("secure_url");
			recipesDataBase.setImageUrl(imageURL);
		}

		// Update instructions
		if (recipesUpdate.getInstructions() != null)

		{
			// 1. Snapshot old images to track what needs to be deleted later
			List<RecipeImage> oldImages = new ArrayList<>(recipesDataBase.getImages());
			Set<String> oldUrls = new HashSet<>();
			for (RecipeImage img : oldImages) {
				if (img.getImageUrl() != null && !img.getImageUrl().isEmpty()) {
					oldUrls.add(img.getImageUrl());
				}
			}

			// 2. Delete old RecipeImage entities from database and clear the list
			imageRepo.deleteAll(oldImages);
			recipesDataBase.getImages().clear();

			// 3. Process new instructions
			int imageIndex = 0;
			Set<String> keptUrls = new HashSet<>(); // Track URLs that are kept or added

			for (InstructionRequest instructionReq : recipesUpdate.getInstructions()) {
				RecipeImage imageDataBase = new RecipeImage();
				imageDataBase.setCreatedAt(LocalDate.now());
				imageDataBase.setInstructions(instructionReq.getInstruction());
				imageDataBase.setRecipe(recipesDataBase);

				String finalImageUrl = "";

				if (instructionReq.getImage() != null && instructionReq.getImage()) {
					// Case A: Keep existing image
					if (instructionReq.getExistingUrl() != null && !instructionReq.getExistingUrl().isEmpty()) {
						finalImageUrl = instructionReq.getExistingUrl();
					}
					// Case B: Upload new image
					else if (image != null && imageIndex < image.size()) {
						Map uploadResult2 = cloudinary.uploader().upload(image.get(imageIndex).getBytes(),
								ObjectUtils.emptyMap());
						finalImageUrl = (String) uploadResult2.get("secure_url");
						imageIndex++;
					}
				}
				// Case C: Image is false -> finalImageUrl remains ""

				imageDataBase.setImageUrl(finalImageUrl);
				if (!finalImageUrl.isEmpty()) {
					keptUrls.add(finalImageUrl);
				}

				imageRepo.save(imageDataBase);
			}

			// 4. Cleanup: Delete old images from Cloudinary that are NOT in the new list
			for (String oldUrl : oldUrls) {
				if (!keptUrls.contains(oldUrl)) {
					cloudinaryService.deleteImageByUrl(oldUrl);
				}
			}
		}

		// Update tags
		List<String> tagsListDTO = recipesUpdate.getTags();
		Set<Tags> tagsDataBase = new HashSet<>();
		for (String tagsDTO : tagsListDTO) {
			Tags tags = tagsRepo.findFirstByName(tagsDTO);
			tagsDataBase.add(tags);
		}
		recipesDataBase.setTags(tagsDataBase);

		// Update category
		Categories categories = categoriesRepo
				.findFirstByName(recipesUpdate.getCategory().getName());
		recipesDataBase.setCategory(categories);

		// Clean up notifications related to this recipe
		notifRepo.deleteNotificationsByRecipeId(recipesDataBase.getRecipeId());

		// Save the updated recipe
		recipeRepo.save(recipesDataBase);

		return "Da cap nhat thanh cong";
	}

	@Override
	public String deleteRecipes(String token, Integer Id) {
		String userName = jwt.extractUserName(token);
		User userDataBase = userRepo.findByUserName(userName);
		Recipe recipes = recipeRepo.findById(Id).orElse(null);
		if (recipes == null) {
			throw new DulicateUserException(
					"Bai viet nay khong ton tai hoac da  duoc xoa truoc do nhung chua kip load !!");
		}
		if (!userName.equals(recipes.getUser().getUserName()) && !userDataBase.getRole().equals("ADMIN")) {
			throw new DulicateUserException(
					"Ban khong co quyen xoa bai viet cua nguoi khac , chi co ADMIN hoac nguoi tao bai viet nay moi co the lam duoc dieu do");

		}
		cloudinaryService.deleteImageByUrl(recipes.getImageUrl());
		for (RecipeImage image : recipes.getImages()) {

			cloudinaryService.deleteImageByUrl(image.getImageUrl());
			imageRepo.delete(image);
		}
		notifRepo.deleteNotificationsByRecipeId(recipes.getRecipeId());
		recipeRepo.delete(recipes);

		return "done";
	}

	@Override
	public List<RecipesDTO> getListRecipes(String token) {
		List<Recipe> recipes = recipeRepo.getListRecipes(Recipe.RecipeStatus.APPROVED);
		List<RecipesDTO> recipesListDTO = new ArrayList<>();
		for (Recipe recipe : recipes) {
			RecipesDTO recipesDTO = model.map(recipe, RecipesDTO.class);
			recipesDTO.setAvatarUrl(recipe.getUser().getAvatarUrl());
			recipesDTO.setUserName(recipe.getUser().getUserName());
			recipesDTO.setUpdateAt(recipe.getUpdatedAt().toLocalDate());
			recipesDTO.setCreateAt(recipe.getCreatedAt().toLocalDate());
			recipesDTO.setCategory(recipe.getCategory().getName());
			Set<Tags> tags = recipe.getTags();
			Set<String> tagsDTO = new HashSet<>();
			for (Tags item : tags) {
				tagsDTO.add(item.getName());
			}

			recipesDTO.setTags(tagsDTO);
			recipesDTO.setIngredients(

					Arrays.stream(recipe.getIngredients().split("@"))
							.map(String::trim)
							.collect(Collectors.toList()));
			if (recipe.getNutrition() != null) {
				recipesDTO.setNutrition(
						Arrays.stream(recipe.getNutrition().split("@"))
								.map(String::trim)
								.collect(Collectors.toList()));
			}
			if (token != null) {
				String userName = jwt.extractUserName(token);
				if (userName != null) {
					if (userName.equals(recipe.getUser().getUserName())) {
						recipesDTO.setChange(true);
					}
					if (likeRepo.getCheckLikeByUser(userName, recipe.getRecipeId()) != null) {
						recipesDTO.setLike(true);
					}
				}
			}
			recipesListDTO.add(recipesDTO);
		}
		return recipesListDTO;
	}

}
