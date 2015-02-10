package com.concordiatec.vilnet.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LastestComment extends VicModel implements Serializable{
	
	private int commentId;
	private int userId;
	private String userName;
	private String userPhoto;
	private String commentText;
	private int replyWhose;
	private String replyWhoseName; 
	private int plusCount;
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public int getReplyWhose() {
		return replyWhose;
	}
	public void setReplyWhose(int replyWhose) {
		this.replyWhose = replyWhose;
	}
	public String getReplyWhoseName() {
		return replyWhoseName;
	}
	public void setReplyWhoseName(String replyWhoseName) {
		this.replyWhoseName = replyWhoseName;
	}
	public int getPlusCount() {
		return plusCount;
	}
	public void setPlusCount(int plusCount) {
		this.plusCount = plusCount;
	}
	
}
