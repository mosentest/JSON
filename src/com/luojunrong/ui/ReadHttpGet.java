package com.luojunrong.ui;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

class ReadHttpGet extends AsyncTask<Object, Object, Object>

{



  @Override

  protected Object doInBackground(Object... params) {

     // TODO Auto-generated method stub

    

     HttpGet httpRequest = new HttpGet(params[0].toString());

     try

     {

        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse httpResponse = httpClient.execute(httpRequest);

       

        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)

        {

            String strResult = EntityUtils.toString(httpResponse.getEntity());
            System.out.print(strResult);
            return strResult;

        }

        else

        {

            return "请求出错";

        }

     }

     catch(ClientProtocolException e)

     {

       

     }

     catch (IOException e) {

        // TODO Auto-generated catch block

        e.printStackTrace();

     }

    

     return null;

  }



  @Override

  protected void onCancelled(Object result) {

     // TODO Auto-generated method stub

     super.onCancelled(result);

  }



  @Override

  protected void onPostExecute(Object result) {

     // TODO Auto-generated method stub

     super.onPostExecute(result);

    

     try

     {

        //创建一个JSON对象

        JSONObject jsonObject = new JSONObject(result.toString()).getJSONObject("parent");

        //获取某个对象的JSON数组

        JSONArray jsonArray = jsonObject.getJSONArray("children");

       

        StringBuilder builder = new StringBuilder();

       

        for(int i = 0; i<jsonArray.length(); i++)

        {

            //新建一个JSON对象，该对象是某个数组里的其中一个对象

            JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);

           

            builder.append(jsonObject2.getString("id")); //获取数据
            builder.append(jsonObject2.getString("title"));
            builder.append(jsonObject2.getString("name"));

        }

      //  myTextView.setText(builder.toString());

     }

     catch (JSONException e) {

        // TODO Auto-generated catch block

        e.printStackTrace();

     }

    

  }



  @Override

  protected void onPreExecute() {

     // TODO Auto-generated method stub

     //super.onPreExecute();

     //Toast.makeText(getApplicationContext(), "开始HTTP GET请求", Toast.LENGTH_LONG).show();

  }



  @Override

  protected void onProgressUpdate(Object... values) {

     // TODO Auto-generated method stub

     super.onProgressUpdate(values);

  }

  

}
