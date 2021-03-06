/**
 * 회원(사용자) 모델
 */
package com.concordiatec.vilnet.model;

import java.io.Serializable;
import java.util.List;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@SuppressWarnings("serial")
@Table(name = "dong_usr")
public class LocalUser extends Model implements Serializable {
	@Override
	public String toString() {
		return "LocalUser [usrId=" + usrId + ", email=" + email + ", pwd=" + pwd + ", name=" + name + ", sex=" + sex + ", photo=" + photo + ", isShop=" + isShop + ", shopId=" + shopId + ", shopPhone=" + shopPhone + ", shopAddr1=" + shopAddr1 + ", shopAddr2=" + shopAddr2 + ", shopLng=" + shopLng + ", shopLat=" + shopLat + ", shopScoreCount=" + shopScoreCount + ", shopLikeCount=" + shopLikeCount + ", shopShareCount=" + shopShareCount + ", isOpen=" + isOpen + ", isPause=" + isPause + ", groupName=" + groupName + "]";
	}

	@Column(name = "usrId")
	public int usrId;
	@Column(name = "email")
	public String email;
	@Column(name = "pwd")
	public String pwd;
	@Column(name = "name")
	public String name;
	@Column(name = "sex")
	public int sex;
	@Column(name = "photo")
	public String photo;
	@Column(name = "isShop")
	public boolean isShop;
	@Column(name = "shopId")
	public int shopId;
	@Column(name = "shopPhone")
	public String shopPhone;
	@Column(name = "shopAddr1")
	public String shopAddr1;
	@Column(name = "shopAddr2")
	public String shopAddr2;
	@Column(name = "shopLng")
	public double shopLng;
	@Column(name = "shopLat")
	public double shopLat;
	@Column(name = "shopScoreCount")
	public int shopScoreCount;
	@Column(name = "shopLikeCount")
	public int shopLikeCount;
	@Column(name = "shopShareCount")
	public int shopShareCount;
	@Column(name = "isOpen")
	public boolean isOpen;
	@Column(name = "isPause")
	public boolean isPause;
	@Column(name = "groupName")
	public String groupName;

	public LocalUser() {
		super();
	}
}
