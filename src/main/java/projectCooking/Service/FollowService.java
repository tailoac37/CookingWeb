package projectCooking.Service;

import java.util.List;

import projectCooking.Model.UserDTO;

public interface FollowService {
	public String followUser(String token , Integer Id) ;
	public String delFollow(String token , Integer Id)  ; 
	public List<UserDTO> getfollowers(Integer Id)  ; 
	public List<UserDTO> getfollowing(Integer Id)  ; 
}
