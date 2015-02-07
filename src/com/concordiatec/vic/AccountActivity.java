package com.concordiatec.vic;

import java.util.regex.Pattern;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.constant.RegPattern;
import com.concordiatec.vic.constant.ResponseStatus;
import com.concordiatec.vic.helper.BroadHelper;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.AccountService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;

public class AccountActivity extends SubPageSherlockActivity implements OnClickListener {

	private RelativeLayout showNamePop;
	private RelativeLayout showSexPop;
	private RelativeLayout shopPwdPop;
	private LinearLayout popName;
	private EditText editName;
	private TextView closePopName;
	private TextView submitPopName;
	private LinearLayout popSex;
	private TextView closePopSex;
	private TextView submitPopSex;
	private RadioButton female;
	private RadioButton male;
	private LinearLayout popPwd;
	private EditText editOldPwd;
	private EditText editNewPwd;
	private TextView closePopPwd;
	private TextView submitPopPwd;
	private LocalUser user;
	private AccountService aService;
	private TextView textEmail;
	private TextView textName;
	private TextView textSex;
	
	private void initViews() {
		showNamePop = (RelativeLayout) findViewById(R.id.showNamePop);
		showSexPop = (RelativeLayout) findViewById(R.id.showSexPop);
		shopPwdPop = (RelativeLayout) findViewById(R.id.shopPwdPop);
		
		textEmail = (TextView) findViewById(R.id.textEmail);
		textName = (TextView) findViewById(R.id.textName);
		textSex = (TextView) findViewById(R.id.textSex);
		
		
		popName = (LinearLayout) findViewById(R.id.popName);
		editName = (EditText) findViewById(R.id.editName);
		closePopName = (TextView) findViewById(R.id.closePopName);
		submitPopName = (TextView) findViewById(R.id.submitPopName);
		
		popSex = (LinearLayout) findViewById(R.id.popSex);
		closePopSex = (TextView) findViewById(R.id.closePopSex);
		submitPopSex = (TextView) findViewById(R.id.submitPopSex);
		female = (RadioButton) findViewById(R.id.female);
		male = (RadioButton) findViewById(R.id.male);
		
		popPwd = (LinearLayout) findViewById(R.id.popPwd);
		editOldPwd = (EditText) findViewById(R.id.editOldPwd);
		editNewPwd = (EditText) findViewById(R.id.editNewPwd);
		closePopPwd = (TextView) findViewById(R.id.closePopPwd);
		submitPopPwd = (TextView) findViewById(R.id.submitPopPwd);
		
		showNamePop.setOnClickListener(this);
		closePopName.setOnClickListener(this);
		submitPopName.setOnClickListener(this);
		popName.setOnClickListener(this);

		showSexPop.setOnClickListener(this);
		closePopSex.setOnClickListener(this);
		submitPopSex.setOnClickListener(this);
		popSex.setOnClickListener(this);

		shopPwdPop.setOnClickListener(this);
		closePopPwd.setOnClickListener(this);
		submitPopPwd.setOnClickListener(this);
		popPwd.setOnClickListener(this);
		
	}
	
