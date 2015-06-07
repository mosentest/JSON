package cn.monica.exam.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.*;
import cn.monica.exam.adapter.IListViewCallBack;
import cn.monica.exam.adapter.ListViewAdapter;
import cn.monica.exam.entity.Favourite;
import cn.monica.exam.entity.ProjectType;
import cn.monica.exam.entity.Question;
import cn.monica.exam.utils.HttpUrlConstacts;
import cn.monica.exam.utils.HttpUtil;
import cn.monica.exam.utils.WarnUtils;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import com.luojunrong.R;
import cn.monica.exam.utils.ActivityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteActivity extends FinalActivity{


	@ViewInject(id=R.id.exam_list)
	ListView listView;

	List<Question> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_exam);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_title_common);
		initTitleBar();

		initViews();
	}


	private void initViews(){
		Map<String, String> param = new HashMap<String, String>();
		SharedPreferences userPreferences = FavouriteActivity.this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
		param.put("id","");
		param.put("userId",userPreferences.getInt("USER_LOGIN_ID",-1)+"");
		param.put("username",userPreferences.getString("USER_LOGIN_NAME",""));
		HttpUtil.NetWorkDoGetAsyncTask doget = new HttpUtil.NetWorkDoGetAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_SHOW_FAV_QUESTION, param , new HttpUtil.NetWorkCallBack() {
			@Override
			public void onSuccess(String jsonDate) {
				try {
					JSONObject data = new JSONObject(jsonDate);
					JSONObject page = data.getJSONObject("page");
					JSONArray content = page.getJSONArray("content");
					list = new ArrayList<Question>();
					for(int i = 0 ; i < content.length();i++){
						JSONObject temp = content.getJSONObject(i);
						JSONObject tbDiscipline = temp.getJSONObject("tbDiscipline");
						Question question = new Question(tbDiscipline.toString());
						list.add(question);
					}

					listView.setAdapter(new ListViewAdapter<Question>(FavouriteActivity.this, list, new IListViewCallBack<Question>() {
						@Override
						public View getView(Context context, int position, View convertView, List<Question> list) {
							convertView = LayoutInflater.from(context).inflate(R.layout.project_listview_item, null);
							TextView tv = (TextView) convertView.findViewById(R.id.project_item_tv);
							Question projectType = list.get(position);
							tv.setText(projectType.question);
							return convertView;
						}
					}));
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Bundle data = new Bundle();
							Question question = list.get(position);
							data.putSerializable("QUESTION", question);
							ActivityUtils.to(FavouriteActivity.this, ShowQuestionActivity.class, data);
						}
					});
					listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
							AlertDialog dialog = new AlertDialog.Builder(FavouriteActivity.this).setTitle("取消收藏").setMessage("你确定要取消该问题吗？").setNegativeButton("确定", new WarnUtils.OnClickListener() {
								@Override
								public void onClick(final DialogInterface dialog, int which) {
									Map<String,String> param = new HashMap<String, String>();
									final Question question = list.get(position);
									param.put("id",question.id+"");
									HttpUtil.NetWorkDoPostAsyncTask doPost = new HttpUtil.NetWorkDoPostAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_DEL_FAV_QUESTION, param, new HttpUtil.NetWorkCallBack() {
										@Override
										public void onSuccess(String jsonDate) {
											list.remove(question);
											ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
											adapter.notifyDataSetChanged();
											dialog.dismiss();
											Toast.makeText(FavouriteActivity.this,"取消收藏成功!",Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onError(String msg) {
											Toast.makeText(FavouriteActivity.this,"取消收藏失败!",Toast.LENGTH_SHORT).show();
										}
									});
									doPost.execute();
								}
							}).setPositiveButton("取消", new WarnUtils.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create();
							dialog.show();
							return false;
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String msg) {

			}
		});
		doget.execute();
	}


	private void initTitleBar() {
		ImageView leftButton = (ImageView)findViewById(R.id.left_btn);
		leftButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(R.string.fav_title);
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}

}
