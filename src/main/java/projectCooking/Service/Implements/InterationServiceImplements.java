package projectCooking.Service.Implements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.UserDTO;
import projectCooking.Repository.LikeRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Like;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.InterationService;
import projectCooking.Service.JWTService;
import projectCooking.Service.NotificationService;
@Service
public class InterationServiceImplements implements InterationService {
	@Autowired
	private JWTService jwt ; 
	@Autowired
	private RecipesRepo recipesRepo ; 
	@Autowired
	private LikeRepo likeRepo ; 
	@Autowired
	private UserRepo userRepo ; 
	@Autowired
	private ModelMapper model ; 
	@Autowired
	private NotificationService notificationService ; 
	@Override
	public String likeRecipes(String token, Integer Id) {
		String userName = jwt.extractUserName(token) ; 
		User user = userRepo.findByUserName(userName) ;
		Recipe recipe = recipesRepo.findById(Id).orElse(null) ;
		if(user==null)
		{
			throw new DulicateUserException("Vui long dang nhap lai , chung toi khong tim thay ten cua ban !!!") ;
		}
		if(recipe==null)
		{
			throw new DulicateUserException("Bai viet nay khong ton tai , vui long thu lai sau!!!")  ; 
		}
		Like like = likeRepo.getCheckLikeByUser(userName, Id) ;
		if(like == null)
		{
			like = new Like()  ; 
			like.setRecipe(recipe);
			like.setUser(user);
			like.setCreatedAt(LocalDate.now());
			likeRepo.save(like)  ; 
			recipe.setLikeCount(recipe.getLikeCount()+1);
			recipesRepo.save(recipe)  ; 
			notificationService.sendLikeNotification(user, recipe.getUser(), recipe);
		}
		return "done";
	}

	@Override
	public String delLikeRecipes(String token, Integer Id) {
		String userName = jwt.extractUserName(token) ; 
		User user = userRepo.findByUserName(userName) ;
		Recipe recipe = recipesRepo.findById(Id).orElse(null) ;
		if(user==null)
		{
			throw new DulicateUserException("Vui long dang nhap lai , chung toi khong tim thay ten cua ban !!!") ;
		}
		if(recipe==null)
		{
			throw new DulicateUserException("Bai viet nay khong ton tai , vui long thu lai sau!!!")  ; 
		}
		Like like = likeRepo.getCheckLikeByUser(userName, Id) ;
		if(like != null)
		{
			likeRepo.delete(like);
			recipe.setLikeCount(recipe.getLikeCount()-1);
			recipesRepo.save(recipe); 
		}
		else 
		{
			return "ban khong co quyen xoa like nay , hoac ban chua dang nhap " ; 
		}
		return "done";
	}

	@Override
	public List<UserDTO> getListUserLike(Integer Id) {
		List<User> users = likeRepo.getListUserLike(Id) ; 
		List<UserDTO> userDTO = new ArrayList<>()  ; 
		for(User user :users)
		{
			UserDTO item = model.map(user, UserDTO.class)  ;
			userDTO.add(item)  ; 
		}
		return userDTO;
	}

	@Override
	public String view( Integer Id , String token ) {
		Recipe recipe = recipesRepo.findById(Id).orElse(null)  ; 
		
		if(recipe==null)
		{
			return "bai viet nay khong ton tai " ; 
		}
		recipe.setViewCount(recipe.getViewCount() +1 );
		recipesRepo.save(recipe) ; 
		if(token != null )
		{
			String userName = jwt.extractUserName(token)  ; 
			User sender =userRepo.findByUserName(userName)  ; 
			if(sender == null )
			{
				return " ban dang nhap lai di "  ; 
			}
			
			notificationService.ViewRecipesNotification(sender, recipe.getUser(), recipe);
			return "done" ;
		}
		return "chua dang nhap nen xem thi van duoc, nhung khong gui thong bao duoc" ;  
		
	}

}
