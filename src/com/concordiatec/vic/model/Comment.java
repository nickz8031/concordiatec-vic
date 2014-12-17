package com.concordiatec.vic.model;

public class Comment {
	private int id;
	private int writerId;
	private String writerName;
	private String writerPhotoURL;
	private String writerContent;
	private int pastTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getWriterContent() {
		return writerContent;
	}
	public void setWriterContent(String writerContent) {
		this.writerContent = writerContent;
	}
	public int getPastTime() {
		return pastTime;
	}
	public void setPastTime(int pastTime) {
		this.pastTime = pastTime;
	}
	
}
