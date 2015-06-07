package cn.monica.exam.activity;

import cn.monica.exam.ExamApplication;
import com.luojunrong.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class SettingActivity extends FinalActivity implements OnClickListener{
	@ViewInject(id=R.id.txtBig) TextView txtBig;
	@ViewInject(id=R.id.txtMiddle) TextView txtMiddle;
	@ViewInject(id=R.id.txtSmall) TextView txtSmall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_setting);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);        
		initTitleBar();

		initView();

		txtSmall.setOnClickListener(this);
		txtMiddle.setOnClickListener(this);
		txtBig.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	private void initView(){
		txtSmall.setTextColor(R.color.black);
		txtMiddle.setTextColor(R.color.black);
		txtBig.setTextColor(R.color.black);

		if(ExamApplication.fontSize == 16){
			txtSmall.setTextColor(this.getResources().getColor(R.color.red));
		}else if(ExamApplication.fontSize == 20){
			txtMiddle.setTextColor(this.getResources().getColor(R.color.red));
		}else if(ExamApplication.fontSize == 24){
			txtBig.setTextColor(this.getResources().getColor(R.color.red));
		}
	}

	/**
	 * 设置左右标签,专门设置title_bar
	 */
	private void initTitleBar() {
		ImageView leftButton = (ImageView)findViewById(R.id.left_btn);
		leftButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(R.string.setting_title);        
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtSmall:
			ExamApplication.fontSize = 16;
			break;
		case R.id.txtMiddle:
			ExamApplication.fontSize = 20;
			break;
		case R.id.txtBig:
			ExamApplication.fontSize = 24;
			break;
		default:
			break;
		}
		initView();
	}
}
