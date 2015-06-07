package cn.monica.exam.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class Word implements Serializable {
    public int id;
    public String title;
    public String content;

    public Word(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            id = jsonObject.getInt("id");
            title = jsonObject.getString("title");
            content = jsonObject.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
