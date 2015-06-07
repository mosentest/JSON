package cn.monica.exam.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import cn.monica.exam.adapter.IListViewCallBack;
import cn.monica.exam.adapter.ListViewAdapter;
import cn.monica.exam.entity.ProjectType;
import cn.monica.exam.entity.Word;
import cn.monica.exam.utils.ActivityUtils;
import com.luojunrong.R;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WordActivity extends FinalActivity{

	@ViewInject(id=R.id.word_title)
	TextView title;
	@ViewInject(id=R.id.word_content)
	TextView content;

	Word word;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_word);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);
		initTitleBar();

		initViews();
	}


	private void initViews(){
		Bundle extras = getIntent().getExtras();
		word = (Word) extras.getSerializable("WORD_DATA");
		title.setText(word.title);
		content.setText(word.content);
	}


	private void initTitleBar() {
		ImageView leftButton = (ImageView)findViewById(R.id.left_btn);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(R.string.word_title);
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.fill_lay:
//			ActivityUtils.to(this, FillQActivity.class);
//			break;
//		case R.id.choice_lay:
//			ActivityUtils.to(this, ChoiceQActivity.class);
//			break;
//		case R.id.judge_lay:
//			ActivityUtils.to(this, JudgeQActivity.class);
//			break;
//		default:
//			break;
//		}
//	}
}
