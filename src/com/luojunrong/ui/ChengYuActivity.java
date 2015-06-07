package com.luojunrong.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.luojunrong.R;

public class ChengYuActivity extends Activity {
	
	private static final int MSG_UPDATE_TV1 = 1;
    private static final int MSG_UPDATE_TV2 = 2;
    private static final int MSG_ERROR = 3;
	private Button button1;
	private EditText ET1;
	private TextView tv1;
	private TextView tv2;
	private final String UrlStr = "http://brisk.eu.org/api/cycd.php";
	
	private Runnable runnable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activitymain);
		this.getElement();
		final Handler myHandler = new Handler() {  
	          public void handleMessage(Message msg) {   
	               switch ((int)msg.what) {   
	                    case MSG_UPDATE_TV1:   
	                         tv1.setText("解释 : "+(String)msg.obj);
	                         break;   
	                    case MSG_UPDATE_TV2:   
	                         tv2.setText("成语 : "+(String)msg.obj+":");
	                         break; 
	                    case MSG_ERROR:   
	                    	 Toast.makeText(getApplicationContext(), "获取失败！",Toast.LENGTH_LONG ).show();
	                         break; 
	               }   
	               super.handleMessage(msg);   
	          }   
	     };
	     runnable = new Runnable() {
				public void run() {
					GEThttpData(UrlStr,ET1.getText().toString().trim(),myHandler);	
				}
			};
	     
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "---->OK<---",Toast.LENGTH_SHORT ).show();;
				new Thread(runnable).start();
					
				
			}
		});
		
		
		
		
		
	}
	
	private void getElement()
	{
		button1 = (Button)findViewById(R.id.button1);
		tv1 = (TextView)findViewById(R.id.TV1);
		tv2 = (TextView)findViewById(R.id.TV2);
		ET1 = (EditText)findViewById(R.id.et1);
		return;
	}
	
	private void GEThttpData(String path,String params,Handler myHandler)
	{
		HttpClient client = new DefaultHttpClient();
	    StringBuilder builder = new StringBuilder();
	    JSONObject realData;
	    HttpGet myget = new HttpGet(path+"?word="+params);
	    Log.v("url",path+"?word="+params);
	    try {
	        HttpResponse response = client.execute(myget);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	        response.getEntity().getContent()));
	        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
	            builder.append(s);
	        }
	        JSONObject jsonObject = new JSONObject(builder.toString());
	        
	        JSONArray res = jsonObject.getJSONArray("res");
	        for (int i = 0; i < res.length(); i++)
	        {
				realData = (JSONObject) res.get(i);
				String text = realData.getString("text");
				Message msg = new Message();
				msg.what = MSG_UPDATE_TV1;
				msg.obj = text;
				myHandler.sendMessage(msg); 
				
				String word = realData.getString("word");
				
				
				Message msg2 = new Message();
				msg2.what = MSG_UPDATE_TV2;
				msg2.obj = word;
				myHandler.sendMessage(msg2); 
				
				
	        }
	        
	    } catch (Exception e) {

	        Message msg = new Message();
			msg.what = MSG_ERROR;
	        myHandler.sendMessage(msg); 
	        e.printStackTrace();
	    }
	}
	
}
