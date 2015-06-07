package cn.monica.exam.utils;



public class HttpUrlConstacts {
    public static  String HEAD="http://";
    public static  String APP_NAME="LJR_study";
    public static  String IP ="10.20.90.85:8080";
    public static  String URL_USER_LOGIN= "login.html";
    public static  String URL_GET_PROJECT="subjecttype/list.html";
    public static  String URL_GET_WENJUAN="questionnaire/list.html";
    public static  String URL_GET_WORD="article/list.html";
    public static  String URL_GET_QUESTIONS="questionnairediscipline/list.html";
    public static  String URL_GET_QUESTION_ITEM="disciplineoption/list.html";
    public static  String URL_ADD_QUESTION_SCORE="userquestionnaire/add";
    public static String URL_ADD_ERROR_QUESTION="userwrongdiscipline/add";
    public static String URL_DEL_ERROR_QUESTION="userwrongdiscipline/delete";
    public static String URL_SHOW_ERROR_QUESTION="userwrongdiscipline/list.html";
    public static String URL_ADD_FAV_QUESTION="usercollectdiscipline/add";
    public static String URL_DEL_FAV_QUESTION="usercollectdiscipline/delete";
    public static String URL_SHOW_FAV_QUESTION="usercollectdiscipline/list.html";

    public static void setIP(String IP) {
        HttpUrlConstacts.IP = IP;
    }

    public  static String getHost(){
        return HEAD + IP+"/"+APP_NAME+"/";
    }
}
