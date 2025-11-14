package projectCooking.API;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.UserDTO;
import projectCooking.Service.InterationService;

@RestController
public class InterationAPI {
// like 
	@Autowired
	private InterationService service  ;
	@PostMapping("/api/user/recipes/{id}/like") 
	public String likeRecipes(@RequestHeader("Authorization") String auth ,@PathVariable("id") Integer Id)
	{
		String token = auth.replace("Bearer","") ;
		return service.likeRecipes(token, Id) ;
	}
	@DeleteMapping("/api/user/recipes/{id}/like") 
	public String DellikeRecipes(@RequestHeader("Authorization") String auth ,@PathVariable("id") Integer Id)
	{
		String token = auth.replace("Bearer","") ;
		return service.delLikeRecipes(token, Id) ;
	}
	@GetMapping("/api/recipes/{id}/likes")
	public List<UserDTO> getListUserLike(@PathVariable("id") Integer Id)
	{
		return service.getListUserLike(Id) ; 
	}
	@PatchMapping("/api/recipes/{id}/view")
	public String view ( @PathVariable("id") Integer Id , @RequestHeader(value ="Authorization" , required =false) String auth )
	{
		String token = null ; 
		if(auth !=null)
		{
			token = auth.replace("Bearer","") ;
		}
		return service.view(Id, token) ; 
		 
	}
}
