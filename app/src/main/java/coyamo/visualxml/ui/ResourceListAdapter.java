package coyamo.visualxml.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coyamo.visualxml.R;
import coyamo.visualxml.utils.Utils;

public class ResourceListAdapter extends RecyclerView.Adapter<ResourceListAdapter.ViewHolder> {
    private Map<String, String> map;
    private int type;
    private Context ctx;

    public ResourceListAdapter(int type, Map<String, String> map) {
        this.map = map;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resource_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parent.getContext())
                        .setTitle("删除")
                        .setMessage("删除数据")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                map.remove(new ArrayList(map.keySet()).get(holder.getAdapterPosition()));
                                notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> names = new ArrayList<>(map.keySet());
        String value = map.get(names.get(position));
        holder.name.setText(names.get(position));
        holder.value.setText(value);

        holder.value.setBackgroundColor(Color.TRANSPARENT);
        holder.value.setCompoundDrawables(null, null, null, null);
        switch (type) {
            case 0://str
                break;
            case 1://drawable
                Drawable drawable = DrawableWrapper.createFromPath(value);
                if (drawable != null) {
                    drawable.setBounds(0, 0, 64, 64);
                    holder.value.setCompoundDrawables(drawable, null, null, null);
                } else
                    Toast.makeText(holder.item.getContext(), "不是图片的路径", Toast.LENGTH_SHORT).show();
                break;
            case 2://color
                if (Utils.isColor(value))
                    holder.value.setBackgroundColor(Color.parseColor(value));
                else
                    Toast.makeText(holder.item.getContext(), "颜色格式错误", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public void addData() {
        final int p = (int) Utils.dp2px(ctx, 24);
        final int pt = (int) Utils.dp2px(ctx, 16);
        final int pb = (int) Utils.dp2px(ctx, 20);
        LinearLayout ll = new LinearLayout(ctx);
        ll.setPadding(p, pt, p, pb);
        ll.setOrientation(LinearLayout.VERTICAL);
        final EditText n = new EditText(ctx);
        n.setHint("名字");

        final EditText va = new EditText(ctx);
        ll.addView(n);
        ll.addView(va);
        va.setHint("值");
        new AlertDialog.Builder(ctx)
                .setTitle("添加")
                .setView(ll)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _key = n.getText().toString().trim();
                        String _value = va.getText().toString().trim();
                        if (!_key.isEmpty() && !_value.isEmpty()) {
                            map.put(_key, _value);
                            notifyDataSetChanged();
                        }
                    }
                })
                .show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView value;
        View item;

        ViewHolder(View view) {
            super(view);
            item = view;
            name = view.findViewById(R.id.name);
            value = view.findViewById(R.id.value);
        }
    }
}

