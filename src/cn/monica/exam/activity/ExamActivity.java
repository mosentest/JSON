package cn.monica.exam.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.*;
import cn.monica.exam.adapter.IListViewCallBack;
import cn.monica.exam.adapter.ListViewAdapter;
import cn.monica.exam.entity.ProjectType;
import cn.monica.exam.utils.HttpUrlConstacts;
import cn.monica.exam.utils.HttpUtil;
import com.luojunrong.R;
import cn.monica.exam.utils.ActivityUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class ExamActivity extends FinalActivity{

	@ViewInject(id=R.id.exam_list)
	ListView listView;
	List<ProjectType> list;

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
							list.add(projectType); //知识手册如果是语数英就加入到list去
						}else {
							continue;
						}
//						list.add(projectType);
					}

					listView.setAdapter(new ListViewAdapter<ProjectType>(ExamActivity.this, list, new IListViewCallBack<ProjectType>() {
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
							ActivityUtils.to(ExamActivity.this, WenJuanActivity.class,data);
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
		titleText.setText(R.string.exam_title);        
		ImageView rightButton = (ImageView)findViewById(R.id.right_btn);
		rightButton.setVisibility(View.GONE);
	}

}
