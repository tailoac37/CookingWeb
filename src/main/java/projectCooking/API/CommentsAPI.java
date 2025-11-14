package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.CommentsDTO;
import projectCooking.Request.CommentsRequest;
import projectCooking.Service.CommentsService;

@RestController
public class CommentsAPI {
	@Autowired
	private CommentsService service ; 
	@PostMapping("/api/user/recipes/{id}/comments")
	public String SendsComments(@RequestHeader("Authorization") String auth , @RequestBody() CommentsRequest comments , @PathVariable("id") Integer Id )
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.sendsComments(token, comments, Id)  ; 
	}
	@GetMapping("/api/recipes/{id}/comments")
	public List<CommentsDTO> getListComments(@PathVariable("id") Integer Id )
	{
		return service.GetListComments(Id)   ; 
	}
	@PutMapping("/api/user/comments/{id}") 
	public String updateComments (@PathVariable("id") Integer Id,@RequestBody CommentsRequest comments , @RequestHeader("Authorization") String auth  ) 
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.updateComments(Id, comments , token) ; 
	}
	@DeleteMapping("/api/user/comments/{id}")
	public String deleteComments(@PathVariable("id") Integer Id , @RequestHeader("Authorization") String auth) 
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.deleteComments(Id , token) ; 
	}
	@PostMapping("/api/user/comments/{id}/reply")
	public String replyComments(@PathVariable("id") Integer Id,@RequestBody CommentsRequest comments , @RequestHeader("Authorization") String auth )
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.replyComments(Id, token, comments) ; 
	}
}
