package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.UserDTO;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.FollowService;

@RestController
public class FollowAPI {
	@Autowired
	FollowService service ; 
	@PostMapping("/api/user/{id}/follow")
	public String followUser(@RequestHeader("Authorization") String auth ,@PathVariable("id") Integer Id)
	{
		String token = auth.replace("Bearer", "") ;
		return service.followUser(token, Id) ;
	}
	@DeleteMapping("/api/user/{id}/follow")
	public String delfollow(@RequestHeader("Authorization") String auth ,@PathVariable("id") Integer Id)
	{
		String token = auth.replace("Bearer", "") ;
		return service.delFollow(token, Id) ;
	}
	@GetMapping("/api/{id}/followers") 
	public List<UserDTO> getfollowers(@PathVariable("id") Integer Id)  
	{
		return service.getfollowers(Id) ; 
	}
	@GetMapping("/api/{id}/following") 
	public List<UserDTO> getfollowing(@PathVariable("id") Integer Id)  
	{
		return service.getfollowing(Id) ; 
	}
}
