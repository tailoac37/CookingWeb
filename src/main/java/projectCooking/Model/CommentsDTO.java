package projectCooking.Model;

import java.time.LocalDate;
import java.util.List;

public class CommentsDTO {
	private Integer commentID ; 
	private String username , content , avatarUrl  ; 
	private CommentsDTO parentComment ; 
	private LocalDate createAt , updateAt  ;
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContents() {
		return content;
	}
	public void setContents(String contents) {
		this.content = contents;
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
	
	
}
