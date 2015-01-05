package com.concordiatec.vic.base;
import android.content.Intent;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.concordiatec.vic.ArticleDetailActivity;
import com.concordiatec.vic.LoginActivity;
import com.concordiatec.vic.R;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.LoginService;
import com.concordiatec.vic.widget.CircleImageView;

public class BaseSherlockFragmentActivity extends SherlockFragmentActivity {
	protected ActionBar actionBar;
	private LoginService lService;
	private CircleImageView profilePhoto;
	private TextView userName;
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
		
		LinearLayout profileLayout = (LinearLayout) actionBar.getCustomView().findViewById(R.id.actionbar_profile_layout);
		profilePhoto = (CircleImageView) profileLayout.findViewById(R.id.main_actionbar_profile_photo);
		userName = (TextView) profileLayout.findViewById(R.id.main_actionbar_custom_title);
		
		lService = new LoginService(this);
		User usr = lService.getLoginUser();
		if( usr != null && usr.getId() > 0 ){
			setIcon(R.drawable.demo_avatar);
			setTitle(R.string.demo_writer_name);
		}else{
			profileLayout.setOnClickListener( new ActionbarClickListener() );
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
			case RESULT_OK:
				setIcon(R.drawable.demo_avatar);
				setTitle(R.string.demo_writer_name);
				break;
			default:
				break;
		}
	}
	
	protected void setIcon( int resId ){
		profilePhoto.setImageResource(resId);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		userName.setText(title);
	}
	
	private final class ActionbarClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent( getApplicationContext() , LoginActivity.class);
			startActivityForResult(intent,0);
		}
		
	}
	
}
