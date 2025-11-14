package projectCooking.Service.Implements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Model.UserDTO;
import projectCooking.Repository.FavoriteRepo;
import projectCooking.Repository.FollowRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Follow;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.FollowService;
import projectCooking.Service.JWTService;
import projectCooking.Service.NotificationService;
@Service
public class FollowServiceImplements implements FollowService {
	@Autowired
	JWTService jwt ; 
	@Autowired
	FollowRepo followRepo ; 
	@Autowired
	UserRepo userRepo ; 
	@Autowired
	NotificationService notifService; 
	@Autowired
	ModelMapper model ; 
	@Override
	public String followUser(String token ,Integer Id) {
		String userName = jwt.extractUserName(token)  ; 
		User sender = userRepo.findByUserName(userName)  ; 
		if(sender == null)
		{
			return "xem lai nguoi gui di , nguoi gui khong hop le "  ; 
		}
		User receiver = userRepo.findById(Id).orElse(null)  ; 
		if(receiver == null)
		{
			return "nguoi nhan khong ton tai ( database khong co nguoi dung nao co id nhu vay ca)" ; 
		}
		if(followRepo.checkFollwing(sender.getUserName(), receiver.getUserName())>0)
		{
			return "ban da follow nguoi nay tu truoc roi "  ; 
		}
		Follow follow = new Follow() ; 
		follow.setFollowing(receiver); 
		follow.setFollower(sender);
		follow.setCreatedAt(LocalDate.now());
		followRepo.save(follow)  ; 
		notifService.FollowNotification(sender, receiver);
 		return "done";
	}
	@Override
	public String delFollow(String token, Integer Id) {
		String userName = jwt.extractUserName(token)  ; 
		User sender = userRepo.findByUserName(userName)  ; 
		if(sender == null)
		{
			return "xem lai nguoi gui di , nguoi gui khong hop le "  ; 
		}
		User receiver = userRepo.findById(Id).orElse(null)  ; 
		if(receiver == null)
		{
			return "nguoi nhan khong ton tai ( database khong co nguoi dung nao co id nhu vay ca)" ; 
		}
		if(followRepo.checkFollwing(sender.getUserName(), receiver.getUserName())==0)
		{
			return "ban da khong follow nguoi nay tu truoc roi "  ; 
		}
		followRepo.deleteFollowing(sender.getUserId(), receiver.getUserId());
		return "done";
	}
	@Override
	public List<UserDTO> getfollowers(Integer Id) {
		List<User> result = followRepo.getFollower(Id) ; 
		List<UserDTO> userDTOList  = new ArrayList<>()  ; 
		for(User user : result) 
		{
			UserDTO userDTO = model.map(user , UserDTO.class)  ; 
			userDTOList.add(userDTO)  ; 
		}
		return userDTOList ;
		
	}
	@Override
	public List<UserDTO> getfollowing(Integer Id) {
		List<User> result = followRepo.getFollowing(Id) ; 
		List<UserDTO> userDTOList  = new ArrayList<>()  ; 
		for(User user : result) 
		{
			UserDTO userDTO = model.map(user , UserDTO.class)  ; 
			userDTOList.add(userDTO)  ; 
		}
		return userDTOList ;
	}

}
