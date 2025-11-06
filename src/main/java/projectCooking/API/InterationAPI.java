package projectCooking.API;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
}
