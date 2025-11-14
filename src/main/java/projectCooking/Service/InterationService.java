package projectCooking.Service;

import java.util.List;

import projectCooking.Model.UserDTO;

public interface InterationService {
	public String likeRecipes(String token , Integer Id)  ; 
	public String delLikeRecipes(String token , Integer Id) ;
	public List<UserDTO> getListUserLike(Integer Id)  ; 
	public String view( Integer Id , String token) ; 
}
