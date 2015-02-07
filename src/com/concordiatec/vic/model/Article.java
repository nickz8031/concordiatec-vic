/**
 * 글(소식) 모델
 */
package com.concordiatec.vic.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Article extends VicModel implements Serializable {
	/**
	 * article id
	 */
	private int id;
	/**
	 * article type
	 */
	private int kind;
	/**
	 * article link type if it is not normal type
	 */
	private int effectId;
	/**
	 * article contents
	 */
	private String content;
	/**
	 * past time from write article
	 */
	private int pastTime;
	/**
	 * writer id
	 */
	private int writerId;
	/**
	 * writer is shop account
	 */
	private boolean writerIsShop;
	/**
	 * writer shop id
	 */
	private int writerShopId;
	/**
	 * writer name
	 */
	private String writerName;
	/**
	 * writer photo
	 */
	private String writerPhotoURL;
	/**
	 * cover image
	 */
	private String coverImageURL;
	/**
	 * cover image width
	 */
	private int coverImageWidth;
	/**
	 * cover image height
	 */
	private int coverImageHeight;
	
	private List<ArticleImages> images;
	/**
	 * appoint shop id
	 */
	private int shopId;
	/**
	 * appoint shop name
	 */
	private String shopName;
	/**
	 * appoint shop address
	 */
	private String shopAddr;
	/**
	 * appoint shop group id
	 */
	private int shopGroupId;
	/**
	 * be liked count
	 */
	private int likeCount;
	
	private int shareCount;
	/**
	 * comment count of article
	 */
	private int commentCount;
	/**
	 * latest comment objects of article
	 */
	private List<LastestComment> latestComments;
	
	private boolean isLike;
	
	/**
	 * allow comment for article
	 */
	private boolean allowComment;
	
	private int minHeight;
	

	public int getWriterShopId() {
		return writerShopId;
	}
	public boolean getWriterIsShop() {
		return writerIsShop;
	}
	public void setWriterIsShop(boolean writerIsShop) {
		this.writerIsShop = writerIsShop;
	}
	public void setWriterShopId(int writerShopId) {
		this.writerShopId = writerShopId;
	}
	public boolean isAllowComment() {
		return allowComment;
	}
	public void setIsAllowComment(boolean allowComment) {
		this.allowComment = allowComment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getEffectId() {
		return effectId;
	}
	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getCoverImageURL() {
		return coverImageURL;
	}
	public void setCoverImageURL(String coverImageURL) {
		this.coverImageURL = coverImageURL;
	}
	public int getCoverImageWidth() {
		return coverImageWidth;
	}
	public void setCoverImageWidth(int coverImageWidth) {
		this.coverImageWidth = coverImageWidth;
	}
	public int getCoverImageHeight() {
		return coverImageHeight;
	}
	public void setCoverImageHeight(int coverImageHeight) {
		this.coverImageHeight = coverImageHeight;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopAddr() {
		return shopAddr;
	}
	public void setShopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}
	public int getShopGroupId() {
		return shopGroupId;
	}
	public void setShopGroupId(int shopGroupId) {
		this.shopGroupId = shopGroupId;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public List<LastestComment> getLatestComments() {
		return latestComments;
	}
	public void setLatestComments(List<LastestComment> latestComments) {
		this.latestComments = latestComments;
	}
	public String getWriterPhotoURL() {
		return writerPhotoURL;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	
	public void setWriterPhotoURL(String writerPhotoURL) {
		this.writerPhotoURL = writerPhotoURL;
	}
	
	public List<ArticleImages> getImages() {
		return images;
	}
	public void setImages(List<ArticleImages> images) {
		this.images = images;
	}
	public int getShareCount() {
		return shareCount;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	public int getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}
	public void setAllowComment(boolean allowComment) {
		this.allowComment = allowComment;
	}
	
}
