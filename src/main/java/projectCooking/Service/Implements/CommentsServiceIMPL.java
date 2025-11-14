package projectCooking.Service.Implements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.CommentsDTO;
import projectCooking.Repository.CommentsRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.Comment;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.CommentsRequest;
import projectCooking.Service.CommentsService;
import projectCooking.Service.JWTService;
import projectCooking.Service.NotificationService;
@Service
public class CommentsServiceIMPL implements CommentsService {
	@Autowired
	private JWTService jwt ; 
	@Autowired
	private CommentsRepo commentsRepo ; 
	@Autowired
	private UserRepo userRepo ; 
	@Autowired
	private RecipesRepo recipeRepo ; 
	@Autowired
	private NotificationService notifService  ;
	@Autowired
	private ModelMapper model ; 
	@Override
	public String sendsComments(String token, CommentsRequest comments , Integer Id) {
		String userName = jwt.extractUserName(token)  ; 
		if( userName == null)
		{
			return "backend khong tim duoc ten cua ban , hinh nhu ten ban da bi xoa khoi database , hoac token cua ban da het han" ; 	
		}
		User user  = userRepo.findByUserName(userName)  ; 
		if(user == null )
		{
			return " token cua ban da het han , vui long dang nhap lai de comments"  ; 
		}
		Recipe recipe  = recipeRepo.findById(Id).orElse(null)  ; 
		if(recipe == null)
		{
			return "Cong thuc nay khong ton tai trong database"  ; 
		}
 		Comment comment = new Comment()  ; 
		comment.setContent(comments.getContents())  ; 
		comment.setCreatedAt(LocalDate.now());
		comment.setUpdatedAt(LocalDate.now());
		comment.setRecipe(recipe);
		comment.setUser(user);
		commentsRepo.save(comment)  ;
		notifService.sendCommentsNotification(user, recipe.getUser(), recipe ,  comments.getContents());
		return "done";
	}
	@Override
	public List<CommentsDTO> GetListComments(Integer Id ) {
		Recipe recipe = recipeRepo.findById(Id).orElse(null)  ; 
		if(recipe == null)
		{
			 throw new DulicateUserException("Cong thuc nay khong ton tai , hinh nhu da bi xoa bo khoi database")  ; 
		}
		List<Comment> commentsList = recipe.getComments();
		List<CommentsDTO> commentsDTOList = new ArrayList<>();

		for(Comment comments : commentsList) {
		 
		    if(comments.getParentComment() == null) {
		        CommentsDTO commentsDTO = model.map(comments, CommentsDTO.class);
		        commentsDTO.setAvatarUrl(comments.getUser().getAvatarUrl());
		        commentsDTO.setUserName(comments.getUser().getUserName());
		        commentsDTO.setUpdateAt(comments.getUpdatedAt());
		        commentsDTO.setCreateAt(comments.getCreatedAt());
		        commentsDTO.setParentComment(null); // 
		        commentsDTO.setUserId(comments.getUser().getUserId());
		       
		        List<CommentsDTO> repliesDTOList = new ArrayList<>();
		        for(Comment reply : commentsList) {
		            if(reply.getParentComment() != null && 
		               reply.getParentComment().getCommentId().equals(comments.getCommentId())) {
		                
		                CommentsDTO replyDTO = model.map(reply, CommentsDTO.class);
		                replyDTO.setAvatarUrl(reply.getUser().getAvatarUrl());
		                replyDTO.setUserName(reply.getUser().getUserName());
		                replyDTO.setUpdateAt(reply.getUpdatedAt());
		                replyDTO.setCreateAt(reply.getCreatedAt());
		                replyDTO.setParentCommentId(comments.getCommentId());
		                replyDTO.setUserId(reply.getUser().getUserId());
		                
		                replyDTO.setParentComment(null);
		                replyDTO.setReplies(new ArrayList<>());
		                
		                repliesDTOList.add(replyDTO);
		            }
		        }
		        
		        commentsDTO.setReplies(repliesDTOList);
		        commentsDTOList.add(commentsDTO);
		    }
		
		
		}
		return commentsDTOList;
	}
	@Override
	public String updateComments(Integer Id, CommentsRequest comments , String token ) {
		String userName = jwt.extractUserName(token)  ;
		User user = userRepo.findByUserName(userName)  ; 
		if(user == null)
		{
			return " nguoi dung khong hop le, chung toi khong tim thay ban trong database " ; 
		}
		Comment comment = commentsRepo.findById(Id).orElse(null)  ;
		if(comment == null)  
		{
			return "khong tim thay comments nay"  ; 
		}
		if(!comment.getUser().getUserName().equals(userName))
		{
			return "Ban khong quyen chinh sua cmt nay , vi ban khong phai la nguoi cmt bai viet nay   " ; 
		}
		comment.setContent(comments.getContents());
		comment.setUpdatedAt(LocalDate.now());
		commentsRepo.save(comment)  ; 
		return "da cap nhat";
	}
	@Override
	public String deleteComments(Integer Id , String token ) {
		String userName = jwt.extractUserName(token)  ;
		User user = userRepo.findByUserName(userName)  ; 
		if(user == null)
		{
			return " nguoi dung khong hop le, chung toi khong tim thay ban trong database " ; 
		}
		Comment comment = commentsRepo.findById(Id).orElse(null)  ; 
		if(comment == null )
		{
			return " comments nay khong ton tai "   ; 
		}
		if(!comment.getUser().getUserName().equals(userName) && !comment.getRecipe().getUser().getUserName().equals(userName))
		{
			return "Ban khong quyen chinh sua cmt nay , vi ban khong phai la nguoi cmt bai viet nay   " ; 
		}
		commentsRepo.delete(comment);
		return "done";
	}
	@Override
	public String replyComments(Integer Id, String token, CommentsRequest comments) {
		Comment comment = commentsRepo.findById(Id).orElse(null)   ; 
		if(comment == null)
		{
			return "khong tim thay cmt nay trong database"  ; 
		}
		String userName = jwt.extractUserName(token) ; 
		User Sender = userRepo.findByUserName(userName)  ; 
		if(Sender == null)  
		{
			return " nguoi gui cmt khong ton tai , hoac token da het han"  ; 
		}
		Comment commentSender = new Comment()  ; 
		commentSender.setParentComment(comment) ; 
		commentSender.setContent(comments.getContents());
		commentSender.setCreatedAt(LocalDate.now());
		commentSender.setUpdatedAt(LocalDate.now());
		commentSender.setRecipe(comment.getRecipe());
		commentSender.setUser(Sender);
		commentsRepo.save(commentSender)  ; 
		notifService.sendCommentsNotification(Sender, comment.getRecipe().getUser(), comment.getRecipe(), comments.getContents());
		notifService.ReplyCommentsNotification(Sender, comment.getUser(),comment.getRecipe(), comments.getContents());
		return "da gui cmt";
	}

}
