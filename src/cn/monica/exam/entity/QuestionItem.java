package cn.monica.exam.entity;

import org.json.JSONException;
import org.json.JSONObject;


public class QuestionItem {
    public  int id;
    public String content;

    public QuestionItem(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            id = jsonObject.getInt("id");
            content = jsonObject.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}