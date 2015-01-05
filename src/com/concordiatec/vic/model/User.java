/**
 * 회원(사용자) 모델
 */
package com.concordiatec.vic.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="User")
public class User extends Model {
	
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
}
