/**
 * 
 */
package com.luojunrong.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.*;
import cn.monica.exam.activity.WordListActivity;
import cn.monica.exam.adapter.IListViewCallBack;
import cn.monica.exam.adapter.ListViewAdapter;
import cn.monica.exam.entity.ProjectType;
import cn.monica.exam.utils.ActivityUtils;
import cn.monica.exam.utils.HttpUrlConstacts;
import cn.monica.exam.utils.HttpUtil;
import com.luojunrong.other.events;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.luojunrong.R;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wzj
 * 
 */
public class OptionActivity extends Activity {

	ListView listView;

	List<ProjectType> list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyproject);
		listView = (ListView) this.findViewById(R.id.studyproject_list);
		initViews();
	}
	private void initViews(){
		HttpUtil.NetWorkDoGetAsyncTask doget = new HttpUtil.NetWorkDoGetAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_GET_PROJECT, new HashMap<String, String>(), new HttpUtil.NetWorkCallBack() {
			@Override
			public void onSuccess(String jsonDate) {
				try {
					JSONObject data = new JSONObject(jsonDate);
					JSONArray typeList = data.getJSONArray("list");
					list = new ArrayList<ProjectType>();
					for(int i = 0 ; i < typeList.length();i++){
						JSONObject temp = typeList.getJSONObject(i);
						ProjectType projectType = new ProjectType(temp.toString());

						if(projectType.projectTypeName.equals("语文")||projectType.projectTypeName.equals("数学")||projectType.projectTypeName.equals("英语")){
							continue;
						}else {
							list.add(projectType);
						}
					}

					listView.setAdapter(new ListViewAdapter<ProjectType>(OptionActivity.this, list, new IListViewCallBack<ProjectType>() {
						@Override
						public View getView(Context context, int position, View convertView, List<ProjectType> list) {
							convertView = LayoutInflater.from(context).inflate(R.layout.project_listview_item, null);
							TextView tv = (TextView) convertView.findViewById(R.id.project_item_tv);
							ProjectType projectType = list.get(position);
							tv.setText(projectType.projectTypeName);
							return convertView;
						}
					}));
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Bundle data = new Bundle();
							ProjectType projectType = list.get(position);
							data.putInt("PROJECT_ID", projectType.projectTypeId);
							ActivityUtils.to(OptionActivity.this, WordListActivity.class, data);
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
		leftButton.setVisibility(View.GONE);
		TextView titleText = (TextView)findViewById(R.id.title);
		titleText.setText(R.string.study_title);
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}
}
