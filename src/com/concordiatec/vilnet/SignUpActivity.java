package com.concordiatec.vilnet;

import java.util.regex.Pattern;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.base.SubPageSherlockActivity;
import com.concordiatec.vilnet.constant.RegPattern;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.model.LocalLoginAccount;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.UserService;
import com.concordiatec.vilnet.util.EncryptUtil;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.google.gson.internal.LinkedTreeMap;

public class SignUpActivity extends SubPageSherlockActivity {
	private Button submit;
	private EditText nickName;
	private EditText email;
	private EditText pwd;
	private CheckBox locationServiceAgree;
	private CheckBox infomationAgree;
	private RadioButton female;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		setTitle(getString(R.string.signup));
		
		submit = (Button) findViewById(R.id.reg_submit_btn);
		nickName = (EditText) findViewById(R.id.reg_nickname);
		email = (EditText) findViewById(R.id.reg_email);
		pwd = (EditText) findViewById(R.id.reg_pwd);
		locationServiceAgree = (CheckBox) findViewById(R.id.agree_location_service);
		infomationAgree = (CheckBox) findViewById(R.id.agree_information_service);
		female = (RadioButton) findViewById(R.id.reg_radio_female);
		submit.setOnClickListener(new RegSubmitListener());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		setContentView(R.layout.null_layout);
		submit = null;
		nickName = null;
		email = null;
		pwd = null;
		locationServiceAgree = null;
		infomationAgree = null;
		female = null;
	}

	private final class RegSubmitListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//닉네임 입력
			String nickNameString = nickName.getText().toString();
			boolean nickNameFalseFlag = (!Pattern.matches(RegPattern.NICK_NAME, nickNameString));
			if( nickNameFalseFlag ){
				nickName.requestFocus();
				NotifyUtil.toast(SignUpActivity.this, getString(R.string.please_enter_nickname));
				return;
			}
			//이메일 입력
			final String emailString = email.getText().toString();
			boolean emailFalseFlag = (!Pattern.matches(RegPattern.EMAIL, emailString));
			if( emailFalseFlag ){
				email.requestFocus();
				NotifyUtil.toast(SignUpActivity.this, getString(R.string.please_enter_email));
				return;
			}

			//비밀번호 입력
			String pwdString = pwd.getText().toString();
			boolean pwdFalseFlag = (pwdString.length()>20 || pwdString.length() < 6);
			if( pwdFalseFlag ){
				pwd.requestFocus();
				NotifyUtil.toast(SignUpActivity.this, getString(R.string.please_enter_pwd));
				return;
			}
			//약관동의 위치기반
			if( !locationServiceAgree.isChecked() ){
				NotifyUtil.toast(SignUpActivity.this, getString(R.string.please_agree_location_service));	
				return;
			}
			//약관동의 개인정보
			if( !infomationAgree.isChecked() ){
				NotifyUtil.toast(SignUpActivity.this, getString(R.string.please_agree_personal_information_usage));		
				return;
			}
			
			LocalUser user = new LocalUser();
			user.email = emailString;
			user.pwd = EncryptUtil.EncPwd(pwdString);
			user.name = nickNameString;
			user.sex = female.isChecked() ? 0 : 1;
			final UserService userService = new UserService(getApplicationContext());
			userService.signup(user, new SimpleVicResponseListener(){
				@Override
				public void onSuccess(ResData data) {
					LocalLoginAccount.addData(emailString);
					LocalUser usr = userService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
					userService.login(usr);
					SignUpActivity.this.finish();
				}
			});
		}
	}
}
