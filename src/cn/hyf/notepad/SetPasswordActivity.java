package cn.hyf.notepad;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.luojunrong.R;

public class SetPasswordActivity extends Activity {
	String password;
	FileService fservice;
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
		setContentView(R.layout.setpassword);
		fservice=new FileService(this);
		initPassword();
	}
	public void setPass(View v){
		switch(v.getId()){
		case R.id.setPassOk:
			//Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
			EditText et=(EditText)this.findViewById(R.id.oldPassEdit);
			String input=(et).getText().toString();
			if(input.equals(password)) {
				EditText et2=(EditText)this.findViewById(R.id.newPassEdit);
				String input2=(et2).getText().toString();
				if(input2.equals("")){
					Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
				}else{
					try {
						fservice.saveToAttr(FileName.PASSWORD, input2 + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Toast.makeText(this, "修改完成", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
			else Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
			break;
		case R.id.setPassCancle:
			finish();
			break;
		}
	}
}