	@Override
	protected void onDestroy() {
		showNamePop = null;
		showSexPop = null;
		shopPwdPop = null;
		popName = null;
		editName = null;
		closePopName = null;
		submitPopName = null;
		popSex = null;
		closePopSex = null;
		submitPopSex = null;
		female = null;
		male = null;
		popPwd = null;
		editOldPwd = null;
		editNewPwd = null;
		closePopPwd = null;
		submitPopPwd = null;
		user = null;
		aService = null;
		textEmail = null;
		textName = null;
		textSex = null;
		setContentView(R.layout.null_layout);
		super.onDestroy();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zero_account_act);
		setTitle(getString(R.string.account_setting));		
		user = new UserService(this).getLoginUser();
		aService = new AccountService(this);
		initViews();
		textName.setText(user.name);
		textEmail.setText(user.email);
		textSex.setText(user.sex > 0 ? getString(R.string.male) : getString(R.string.female));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			// name
			case R.id.showNamePop:
				editName.setText(user.name);
				popName.setVisibility(View.VISIBLE);
				break;
			case R.id.closePopName:
				popName.setVisibility(View.GONE);
				break;
			case R.id.submitPopName:
				final String newNicknName = editName.getText().toString();
				if( newNicknName.equals( user.name ) ) return;
				if( !Pattern.matches(RegPattern.NICK_NAME, newNicknName) ){
					NotifyUtil.toast(this, R.string.invalid_nickname);
					return;
				}
				ProgressUtil.show(this);
				aService.modifyNickName(newNicknName , new SimpleVicResponseListener(){
					@Override
					public void onSuccess(ResData data) {
						user.name = newNicknName;
						user.save();
						BroadHelper.sendOnlineBroad(AccountActivity.this , true);
						NotifyUtil.toast(AccountActivity.this, R.string.modify_nickname_succed);
						popName.setVisibility(View.GONE);
						ProgressUtil.dismiss();
					}
					@Override
					public void onFailure(int httpResponseCode, String responseBody) {
						if( Constant.DEBUG ){
							super.onFailure(httpResponseCode, responseBody);
						}
						NotifyUtil.toast(AccountActivity.this, R.string.modify_nickname_failed);
						ProgressUtil.dismiss();
					}
				});
				break;
			case R.id.popName:
				popName.setVisibility(View.GONE);
				break;
			// sex
			case R.id.showSexPop:
				if(user.sex > 0){
					male.setChecked(true);
				}else{
					female.setChecked(true);
				}
				popSex.setVisibility(View.VISIBLE);
				break;
			case R.id.closePopSex:
				popSex.setVisibility(View.GONE);
				break;
			case R.id.submitPopSex:
				final int sex = male.isChecked() ? 1 : 0;
				if( sex == user.sex ) return;
				ProgressUtil.show(this);
				aService.modifyGender( sex , new SimpleVicResponseListener(){
					@Override
					public void onSuccess(ResData data) {
						user.sex = sex;
						user.save();
						BroadHelper.sendOnlineBroad(AccountActivity.this , true);
						NotifyUtil.toast(AccountActivity.this, R.string.modify_sex_succed);
						popSex.setVisibility(View.GONE);
						ProgressUtil.dismiss();
					}
					@Override
					public void onFailure(int httpResponseCode, String responseBody) {
						if( Constant.DEBUG ){
							super.onFailure(httpResponseCode, responseBody);
						}
						NotifyUtil.toast(AccountActivity.this, R.string.error_request);
						ProgressUtil.dismiss();
					}
				});
				break;
			// pwd
			case R.id.shopPwdPop:
				editOldPwd.setText(null);
				editNewPwd.setText(null);
				popPwd.setVisibility(View.VISIBLE);
				break;
			case R.id.closePopPwd:
				popPwd.setVisibility(View.GONE);
				break;
			case R.id.submitPopPwd: //change pwd
				String oldpwd = editOldPwd.getText().toString();
				if( !Pattern.matches(RegPattern.PASSWORD, oldpwd) ){
					NotifyUtil.toast(this, R.string.invalid_old_pwd);
					return;
				}
				String newpwd = editNewPwd.getText().toString();
				if( !Pattern.matches(RegPattern.PASSWORD, newpwd) ){
					NotifyUtil.toast(this, R.string.please_enter_pwd);
					return;
				}
				ProgressUtil.show(this);
				aService.modifyPwd(oldpwd, newpwd, new SimpleVicResponseListener(){
					@Override
					public void onSuccess(ResData data) {
						NotifyUtil.toast(AccountActivity.this, R.string.modify_pwd_succed);
						popPwd.setVisibility(View.GONE);
						ProgressUtil.dismiss();
					}
					@Override
					public void onFailure(int httpResponseCode, String responseBody) {
						if( Constant.DEBUG ){
							super.onFailure(httpResponseCode, responseBody);
						}
						if( httpResponseCode == ResponseStatus.ERROR_REQUEST ){
							NotifyUtil.toast(AccountActivity.this, R.string.please_check_old_pwd);
						}else{
							NotifyUtil.toast(AccountActivity.this, R.string.modify_pwd_failed);
						}
						ProgressUtil.dismiss();
					}
				});
				break;
			case R.id.popPwd:
				popPwd.setVisibility(View.GONE);
				break;
		}
	}
}
