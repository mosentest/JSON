package cn.monica.exam.entity;

import org.json.JSONException;
import org.json.JSONObject;


public class ProjectType {
    public int projectTypeId;
    public String projectTypeName;

    public ProjectType(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
           projectTypeId =  jsonObject.getInt("id");
            projectTypeName =  jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
