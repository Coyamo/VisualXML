package coyamo.visualxml.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResourcePagerAdapter extends PagerAdapter {
    private Context ctx;
    private List<Map<String, String>> list;
    private List<ResourceListAdapter> adapters;
    private List<RecyclerView> views;
    private List<String> names;

    public ResourcePagerAdapter(Context ctx, List<String> names, List<Map<String, String>> list) {
        this.ctx = ctx;
        this.list = list;
        this.names = names;
        adapters = new ArrayList<>();
        views = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ResourceListAdapter adapter = new ResourceListAdapter(i, list.get(i));
            RecyclerView rv = new RecyclerView(ctx);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(ctx));
            adapters.add(adapter);
            views.add(rv);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    public void addData(int i) {
        adapters.get(i).addData();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
