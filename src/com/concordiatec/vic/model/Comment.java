/**
 * 댓글 데이타 모델
 */
package com.concordiatec.vic.model;

public class Comment extends VicModel {
	/**
	 * 댓글 아이디
	 */
	private int id;
	
	/**
	 * 댓글 소속 원글 아이디
	 */
	private int articleId;
	
	/**
	 * 댓글 작성자 아이디;
	 */
	private int writerId;
	/**
	 * 댓글 작성자 이름
	 */
	private String writerName;
	/**
	 * 댓글 작성자 사진
	 */
	private String writerPhotoURL;
	/**
	 * 댓글 내용
	 */
	private String content;
	/**
	 * 댓글 등록 후 경과한 시간
	 */
	private int pastTime;
	/**
	 * 댓글 공감수
	 */
	private int plusCount;
	/**
	 * reply comment id
	 */
	private int replyId;
	/**
	 * be replied user id
	 */
	private int replyWhose;
	/**
	 * be replied user name
	 */
	private String replyWhoseName; 
	/**
	 * is plus to this comment
	 */
	private boolean isPlus;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPastTime() {
		return pastTime;
	}
	public void setPastTime(int pastTime) {
		this.pastTime = pastTime;
	}
	public int getWriterId() {
		return writerId;
	}
	public void setWriterId(int writerId) {
		this.writerId = writerId;
	}
	public String getWriterName() {
		return writerName;
	}
	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
	public String getWriterPhotoURL() {
		return writerPhotoURL;
	}
	public void setWriterPhotoURL(String writerPhotoURL) {
		this.writerPhotoURL = writerPhotoURL;
	}
	public int getPlusCount() {
		return plusCount;
	}
	public void setPlusCount(int plusCount) {
		this.plusCount = plusCount;
	}
	public boolean isPlus() {
		return isPlus;
	}
	public void setPlus(boolean isPlus) {
		this.isPlus = isPlus;
	}
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
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
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	
}
