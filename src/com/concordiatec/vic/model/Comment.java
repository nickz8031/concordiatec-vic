/**
 * 댓글 데이타 모델
 */
package com.concordiatec.vic.model;

public class Comment {
	/**
	 * 댓글 아이디
	 */
	private int id;
	/**
	 * 댓글 작성자;
	 */
	private User writer;
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
	private int upCount;
	
	
	public User getWriter() {
		return writer;
	}
	public void setWriter(User writer) {
		this.writer = writer;
	}
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
	public int getUpCount() {
		return upCount;
	}
	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}
	
}
