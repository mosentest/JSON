/**
 * 
 */
package com.luojunrong.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.luojunrong.R;
/**
 * @author wzj
 * 
 */
public class DicActivity extends ListActivity {


	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.vlist,
				new String[]{"title","info","img"},
				new int[]{R.id.title,R.id.info,R.id.img});
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		switch (position) {
		
			
		case 1:
			Intent intent1 = new Intent(DicActivity.this, Dictionary.class);
			startActivity(intent1);
			
			
			break;
		case 0:
			Intent intent = new Intent(DicActivity.this, ChengYuActivity.class);
			startActivity(intent);
			
			
			break;
		default:
			break;
		}
		
		
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "成语字典");
		map.put("info", "");
		map.put("img", R.drawable.zhenzhi);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "单词查询");
		map.put("info", "");
		map.put("img", R.drawable.history);
		list.add(map);
		
		
		return list;
	}
}
