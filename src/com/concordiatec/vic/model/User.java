/**
 * 회원(사용자) 모델
 */
package com.concordiatec.vic.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
@Table(name="vic_usr")
public class User extends Model {
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
	public User() {
		super();
	}
	public User(String email, String pwd, String name, String photo) {
		super();
		this.email = email;
		this.pwd = pwd;
		this.name = name;
		this.photo = photo;
	}
	
}
