package cn.monica.exam.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.monica.exam.ExamApplication;
import com.luojunrong.R;
import cn.monica.exam.entity.Problems;
import cn.monica.exam.service.ProblemService;
import cn.monica.exam.utils.WarnUtils;

@SuppressLint("HandlerLeak")
public class JudgeQActivity extends FinalActivity implements OnClickListener{
	@ViewInject(id=R.id.txtFillNum) TextView txtNum;
	@ViewInject(id=R.id.txtFillContent) TextView txtContent;
	@ViewInject(id=R.id.txtFillAnswer) TextView txtAnswer;
	@ViewInject(id=R.id.btnA) Button btnRight;
	@ViewInject(id=R.id.btnB) Button btnError;
	@ViewInject(id=R.id.btnFillAdd) Button btnAdd;
	@ViewInject(id=R.id.btnFillLook) Button btnLook;
	@ViewInject(id=R.id.btnLast) Button btnLast;
	@ViewInject(id=R.id.btnNext) Button btnNext;

	//数据库操作
	private ProblemService proService;
	//查找出的所有填空题
	private List<Problems> proList;
	//记录此时的题目顺序号
	private int nowIndex;
	//错题加载等待框
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_judge);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);        
		initTitleBar();

		initData();
		initFontSize();
	}

	private void initData(){
		nowIndex = 0;
		proService = new ProblemService(this);
		proList = proService.getGroupData("2");

		if(null == proList) {
			WarnUtils.toast(this, "没有数据内容！");
			finish();
		} else {
			txtNum.setText("第1题");
			txtContent.setText(proList.get(0).getTitle());
			txtAnswer.setText("答案：" + proList.get(0).getAnswer());
			txtAnswer.setVisibility(View.GONE);
		}		

		btnRight.setOnClickListener(this);
		btnError.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnLook.setOnClickListener(this);
		btnLast.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}
	
	private void initFontSize(){
		int fontSize = ExamApplication.fontSize;
		txtContent.setTextSize(fontSize);
		txtAnswer.setTextSize(fontSize);
		btnRight.setTextSize(fontSize);
		btnError.setTextSize(fontSize);
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
		titleText.setText(R.string.exam_title);        
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}

	private void answerProblem(String choice){
		String answer = proList.get(nowIndex).getAnswer();
		if(choice.equals(answer)){//答对
			proList.get(nowIndex).setFalse(true);
		}else{//答错
			proList.get(nowIndex).setFalse(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnA:
			btnRight.setTextColor(this.getResources().getColor(R.color.red));
			btnError.setTextColor(this.getResources().getColor(R.color.black));
			answerProblem(v.getTag().toString());
			break;
		case R.id.btnB:
			btnRight.setTextColor(this.getResources().getColor(R.color.black));
			btnError.setTextColor(this.getResources().getColor(R.color.red));
			answerProblem(v.getTag().toString());
			break;
		case R.id.btnFillAdd:
			boolean flag = proService.AddFavourite(proList.get(nowIndex).getId(), "2");
			if(flag){
				WarnUtils.toast(this, "收藏成功！");
			} else {
				WarnUtils.toast(this, "您已经收藏过了！");
			}
			break;
		case R.id.btnFillLook:
			txtAnswer.setVisibility(View.VISIBLE);
			break;
		case R.id.btnLast:
			if(nowIndex <= 0) {
				WarnUtils.toast(this, "已经是第一题了！");
			}else{
				nowIndex --;
				if(nowIndex == 0){
					String num = "第1题";	
					showText(num, proList.get(0));
					btnNext.setText("下一题");
				}else{
					String num = "第" + (nowIndex + 1) + "题";	
					showText(num, proList.get(nowIndex));
					btnNext.setText("下一题");
				}				
			}
			break;
		case R.id.btnNext:
			if(nowIndex >= (proList.size() - 1)) {
				btnNext.setEnabled(false);
				showErrorDialog();
			}else{
				//btnNext.setEnabled(true);
				nowIndex++;
				int size = proList.size() - 1;
				if(nowIndex == size) {
					String num = "第" + (nowIndex + 1) + "题";				
					showText(num, proList.get(size));
					btnNext.setText("完成答题");
				}else{
					String num = "第" + (nowIndex + 1) + "题";				
					showText(num, proList.get(nowIndex));
					btnNext.setText("下一题");
				}				
			}
			break;
		default:
			break;
		}
	}

	private void showText(String num, Problems pro){
		txtNum.setText(num);
		txtContent.setText(pro.getTitle());
		txtAnswer.setText("答案：" + pro.getAnswer());
		txtAnswer.setVisibility(View.GONE);
		btnRight.setTextColor(this.getResources().getColor(R.color.black));
		btnError.setTextColor(this.getResources().getColor(R.color.black));
	}

	/**
	 * 显示加载题框
	 */
	private void showErrorDialog(){
		proDialog = ProgressDialog.show(JudgeQActivity.this, "错题加载", "加载中，请稍后……"); 
		/* 开启一个新线程，在新线程里执行耗时的方法 */  
		new Thread(new Runnable() {  
			@Override  
			public void run() {  
				addErrors();// 耗时的方法  
				handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler  
			}  

		}).start(); 
	}

	Handler handler = new Handler() {  
		@Override  
		public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法  
			if(errResult == true){
        		WarnUtils.toast(JudgeQActivity.this, "添加错题集成功！");
        	}else{
        		WarnUtils.toast(JudgeQActivity.this, "添加错题集成功！");
        	}
        	proDialog.dismiss();// 关闭ProgressDialog  
        	showResult(); //显示结果框
		}  
	};  

	boolean errResult = true;
	//总题数
	int totalNum = 0;
	//错题数
	int errorNum = 0;
	//正确数
	int rightNum = 0;
	//错题集
	List<Problems> errorList = new ArrayList<Problems>();
	/**
	 * 添加错集
	 */
	private void addErrors(){
		errorList.clear();
		totalNum = proList.size();
		errorNum = 0;
		rightNum = 0;
		for (Problems pro : proList) {
			if(pro.isFalse()){
				rightNum ++;
			}else{
				errorNum ++;
				errorList.add(pro);
			}
		}	
		errResult = proService.AddError("2", errorList);
	}

	/**
	 * 显示答题结果
	 */
	private void showResult(){
		String message = null;
		if(rightNum == totalNum){
			message = "您太棒了，全答对了！";
		}else if(errorNum == totalNum){
			message = "很遗憾，您一题都没有答对！";
		}else{
			message = "总共" + totalNum + "题，您答对" + rightNum + "题，答错" + errorNum + "题，再接再厉哦";
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(JudgeQActivity.this);
		builder.setTitle("答题结果");
		builder.setMessage(message);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		//		builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {			
		//			@Override
		//			public void onClick(DialogInterface dialog, int which) {
		//				dialog.dismiss();
		//			}
		//		});
		builder.create().show();
	}

}

