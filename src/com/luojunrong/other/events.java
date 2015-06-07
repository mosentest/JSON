package com.luojunrong.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.luojunrong.R;

public class events extends Activity {

	private SQLiteDatabase db = null;
	private Button pri;
	private Button next;
	private TextView TV1;
	private TextView TV2;
	private Cursor cursor;
	private static int index = 1;
	String tablename = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.eventlayout);
		
		tablename = this.getIntent().getStringExtra("String");
		Log.v("ljr", tablename);
		DatabaseHelper database = new DatabaseHelper(this); 

		db = database.getReadableDatabase();
		
//		this.initDase();
		this.readmsgText();
		pri=(Button)findViewById(R.id.button1);
		next=(Button)findViewById(R.id.button2);
		TV1 = (TextView)findViewById(R.id.textview1);
		TV2 = (TextView)findViewById(R.id.textview2);
		
		pri.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), ""+index, Toast.LENGTH_LONG).show();
				if (index <=1) {
					Toast.makeText(getApplicationContext(), "这里是首页", Toast.LENGTH_LONG).show();
					return;
				}
				GetDatabase(--index);
				Log.v("ljr", "index ="+String.valueOf(index));
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), ""+index, Toast.LENGTH_LONG).show();
				if (index >=cursor.getCount()) {
					Toast.makeText(getApplicationContext(), "这里是尾页", Toast.LENGTH_LONG).show();
					return;
				}
				GetDatabase(++index);
				Log.v("ljr", "index ="+String.valueOf(index));
			}
		});
		
		
		GetDatabase(index);
	}
	
	
	
	private boolean GetDatabase(int count)
	{
		cursor = db.query(tablename,null,null,null,null,null,null);
	
		if(count > cursor.getCount())
			return false;
		
		cursor.move(count);
		
		 String context = cursor.getString(cursor.getColumnIndex("context"));
	     String title = cursor.getString(cursor.getColumnIndex("title"));
		
		Log.v("ljr",context);
		TV2.setText(title);
		TV1.setText(context);

		return true;
		
		
	}

	
	private void readmsgText()
	{
		InputStream inputStream = getResources().openRawResource(R.raw.msg); 
		getString(inputStream,tablename);
	}
	
	
	
	
	public void getString(InputStream inputStream,String tablename) 
	{
		int i = 0;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		//StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) 
			{
				//sb.append(line);
				
				if(line.startsWith(tablename))
				{
					Log.v("ljr", line);
					
					String str[] = line.split("@");
					ContentValues cv = new ContentValues();
					
					
					
					cv.put("title", str[1]);
					cv.put("context",str[2]); 
					db.insert(tablename,null,cv);
					Log.v("ljr", "index  ="+String.valueOf(++i));
					
				}
				
				//sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

}
