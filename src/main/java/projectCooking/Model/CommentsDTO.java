package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class CommentsDTO {
	private Integer commentID ,parentCommentId ,userId; 
	private String userName , content , avatarUrl  ; 
	private CommentsDTO parentComment ; 
	private LocalDate createAt , updateAt  ;
	private List<CommentsDTO> replies; 
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public Integer getCommentID() {
		return commentID;
	}
	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDate getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}
	public LocalDate getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(LocalDate updateAt) {
		this.updateAt = updateAt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public CommentsDTO getParentComment() {
		return parentComment;
	}
	public void setParentComment(CommentsDTO parentComment) {
		this.parentComment = parentComment;
	}
	public List<CommentsDTO> getReplies() {
		return replies;
	}
	public void setReplies(List<CommentsDTO> replies) {
		this.replies = replies;
	}
	public Integer getParentCommentId() {
		return parentCommentId;
	}
	public void setParentCommentId(Integer parentCommentId) {
		this.parentCommentId = parentCommentId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
