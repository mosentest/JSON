package cn.monica.exam.activity;

import java.util.List;

import cn.monica.exam.ExamApplication;
import com.luojunrong.R;
import cn.monica.exam.entity.Problems;
import cn.monica.exam.service.ProblemService;
import cn.monica.exam.utils.WarnUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class FillQActivity extends FinalActivity implements OnClickListener{
	@ViewInject(id=R.id.txtFillNum) TextView txtNum;
	@ViewInject(id=R.id.txtFillContent) TextView txtContent;
	@ViewInject(id=R.id.txtFillAnswer) TextView txtAnswer;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置标题
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_fill);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);        
		initTitleBar();

		initData();
		initFontSize();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initData(){
		nowIndex = 0;
		proService = new ProblemService(this);
		proList = proService.getGroupData("1");

		if(null == proList) {
			WarnUtils.toast(this, "没有数据内容！");
			finish();
		} else {
			txtNum.setText("第1题");
			txtContent.setText(proList.get(0).getTitle());
			txtAnswer.setText("答案：" + proList.get(0).getAnswer());
			txtAnswer.setVisibility(View.GONE);
		}		

		btnAdd.setOnClickListener(this);
		btnLook.setOnClickListener(this);
		btnLast.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}
	
	private void initFontSize(){
		int fontSize = ExamApplication.fontSize;
		txtContent.setTextSize(fontSize);
		txtAnswer.setTextSize(fontSize);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFillAdd:
			boolean flag = proService.AddFavourite(proList.get(nowIndex).getId(), "1");
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
					showText(num, proList.get(0).getTitle(), proList.get(0).getAnswer());
				}else{
					String num = "第" + (nowIndex + 1) + "题";	
					showText(num, proList.get(nowIndex).getTitle(), proList.get(nowIndex).getAnswer());
				}				
			}
			break;
		case R.id.btnNext:
			if(nowIndex >= (proList.size() - 1)) {
				WarnUtils.toast(this, "已经是最后一题了！");
			}else{
				nowIndex++;
				int size = proList.size() - 1;
				if(nowIndex == size) {
					String num = "第" + (nowIndex + 1) + "题";				
					showText(num, proList.get(size).getTitle(), proList.get(size).getAnswer());
				}else{
					String num = "第" + (nowIndex + 1) + "题";				
					showText(num, proList.get(nowIndex).getTitle(), proList.get(nowIndex).getAnswer());
				}				
			}
			break;
		default:
			break;
		}
	}

	private void showText(String num, String content, String answer){
		txtNum.setText(num);
		txtContent.setText(content);
		txtAnswer.setText("答案：" + answer);
		txtAnswer.setVisibility(View.GONE);
	}

}
