package coyamo.visualxml;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import coyamo.visualxml.lib.parser.AndroidXmlParser;
import coyamo.visualxml.lib.parser.ReadOnlyParser;
import coyamo.visualxml.lib.proxy.ProxyResources;
import coyamo.visualxml.lib.ui.OutlineView;
import coyamo.visualxml.lib.utils.MessageArray;
import coyamo.visualxml.lib.utils.Utils;
import coyamo.visualxml.ui.adapter.ErrorMessageAdapter;
import coyamo.visualxml.ui.menu.CheckBoxActionProvider;
import coyamo.visualxml.ui.treeview.ViewBean;
import coyamo.visualxml.ui.treeview.ViewNodeBinder;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class ViewActivity extends AppCompatActivity {
    private OutlineView outlineView;
    private DrawerLayout drawer;
    private LinearLayout drawerSub, drawerSub2;
    private boolean isEditMode = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
        drawer = findViewById(R.id.drawerLayout);
        drawerSub = findViewById(R.id.drawer_sub);
        drawerSub2 = findViewById(R.id.drawer_sub2);
        outlineView = findViewById(R.id.outline_view);
        RecyclerView rv = findViewById(R.id.err_list);
        rv.setAdapter(new ErrorMessageAdapter());
        rv.setLayoutManager(new LinearLayoutManager(this));
        ProxyResources.getInstance().getViewIdMap().clear();
        MessageArray.getInstanse().clear();

        final List<TreeNode> nodes = new ArrayList<>();

        final Stack<TreeNode> treeNodeStack = new Stack<>();
        try {

            AndroidXmlParser.with(outlineView)
                    .setOnParseListener(new AndroidXmlParser.OnParseListener() {
                        @Override
                        public void onAddChildView(View v, ReadOnlyParser parser) {
                            ViewBean bean = new ViewBean(v, parser);
                            bean.setViewGroup(v instanceof ViewGroup);

                            TreeNode<ViewBean> child = new TreeNode<>(bean);
                            //如果没有父Group
                            //理论上这种情况应该不存在的？
                            //这里暂不考虑
                            if (treeNodeStack.size() == 0) {
                                //添加到根
                                nodes.add(child);
                            } else {
                                //添加到父亲
                                TreeNode node = treeNodeStack.peek();
                                node.addChild(child);
                            }
                        }

                        @Override
                        public void onJoin(ViewGroup viewGroup, ReadOnlyParser parser) {
                            ViewBean bean = new ViewBean(viewGroup, parser);
                            bean.setViewGroup(true);

                            //生成group
                            TreeNode<ViewBean> child = new TreeNode<>(bean);
                            if (treeNodeStack.size() == 0) {
                                treeNodeStack.push(child);
                            } else {
                                TreeNode node = treeNodeStack.peek();
                                node.addChild(child);
                                treeNodeStack.push(child);
                            }
                        }

                        @Override
                        public void onRevert(ViewGroup viewGroup, ReadOnlyParser parser) {
                            TreeNode node = treeNodeStack.pop();
                            //这种情况就是最外层的添加到node完成
                            //把它添加到根list

                            if (treeNodeStack.size() == 0) {
                                //展开最外层
                                node.expand();
                                nodes.add(node);
                            }
                            //也有可能开始一个新的onJoin处理下一个group
                            //或view
                            //这种应该是不规范的xml
                            //暂不考虑

                        }

                        @Override
                        public void onFinish() {
                        }

                        @Override
                        public void onStart() {
                        }
                    })
                    .parse(getIntent().getStringExtra("xml"));

            loadViewTree(nodes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (MessageArray.getInstanse().getList().size() > 0) {
            if (!drawer.isDrawerOpen(drawerSub))
                drawer.openDrawer(drawerSub);
        }

        outlineView.setHoldOutline(false);


        /*outlineView.setOutlineLongClickListener(new OutlineView.OnOutlineLongClickListener() {
            @Override
            public boolean onLongClick(View v, int displayType) {
                Log.d("test",v.getClass().getSimpleName()+"  long click");
                return false;
            }
        });*/


        //添加一个笔和眼睛切换的图标在toolbar
        //切换编辑模式 和 查看数据模式
        //设计模式和蓝图模式可以考虑现实不同的东西
        outlineView.setOutlineClickListener(new OutlineView.OnOutlineClickListener() {
            @Override
            public void onDown(final View v, int displayType) {
                if (!isEditMode) {

                }
            }

            @Override
            public void onCancel(View v, int displayType) {

            }

            @Override
            public void onClick(View v, int displayType) {
                if (isEditMode) {
                    ViewBean bean = findBeanByView(nodes, v);
                    StringBuilder sb = new StringBuilder();
                    if (bean != null) {

                        for (ViewBean.ViewInfo info : bean.getInfoList()) {
                            sb.append(info.getAttributeName())
                                    .append("=")
                                    .append(info.getAttributeValue())
                                    .append("\n");
                        }
                    }
                    new AlertDialog.Builder(ViewActivity.this)
                            .setMessage(sb)
                            .show();
                }

            }
        });

    }


    private void loadViewTree(List<TreeNode> nodes) {
        RecyclerView rv = findViewById(R.id.view_tree);
        rv.setLayoutManager(new LinearLayoutManager(this));

        TreeViewAdapter adapter = new TreeViewAdapter(nodes, Collections.singletonList(new ViewNodeBinder()));
        adapter.setPadding((int) Utils.dp2px(this, 16));
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onLongClick(TreeNode node, RecyclerView.ViewHolder holder) {
                ViewBean viewBean = (ViewBean) node.getContent();

                return true;
            }

            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                ViewBean type = (ViewBean) node.getContent();
                if (type.isViewGroup()) {
                    onToggle(!node.isExpand(), holder);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                ViewNodeBinder.ViewHolder dirViewHolder = (ViewNodeBinder.ViewHolder) holder;
                final ImageView ivArrow = dirViewHolder.arrow;
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree).start();
            }
        });
        rv.setAdapter(adapter);

    }

    private ViewBean findBeanByView(List<TreeNode> nodes, View v) {
        for (TreeNode node : nodes) {
            ViewBean bean = (ViewBean) node.getContent();
            if (bean.getView() == v) {
                return bean;
            } else {
                bean = findBeanByView(node.getChildList(), v);
                if (bean != null) return bean;
            }
        }
        return null;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(drawerSub) || drawer.isDrawerOpen(drawerSub2)) {
            drawer.closeDrawers();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.debug:
                if (drawer.isDrawerOpen(drawerSub))
                    drawer.closeDrawer(drawerSub);
                else
                    drawer.openDrawer(drawerSub);
                break;
            case R.id.component_tree:
                if (drawer.isDrawerOpen(drawerSub2))
                    drawer.closeDrawer(drawerSub2);
                else
                    drawer.openDrawer(drawerSub2);
                break;
            case R.id.display_view:
                outlineView.setDisplayType(OutlineView.DISPLAY_VIEW);
                break;
            case R.id.display_design:
                outlineView.setDisplayType(OutlineView.DISPLAY_DESIGN);
                break;
            case R.id.display_blueprint:
                outlineView.setDisplayType(OutlineView.DISPLAY_BLUEPRINT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view, menu);
        CheckBoxActionProvider p = (CheckBoxActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.toggle_edit));
        p.setOnCheckedChangeListener(new CheckBoxActionProvider.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View buttonView, boolean isChecked) {
                isEditMode = !isChecked;
                outlineView.setHoldOutline(isChecked);
            }
        });
        return true;
    }
}
