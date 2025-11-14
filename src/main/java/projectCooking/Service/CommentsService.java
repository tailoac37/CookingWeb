package projectCooking.Service;

import java.util.List;

import projectCooking.Model.CommentsDTO;
import projectCooking.Request.CommentsRequest;

public interface CommentsService {
	public String sendsComments(String token , CommentsRequest comments , Integer Id) ; 
	public List<CommentsDTO> GetListComments(Integer Id)  ; 
	public String updateComments(Integer Id , CommentsRequest comments , String token )   ; 
	public String deleteComments(Integer Id, String token )  ;
	public String replyComments(Integer Id , String token , CommentsRequest comments) ;
}
