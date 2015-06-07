package cn.hyf.notepad;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.luojunrong.R;


public class EditActivity extends Activity {
	private String filename;
	private String filecontent;
	private FileService fservice;
	DBService dbservice;
	String hidden="false";
	private final int NAME_LENGTH = 17;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		fservice = new FileService(this);
		dbservice=new DBService(this);
		Intent intent = getIntent();
		filename = intent.getStringExtra("name");
		if (filename != null)
			show();

	}

	public void saveNotepad(View v) {
		if (filename != null)
			{deleteNotepad(v);
			}	
		filecontent = ((EditText) this.findViewById(R.id.editText)).getText()
				.toString();
		if(filecontent.equals("")){
			Toast.makeText(getApplicationContext(),"内容不能为空", Toast.LENGTH_SHORT)
			.show();
		}else{
		if (filecontent.length() < NAME_LENGTH)
			{filename = filecontent;}
		else
			{filename = filecontent.substring(0, NAME_LENGTH);}
		
		if (filename.contains("\n"))
			filename = filename.substring(0, filename.indexOf("\n"));	
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd号 EEE HH:mm:ss");
		filecontent+="\r\n[保存于]"+sdf.format(date);
		// 保存文件到sd卡
		boolean b=true;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				b=fservice.saveToSdCard(filename, filecontent);
			} else {
				Toast.makeText(getApplicationContext(), "sd卡不存在或写保护",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
		if(b){
			//添加到数据库
			dbservice.save(new Notepad(filename,hidden));
			this.finish();
		}else {
			Toast.makeText(this, "文件已经存在,请重命名", Toast.LENGTH_SHORT).show();
			filename=null;
		}
		}
	}

	public void deleteNotepad(View v) {
		if (filename != null) {
			try {
				fservice.delete(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
			hidden=dbservice.delete(filename).getHidden();
			this.finish();
		}
	}
	//打开记事，显示内容
	public void show() {
		// 显示文件
		String filecontent = "";
		EditText fcontentText = (EditText) findViewById(R.id.editText);
		try {
			if(filename.startsWith("【隐】")){
				filename=filename.substring(3);
			}
			filecontent = fservice.readSdCard(filename);
			fcontentText.setText(filecontent);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "读取失败", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		if(filename!=null){
		String hidden=dbservice.find(filename).getHidden();
		if(hidden.equals("true"))
			menu.add(0,0,0,"取消隐藏");
		else menu.add(0,0,0,"隐藏");
		menu.add(0,1,1,"返回");
		}
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case 0:
			if(item.getTitle().equals("隐藏")){
			dbservice.update(new Notepad(filename,"true"));
			Toast.makeText(getApplicationContext(), "隐藏成功", Toast.LENGTH_SHORT)
			.show();
			item.setTitle("取消隐藏");
			}else {
				dbservice.update(new Notepad(filename,"false"));
				Toast.makeText(getApplicationContext(), "已取消隐藏", Toast.LENGTH_SHORT)
				.show();
				item.setTitle("隐藏");
			}
			break;
		case 1:
			finish();
			break;
		}
		return true;
	}
}
