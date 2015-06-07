package cn.monica.exam.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


public class HttpUtil {

    public static interface NetWorkCallBack{
        void onSuccess(String jsonDate);
        void onError(String msg);
    }

    public static String doGet(String path,Map<String,String> param)
    {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,3000);//连接时间
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,3000);//数据传输时间
        StringBuilder builder = new StringBuilder();
        JSONObject realData;
        StringBuffer sb = new StringBuffer();
        sb.append(path);
        if(param.size() > 0){
            sb.append("?");
            Set<Map.Entry<String, String>> entries = param.entrySet();
            for (Map.Entry<String, String> temp : entries){

            }

            Set<Map.Entry<String, String>> entry = param.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entry.iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key=(String)next.getKey();
                String value=(String)next.getValue();
                sb.append(key + "=" + value + "&");
            }
        }
        if(param.size() > 0) {
            sb.setLength(sb.length() - 1);
        }
        String url = sb.toString();
        Log.e("path",url);
        HttpGet doget = new HttpGet(url);
        try {
            HttpResponse response = client.execute(doget);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            String data = builder.toString();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return "{'success':false}";
        }
    }

    public static String doPost(String path,Map<String,String> param)
    {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,3000);//连接时间
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,3000);//数据传输时间
        JSONObject jsonParam = new JSONObject();
        StringBuilder builder = new StringBuilder();
        if(param.size() > 0){
            Set<Map.Entry<String, String>> entries = param.entrySet();
            for (Map.Entry<String, String> temp : entries){
                try {
                    jsonParam.put(temp.getKey(), temp.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("path", path);
        HttpPost dopost = new HttpPost(path);
        try {
            Log.e("pathjson", jsonParam.toString());
            StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            dopost.setEntity(entity);
            HttpResponse response = client.execute(dopost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            String data = builder.toString();
            return data;
        } catch (Exception e) {
            return "登录失败！";
        }
    }

    public static class NetWorkDoGetAsyncTask extends AsyncTask<Void,Void,String>{
        private String path;
        private Map<String,String> param;
        private NetWorkCallBack callBack;

        public NetWorkDoGetAsyncTask(String path, Map<String, String> param, NetWorkCallBack callBack) {
            this.path = path;
            this.param = param;
            this.callBack = callBack;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = HttpUtil.doGet(path, param);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.e("json",result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("success")){
                    callBack.onSuccess(result);
                }else{
                    callBack.onError(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class NetWorkDoPostAsyncTask extends AsyncTask<Void,Void,String>{
        private String path;
        private Map<String,String> param;
        private NetWorkCallBack callBack;

        public NetWorkDoPostAsyncTask(String path, Map<String, String> param, NetWorkCallBack callBack) {
            this.path = path;
            this.param = param;
            this.callBack = callBack;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = HttpUtil.doPost(path, param);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("json",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("success")){
                    callBack.onSuccess(result);
                }else{
                    callBack.onError(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
