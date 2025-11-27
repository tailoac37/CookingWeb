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
import projectCooking.Model.CategoriesDTO;
import projectCooking.Model.RecipesDTO;
import projectCooking.Repository.AdminActionRepo;
import projectCooking.Repository.CategoryRepo;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.AdminAction;
import projectCooking.Repository.Entity.Categories;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Tags;
import projectCooking.Repository.Entity.User;
import projectCooking.Repository.Entity.AdminAction.ActionType;
import projectCooking.Request.CategoriesRequest;
import projectCooking.Service.CategoriesService;
import projectCooking.Service.JWTService;
import projectCooking.Service.NotificationService;
import projectCooking.Service.CloudinaryService.CloudinaryService;

@Service
public class CategoriesServcieImplements implements CategoriesService {
	@Autowired
	private CategoryRepo categoriesRepo;
	@Autowired
	private ModelMapper model;
	@Autowired
	private JWTService jwt;
	@Autowired
	private LikeRepo likeRepo;
	@Autowired
	private Cloudinary cloudinary;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private AdminActionRepo adminActionRepo;
	@Autowired
	private NotificationService notifService;
	@Autowired
	private CloudinaryService cloudinaryService;

	@Override
	public List<CategoriesDTO> getListCategories() {
		List<Categories> categories = categoriesRepo.findAll();
		List<CategoriesDTO> categoriesListDTO = new ArrayList<>();
		for (Categories category : categories) {
			// Only show active categories
			if (category.getIsActive() != null && category.getIsActive()) {
				CategoriesDTO DTO = model.map(category, CategoriesDTO.class);
				categoriesListDTO.add(DTO);
			}
		}

		return categoriesListDTO;
	}

	@Override
	public CategoriesDTO getListRecipeByCategories(String token, Integer Id) {
		List<Recipe> recipes = categoriesRepo.getListRecipeByCategories(Id);
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
		Categories categories = categoriesRepo.findById(Id).orElse(null);
		if (categories == null) {
			throw new DulicateUserException("categories khong ton  tai");
		}
		CategoriesDTO categoriesDTO = model.map(categories, CategoriesDTO.class);
		categoriesDTO.setRecipes(recipesListDTO);
		return categoriesDTO;

	}

	@Override
	public String addCategories(String token, MultipartFile image, CategoriesRequest categories) throws IOException {

		Categories database = model.map(categories, Categories.class);
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			throw new DulicateUserException("nguoi dung khong ton tai");
		}

		Map uploadImage = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
		String imageURL = (String) uploadImage.get("secure_url");
		database.setImageUrl(imageURL);
		database.setCreatedAt(LocalDate.now());
		categoriesRepo.save(database);
		AdminAction adminAction = new AdminAction();
		adminAction.setActionType(ActionType.CATEGORIES);
		adminAction.setAdmin(user);
		adminAction.setAdminNote("Vừa mới thêm 1 categories mới");
		adminActionRepo.save(adminAction);
		notifService.sendChangeNotificationToAdmins(user, "Vừa mới thêm 1 categories mới !!");
		return "done";
	}

	@Override
	public String updateCategories(String token, MultipartFile image, CategoriesRequest categories, Integer Id)
			throws IOException {
		Categories data = categoriesRepo.findById(Id).orElse(null);
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			throw new DulicateUserException("nguoi dung khong ton tai");
		}
		if (data == null) {
			throw new DulicateUserException("categories khong ton tai trong database");
		}
		if (image != null) {
			if (data.getImageUrl() != null) {
				cloudinaryService.deleteImageByUrl(data.getImageUrl());
			}

			Map uploadImage = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
			String imageURL = (String) uploadImage.get("secure_url");
			data.setImageUrl(imageURL);
		}
		if (categories != null) {
			if (categories.getDescription() != null) {
				data.setDescription(categories.getDescription());
			}
			if (categories.getName() != null) {
				data.setName(categories.getName());
			}
		}

		categoriesRepo.save(data);

		AdminAction adminAction = new AdminAction();
		adminAction.setActionType(ActionType.CATEGORIES);
		adminAction.setAdmin(user);
		adminAction.setAdminNote("Vừa mới cập nhật 1 categories");
		adminActionRepo.save(adminAction);
		notifService.sendChangeNotificationToAdmins(user, "Vừa mới cập nhật 1 categories  !!");
		return "done";
	}

	@Override
	public String deactivateCategory(String token, Integer Id) {
		Categories categories = categoriesRepo.findById(Id).orElse(null);
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			throw new DulicateUserException("nguoi dung khong ton tai");
		}
		if (categories == null) {
			throw new DulicateUserException("khong tim thay categories nay");
		}

		// Soft delete: Set isActive to false instead of actual deletion
		categories.setIsActive(false);
		categoriesRepo.save(categories);

		AdminAction adminAction = new AdminAction();
		adminAction.setActionType(ActionType.CATEGORIES);
		adminAction.setAdmin(user);
		adminAction.setAdminNote("Vừa deactivate 1 categories");
		adminActionRepo.save(adminAction);
		notifService.sendChangeNotificationToAdmins(user, "Vừa mới deactivate 1 categories  !!");
		return "Category has been deactivated successfully";
	}

}
