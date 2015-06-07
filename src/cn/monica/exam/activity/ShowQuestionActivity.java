package cn.monica.exam.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import cn.monica.exam.entity.Question;
import cn.monica.exam.entity.QuestionItem;
import cn.monica.exam.utils.HttpUrlConstacts;
import cn.monica.exam.utils.HttpUtil;
import cn.monica.exam.utils.WarnUtils;
import com.luojunrong.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowQuestionActivity extends Activity {

	TextView content;
	TextView tip;
	Button btnAddError;
	Button btnAddFav;
	Button btnNext;
	Button btnTip;
	RadioGroup optionGroup;
	RadioButton optionA;
	RadioButton optionB;
	RadioButton optionC;
	RadioButton optionD;

	Question question;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_question_choice);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);
		initTitleBar();
		initView();
		initData();
	}

	private void initView() {

		content = (TextView) findViewById(R.id.question_content);
		tip = (TextView) findViewById(R.id.question_option_tip);
		btnAddError = (Button) findViewById(R.id.question_add_error);
		btnAddFav = (Button) findViewById(R.id.question_add_fav);
		 btnNext = (Button) findViewById(R.id.question_next);
		 btnTip = (Button) findViewById(R.id.question_tip);
		 optionGroup = (RadioGroup) findViewById(R.id.question_option_group);
		 optionA = (RadioButton) findViewById(R.id.question_option_a);
		 optionB = (RadioButton) findViewById(R.id.question_option_b);
		 optionC = (RadioButton) findViewById(R.id.question_option_c);
		 optionD = (RadioButton) findViewById(R.id.question_option_d);
		btnAddError.setVisibility(View.GONE);
		btnAddFav.setVisibility(View.GONE);
		btnNext.setVisibility(View.GONE);
		btnTip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tip.setVisibility(View.VISIBLE);
			}
		});

	}

	private void initData(){
		Bundle extras = getIntent().getExtras();
		question = (Question) extras.getSerializable("QUESTION");
		updateQuestion();
	}

	private void updateQuestion() {
			content.setText(question.question);
			tip.setText(question.answers);
			fetchQuestionItems(question);
	}


	private void fetchQuestionItems(final Question question) {
		final HashMap<String, String> param = new HashMap<String, String>();
		param.put("id", question.id  + "");
		HttpUtil.NetWorkDoGetAsyncTask doGet = new HttpUtil.NetWorkDoGetAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_GET_QUESTION_ITEM, param, new HttpUtil.NetWorkCallBack() {
			@Override
			public void onSuccess(String jsonDate) {
				List<QuestionItem> items = new ArrayList<QuestionItem>();
				try {
					JSONObject data = new JSONObject(jsonDate);
					JSONArray questionList = data.getJSONArray("list");

					for(int i = 0 ; i < questionList.length();i++){
						JSONObject temp = questionList.getJSONObject(i);
						QuestionItem questionItem = new QuestionItem(temp.toString());
						items.add(questionItem);
					}
					question.questionItems = items;
					if(question.questionItems != null && question.questionItems.size() != 0){
						QuestionItem questionItemA = question.questionItems.get(0);
						optionA.setText(questionItemA.content);
						QuestionItem questionItemB = question.questionItems.get(1);
						optionB.setText(questionItemB.content);
						QuestionItem questionItemC = question.questionItems.get(2);
						optionC.setText(questionItemC.content);
						QuestionItem questionItemD = question.questionItems.get(3);
						optionD.setText(questionItemD.content);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String msg) {

			}
		});
		doGet.execute();
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
		titleText.setText(R.string.exam_problem);
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}


}
