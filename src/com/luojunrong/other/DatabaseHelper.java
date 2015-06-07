package com.luojunrong.other;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "event.db"; //数据库名称
    private static final int version = 1; //数据库版本
     
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
    	File file = new File(DB_NAME);
    	if(file.exists())
    	{
    		Log.v("ljr", "exists");
    		file.delete();
    	}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String sql = "create table event(title varchar(200) not null,context varchar(2000) not null);";          
        db.execSQL(sql);
        
        
        sql = "create table history(title varchar(200) not null,context varchar(2000) not null);";          
        db.execSQL(sql);
        
        
        sql = "create table life(title varchar(200) not null,context varchar(2000) not null);";          
        db.execSQL(sql);
		
        sql = "create table shige(title varchar(200) not null,context varchar(2000) not null);";          
        db.execSQL(sql);
        
        sql = "create table heathy(title varchar(200) not null,context varchar(2000) not null);";          
        db.execSQL(sql);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
 
    }
}
