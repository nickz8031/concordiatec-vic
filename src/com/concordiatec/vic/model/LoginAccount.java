package com.concordiatec.vic.model;

import java.util.ArrayList;
import java.util.List;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.concordiatec.vic.util.StringUtil;
@Table(name="vic_laccount")
public class LoginAccount extends Model {
	@Column(name="email")
	public String email;
	
	public LoginAccount() {
		super();
	}
	
	public LoginAccount(String email){
		super();
		this.email = email;
	}
	
	
	public static void addData( String data ){
		if( StringUtil.isEmpty(data) ) return;
		LoginAccount account = new Select().from(LoginAccount.class).where("email=?" , data).executeSingle();
		if( account == null ){
			account = new LoginAccount();
			account.email = data;
			account.save();
		}
	}
	
	public static List<String> getAll(){
		List<String> datas = new ArrayList<String>();
		List<LoginAccount> items = new Select().from(LoginAccount.class).orderBy("Id DESC").execute();
		if( items != null && items.size() > 0 ){
			for ( LoginAccount account : items ) {
				datas.add(account.email);
			}
		}
		return datas;
	}
}
