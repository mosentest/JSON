package cn.hyf.notepad;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "filelist.db", null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table notepad(name varchar(20) primary key,hidden varchar(5))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

public class DBService {
	private DBOpenHelper dbOpenHelper;
	
	public DBService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public void save(Notepad notepad) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into notepad(name,hidden) values(?,?)",
				new Object[] { notepad.getName(),notepad.getHidden()});
	}

	public Notepad delete(String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Notepad notepad=find(name);
		db.execSQL("delete from notepad where name=?", new Object[] {name });
		return notepad;
	}

	public void update(Notepad notepad) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update notepad set hidden=? where name=?",
				new Object[] { notepad.getHidden(),notepad.getName() });
	}

	public Notepad find(String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from notepad where name=?",
				new String[] { name });
		if (cursor.moveToFirst()) {
			String filename = cursor.getString(cursor.getColumnIndex("name"));
			String hidden = cursor.getString(cursor.getColumnIndex("hidden"));
			return new Notepad(filename,hidden);
		}
		return null;
	}

	public List<Notepad> getScrollDate(int offset, int maxResult,boolean displayAll) {
		List<Notepad> notepads = new ArrayList<Notepad>();
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from notepad limit ?,?",
				new String[] { String.valueOf(offset),
						String.valueOf(maxResult) });
		if(displayAll){
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String hidden = cursor.getString(cursor
						.getColumnIndex("hidden"));
				if (hidden.equals("true")) name="【隐】"+name;
				notepads.add(new Notepad(name,hidden));
			}
		} else {
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String hidden = cursor.getString(cursor
						.getColumnIndex("hidden"));
				if (hidden.equals("false"))
					notepads.add(new Notepad(name,hidden));
			}
		}
		cursor.close();
		return notepads;
	}
}
