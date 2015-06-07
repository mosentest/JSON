package cn.hyf.notepad;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

import com.luojunrong.R;

public class HiddenCamera extends Activity implements SurfaceHolder.Callback{
	private Camera mCamera01;
	private AudioManager audioMa;
	private String filepath =Environment.getExternalStorageDirectory()+"/BianQian/Photo/";
    {
        File file = new File(filepath);
        file.mkdirs();
        long size=0;
        File[] files=file.listFiles();
        for(File f:files){
        	if(f.isFile()){
        		size+=f.length();
        	}
        }
        if(size>1024*1024*20){
            for(File f:files){
            	if(f.isFile()){
            		f.delete();
            	}
            }
        }
    } 
    PictureCallback jpeg = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //自定义文件保存路径  以拍摄时间区分命名
                String filename =new SimpleDateFormat("yyyy_MM_dd ~ HH_mm_ss").format(new Date())+".jpg";
                File file = new File(filepath+filename);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
                bos.flush();// 刷新此缓冲区的输出流
                bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
               camera.stopPreview();//关闭预览 处理数据
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    		HiddenCamera.this.finish();
        }
    };

    private void takePicture(){
    	 Parameters params = mCamera01.getParameters();
         params.setPictureFormat(PixelFormat.JPEG);//图片格式
         params.setPreviewSize(320, 240);//图片大小
         mCamera01.setParameters(params);//将参数设置到我的camera
         mCamera01.takePicture(null, null, jpeg);//将拍摄到的照片给自定义的对象
    }
    private SurfaceView mSurfaceView01;
	private SurfaceHolder mSurfaceHolder01;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera);
        mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
        mSurfaceHolder01 = mSurfaceView01.getHolder();
        mSurfaceHolder01.addCallback(HiddenCamera.this);
        mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        audioMa=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera01 = Camera.open(1);
		mCamera01.startPreview();
		takePicture();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera01.stopPreview();
		mCamera01.release();
		mCamera01 = null;
		audioMa.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
}
