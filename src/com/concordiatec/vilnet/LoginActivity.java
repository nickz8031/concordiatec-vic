package com.concordiatec.vilnet;

import java.util.regex.Pattern;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.base.SubPageSherlockActivity;
import com.concordiatec.vilnet.constant.RegPattern;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.model.LocalLoginAccount;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.UserService;
import com.concordiatec.vilnet.util.LogUtil;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.google.gson.internal.LinkedTreeMap;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends SubPageSherlockActivity {
	private Button loginButton;
	private Button signUpButton;
	private Button findPwdButton;
	private AutoCompleteTextView email;
	private EditText pwd;
	private ImageView clearEmail;
	private ImageView clearPwd;
	private UserService lService;
	@Override
	public void onBackPressed() {
		if( ProgressUtil.isShowing() ){
			return;
		}
		super.onBackPressed();
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle(getString(R.string.login));
		lService = new UserService(this);
		// email edit text
		email = (AutoCompleteTextView) findViewById(R.id.user_account);
		// pwd edit text
		pwd = (EditText) findViewById(R.id.user_pwd);
		// clear btns
		clearEmail = (ImageView) findViewById(R.id.clear_enter_email);
		clearPwd = (ImageView) findViewById(R.id.clear_enter_pwd);
		
		// create ArrayAdapter
		
		ArrayAdapter<String> av = new ArrayAdapter<String>(this, R.layout.li_auto_complete_drop, LocalLoginAccount.getAll());
		email.setAdapter(av);
		email.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					clearEmail.setVisibility(View.VISIBLE);
				} else {
					clearEmail.setVisibility(View.GONE);
				}
			}
		});
		
		
		
		pwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					clearPwd.setVisibility(View.VISIBLE);
				} else {
					clearPwd.setVisibility(View.GONE);
				}
			}
		});
		// login button
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(new loginClickListener());
		clearEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				email.setText(null);
			}
		});
		clearPwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwd.setText(null);
			}
		});
		/**
		 * soft input keyboard enter listener
		 */
		pwd.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					new loginClickListener().onClick(loginButton);
					return true;
				} else {
					return false;
				}
			}
		});
		// sign up button
		signUpButton = (Button) findViewById(R.id.sign_up_button);
		signUpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
		// find password button
		findPwdButton = (Button) findViewById(R.id.find_pwd_button);
		findPwdButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
				startActivity(intent);
			}
		});
	}

	private final class loginClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			final String emailString = email.getText().toString();
			String pwdString = pwd.getText().toString();
			boolean emailFalseFlag = (!Pattern.matches(RegPattern.EMAIL, emailString));
			boolean pwdFalseFlag = (!Pattern.matches(RegPattern.PASSWORD, pwdString));
			LogUtil.show(String.valueOf(pwdFalseFlag));
			
			if (emailFalseFlag) {
				email.requestFocus();
				showErrorNotify();
			} else if (pwdFalseFlag) {
				pwd.requestFocus();
				showErrorNotify();
			} else {
				ProgressUtil.show(LoginActivity.this);
				lService.doLogin(emailString, pwdString, new SimpleVicResponseListener() {
					@Override
					public void onSuccess(ResData data) {
						//save email if it did not exist in local database
						LocalLoginAccount.addData(emailString);
						LocalUser usr = lService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
						lService.login(usr);
						LoginActivity.this.finish();
					}
					
					@Override
					public void onFailure(int httpResponseCode, String responseBody) {
						LogUtil.show(responseBody);
						showErrorNotify();
						ProgressUtil.dismiss();
					}
					@Override
					public void onEmptyResponse() {
						showErrorNotify();
						ProgressUtil.dismiss();
					}
				});
			}
		}
	}
	
	/**
	 * show notify toast
	 * 
	 * @param int resourceId
	 */
	private void showErrorNotify() {
		NotifyUtil.toastCustom(getApplicationContext(), getString(R.string.please_enter_valid_login_info), Math.round(getResources().getDimension(R.dimen.login_toast_y)));
	}
}
