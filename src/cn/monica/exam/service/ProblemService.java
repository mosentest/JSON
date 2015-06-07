package cn.monica.exam.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.monica.exam.db.ExamDBHelper;
import cn.monica.exam.entity.ErrorItem;
import cn.monica.exam.entity.Favourite;
import cn.monica.exam.entity.Problems;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProblemService {
	private ExamDBHelper dbHelper = null;
	private Context mContext;

	public ProblemService(Context context) {
		this.mContext = context;

		dbHelper = new ExamDBHelper(mContext);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到问题数据
	 * @return
	 */
	public List<Problems> getGroupData(String type){		
		List<Problems> groupData = new ArrayList<Problems>();
		//File file = new File(context.getFilesDir(), "commonnum.db");
		//SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("problems", 
					new String[]{"id", "title", "answer", "a", "b", "c", "d", "type"}, 
					"type=?", new String[]{type}, null, null, null);
			while(c.moveToNext()){
				Problems problem = new Problems();
				problem.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
				problem.setTitle(c.getString(c.getColumnIndex("title")));
				problem.setAnswer(c.getString(c.getColumnIndex("answer")));
				problem.setA(c.getString(c.getColumnIndex("a")));
				problem.setB(c.getString(c.getColumnIndex("b")));
				problem.setC(c.getString(c.getColumnIndex("c")));
				problem.setD(c.getString(c.getColumnIndex("d")));
				problem.setType(Integer.parseInt(c.getString(c.getColumnIndex("type"))));
				problem.setFalse(false);								
				groupData.add(problem);
			}
			c.close();
			db.close();
		}
		return groupData;
	}

	/**
	 * 得到错题数据
	 * @return
	 */
	public List<Problems> getErrorData(String type){
		Cursor c = null;
		List<Problems> groupData = new ArrayList<Problems>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			//获取错题集
			List<ErrorItem> errList = new ArrayList<ErrorItem>();
			c = db.query("error", 
					new String[]{"id", "tid", "type"}, 
					"type=?", new String[]{type}, null, null, null);
			while(c.moveToNext()){
				ErrorItem err = new ErrorItem();
				err.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
				err.setTid(Integer.parseInt(c.getString(c.getColumnIndex("tid"))));
				err.setType(Integer.parseInt(c.getString(c.getColumnIndex("type"))));
				errList.add(err);
			}
			if(errList.size() > 0){
				//循环错题集，取里里面的ID
				for (ErrorItem errorItem : errList) {
					//获取题目集
					c = db.query("problems", 
							new String[]{"id", "title", "answer", "a", "b", "c", "d", "type"}, 
							"id=?", new String[]{errorItem.getTid()+""}, null, null, null);
					while(c.moveToNext()){
						Problems problem = new Problems();
						problem.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
						problem.setTitle(c.getString(c.getColumnIndex("title")));
						problem.setAnswer(c.getString(c.getColumnIndex("answer")));
						problem.setA(c.getString(c.getColumnIndex("a")));
						problem.setB(c.getString(c.getColumnIndex("b")));
						problem.setC(c.getString(c.getColumnIndex("c")));
						problem.setD(c.getString(c.getColumnIndex("d")));
						problem.setType(Integer.parseInt(c.getString(c.getColumnIndex("type"))));
						problem.setFalse(false);								
						groupData.add(problem);
					}
				}
			}
			c.close();
			db.close();
			if(errList.size() <= 0){
				return null;
			}
		}
		return groupData;
	}

	/**
	 * 得到收藏数据
	 * @return
	 */
	public List<Problems> getFavouriteData(String type){
		Cursor c = null;
		List<Problems> groupData = new ArrayList<Problems>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			//获取错题集
			List<Favourite> favList = new ArrayList<Favourite>();
			c = db.query("favourite", 
					new String[]{"id", "tid", "type"}, 
					"type=?", new String[]{type}, null, null, null);
			while(c.moveToNext()){
				Favourite fav = new Favourite();
				fav.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
				fav.setTid(Integer.parseInt(c.getString(c.getColumnIndex("tid"))));
				fav.setType(Integer.parseInt(c.getString(c.getColumnIndex("type"))));
				favList.add(fav);
			}
			if(favList.size() > 0){
				//循环错题集，取里里面的ID
				for (Favourite favItem : favList) {
					//获取题目集
					c = db.query("problems", 
							new String[]{"id", "title", "answer", "a", "b", "c", "d", "type"}, 
							"id=?", new String[]{favItem.getTid()+""}, null, null, null);
					while(c.moveToNext()){
						Problems problem = new Problems();
						problem.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
						problem.setTitle(c.getString(c.getColumnIndex("title")));
						problem.setAnswer(c.getString(c.getColumnIndex("answer")));
						problem.setA(c.getString(c.getColumnIndex("a")));
						problem.setB(c.getString(c.getColumnIndex("b")));
						problem.setC(c.getString(c.getColumnIndex("c")));
						problem.setD(c.getString(c.getColumnIndex("d")));
						problem.setType(Integer.parseInt(c.getString(c.getColumnIndex("type"))));
						problem.setFalse(false);								
						groupData.add(problem);
					}
				}	
			}
			c.close();
			db.close();
			if(favList.size() <= 0){
				return null;
			}
		}
		return groupData;
	}

	/**
	 * 添加错题
	 * @param id
	 * @return
	 */
	public boolean AddError(String type, List<Problems> errorList){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			int result = db.delete("error", "type=?", new String[]{type});
			if(result >= 0) {
				for (Problems item : errorList) {
					ContentValues values = new ContentValues();
					values.put("tid", item.getId());
					values.put("type", item.getType());
					db.insert("error", "_id", values);
				}
			}
			db.close();
			return true;
		}
		return false;
	}

	/**
	 * 添加收藏
	 * @param id
	 * @return
	 */
	public boolean AddFavourite(int id, String type){
		int result = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Favourite errItem = null;
		if(db.isOpen()){
			Cursor c = db.query("favourite", 
					new String[]{"id", "tid"}, 
					"tid=?", new String[]{id+""}, null, null, null);
			while(c.moveToNext()){
				errItem = new Favourite();
				errItem.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
				errItem.setTid(Integer.parseInt(c.getString(c.getColumnIndex("tid"))));
			}
			if(errItem == null) {
				ContentValues values = new ContentValues();
				values.put("tid", id);
				values.put("type", type);
				result = (int) db.insert("favourite", "_id", values);
				if(result > 0){
					return true;
				}
			}
			c.close();
			db.close();
		}
		return false;
	}

	/**
	 * 删除错题
	 * @param id
	 * @return
	 */
	public boolean DeleteError(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			int result = db.delete("error", "tid=?", new String[]{id+""});
			if(result > 0) {
				return true;
			}
			db.close();
		}
		return false;
	}

	/**
	 * 删除收藏
	 * @param id
	 * @return
	 */
	public boolean DeleteFavourite(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			int result = db.delete("favourite", "tid=?", new String[]{id+""});
			db.close();
			if(result > 0) {
				return true;
			}
		}
		return false;
	}
}
