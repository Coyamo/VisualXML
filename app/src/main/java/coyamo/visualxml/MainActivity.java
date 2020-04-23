package coyamo.visualxml;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import coyamo.visualxml.lib.proxy.ProxyResources;
import coyamo.visualxml.ui.adapter.ResourcePagerAdapter;
import coyamo.visualxml.ui.adapter.SignAdapter;

public class MainActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private EditText editor;
    //private ProxyResources resources;
    private DrawerLayout drawer;
    private LinearLayout drawerSub;
    private ImageButton add;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ProxyResources.init(this);

        tab = findViewById(R.id.tablayout);
        add = findViewById(R.id.title_add);
        drawerSub = findViewById(R.id.main_drawer_sub);
        drawer = findViewById(R.id.maindrawerLayout);
        editor = findViewById(R.id.editor);
        RecyclerView signlist = findViewById(R.id.sign_list);
        //resources = ProxyResources.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Raw XML");

        signlist.setAdapter(new SignAdapter(editor));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        signlist.setLayoutManager(llm);

        try {
            InputStream ins = getAssets().open("test.xml");
            byte[] b = new byte[ins.available()];
            ins.read(b);
            editor.setText(new String(b));
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final List<String> names = new ArrayList<>();
        names.add("String");
        names.add("Drawable");
        names.add("Color");


        final ResourcePagerAdapter adapter = new ResourcePagerAdapter(this, names);
        final ViewPager pager = findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        pager.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(pager.getCurrentItem());
            }
        });


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
                if (drawer.isDrawerOpen(drawerSub)) drawer.closeDrawer(drawerSub);
                else drawer.openDrawer(drawerSub);
                break;
            case R.id.about:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("关于")
                        .setMessage("硬解Xml，可以解析大部分View和属性。可以调用大部分系统color、drawable、style、attr、string,代码有点辣眼睛 凑合着看\nರ_ರ ...")
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(drawerSub)) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }

    }
}
