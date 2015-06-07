package cn.monica.exam.entity;

import org.json.JSONException;
import org.json.JSONObject;


public class UserInfo {
    public int id;
    public String username;
    public String name;
    public  String password;
    public long loginTime;

    public UserInfo(String jsonData) {
        try {
            JSONObject data = new JSONObject(jsonData);
            String entity = data.getString("entity");
            JSONObject entityObj = new JSONObject(entity);
            id = entityObj.getInt("id");
           username = entityObj.getString("username");
           name = entityObj.getString("name");
            password = entityObj.getString("password");
           loginTime = entityObj.getLong("loginTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
