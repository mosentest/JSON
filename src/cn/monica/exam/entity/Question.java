package cn.monica.exam.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public class Question implements Serializable {
    public int id;
    public String question;
    public String answers;
    public int score;
    public List<QuestionItem> questionItems;

    public Question(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
           id =  jsonObject.getInt("id");
           question =  jsonObject.getString("question");
           answers =  jsonObject.getString("answers");
            score =  jsonObject.getInt("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
