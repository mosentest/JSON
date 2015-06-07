package hyuuhit.Curriculum;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.luojunrong.R;

public class Curriculum extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kecheng);
        
        TextView tv=(TextView)findViewById(R.id.day);
        Date now=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy年MM月dd日");
        
        tv.setText(f.format(now).toString());
        
        ListView lv = (ListView)findViewById(R.id.ListView01);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[]{getString(R.string.mon),getString(R.string.tue),getString(R.string.wed),getString(R.string.thu),getString(R.string.fri),getString(R.string.sat),getString(R.string.sun)});
        lv.setAdapter(aa);
        
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i=new Intent(Curriculum.this,Day.class);
				i.putExtra("d", arg2);
				startActivity(i);
				
				//setTitle(Integer.toString(arg2));
			}
        	
        });
    }
}