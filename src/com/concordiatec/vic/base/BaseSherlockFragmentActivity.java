package com.concordiatec.vic.base;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.R;
import com.concordiatec.vic.UserActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.helper.BroadHelper;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.Route;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
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
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayShowCustomEnabled(true);
		
		profileLayout = (LinearLayout) actionBar.getCustomView().findViewById(R.id.actionbar_profile_layout);
		profilePhoto = (CircleImageView) profileLayout.findViewById(R.id.main_actionbar_profile_photo);
		userName = (TextView) profileLayout.findViewById(R.id.main_actionbar_custom_title);
		
		lService = new UserService(this);
		final LocalUser usr = lService.getLoginUser();
		
		if( usr != null && usr.getId() > 0 ){
			setIcon(usr.photo);
			setTitle(usr.name);
			
			profileLayout.setOnClickListener(new ActionbarAvatarClick() );
		}else{
			profileLayout.setOnClickListener( new GoLogin() );
		}
		
		// sign in/out broadcast receiver register
		BroadHelper.initOnlineBroadReciever(getApplicationContext(), new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				restoreActionbar();
			}
		});
	}
	
	protected void setIcon( int resId ){
		profilePhoto.setImageResource(resId);
	}
	
	protected void setIcon(String uri){
		if( StringUtil.isEmpty(uri) ){
			Glide.with(this).load(R.drawable.ic_default_avatar).into(profilePhoto);
		}else{
			Glide.with(this).load(uri).into(profilePhoto);
		}
		
	}
	
	@Override
	public void setTitle(CharSequence title) {
		userName.setText(title);
	}
	
	protected void logout(){
		lService.logout();
	}
	
	private void restoreActionbar(){
		LocalUser usr = lService.getLoginUser();
		if( usr != null && usr.getId() > 0 ){
			setIcon(usr.photo);
			setTitle(usr.name);
			profileLayout.setOnClickListener( new ActionbarAvatarClick() );
		}else{
			setIcon(R.drawable.ic_default_avatar);
			setTitle(R.string.login);
			profileLayout.setOnClickListener( new GoLogin() );
		}
		invalidateOptionsMenu();
	}
	
	protected final class GoLogin implements OnClickListener{
		@Override
		public void onClick(View v) {
			Route.goLogin(BaseSherlockFragmentActivity.this);
		}
		
	}

	protected final class ActionbarAvatarClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			LocalUser usr = lService.getLoginUser();
			Intent intent = new Intent( getApplicationContext() , UserActivity.class );
			intent.putExtra("user_id", usr.usrId);
			startActivity(intent);
		}
	} 
	
	
}
