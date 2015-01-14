package com.concordiatec.vic;

import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.LoginAccount;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
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
		
		ArrayAdapter<String> av = new ArrayAdapter<String>(this, R.layout.li_auto_complete_drop, LoginAccount.getAll());
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
			String emailPattern = "[a-zA-Z0-9]?[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+.[a-z]{2,5}";
			boolean emailFalseFlag = (!email.getText().toString().matches(emailPattern) || email.getText().length() < 1);
			boolean pwdFalseFlag = (pwd.getText().length() < 1);
			if (emailFalseFlag) {
				email.requestFocus();
				showErrorNotify();
			} else if (pwdFalseFlag) {
				pwd.requestFocus();
				showErrorNotify();
			} else {
				ProgressUtil.show(LoginActivity.this);
				lService.doLogin(email.getText().toString(), EncryptUtil.EncPwd(pwd.getText().toString()), new VicResponseListener() {
					@Override
					public void onSuccess(ResData data) {
						LogUtil.show( data.getData().toString() );
						//save email if it did not exist
						LoginAccount.addData(email.getText().toString());
						User usr = lService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
						lService.login(usr);
						LoginActivity.this.setResult(RESULT_OK);
						LoginActivity.this.finish();
					}
					
					@Override
					public void onFailure(int httpResponseCode, String responseBody) {
						LogUtil.show(responseBody);
					}
					
					@Override
					public void onError(ResData data) {
						showErrorNotify();
					}

					@Override
					public void onProgress(int written, int totalSize) {
						// TODO Auto-generated method stub
						
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
