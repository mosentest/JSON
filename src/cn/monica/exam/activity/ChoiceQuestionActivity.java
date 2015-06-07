package cn.monica.exam.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import cn.monica.exam.ExamApplication;
import cn.monica.exam.adapter.IListViewCallBack;
import cn.monica.exam.adapter.ListViewAdapter;
import cn.monica.exam.entity.Problems;
import cn.monica.exam.entity.Question;
import cn.monica.exam.entity.QuestionItem;
import cn.monica.exam.entity.WenJuan;
import cn.monica.exam.service.ProblemService;
import cn.monica.exam.utils.ActivityUtils;
import cn.monica.exam.utils.HttpUrlConstacts;
import cn.monica.exam.utils.HttpUtil;
import cn.monica.exam.utils.WarnUtils;
import com.luojunrong.R;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChoiceQuestionActivity extends Activity {

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

	int sumScore = 0;
	int questionIndex = 0;
	int wenjuan_id;
	List<Question> list;

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
		btnTip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tip.setVisibility(View.VISIBLE);
			}
		});
		btnAddError.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,String> param = new HashMap<String, String>();
				Question question = list.get(questionIndex);
				SharedPreferences userPreferences = ChoiceQuestionActivity.this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
				int user_login_id = userPreferences.getInt("USER_LOGIN_ID",-1);
				param.put("userId",user_login_id+"");
				param.put("disciplineId",question.id+"");
				HttpUtil.NetWorkDoPostAsyncTask doPost = new HttpUtil.NetWorkDoPostAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_ADD_ERROR_QUESTION, param, new HttpUtil.NetWorkCallBack() {
					@Override
					public void onSuccess(String jsonDate) {
						Toast.makeText(ChoiceQuestionActivity.this,"添加成功!",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(String msg) {
						Toast.makeText(ChoiceQuestionActivity.this,"添加失败!",Toast.LENGTH_SHORT).show();
					}
				});
				doPost.execute();
			}
		});

		btnAddFav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,String> param = new HashMap<String, String>();
				Question question = list.get(questionIndex);
				SharedPreferences userPreferences = ChoiceQuestionActivity.this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
				int user_login_id = userPreferences.getInt("USER_LOGIN_ID",-1);
				param.put("userId",user_login_id+"");
				param.put("disciplineId",question.id+"");
				HttpUtil.NetWorkDoPostAsyncTask doPost = new HttpUtil.NetWorkDoPostAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_ADD_FAV_QUESTION, param, new HttpUtil.NetWorkCallBack() {
					@Override
					public void onSuccess(String jsonDate) {
						Toast.makeText(ChoiceQuestionActivity.this,"收藏成功!",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(String msg) {
						Toast.makeText(ChoiceQuestionActivity.this,"收藏失败!",Toast.LENGTH_SHORT).show();
					}
				});
				doPost.execute();
			}
		});
	}

	private void initData(){
		Bundle extras = getIntent().getExtras();
		wenjuan_id = extras.getInt("WENJUAN_ID");
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("questionnaireId", wenjuan_id + "");
		HttpUtil.NetWorkDoGetAsyncTask doget = new HttpUtil.NetWorkDoGetAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_GET_QUESTIONS,param , new HttpUtil.NetWorkCallBack() {
			@Override
			public void onSuccess(String jsonDate) {
				try {
					JSONObject data = new JSONObject(jsonDate);
					JSONObject page = data.getJSONObject("page");
					JSONArray questionList = page.getJSONArray("content");
					list = new ArrayList<Question>();
					for(int i = 0 ; i < questionList.length();i++){
						JSONObject temp = questionList.getJSONObject(i);
						JSONObject tbDiscipline = temp.getJSONObject("tbDiscipline");
						Question question = new Question(tbDiscipline.toString());
						list.add(question);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				updateQuestion();
			}

			@Override
			public void onError(String msg) {

			}
		});
		doget.execute();

	}

	private void updateQuestion() {
		if(questionIndex == (list.size()-1)){
			btnNext.setText("提交");
			btnNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addScore();
					AlertDialog dialog = new AlertDialog.Builder(ChoiceQuestionActivity.this).setTitle("总得分").setMessage("你试卷的总分是："+sumScore).setPositiveButton("确定", new WarnUtils.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences userPreferences = ChoiceQuestionActivity.this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
							int user_login_id = userPreferences.getInt("USER_LOGIN_ID",-1);

							Map<String,String> param = new HashMap<String, String>();
							param.put("userId",user_login_id+"");
							param.put("questionnarieId",wenjuan_id+"");
							param.put("sum",sumScore+"");
							HttpUtil.NetWorkDoPostAsyncTask doPost = new HttpUtil.NetWorkDoPostAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_ADD_QUESTION_SCORE, param, new HttpUtil.NetWorkCallBack() {
								@Override
								public void onSuccess(String jsonDate) {
									Toast.makeText(ChoiceQuestionActivity.this,"成绩提交成功！",Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onError(String msg) {
									Toast.makeText(ChoiceQuestionActivity.this,"成绩提交失败！",Toast.LENGTH_SHORT).show();
								}
							});
							doPost.execute();
							dialog.dismiss();
							ChoiceQuestionActivity.this.finish();
						}
					}).create();
					dialog.show();
				}
			});
		}else{
			btnNext.setText("下一题");
			btnNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addScore();
					questionIndex++;
					updateQuestion();
				}
			});
		}
		tip.setVisibility(View.GONE);
		//这里开始写问题的逻辑
		if(list.size() == 0){
			Toast.makeText(ChoiceQuestionActivity.this,"该问卷没有题目！",Toast.LENGTH_SHORT).show();
			ChoiceQuestionActivity.this.finish();
		}else {
			Question question = list.get(questionIndex);
			content.setText(question.question);
			tip.setText(question.answers);
			fetchQuestionItems(question);
		}
	}

	private void addScore() {
		//这里开始写问题的逻辑
		Question question = list.get(questionIndex);
		int checkedRadioButtonId = optionGroup.getCheckedRadioButtonId();
		int answerIndex = -1;
		switch (checkedRadioButtonId){
			case R.id.question_option_a:
				answerIndex = 0;
				break;
			case R.id.question_option_b:
				answerIndex = 1;
				break;
			case R.id.question_option_c:
				answerIndex = 2;
				break;
			case R.id.question_option_d:
				answerIndex = 3;
				break;
		}
		QuestionItem questionItem = question.questionItems.get(answerIndex);
		if (question.answers.equals(questionItem.content)){
			sumScore += question.score;
//			Toast.makeText(ChoiceQuestionActivity.this,""+ sumScore,Toast.LENGTH_SHORT).show();
		}
	}

	private void fetchQuestionItems(final Question question) {
		final HashMap<String, String> param = new HashMap<String, String>();

		param.put("id", question.id  + "");
		HttpUtil.NetWorkDoGetAsyncTask doGet = new HttpUtil.NetWorkDoGetAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_GET_QUESTION_ITEM, param, new HttpUtil.NetWorkCallBack() {
			@Override
			public void onSuccess(String jsonDate) {
				List<QuestionItem> items = new ArrayList<QuestionItem>();
				try {
					Log.e("jsonDate++",jsonDate);
					JSONObject data = new JSONObject(jsonDate);
					JSONArray questionList = data.getJSONArray("list");

					for(int i = 0 ; i < questionList.length();i++){
						JSONObject temp = questionList.getJSONObject(i);
						QuestionItem questionItem = new QuestionItem(temp.toString());
						Log.e("问题选项",questionItem.content);
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
