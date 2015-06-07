package cn.monica.exam.activity;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cn.monica.exam.utils.HttpUrlConstacts;
import com.luojunrong.R;
import cn.monica.exam.utils.ActivityUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FinalActivity{
	@ViewInject(id=R.id.mStudy_layout) LinearLayout mStudy;
	@ViewInject(id=R.id.mExam_layout) LinearLayout mExam;
	@ViewInject(id=R.id.mFavour_layout) LinearLayout mFavour;
	@ViewInject(id=R.id.mError_layout) LinearLayout mError;
	@ViewInject(id=R.id.mLogin_layout) LinearLayout mLogin;
	@ViewInject(id=R.id.mLogout_layout) LinearLayout mLogout;

	boolean loginFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);        
		initTitleBar();

		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences userInfo = this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
		loginFlag = userInfo.getBoolean("USER_LOGIN_FLAG", false);
		if(loginFlag == false){
			mLogin.setVisibility(View.VISIBLE);
			mLogout.setVisibility(View.GONE);
		}else{
			mLogin.setVisibility(View.GONE);
			mLogout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initTitleBar() {
		ImageView leftButton = (ImageView)findViewById(R.id.left_btn);
		leftButton.setVisibility(View.GONE);
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(R.string.main_title);        
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}

	private void setListener(){
		mStudy.setOnClickListener(mClick);
		mExam.setOnClickListener(mClick);
		mFavour.setOnClickListener(mClick);
		mError.setOnClickListener(mClick);
		mLogin.setOnClickListener(mClick);
		mLogout.setOnClickListener(mClick);
	}
	
	private android.view.View.OnClickListener mClick = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mStudy_layout:
				ActivityUtils.to(MainActivity.this, StudyActivity.class);
				break;
			case R.id.mExam_layout:
				if(loginFlag){
					ActivityUtils.to(MainActivity.this, ExamActivity.class);
				}else{
					ActivityUtils.to(MainActivity.this, LoginAcitiviy.class);
				}
				break;
			case R.id.mFavour_layout:
				if(loginFlag){
					ActivityUtils.to(MainActivity.this, FavouriteActivity.class);
				}else{
					ActivityUtils.to(MainActivity.this, LoginAcitiviy.class);
				}
				break;
			case R.id.mError_layout:
				if(loginFlag){
					ActivityUtils.to(MainActivity.this, ErrorActivity.class);
				}else{
					ActivityUtils.to(MainActivity.this, LoginAcitiviy.class);
				}
				break;
				case R.id.mLogin_layout:
					ActivityUtils.to(MainActivity.this, LoginAcitiviy.class);
					break;
				case R.id.mLogout_layout:
					SharedPreferences userPreferences = MainActivity.this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
					SharedPreferences.Editor edit = userPreferences.edit();
					edit.putBoolean("USER_LOGIN_FLAG", false);
					edit.putInt("USER_LOGIN_ID", -1);
					edit.putString("USER_LOGIN_NAME", "");
					edit.apply();
					edit.commit();
					Toast.makeText(MainActivity.this, "您已注销！", Toast.LENGTH_SHORT).show();
					mLogin.setVisibility(View.VISIBLE);
					mLogout.setVisibility(View.GONE);
					break;
				
				
			}
		}
	};
	


}
