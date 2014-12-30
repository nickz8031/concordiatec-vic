/**
 * 글(소식) 모델
 */
package com.concordiatec.vic.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Article extends Model {
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
	@SerializedName("effected_id")
	private int effectId;
	/**
	 * article contents
	 */
	private String content;
	/**
	 * past time from write article
	 */
	@SerializedName("pasttime")
	private int pastTime;
	/**
	 * writer id
	 */
	@SerializedName("writer_id")
	private int writerId;
	/**
	 * writer name
	 */
	@SerializedName("writer_name")
	private String writerName;
	/**
	 * writer photo
	 */
	@SerializedName("writer_photo")
	private String writerPhotoURL;
	/**
	 * cover image
	 */
	@SerializedName("image")
	private String coverImageURL;
	/**
	 * cover image width
	 */
	@SerializedName("image_width")
	private int coverImageWidth;
	/**
	 * cover image height
	 */
	@SerializedName("image_height")
	private int coverImageHeight;
	
	@SerializedName("images")
	private List<ArticleImages> images;
	/**
	 * appoint shop id
	 */
	@SerializedName("shop_id")
	private int shopId;
	/**
	 * appoint shop name
	 */
	@SerializedName("shop_name")
	private String shopName;
	/**
	 * appoint shop address
	 */
	@SerializedName("shop_addr")
	private String shopAddr;
	/**
	 * appoint shop group id
	 */
	@SerializedName("shop_group")
	private int shopGroupId;
	/**
	 * be liked count
	 */
	@SerializedName("like_count")
	private int likeCount;
	
	@SerializedName("share_count")
	private int shareCount;
	/**
	 * comment count of article
	 */
	@SerializedName("comment_count")
	private int commentCount;
	/**
	 * latest comment objects of article
	 */
	@SerializedName("lastest_comments")
	private List<Comment> latestComments;
	
	private boolean isLike;
	
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
	public List<Comment> getLatestComments() {
		return latestComments;
	}
	public void setLatestComments(List<Comment> latestComments) {
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
		return 0;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
}
