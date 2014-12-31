package com.concordiatec.vic.model;

import com.google.gson.annotations.SerializedName;

public class LastestComment extends Model{
	
	@SerializedName("comment_id")
	private int commentId;
	@SerializedName("user_id")
	private int userId;
	@SerializedName("user_name")
	private String userName;
	@SerializedName("user_photo")
	private String userPhoto;
	@SerializedName("comment_text")
	private String commentText;
	
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
	
}
