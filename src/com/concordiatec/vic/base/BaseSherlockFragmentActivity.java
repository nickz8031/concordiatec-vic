package com.concordiatec.vic.base;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.LoginActivity;
import com.concordiatec.vic.R;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.widget.CircleImageView;

public class BaseSherlockFragmentActivity extends SherlockFragmentActivity {
	protected ActionBar actionBar;
	private UserService lService;
	private CircleImageView profilePhoto;
	private TextView userName;
	private LinearLayout profileLayout;
	
	@Override
	protected void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.main_custom_ab);
		actionBar.setDisplayShowCustomEnabled(true);
		
		profileLayout = (LinearLayout) actionBar.getCustomView().findViewById(R.id.actionbar_profile_layout);
		profilePhoto = (CircleImageView) profileLayout.findViewById(R.id.main_actionbar_profile_photo);
		userName = (TextView) profileLayout.findViewById(R.id.main_actionbar_custom_title);
		
		lService = new UserService(this);
		User usr = lService.getLoginUser();
		
		if( usr != null && usr.getId() > 0 ){
			setIcon(usr.photo);
			setTitle(usr.name);
			profileLayout.setOnClickListener( new Logout() );
		}else{
			profileLayout.setOnClickListener( new GoLogin() );
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK ){
			restoreActionbar();
			sendOnlineBroad(true);
		}
	}
	
	protected void setIcon( int resId ){
		profilePhoto.setImageResource(resId);
	}
	
	protected void setIcon(String uri){
		Glide.with(this).load(uri).into(profilePhoto);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		userName.setText(title);
	}
	
	private void restoreActionbar(){
		User usr = lService.getLoginUser();
		if( usr != null && usr.getId() > 0 ){
			setIcon(usr.photo);
			setTitle(usr.name);
			profileLayout.setOnClickListener( new Logout() );
		}else{
			setIcon(R.drawable.ic_default_avatar);
			setTitle(R.string.login);
			profileLayout.setOnClickListener( new GoLogin() );
		}
	}
	
	protected final class GoLogin implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent( getApplicationContext() , LoginActivity.class);
			startActivityForResult(intent,0);
		}
		
	}
	
	
	protected final class Logout implements OnClickListener{
		@Override
		public void onClick(View v) {
			lService.logout();
			restoreActionbar();
			sendOnlineBroad(false);
		}
		
	}
	
	 
	private void sendOnlineBroad( boolean isOnline ){
		Intent intent = new Intent(Constant.ONLINE_BROAD_ACTION);
		intent.putExtra(Constant.ONLINE_BROAD_INTENT_KEY, isOnline);
		sendBroadcast(intent);
	}
	
}
