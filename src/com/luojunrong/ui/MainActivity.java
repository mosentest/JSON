package com.luojunrong.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.monica.exam.activity.LoginAcitiviy;
import cn.monica.exam.utils.HttpUrlConstacts;
import com.luojunrong.NewsApplication;
import com.luojunrong.R;

public class MainActivity extends Activity{

	
	private ImageButton button1;
	private ImageButton button2;
	private ImageButton button3;
	private ImageButton button4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mainwin);
		
		button1 = (ImageButton)findViewById(R.id.button1);
		button2 = (ImageButton)findViewById(R.id.button2);
		button3 = (ImageButton)findViewById(R.id.button3);
		button4 = (ImageButton)findViewById(R.id.button4);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {//每天学一点
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, OptionActivity.class);
				startActivity(intent);
			}
		});
		
		
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {//成语
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(MainActivity.this, DicActivity.class);
				startActivity(intent1);
			}
		});
			
		button3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {//课程表
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(MainActivity.this, cn.monica.exam.activity.MainActivity.class);
				startActivity(intent2);
			}
		});
		button4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {//备忘录
				// TODO Auto-generated method stub
				
				Intent intent3 = new Intent(MainActivity.this,MyToolActivity.class);
				startActivity(intent3);
				
				
			}
		});
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()){
			case R.id.set_ip:
				MyDialog dialog = new MyDialog(this);
				dialog.show();
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.main_menu,menu);
		return true;
	}

	private class MyDialog extends Dialog{

		EditText ip;
		Button ok;
		Button cancel;

		public MyDialog(final Context context) {
			super(context);
			setTitle("设置IP");
			setContentView(R.layout.dialog_mydialog);
			ip = (EditText) findViewById(R.id.edit_ip);
			ok = (Button) findViewById(R.id.ok);
			cancel = (Button) findViewById(R.id.cancel);
			ip.setText(HttpUrlConstacts.IP);
			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String iptext = ip.getText().toString();
//					SharedPreferences userPreferences = NewsApplication.getInstance().getSharedPreferences("USER_INFO", MODE_PRIVATE);
//					SharedPreferences.Editor edit = userPreferences.edit();
//					edit.putString("IP", iptext);
//					edit.apply();
//					edit.commit();
					HttpUrlConstacts.setIP(iptext);
					MyDialog.this.dismiss();
				}
			});
			cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MyDialog.this.dismiss();
				}
			});

		}

	}
}
