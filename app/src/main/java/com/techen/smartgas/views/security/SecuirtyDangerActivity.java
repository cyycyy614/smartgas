package com.techen.smartgas.views.security;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techen.smartgas.R;
import com.techen.smartgas.model.OptionBean;
import com.techen.smartgas.model.SecurityDangerBean;
import com.techen.smartgas.model.SecurityResultBean;
import com.techen.smartgas.model.SecurityTempBean;
import com.techen.smartgas.util.HttpService;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.SharedPreferencesUtil;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.widget.RequiredTextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecuirtyDangerActivity extends AppCompatActivity {
    @BindView(R.id.scrollview_ll)
    LinearLayout mScrollCont;
    @BindView(R.id.dynamic)
    LinearLayout dynamicCont;
    @BindView(R.id.btn_goback)
    Button btnGoBack;
    @BindView(R.id.btn_check)
    Button btnCheck;

    List<SecurityDangerBean> list;
    String plan_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secuirty_danger);
        ButterKnife.bind(this);
        setTitle("隐患检测");

        //接受参数
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");

        btnGoBack.setOnClickListener(mListener);
        btnCheck.setOnClickListener(mListener);
        LoadingDialog.getInstance(this).show();
        String securityStr = (String) SharedPreferencesUtil.get(SecuirtyDangerActivity.this,"dangerShowList","{}");
        list = Tool.toArray(securityStr, SecurityDangerBean.class);
        if(list != null){
            dynamicCont.removeAllViews();
            generate_Comp();
        }
        LoadingDialog.getInstance(this).hide();
        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_goback:                          //返回按钮
                    LoadingDialog.setInstance(null);
                    SecuirtyDangerActivity.this.finish();
                    break;
                case R.id.btn_check:                          //已检查按钮
                    // 打开确认页面
                    Intent i = new Intent(SecuirtyDangerActivity.this,SecurityConfirmActivity.class);
                    i.putExtra("plan_id", plan_id + "");
                    LoadingDialog.setInstance(null);
                    SecurityAddActivity.instance.finish(); // 关掉安检入户页面
                    SecuirtyDangerActivity.this.finish(); // 关掉上一个页面
                    startActivity(i);
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoadingDialog.setInstance(null);
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generate_Comp(){
        // 获取
        View view = LayoutInflater.from(this).inflate(R.layout.item_group_cell, null,false);
        for(SecurityDangerBean dangerItem: list){
            String groupName = dangerItem.getGroupName();
            // 标题-- 管道
            generate_title(view,groupName);
            // 生成内容
            for(SecurityDangerBean.DangerListBean itemBean:dangerItem.getDangerList()){
                generate_cont(view,itemBean);
            }
        }
    }

    private void generate_title(View view, String groupName){
        // 标题-- 管道
        LinearLayout linearLayout_group = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_title, (ViewGroup) view,false);
        TextView group_name = (TextView) linearLayout_group.findViewById(R.id.group_name);
        group_name.setText(groupName);
        removeView(linearLayout_group);
        dynamicCont.addView(linearLayout_group);
    }

    private void generate_cont(View view, SecurityDangerBean.DangerListBean itemBean){
        LinearLayout layout_danger = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_show_danger, (ViewGroup) view,false);
        TextView label_item = (TextView) layout_danger.findViewById(R.id.label_item);
        TextView item_val = (TextView) layout_danger.findViewById(R.id.item_val);
        TextView text_method = (TextView) layout_danger.findViewById(R.id.text_method);
        TextView select_report = (TextView) layout_danger.findViewById(R.id.select_report);
        EditText textarea = (EditText) layout_danger.findViewById(R.id.textarea);
        FlowLayout flowlayout = (FlowLayout) layout_danger.findViewById(R.id.flowlayout);
        label_item.setText(itemBean.getItemName() + ":");
        item_val.setText(itemBean.getItemVal());
        // 整改部分
        item_val.setText("是");
        text_method.setText(itemBean.getItemVal());
        select_report.setText(itemBean.getIsReport());
        textarea.setText(itemBean.getDemo());
        textarea.setFocusable(false);
        textarea.setFocusableInTouchMode(false);
        // 照片
        // 上传后显示的图片
        for(SecurityDangerBean.DangerListBean.ImgListBean imgListBean: itemBean.getImgList()){
            RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_show_img, flowlayout,false);
            ImageView img = (ImageView) relative_img.findViewById(R.id.upload_img);
            //设置网络图片
            Glide.with(this).load(HttpService.BASE_FILE_URL + imgListBean.getPhoto_url()).into(img);
            flowlayout.addView(relative_img);
        }
        if(itemBean.getImgList() != null && itemBean.getImgList().size() == 0){
            flowlayout.setVisibility(View.GONE);
        }else{
            flowlayout.setVisibility(View.VISIBLE);
        }
        dynamicCont.addView(layout_danger);
    }

    private void removeView(View child){
        if(child.getParent() != null){
            ((ViewGroup)child.getParent()).removeView(child);
        }
    }
}