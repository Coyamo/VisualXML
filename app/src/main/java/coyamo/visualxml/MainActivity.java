package coyamo.visualxml;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import coyamo.visualxml.proxy.ProxyResources;
import androidx.recyclerview.widget.*;
import coyamo.visualxml.ui.*;
import android.widget.*;

public class MainActivity extends Activity {
    private EditText editor;
	private RecyclerView signlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ProxyResources.init(this);
		
       
        editor = findViewById(R.id.editor);
		signlist=findViewById(R.id.sign_list);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Raw XML");

		signlist.setAdapter(new SignAdapter(editor));
		LinearLayoutManager llm=new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.HORIZONTAL);
		signlist.setLayoutManager(llm);
		
        try {
           InputStream ins= getAssets().open("test.xml");
           byte[] b=new byte[ins.available()];
           ins.read(b);
           editor.setText(new String(b));
           ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.run:
                Intent i = new Intent(MainActivity.this, ViewActivity.class);
                i.putExtra("xml", editor.getText().toString());
                startActivity(i);
                break;
            case R.id.res:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("暂未完成")
                        .setMessage("用于处理对资源文件id的映射")
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.about:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("关于")
                        .setMessage("硬解Xml，可以解析大部分View 和属性。可以调用系统color和drawable,style,attr（其他还没有弄),代码有点辣眼睛 凑合着看\nರ_ರ ...")
                        .setPositiveButton("确定", null)
                        .show();
                break;
            
				
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
