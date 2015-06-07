package cn.hyf.notepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.luojunrong.R;

public class PasswordActivity extends Activity {
	String password;
	boolean pass=false;
	Intent intent;
	FileService fservice;;
	private void initPassword(){
		try {
			password=fservice.readAttr(FileName.PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		fservice=new FileService(this);
		intent=this.getIntent();
		initPassword();
	}
	public void checkPass(View v){
		switch(v.getId()){
		case R.id.checkPassOk:
			//Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
			EditText et=(EditText)this.findViewById(R.id.checkPassEdit);
			String input=(et).getText().toString();
			if(input.equals(password)) {
				pass=true;		
				Bundle bundle=new Bundle();
				bundle.putBoolean("pass", pass);
				intent.putExtras(bundle);
				PasswordActivity.this.setResult(RESULT_OK,intent);
				finish();
			}
			else Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
			break;
		case R.id.checkPassCancle:
			finish();
			break;
		}
	}
}
