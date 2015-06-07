package cn.hyf.notepad;
/**
 * 1.4: 增加暗拍功能
 * 1.3：增加数据库，用以隐藏某一便签，显示便签,并添加密码
 * 1.2：增加将BianQian文件夹下的便签，自动导入列表。（函数为autoImport）
 */
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.luojunrong.R;

public class FileListActivity extends Activity {
	private ListView listView;
	FileService fservice;
	DBService dbservice;
	boolean displayAll=false;
	boolean pass=false;
	int pass_count=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianqian);    
        fservice=new FileService(this);
        dbservice=new DBService(this);
     //   OpenRecording();   
        listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(new ItemClickListener());
        show();
    }
    //listView的 点击事件
	private final class ItemClickListener implements OnItemClickListener{

		//@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView lView=(ListView)parent;
			HashMap<String,Object> notepad=(HashMap<String,Object>)lView.getItemAtPosition(position);
			String filename=notepad.get("name").toString();
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd号 EEE HH:mm:ss");
			String filecontent=sdf.format(date)+"\t[点击了] "+filename+"\r\n";
			try {
				fservice.saveToAttr(FileName.BIANQIANRECORDING, filecontent);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			openNotepad(filename);
		}	
	}
	//从数据库映射出列表内容
	private void show() {
		fservice.autoImport();
		List<Notepad> notepads = dbservice.getScrollDate(0, 20,displayAll);
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for(Notepad notepad:notepads){
			HashMap<String,Object> item=new HashMap<String, Object>();
			item.put("name",notepad.getName());
			data.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
				new String[] { "name" }, new int[] {
						R.id.name});
		listView.setAdapter(adapter);
	}
	//新建记事
    public void newNotepad(View v){
		Intent intent=new Intent(this,EditActivity.class);
		startActivity(intent);
		Log.v("sss","newNotepad");
    }
    //打开记事
    public void openNotepad(String name){
		Intent intent=new Intent(this,EditActivity.class);
		intent.putExtra("name", name);
		startActivityForResult(intent, 100);
    }
    //activity到前台时
	@Override
	protected void onResume() {
		show();
		super.onResume();
	}
	//开启软件记录功能
	private void OpenRecording() {
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd号 EEE HH:mm:ss");
		String filecontent="=================================================================\r\n"
			+sdf.format(date)+"\t[运行了软件]\r\n";
		try {
			String path=Environment.getExternalStorageDirectory()+"/BianQian/";
			File recordFile=new File(path+"attr/"+FileName.BIANQIANRECORDING);
			if(!recordFile.exists()){
				File helpFile=new File(path+"帮助文件");
				if(helpFile.exists()) helpFile.delete();
				fservice.saveToSdCard("帮助文件", getString(R.string.help));
			}
			fservice.saveToAttr(FileName.BIANQIANRECORDING, filecontent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(Camera.getNumberOfCameras()>1)
		{
		Intent intent=new Intent(FileListActivity.this,HiddenCamera.class);
		startActivity(intent);
		}
		
	}
	//显示全部文件，包括隐藏的。
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,0,0,"显示全部");

		menu.add(0,1,1,"设置密码");
		menu.add(0,2,2,"退出");
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case 0:
			if(item.getTitle().equals("显示全部")){
				if(pass_count==0){
				Intent intent=new Intent(FileListActivity.this,PasswordActivity.class);
				startActivityForResult(intent,0);
				}
				if (pass) {
					displayAll = true;
					FileListActivity.this.onResume();
					item.setTitle("取消显示全部");
				}
			}else {
				pass_count=0;
				pass=false;
				displayAll=false;
				FileListActivity.this.onResume();
				item.setTitle("显示全部");
			}
			
			break;
		case 1:
			Intent intent=new Intent(FileListActivity.this,SetPasswordActivity.class);
			startActivity(intent);
			break;
		case 2:

			finish();
			break;
		}
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
		case RESULT_OK:
			Bundle bundle=data.getExtras();
			pass=bundle.getBoolean("pass");
			pass_count++;
			Toast.makeText(this, "密码正确，请再次点击显示全部", Toast.LENGTH_SHORT).show();
		default:
			break;
		}
	}
}