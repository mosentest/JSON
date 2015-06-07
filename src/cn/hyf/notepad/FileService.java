package cn.hyf.notepad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class FileService {
	private Context context;
	private static final String path=Environment.getExternalStorageDirectory()+"/BianQian/";
	private static final String attrpath=path+"attr/";
	public FileService(Context context) {
		this.context=context;
		new File(path).mkdirs();
		new File(attrpath).mkdirs();
	}
	//删除属性文件
	public void deleteAttr(String filename) throws IOException {
		File file = new File(attrpath,
				filename);
		file.delete();
	}
	//软件属性文件的存放
	public void saveToAttr(String filename,String filecontent)
	throws IOException{
		File file=new File(attrpath,filename);
		FileOutputStream fos = null;
		if (filename.equals(FileName.BIANQIANRECORDING)) {
			if(file.length()>102400){
				deleteAttr(filename);
				file=new File(attrpath,filename);
			}
			fos = new FileOutputStream(file, true);
		} else {
			fos = new FileOutputStream(file);
		}
		byte[] buffer = filecontent.getBytes();
		fos.write(buffer);
		fos.close();
	}
	//属性文件的读取
	public String readAttr(String filename) throws Exception {
		File file = new File(attrpath,
				filename);
		if(filename.equals(FileName.PASSWORD)&&file.exists()==false){
			saveToAttr(FileName.PASSWORD,"hyf");
		}
		FileInputStream fis = new FileInputStream(file);
		String filecontent = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		if(filename.equals(FileName.PASSWORD)) filecontent=br.readLine();
		else
		while (true) {
			String str = br.readLine();
			if (str == null)
				break;
			filecontent += str + "\n";
		}
		fis.close();
		return filecontent;
	}
	//删除便签
	public void delete(String filename) throws IOException {
		File file = new File(path,
				filename);
		file.delete();
	}
	//便签的存储
	public boolean saveToSdCard(String filename, String filecontent)
			throws IOException {
		File[] files=new File(path).listFiles(); 
		for(File f:files){
			if(f.isFile()&&f.getName().equals(filename)){
				return false;
			}
		}
		File file = new File(path,filename);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buffer = filecontent.getBytes();
		fos.write(buffer);
		fos.close();
		return true;
	}
	//便签的读取
	public String readSdCard(String filename) throws Exception {
		File file = new File(path,
				filename);
		FileInputStream fis = new FileInputStream(file);
		String filecontent = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		while (true) {
			String str = br.readLine();
			if (str == null)
				break;
			filecontent += str + "\n";
		}
		fis.close();
		return filecontent;
	}
	//目录文件下便签列表自动导入到数据库
	public void autoImport(){
		DBService dbservice=new DBService(context);
		List<Notepad> list=new ArrayList<Notepad>();
		File[] files=new File(path).listFiles();
		for(File f:files){
			String filename=f.getName();
			if(f.isFile()&&dbservice.find(filename)==null){
				String hidden="false";
				Notepad notepad=new Notepad(filename,hidden);
				dbservice.save(new Notepad(filename,hidden));
			}
		}
	}
}
