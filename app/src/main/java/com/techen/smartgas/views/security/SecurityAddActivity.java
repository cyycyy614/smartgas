package com.techen.smartgas.views.security;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.OptionBean;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.model.SecurityTempBean;
import com.techen.smartgas.util.DateTimeHelper;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.widget.RequiredTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 页面：安检入户页面
 * 描述：新增界面
 */
public class SecurityAddActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userno)
    TextView userno;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.userstatus)
    TextView userstatus;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.layout_hor_ll)
    LinearLayout mLinearLayout;
    @BindView(R.id.activity_rootview)
    RelativeLayout activityRootview;
    @BindView(R.id.scrollvie_ll)
    LinearLayout mScrollCont;

    private ArrayList<Map<String, Object>> itemDatas = new ArrayList<Map<String, Object>>();
    List<SecurityTempBean.ResultBean.GroupInfoListBean> groupInfoListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_add);
        ButterKnife.bind(this);
        setTitle("入户安检");

        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getData();
        getTempData();
    }

    private void getData(){
        RequestUtils request = new RequestUtils();
        // get请求
//        Map<String,Object> getParams = new HashMap<>();
//        getParams.put("handle","test");

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("type","mobile");
            paramObject.put("password","E10ADC3949BA59ABBE56E057F20F883E");
            paramObject.put("username","admin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.post("s",paramObject,false,new HttpResponseListener<SecurityBean>() {
            @Override
            public void onResponse(SecurityBean securityBean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + securityBean);
            }
            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " +  e);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_security_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm://提交按钮
//                Intent i = new Intent(SecurityAddActivity.this,SecurityAddActivity.class);
//                startActivity(i);
                break;
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTempData(){
        String json = Tool.getAsstesJson(SecurityAddActivity.this, "temp.json");
        SecurityTempBean securityTempBean = Tool.fromJson(json, SecurityTempBean.class);
        initTemp(securityTempBean);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        // 获取item的id
        int id = view.getId();
//        if(id == R.id.upload){
//            startUpload();
//        }else if(id == R.id.upload_del1){
//            deleteImg();
//        }
    }

    private void initTemp(SecurityTempBean securityTempBean){
        groupInfoListBean = securityTempBean.getItemDatas();
        initUserInfo();
        addGroupItems();
    }

    private void initUserInfo() {
        title.setText("测试项目");
        username.setText("张氏那");
        userno.setText("0012333");
        mobile.setText("15699998888");
        userstatus.setText("正常");
        address.setText("青岛市李沧区**街道**号春和景园1期3号楼1单元");
    }

    // 动态向横向滚动条里面增加项目，显示各个主分类
    private void addGroupItems() {
        try {
            generateGroupView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void generateGroupView() throws JSONException {
        // 遍历数组
        for (int i = 0; i < groupInfoListBean.size(); i++) {
            SecurityTempBean.ResultBean.GroupInfoListBean group = groupInfoListBean.get(i);
            String code = group.getGroup_code();
            String name = group.getGroup_name();
            String image = group.getGroup_image();
            Long id = group.getId();

            // 增加横向导航
            View view = LayoutInflater.from(this).inflate(R.layout.item_security_add_group, null);
            ImageView iv = (ImageView) view.findViewById(R.id.img);
            // 设置本地图片
//            iv.setImageResource(image);
            //设置网络图片
            Glide.with(this).load(image).into(iv);
            TextView tv = (TextView) view.findViewById(R.id.textview);
            tv.setText(name);
            // 为item设置id
            view.setId(new Long(id).intValue());
            // 为item绑定数据
            view.setTag(id + "-" + i);
            // 为item设置点击事件
            view.setOnClickListener(this);
            // 把item添加到父view中
            mLinearLayout.addView(view);

            // 生成组内的原件
            generateItem(group);
        }
    }

    /**
     * 生成一个组的元件，根据类型不同来做不同的动态显示
     * @param group
     */
    private void generateItem(SecurityTempBean.ResultBean.GroupInfoListBean group) throws JSONException {
        List<SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean> itemList = group.getItemList();
        // 获取
        View view = LayoutInflater.from(this).inflate(R.layout.item_group_cell, null,false);
        // 标题-- 管道
        generate_title(view,group);
        for (int i = 0; i < itemList.size(); i++) {
            SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean = itemList.get(i);
            switch (itemBean.getItem_type()){
                case "input":
                    initInput(view, itemBean);
                    break;
               case "textarea":
                    initTextarea(view, itemBean);
                    break;
                case "checkbox":
                    initCheckbox(view, itemBean);
                break;
                case "radio":
                    initRadio(view, itemBean);
                break;
                case "select":
                    initSelect(view, itemBean);
                break;
                case "date":
                    initDate(view, itemBean);
                break;
            }
        }
        // 增加拍照
        generate_camera(view);
    }

    private void generate_title(View view, SecurityTempBean.ResultBean.GroupInfoListBean group){
        // 标题-- 管道
        LinearLayout linearLayout_group = (LinearLayout) view.findViewById(R.id.group);
        TextView group_name = (TextView) view.findViewById(R.id.group_name);
        group_name.setText(group.getGroup_name());
        removeView(linearLayout_group);
        mScrollCont.addView(linearLayout_group);
    }
    private void generate_camera(View view){
        // layout_camaro
        // 拍照
        LinearLayout linearLayout_camera = (LinearLayout) view.findViewById(R.id.layout_camera);
        removeView(linearLayout_camera);
        mScrollCont.addView(linearLayout_camera);
    }

    private void removeView(View child){
        if(child.getParent() != null){
            ((ViewGroup)child.getParent()).removeView(child);
        }
    }

    private void initInput(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        if(itemBean.getRequired_flag() == 1) {
                // 必须 layout_need_input
                RelativeLayout layout_need_input = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.comp_need_input, null,false);
                RequiredTextView label_need = (RequiredTextView) layout_need_input.findViewById(R.id.label_input_need);
                EditText input_need = (EditText) layout_need_input.findViewById(R.id.input_need);
                label_need.setText(itemBean.getItem_name() + ":");
                input_need.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
                if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                    input_need.setText(itemBean.getDefault_value());
                }
                input_need.setId(new Long(itemBean.getId()).intValue());
//                removeView(layout_need_input);
                mScrollCont.addView(layout_need_input);
        } else {
                // 非必须 layout_unneed_input
                RelativeLayout layout_unneed_input = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.comp_unneed_input, null,false);
                TextView label_unneed = (TextView) layout_unneed_input.findViewById(R.id.label_input_unneed);
                EditText input_unneed = (EditText) layout_unneed_input.findViewById(R.id.input_unneed);
                label_unneed.setText(itemBean.getItem_name() + ":");
                input_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
                if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                    input_unneed.setText(itemBean.getDefault_value());
                }
                input_unneed.setId(new Long(itemBean.getId()).intValue());
//                removeView(layout_unneed_input);
                mScrollCont.addView(layout_unneed_input);
        }
    }


    private void initTextarea(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        if(itemBean.getRequired_flag() == 1) {
                // 非必须 layout_unneed_input
                LinearLayout layout_textarea_unneed = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_need_textarea, null,false);
                TextView label_unneed = (TextView) layout_textarea_unneed.findViewById(R.id.label_textarea_unneed);
                EditText val_unneed = (EditText) layout_textarea_unneed.findViewById(R.id.textarea_unneed);
                label_unneed.setText(itemBean.getItem_name() + ":");
                val_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
                if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                    val_unneed.setText(itemBean.getDefault_value());
                }
                val_unneed.setId(new Long(itemBean.getId()).intValue());
                val_unneed.setTag(itemBean.getId());

                mScrollCont.addView(layout_textarea_unneed);
        } else {
                // 非必须 layout_unneed_input
                LinearLayout layout_textarea_unneed = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_textarea, null,false);;
                TextView label_unneed = (TextView) layout_textarea_unneed.findViewById(R.id.label_textarea_unneed);
                EditText val_unneed = (EditText) layout_textarea_unneed.findViewById(R.id.textarea_unneed);
                label_unneed.setText(itemBean.getItem_name() + ":");
                val_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
                if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                    val_unneed.setText(itemBean.getDefault_value());
                }
                val_unneed.setId(new Long(itemBean.getId()).intValue());

                mScrollCont.addView(layout_textarea_unneed);

        }
    }
    private void initRadio(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        Switch switch_comp;
        RelativeLayout layout_radio;
        if(itemBean.getRequired_flag() == 1) {
            // 必须 layout_need_input
            layout_radio = (RelativeLayout)  LayoutInflater.from(this).inflate(R.layout.comp_need_radio, null,false);
            RequiredTextView label = (RequiredTextView) layout_radio.findViewById(R.id.label_radio_need);
            switch_comp = (Switch) layout_radio.findViewById(R.id.switch_need);
            label.setText(itemBean.getItem_name() + ":");
        } else {
            // 非必须 layout_unneed_radio
            layout_radio = (RelativeLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_radio, null,false);
            TextView label = (TextView) layout_radio.findViewById(R.id.label_radio_unneed);
            switch_comp = (Switch) layout_radio.findViewById(R.id.switch_unneed);
            label.setText(itemBean.getItem_name() + ":");
        }
        switch_comp.setChecked(itemBean.getDefault_value().equals('1') ? true : false);
        switch_comp.setId(new Long(itemBean.getId()).intValue());

        mScrollCont.addView(layout_radio);
        if(itemBean.getHidden_danger_flag() == 1) {
            LinearLayout layout_danger = dangerMethodItem(view,itemBean);
            layout_danger.setVisibility(View.GONE);
            mScrollCont.addView(layout_danger);
            switch_comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", ""+isChecked);
                    if(isChecked){
                        layout_danger.setVisibility(View.VISIBLE);
                    } else {
                        layout_danger.setVisibility(View.GONE);
                    }
                }

            });
        }
    }

    private LinearLayout dangerMethodItem(View view,SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        // 显示整改方式和是否上报 --layout_method
        LinearLayout layout_danger = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_danger, null,false);
        LinearLayout layout_method = (LinearLayout) layout_danger.findViewById(R.id.layout_method);
        LinearLayout layout_isreport = (LinearLayout) layout_danger.findViewById(R.id.layout_isreport);
        TextView select_report = (TextView) layout_isreport.findViewById(R.id.select_report);
        TextView text_method = (TextView) layout_danger.findViewById(R.id.text_method);
        String method = TextUtils.isEmpty(itemBean.getRectification_method()) ? "" : itemBean.getRectification_method();
        text_method.setText(method);
        // text_method.setId(new Long(itemBean.getId()).intValue());
        // 选择列表数据处理
        List<OptionBean> optionList = new ArrayList<OptionBean>();
        OptionBean bean = new OptionBean();
        bean.setLabel("现场整改");
        bean.setValue("1");
        optionList.add(bean);
        bean = new OptionBean();
        bean.setLabel("上报维修");
        bean.setValue("2");
        optionList.add(bean);

        initOptionPicker(select_report,optionList);
        return layout_danger;
    }

    private void initCheckbox(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean) throws JSONException {
        String option_str = itemBean.getItem_option();
        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);

        LinearLayout layout;
        if(itemBean.getRequired_flag() == 1) {
            // 必须 layout_need_input
            layout = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_need_checkbox, null,false);;
            RequiredTextView label_need = (RequiredTextView) layout.findViewById(R.id.label_checkbox_need);
            label_need.setText(itemBean.getItem_name() + ":");
        } else {
            // 非必须 layout_unneed_radio
            layout = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_checkbox, null,false);
            TextView label_unneed = (TextView) layout.findViewById(R.id.label_checkbox_unneed);
            label_unneed.setText(itemBean.getItem_name() + ":");
        }
        generate_checkbox(optionList,layout);

        mScrollCont.addView(layout);
    }

    private void generate_checkbox(List<OptionBean> optionList,LinearLayout layout){
        for(OptionBean bean : optionList){
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(bean.getLabel());
            cb.setTag(bean);
            layout.addView(cb);
        }
    }

    private void initSelect(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean) throws JSONException {
        String option_str = itemBean.getItem_option();
        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
        if(itemBean.getRequired_flag() == 1) {
            // 必须 layout_need_input
            LinearLayout layout_need_select = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_need_select, null,false);
            RequiredTextView label_need = (RequiredTextView) layout_need_select.findViewById(R.id.label_select_need);
            TextView select_need = (TextView) layout_need_select.findViewById(R.id.select_need);
            label_need.setText(itemBean.getItem_name() + ":");
            select_need.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
            select_need.setId(new Long(itemBean.getId()).intValue());
            initOptionPicker(select_need,optionList);

            mScrollCont.addView(layout_need_select);
        } else {
            // 非必须 layout_unneed_radio
            LinearLayout layout_unneed_select = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_select, null,false);
            TextView label_unneed = (TextView) layout_unneed_select.findViewById(R.id.label_select_unneed);
            TextView select_unneed = (TextView) layout_unneed_select.findViewById(R.id.select_unneed);
            label_unneed.setText(itemBean.getItem_name() + ":");
            select_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
            select_unneed.setId(new Long(itemBean.getId()).intValue());
            initOptionPicker(select_unneed,optionList);

            mScrollCont.addView(layout_unneed_select);
        }
    }

    private void initDate(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean) throws JSONException {
        String option_str = itemBean.getItem_option();
        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
        if(itemBean.getRequired_flag() == 1) {
            // 必须 layout_need_date
            LinearLayout layout_need_date = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_need_date, null,false);
            RequiredTextView label_need = (RequiredTextView) layout_need_date.findViewById(R.id.label_date_need);
            TextView date_need = (TextView) layout_need_date.findViewById(R.id.date_need);
            label_need.setText(itemBean.getItem_name() + ":");
            date_need.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
            date_need.setId(new Long(itemBean.getId()).intValue());
            initDatePicker(date_need);

            mScrollCont.addView(layout_need_date);
        } else {
            // 非必须 layout_unneed_date
            LinearLayout layout_unneed_date = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_date, null,false);
            TextView label_unneed = (TextView) layout_unneed_date.findViewById(R.id.label_date_unneed);
            TextView date_unneed = (TextView) layout_unneed_date.findViewById(R.id.date_unneed);
            label_unneed.setText(itemBean.getItem_name() + ":");
            date_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "" : itemBean.getPlaceholder());
            date_unneed.setId(new Long(itemBean.getId()).intValue());
            initDatePicker(date_unneed);

            mScrollCont.addView(layout_unneed_date);
        }
    }


    //初始化选择器
    private void initOptionPicker(TextView select,List<OptionBean> optionList) {
        OptionsPickerView mPickerView; // 选择器
        ArrayList<String> mNameList = new ArrayList<String>();
        ArrayList<String> mValList = new ArrayList<String>();
        for(OptionBean obj : optionList){
            mNameList.add(obj.getLabel());
            mValList.add(obj.getValue());
        }
        mPickerView = new OptionsPickerBuilder(SecurityAddActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = mNameList.get(options1);
                String val = mValList.get(options1);
                select.setText(tx);
                select.setTag(select.getId(),val);
            }
        })
                .setDecorView(activityRootview)//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                .setTitleText("请选择当前用户案件状态")//标题文字
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
                .setCancelText("取消")//取消按钮文字
                .setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
                .setSubmitText("确定")//确认按钮文字
                .setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
                .setLineSpacingMultiplier(1.8f)//行间距
                .setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
                .setSelectOptions(0)//设置选择的值
                .build();

        mPickerView.setPicker(mNameList);//添加数据
        select.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mPickerView.show();
            }
        });
    }

    /**初始化日期选择器控件*/
    private void initDatePicker(TextView dateView) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        //设置最小日期和最大日期
        Calendar startDate = Calendar.getInstance();
        try {
            startDate.setTime(DateTimeHelper.parseStringToDate("1970-01-01"));//设置为2006年4月28日
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endDate = Calendar.getInstance();//最大日期是今天

        //时间选择器
        TimePickerView mDatePickerView = new TimePickerBuilder(SecurityAddActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                dateView.setText(DateTimeHelper.formatToString(date,"yyyy-MM-dd"));
            }
        })
                .setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setTitleText("开始日期")//标题文字
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getResources().getColor(R.color.pickerview_title_text_color))//标题文字颜色
                .setCancelText("取消")//取消按钮文字
                .setCancelColor(getResources().getColor(R.color.pickerview_cancel_text_color))//取消按钮文字颜色
                .setSubmitText("确定")//确认按钮文字
                .setSubmitColor(getResources().getColor(R.color.pickerview_submit_text_color))//确定按钮文字颜色
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(getResources().getColor(R.color.pickerview_center_text_color))//设置选中文本的颜色值
                .setLineSpacingMultiplier(1.8f)//行间距
                .setDividerColor(getResources().getColor(R.color.pickerview_divider_color))//设置分割线的颜色
                .setRangDate(startDate, endDate)//设置最小和最大日期
                .setDate(selectedDate)//设置选中的日期
                .build();
        dateView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDatePickerView.show();
            }
        });
    }



    String imgPath = "";
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startUpload() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        choosePictureDialog(10);
    }

    private  void deleteImg() {
//        imageDel.setVisibility(View.GONE);
//        upload.setImageResource(R.mipmap.icon_pic);
    }

    /**
     * 选择拍照/相册图片
     *
     * @param type 打开类型区分码
     */
    private void choosePictureDialog(int type) {
        View chooseTypeView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_pic_type, null);
        AlertDialog selectDialog = new AlertDialog.Builder(this).setView(chooseTypeView).setCancelable(false).create();
        selectDialog.show();
        Objects.requireNonNull(selectDialog.getWindow()).setBackgroundDrawableResource(R.color.transparent);

        TextView tvChoosePicCamera = chooseTypeView.findViewById(R.id.tv_choose_pic_camera);
        TextView tvChoosePicGallery = chooseTypeView.findViewById(R.id.tv_choose_pic_gallery);
        TextView tvChoosePicCancel = chooseTypeView.findViewById(R.id.tv_choose_pic_cancel);

        tvChoosePicCamera.setOnClickListener(v -> {
            selectDialog.dismiss();
            // 拉起相机
            openCamera(type);
        });
        tvChoosePicGallery.setOnClickListener(v -> {
            selectDialog.dismiss();
            // 打开相册
            openGallery(type);
        });
        tvChoosePicCancel.setOnClickListener(v -> selectDialog.dismiss());
    }

    /**
     * 打开相机
     *
     * @param type 打开类型区分码
     */
    private void openCamera(int type) {
        // 创建照片存储目录
        File imgDir = new File(getFilePath(null));
        // 创建照片
        String photoName = System.currentTimeMillis() + ".png";
        File picture = new File(imgDir, photoName);
        if (!picture.exists()) {
            try {
                picture.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "choosePictureTypeDialog: 创建图片失败", e);
            }
        }
        imgPath = picture.getAbsolutePath();
        // 调用相机拍照
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.custom.view.fileprovider", picture));
        startActivityForResult(camera, type);
    }

    /**
     * 打开相册
     *
     * @param type 打开类型区分码
     */
    private void openGallery(int type) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(gallery, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拉起相机回调data为null，打开相册回调不为null
        switch (requestCode) {
            case 0:
                Toast.makeText(this, "权限获得", Toast.LENGTH_SHORT).show();
                break;
            case 10:
            case 11:
                if (data == null && !imgPath.isEmpty()) {
//                    Glide.with(this).load(imgPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(upload);
//                    imageDel.setVisibility(View.VISIBLE);
                } else if (data != null) {
//                    Glide.with(this).load(data.getData()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(upload);
//                    imageDel.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
        imgPath = "";
    }

    /**
     * 获取存储文件路径
     *
     * @param dir     选择目录
     * @return 路径
     */
    public String getFilePath(String dir) {
        String path;
        // 判断是否有外部存储，是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = getExternalFilesDir(dir).getAbsolutePath();
        } else {
            // 使用内部储存
            path = getFilesDir() + File.separator + dir;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
}