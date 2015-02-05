/**
 * 회원(사용자) 모델
 */
package com.concordiatec.vic.model;

import java.io.Serializable;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
@SuppressWarnings("serial")
@Table(name="vic_usr")
public class LocalUser extends Model implements Serializable {
	@Column(name="usrId")
	public int usrId;
	@Column(name="email")
	public String email;
	@Column(name="pwd")
	public String pwd;
	@Column(name="name")
	public String name;
	@Column(name="photo")
	public String photo;
	@Column(name="isShop")
	public boolean isShop;
	@Column(name="shopId")
	public int shopId;
	@Column(name="shopGroupId")
	public int shopGroupId;
	@Column(name="areaId")
	public int areaId;
	@Column(name="shopFee")
	public int shopFee;
	@Column(name="shopPhone")
	public String shopPhone;
	@Column(name="shopIntro")
	public String shopIntro;
	@Column(name="shopAddr1")
	public String shopAddr1;
	@Column(name="shopAddr2")
	public String shopAddr2;
	@Column(name="shopLng")
	public double shopLng;
	@Column(name="shopLat")
	public double shopLat;
	@Column(name="shopScores")
	public int shopScores;
	@Column(name="shopLikeCount")
	public int shopLikeCount;
	@Column(name="shopShareCount")
	public int shopShareCount;
	@Column(name="shopStatus")
	public int shopStatus;
	@Column(name="shopCreated")
	public String shopCreated;
	@Column(name="sex")
	public int sex;
	public LocalUser() {
		super();
	}
	
}
