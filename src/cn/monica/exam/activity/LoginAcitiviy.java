package cn.monica.exam.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.monica.exam.entity.UserInfo;
import cn.monica.exam.utils.HttpUrlConstacts;
import cn.monica.exam.utils.HttpUtil;
import com.luojunrong.R;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.HashMap;
import java.util.Map;


public class LoginAcitiviy extends Activity {

    Button mLogin;

    EditText mName;

    EditText mPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "您尚未登录，请登录！", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_login);
        mLogin = (Button) this.findViewById(R.id.btn_login);
        mName = (EditText) this.findViewById(R.id.et_name);
        mPassword = (EditText) this.findViewById(R.id.et_password);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String password = mPassword.getText().toString();
                //TODO:登录成功或者失败加入一个标记
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("username",name);
                param.put("password",password);
                HttpUtil.NetWorkDoPostAsyncTask doPostTask = new HttpUtil.NetWorkDoPostAsyncTask(HttpUrlConstacts.getHost()+HttpUrlConstacts.URL_USER_LOGIN, param, new HttpUtil.NetWorkCallBack() {
                    @Override
                    public void onSuccess(String jsonDate) {
                        Toast.makeText(LoginAcitiviy.this,"登录成功！",Toast.LENGTH_SHORT).show();
                        UserInfo userInfo = new UserInfo(jsonDate);
                        SharedPreferences userPreferences = LoginAcitiviy.this.getSharedPreferences("USER_INFO", MODE_PRIVATE);
                        SharedPreferences.Editor edit = userPreferences.edit();
                        edit.putBoolean("USER_LOGIN_FLAG", true);
                        edit.putInt("USER_LOGIN_ID", userInfo.id);
                        edit.putString("USER_LOGIN_NAME", userInfo.username);
                        edit.apply();
                        edit.commit();
                        LoginAcitiviy.this.finish();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(LoginAcitiviy.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
                doPostTask.execute();


            }
        });
    }
}