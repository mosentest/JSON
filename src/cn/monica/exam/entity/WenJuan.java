package cn.monica.exam.entity;

import org.json.JSONException;
import org.json.JSONObject;


public class WenJuan {
   public int id;
    public String name;

    public WenJuan(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
