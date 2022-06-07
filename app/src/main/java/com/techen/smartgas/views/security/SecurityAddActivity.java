package com.techen.smartgas.views.security;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.RequestMethod;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.itheima.retrofitutils.listener.UploadListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.FileUploadBean;
import com.techen.smartgas.model.OptionBean;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.model.SecurityDetailBean;
import com.techen.smartgas.model.SecurityRecordDetailBean;
import com.techen.smartgas.model.SecurityResultBean;
import com.techen.smartgas.model.SecurityTempBean;
import com.techen.smartgas.model.SecurityUserDetailBean;
import com.techen.smartgas.util.BitmapUtil;
import com.techen.smartgas.util.DateTimeHelper;
import com.techen.smartgas.util.FileUtils;
import com.techen.smartgas.util.HttpService;
import com.techen.smartgas.util.ImageCompressUtils;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.SharedPreferencesUtil;
import com.techen.smartgas.util.ThreadPoolUtil;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.widget.RequiredTextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
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
    @BindView(R.id.btn_detail)
    Button btnDetail;
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
    @BindView(R.id.scrollview)
    ScrollView mScrollView;

    String template_id;
    String plan_id;
    String account_id;
    String code;
    View mView;

    public static SecurityAddActivity instance;

    private Map<String, Object> itemDatas = new HashMap<String, Object>();
    private Map<Long, ArrayList<Map<String, Object>>> imgList = new HashMap<Long, ArrayList<Map<String, Object>>>();
    private Map<Long, ArrayList<Map<String, Object>>> dangerImgList = new HashMap<Long, ArrayList<Map<String, Object>>>();
    List<SecurityTempBean.ResultBean.GroupInfoListBean> groupInfoListBean;

    ArrayList<Map<String,Object>> dangerShowList = new ArrayList<>();// 隐患显示信息列表
    ArrayList<Map<String,Object>> sList = new ArrayList<>();// 安检项值信息列表
    ArrayList<Map<String,Object>> hiddenDangerList = new ArrayList<>(); // 隐患信息列表
    ArrayList<Map<String,Object>> sPhotoList = new ArrayList<>(); // 安检图片列表
    private Integer repetition_flag = 0 ;
    // 存储动态添加控件的id
    public static final Map<String, Integer> ITEM_MAP = new HashMap<String, Integer>();
    public static final Map<String, Integer> ITEM_G_MAP = new HashMap<String, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_add);
        mView = getWindow().getDecorView();
        ButterKnife.bind(this);
        instance = this;
        setTitle("入户安检");

        // 接受参数
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        template_id = intent.getStringExtra("template_id");
        account_id = intent.getStringExtra("account_id");
        code = intent.getStringExtra("code");
        repetition_flag = intent.getIntExtra("repetition_flag", 0);

        if(repetition_flag == 1){
            btnDetail.setVisibility(View.VISIBLE);
            btnDetail.setOnClickListener(mListener);
        }else{
            btnDetail.setVisibility(View.GONE);
        }
        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//        getLocalTempData();
//        getData();
        getTempData();
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_detail:
                    // 打开安检记录详情
                    // 打开详情页面
                    Intent intent = new Intent(SecurityAddActivity.this, SecurityDetailActivity.class);
                    intent.putExtra("plan_id", plan_id + "");
                    intent.putExtra("template_id", template_id + "");
                    intent.putExtra("record_id",  "-999");
                    intent.putExtra("account_id",  account_id);
                    startActivity(intent);
                    break;
            }
        }
    };

    // 获取安检用户详情
    private void getUserDetail() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("cons_id", account_id);
        request.get("amiwatergas/mobile/securityPlan/qryConsDetails", getParams, true, SecurityAddActivity.this, new HttpResponseListener<SecurityUserDetailBean>() {
            @Override
            public void onResponse(SecurityUserDetailBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if(bean != null){
                    SecurityUserDetailBean.ResultBean resultBean = bean.getResult();
                    title.setText(resultBean.getCons_addr());
                    username.setText(resultBean.getCons_name());
                    userno.setText(resultBean.getCons_no());
                    mobile.setText(resultBean.getCons_tel());
                    userstatus.setText(resultBean.getCons_status() == 1 ? "正常" : "异常");
                    address.setText(resultBean.getCons_addr());
                }
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " + e);
            }
        });
    }


    private void getTempData(){

        //开始请求
        Request request = ItheimaHttp.newGetRequest("amiwatergas/mobile/securityTemplate/load");//apiUrl格式："xxx/xxxxx"
        Map<String,Object> map = new HashMap<>();
        map.put("id",template_id);

        request.putParamsMap(map);
        request.putHeader("Authorization","Bearer "+ Tool.getToken(SecurityAddActivity.this));
        request.putHeader("lang","en");
        Call call = ItheimaHttp.send(request, new HttpResponseListener<SecurityTempBean>() {

            @Override
            public void onResponse(SecurityTempBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " +bean);
                initTemp(bean);
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                Toast.makeText(SecurityAddActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_confirm://提交按钮
                submit();
                break;
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return false;
    }

    private void getLocalTempData(){
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

    // 点击提交，将数据转换为json数据进行存储
    private void submit(){
        if(!generateCurrentTempData()){
            return;
        }
        // 打开隐患检测页面
        Intent i = new Intent(SecurityAddActivity.this,SecuirtyDangerActivity.class);
        i.putExtra("plan_id", plan_id + "");
        startActivity(i);
    }
    // 根据当前模板及填充数据，生成模板数据，并放在本地存储里面
    private Boolean generateCurrentTempData(){
        dangerShowList = new ArrayList<>();// 隐患显示信息列表
        sList = new ArrayList<>();// 安检项值信息列表
        hiddenDangerList = new ArrayList<>(); // 隐患信息列表
        // 遍历数组
        Map<String,Object> item = new HashMap<>();
        Map<String,Object> dangerItem = new HashMap<>();
        Map<String,Object> showItem = new HashMap<>();
        Map<String,Object> dangerShowItem = new HashMap<>();
        ArrayList<Map<String,Object>> dangerGroupList = new ArrayList<>(); // 隐患信息列表
        Boolean valid = true;

        for (int i = 0; i < groupInfoListBean.size(); i++) {
            SecurityTempBean.ResultBean.GroupInfoListBean group = groupInfoListBean.get(i);
            Long groupId = group.getId();
            dangerGroupList = new ArrayList<>();

            List<SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean> itemList = group.getItemList();
            for (int j = 0; j < itemList.size(); j++) {
                SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean = itemList.get(j);
                Integer isRequired =  itemBean.getRequired_flag();
                String itemName = itemBean.getItem_name();
                String curVal = "";

                Long itemId = itemBean.getId();
                item = new HashMap<>();
                dangerItem = new HashMap<>();
                switch (itemBean.getItem_type()){
                    case "input":
                    case "textarea":
                        int editTextId = ITEM_MAP.get(itemBean.getKey());
                        EditText textView = findViewById(editTextId);
                        if(isRequired == 1 && TextUtils.isEmpty(textView.getText())){ // 判断必填
                            Toast.makeText(SecurityAddActivity.this, itemName + "不能为空！", Toast.LENGTH_SHORT).show();
                            sList = new ArrayList<>();
                            valid = false;
                            return valid;
                        }

                        Editable val = textView.getText();
                        item.put("item_id",itemBean.getId());
                        item.put("item_value",val);
                        curVal = val.toString();
                        sList.add(item);
                        break;
                    case "checkbox":
                        String option_str = itemBean.getItem_option();
                        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
                        ArrayList checkboxlist = new ArrayList();
                        for(OptionBean bean : optionList){
                            int id = ITEM_MAP.get(itemBean.getKey() + bean.getValue());
                            CheckBox cb =  findViewById(id);
                            if(cb.isChecked()){
                                checkboxlist.add(bean.getValue());
                            }
                        }
                        String checkboxVal = Tool.toJson(checkboxlist);
                        item.put("item_id",itemBean.getId());
                        item.put("item_value",checkboxVal == null ? "" : checkboxVal);
                        sList.add(item);
                        curVal = checkboxVal;
                        break;
                    case "radio":
                        int switchId = ITEM_MAP.get(itemBean.getKey());
                        Switch aSwitch = findViewById(switchId);

                        String sval = aSwitch.isChecked() ? "1" : "0";
                        item.put("item_id",itemBean.getId());
                        item.put("item_value",sval);
                        sList.add(item);
                        curVal = sval;
                        break;
                    case "select":
                        int selectId = ITEM_MAP.get(itemBean.getKey());
                        TextView tSelect = findViewById(selectId);
                        String seText = (String) tSelect.getText();
                        String seVal = (String) tSelect.getTag();
                        if(isRequired == 1 && TextUtils.isEmpty(seVal)){ // 判断必填
                            Toast.makeText(SecurityAddActivity.this, itemName + "不能为空，请进行选择！", Toast.LENGTH_SHORT).show();
                            sList = new ArrayList<>();
                            valid = false;
                            return valid;
                        }

                        item.put("item_id",itemBean.getId());
                        item.put("item_value",seVal == null ? "" : seVal);
                        sList.add(item);
                        curVal = seVal;
                        break;
                    case "date":
                        int dateId = ITEM_MAP.get(itemBean.getKey());
                        TextView tDate = null;
                        try {
                            tDate = findViewById(dateId);
                        }catch (Exception ex){
                            tDate = new TextView(getApplicationContext());
                        }
                        if(isRequired == 1 && TextUtils.isEmpty(tDate.getText())){ // 判断必填
                            Toast.makeText(SecurityAddActivity.this, itemName + "不能为空，请选择日期！", Toast.LENGTH_SHORT).show();
                            sList = new ArrayList<>();
                            valid = false;
                            return valid;
                        }

                        String dateText = (String) tDate.getText();
                        item.put("item_id",itemBean.getId());
                        item.put("item_value",dateText);
                        sList.add(item);
                        curVal = dateText;
                        break;
                }

                if(isDanger(itemBean,curVal)) {
                    dangerItem = new HashMap<>();
                    // 隐患信息
                    int did = ITEM_MAP.get(itemBean.getKey() + "-report");
                    int demoid = ITEM_MAP.get(itemBean.getKey() + "-demo");
                    TextView dangerTextView =  findViewById(did);
                    TextView demo =  findViewById(demoid);
                    String dangerVal = (String) dangerTextView.getTag();
                    ArrayList<Map<String,Object>> itemImgList = dangerImgList.get(itemId);
                    Map<String,Object>  wObject = new HashMap<>();
                    dangerVal = TextUtils.isEmpty(dangerVal) ? "1" : dangerVal;
                    // 如果是上报维修
                    if(dangerVal != null && dangerVal.equals("2")){
                        wObject.put("work_content",itemBean.getRectification_method());
                        dangerItem.put("workOrderRepair",wObject);
                    }
                    dangerItem.put("groupId",groupId);
                    dangerItem.put("itemId",itemId);
                    dangerItem.put("infoSource","item");
                    dangerItem.put("description",demo.getText());
                    dangerItem.put("hiddenDangerInfoPhotoList",itemImgList);
                    hiddenDangerList.add(dangerItem);

                    dangerShowItem = new HashMap<>();
                    dangerShowItem.put("itemName",itemName);
                    dangerShowItem.put("itemVal",itemBean.getRectification_method());
                    dangerShowItem.put("isReport",dangerVal.equals("2") ? "上报维修" : "现场整改");
                    dangerShowItem.put("imgList",itemImgList);
                    dangerShowItem.put("demo",demo.getText());
                    dangerGroupList.add(dangerShowItem);
                }
            }
            if(dangerGroupList != null && dangerGroupList.size() > 0){
                showItem = new HashMap<>();
                showItem.put("groupId", groupId.toString());
                showItem.put("groupName", group.getGroup_name());
                showItem.put("dangerList", dangerGroupList);
                dangerShowList.add(showItem);
            }
        }

        if(!valid){
            return false;
        }

        ArrayList<Map<String,Object>> sPhotoList = new ArrayList<>();// 安检项值信息列表
        for (Long key : imgList.keySet()) {
            ArrayList<Map<String, Object>> value = imgList.get(key);
            sPhotoList.addAll(value);
        }

        itemDatas = new HashMap<String, Object>();
        itemDatas.put("planId",plan_id);
        itemDatas.put("accountId",account_id);
        itemDatas.put("repetitionFlag",0);
        itemDatas.put("state",hiddenDangerList.size() > 0 ? "danger" : "normal");
        itemDatas.put("securityCheckItemValueList",sList);
        itemDatas.put("securityCheckRecordPhotoList",sPhotoList);
        itemDatas.put("hiddenDangerList",hiddenDangerList);

        String dataJsonStr = Tool.objectToJson(itemDatas);
        String dangerJsonStr = Tool.objectToJson(dangerShowList);

        SharedPreferencesUtil.put(SecurityAddActivity.this,"securitydata",dataJsonStr);
        SharedPreferencesUtil.put(SecurityAddActivity.this,"dangerShowList",dangerJsonStr);
        System.out.println(dataJsonStr);
        System.out.println("dangerShowList---------------------");
        System.out.println(dangerJsonStr);
        return true;
    }

    private Boolean isDanger(SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean,String curVal){
        Integer isDanger =  itemBean.getHidden_danger_flag() == null ? 0 : itemBean.getHidden_danger_flag();
        String dangerVal =  itemBean.getHidden_danger_value() == null ? "" : itemBean.getHidden_danger_value();
        String dangerJudge =  itemBean.getHidden_danger_judge() == null ? "" : itemBean.getHidden_danger_judge();

        try {
            if(isDanger == 1){
                switch (itemBean.getItem_type()){
                    case "input":
                    case "textarea":
                    case "select":
                        if(dangerJudge.equals("equal")){
                            if(curVal.equals(dangerVal)){
                                return true;
                            }
                        } else if(dangerJudge.equals("greater")){
                            if( Integer.valueOf(curVal) >  Integer.valueOf(dangerVal)){
                                return true;
                            }
                        } else if(dangerJudge.equals("less")){
                            if( Integer.valueOf(curVal) <  Integer.valueOf(dangerVal)){
                                return true;
                            }
                        }
                        break;
                    case "radio":
                    case "checkbox":
                        if(dangerJudge.equals("equal")){
                            if(curVal.equals(dangerVal)){
                                return true;
                            }
                        }
                        break;
                    case "date":
                        if(dangerJudge.equals("equal")){
                            if(curVal.equals(dangerVal)){
                                return true;
                            }
                        } else if(dangerJudge.equals("greater")){
                            if( !DateTimeHelper.compare_date(curVal,dangerVal)){
                                return true;
                            }
                        } else if(dangerJudge.equals("less")){
                            if( DateTimeHelper.compare_date(curVal,dangerVal)){
                                return true;
                            }
                        }
                        break;
                }
            }
        }catch (Exception e){

        }
        return false;
    }
    private void initUserInfo() {
        getUserDetail();
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
            String image = HttpService.BASE_FILE_URL + group.getGroup_image();
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer resId = ITEM_MAP.get(group.getId() + "");
                    LinearLayout layout =  (LinearLayout)mScrollView.findViewById(resId);
                    mScrollView.scrollTo(0,layout.getTop());
                }
            });
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
        Long groupId = group.getId();
        // 获取
        View view = LayoutInflater.from(this).inflate(R.layout.item_group_cell, null,false);
        // 标题-- 管道
        generate_title(view,group);
        for (int i = 0; i < itemList.size(); i++) {
            SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean = itemList.get(i);
            String itemId = itemBean.getId() + "";
            String code = itemBean.getItem_code();
            String type = itemBean.getItem_type();
            String key = itemId + "-" + type;
            itemBean.setKey(key);
            if(itemBean.getVisible_flag() != null && itemBean.getVisible_flag() == 0){

            }
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
        generate_camera(view,groupId);
    }

    private void generate_title(View view, SecurityTempBean.ResultBean.GroupInfoListBean group){
        // 标题-- 管道
        LinearLayout linearLayout_group = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_title, (ViewGroup) view,false);
        TextView group_name = (TextView) linearLayout_group.findViewById(R.id.group_name);
        group_name.setText(group.getGroup_name());
        removeView(linearLayout_group);

        int id = View.generateViewId();
        linearLayout_group.setId(id);
        ITEM_MAP.put(group.getId() + "", id);
        mScrollCont.addView(linearLayout_group);
    }

    LinearLayout curUpload;
    ImageView curUploadImageView;
    FlowLayout curFlowlayout;
    Integer curImgFlag = 0;
    Integer uploadFlag = 100;
    Long curGroupId;
    Long curItemId;
    // 生成拍照组件，并处理上传方法
    private void generate_camera(View view,Long groupId){
        // layout_camaro
        // 拍照
        LinearLayout linearLayout_camera = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_camera, null,false);
        LinearLayout upload = (LinearLayout) linearLayout_camera.findViewById(R.id.upload);
        FlowLayout flowlayout = (FlowLayout) linearLayout_camera.findViewById(R.id.flowlayout);

        int uploadId =  View.generateViewId();
        upload.setId(uploadId);
        Integer imgFlag = 0;
        ArrayList<Map<String, Object>> groupImgList = new ArrayList<Map<String, Object>>();
        imgList.put(groupId,groupImgList);
        uploadFlag = uploadId;
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ArrayList<Map<String, Object>> gImgList = imgList.get(groupId);
                if(gImgList != null && gImgList.size() > 5) {
                    Toast.makeText(SecurityAddActivity.this,"最多上传6张",Toast.LENGTH_SHORT);
                    return;
                }
                curGroupId = groupId;
                curUpload = upload;
                curItemId = new Long(-1).longValue();
                curFlowlayout = flowlayout;
                startUpload();
            }
        });
        mScrollCont.addView(linearLayout_camera);
    }

    private void generate_img(){
        // 上传后显示的图片
        RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_img, curFlowlayout,false);
        curUploadImageView = (ImageView) relative_img.findViewById(R.id.upload_img);
        ImageView upload_del = (ImageView) relative_img.findViewById(R.id.upload_del);
        curFlowlayout.addView(relative_img);

        ArrayList<Map<String, Object>> gImgList = imgList.get(curGroupId);
        upload_del.setTag(gImgList.size());
        curImgFlag++;
        upload_del.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                curFlowlayout.removeView(relative_img);
                curImgFlag = curImgFlag - 1;
                curUpload.setTag(R.id.tag_2,curImgFlag);
                int index = (int) upload_del.getTag();
                ArrayList<Map<String, Object>> gImgList = imgList.get(curGroupId);
                if(gImgList != null && gImgList.size() > 0){
                    try {
                        gImgList.remove(index);
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    private void generate_danger_img(){
        // 上传后显示的图片
        RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_img, curFlowlayout,false);
        curUploadImageView = (ImageView) relative_img.findViewById(R.id.upload_img);
        ImageView upload_del = (ImageView) relative_img.findViewById(R.id.upload_del);
        curFlowlayout.addView(relative_img);

        ArrayList<Map<String, Object>> itemImgList = dangerImgList.get(curItemId);
        upload_del.setTag(itemImgList.size());
        upload_del.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                curFlowlayout.removeView(relative_img);
                int index = (int) upload_del.getTag();
                ArrayList<Map<String, Object>> itemImgList = dangerImgList.get(curItemId);
                if(itemImgList != null && itemImgList.size() > 0){
                    try {
                        itemImgList.remove(index);
                    }catch (Exception ex){

                    }
                }

            }
        });
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
            input_need.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请输入" : itemBean.getPlaceholder());
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                input_need.setText(itemBean.getDefault_value());
            }
            int id = View.generateViewId();
            input_need.setId(id);
            ITEM_MAP.put(itemBean.getKey(), id);
//                input_need.setId(new Long(itemBean.getId()).intValue());
//                removeView(layout_need_input);
            mScrollCont.addView(layout_need_input);
        } else {
            // 非必须 layout_unneed_input
            RelativeLayout layout_unneed_input = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.comp_unneed_input, null,false);
            TextView label_unneed = (TextView) layout_unneed_input.findViewById(R.id.label_input_unneed);
            EditText input_unneed = (EditText) layout_unneed_input.findViewById(R.id.input_unneed);
            label_unneed.setText(itemBean.getItem_name() + ":");
            input_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请输入" : itemBean.getPlaceholder());
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                input_unneed.setText(itemBean.getDefault_value());
            }
            int id = View.generateViewId();
            input_unneed.setId(id);
            ITEM_MAP.put(itemBean.getKey(), id);
//            input_unneed.setId(new Long(itemBean.getId()).intValue());
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
                val_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请输入" : itemBean.getPlaceholder());
                if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                    val_unneed.setText(itemBean.getDefault_value());
                }
                int id = View.generateViewId();
                val_unneed.setId(id);
                ITEM_MAP.put(itemBean.getKey(), id);
                val_unneed.setTag(itemBean.getId());

                mScrollCont.addView(layout_textarea_unneed);
        } else {
                // 非必须 layout_unneed_input
                LinearLayout layout_textarea_unneed = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_textarea, null,false);;
                TextView label_unneed = (TextView) layout_textarea_unneed.findViewById(R.id.label_textarea_unneed);
                EditText val_unneed = (EditText) layout_textarea_unneed.findViewById(R.id.textarea_unneed);
                label_unneed.setText(itemBean.getItem_name() + ":");
                val_unneed.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请输入" : itemBean.getPlaceholder());
                if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                    val_unneed.setText(itemBean.getDefault_value());
                }
                int id = View.generateViewId();
                val_unneed.setId(id);
                ITEM_MAP.put(itemBean.getKey(), id);

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
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                switch_comp.setChecked(itemBean.getDefault_value().equals("1") ? true : false);
            }
        } else {
            // 非必须 layout_unneed_radio
            layout_radio = (RelativeLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_radio, null,false);
            TextView label = (TextView) layout_radio.findViewById(R.id.label_radio_unneed);
            switch_comp = (Switch) layout_radio.findViewById(R.id.switch_unneed);
            label.setText(itemBean.getItem_name() + ":");
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                switch_comp.setChecked(itemBean.getDefault_value().equals("1") ? true : false);
            }
        }
        Boolean isChecked = false;
        if(!TextUtils.isEmpty(itemBean.getDefault_value()) && itemBean.getDefault_value().equals("1")){
            isChecked = true;
        }
        switch_comp.setChecked(isChecked);
//        switch_comp.setId(new Long(itemBean.getId()).intValue());
        int id = View.generateViewId();
        switch_comp.setId(id);
        ITEM_MAP.put(itemBean.getKey(), id);

        mScrollCont.addView(layout_radio);
        if(itemBean.getHidden_danger_flag() !=null && itemBean.getHidden_danger_flag() == 1) {
            LinearLayout layout_danger = dangerMethodItem(view,itemBean);
            if(isDanger(itemBean,isChecked ? "1" : "0")){
                layout_danger.setVisibility(View.VISIBLE);
            } else {
                layout_danger.setVisibility(View.GONE);
            }
            mScrollCont.addView(layout_danger);
            switch_comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isDanger(itemBean,isChecked ? "1" : "0")){
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
        TextView textarea = (TextView) layout_danger.findViewById(R.id.textarea);
        LinearLayout upload = (LinearLayout) layout_danger.findViewById(R.id.upload);
        FlowLayout flowlayout = (FlowLayout) layout_danger.findViewById(R.id.flowlayout);
        String method = TextUtils.isEmpty(itemBean.getRectification_method()) ? "" : itemBean.getRectification_method();
        text_method.setText(method);
        // 图片
        ArrayList<Map<String, Object>> itemImgList = new ArrayList<Map<String, Object>>();
        dangerImgList.put(itemBean.getId(),itemImgList);
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ArrayList<Map<String, Object>> itemImgList = dangerImgList.get(itemBean.getId());
                if(itemImgList != null && itemImgList.size() > 5) {
                    Toast.makeText(SecurityAddActivity.this,"最多上传6张",Toast.LENGTH_SHORT);
                    return;
                }
                curGroupId = itemBean.getGroup_id();
                curItemId = itemBean.getId();
                curUpload = upload;
                curFlowlayout = flowlayout;
                startUpload();
            }
        });
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
        int id = View.generateViewId();
        select_report.setId(id);
        select_report.setText("现场整改");
        select_report.setTag("1");
        ITEM_MAP.put(itemBean.getKey() + "-report", id);

        int demo_id = View.generateViewId();
        textarea.setId(demo_id);
        ITEM_MAP.put(itemBean.getKey() + "-demo", demo_id);

        initOptionPicker(select_report,optionList,itemBean,null);
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
        generate_checkbox(optionList,layout,itemBean,view);
    }

    private void generate_checkbox(List<OptionBean> optionList,LinearLayout layout,SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean,View view){
        LinearLayout layout_danger = null;
        if(itemBean.getHidden_danger_flag() !=null && itemBean.getHidden_danger_flag() == 1) {
            layout_danger = dangerMethodItem(view,itemBean);
        }
        for(OptionBean bean : optionList){
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(bean.getLabel());
            cb.setTag(bean.getValue());
            if(!TextUtils.isEmpty(itemBean.getDefault_value()) && itemBean.getDefault_value().equals(bean.getValue())){
                cb.setChecked(true);
                if(layout_danger != null){
                    if(isDanger(itemBean,bean.getValue())){
                        layout_danger.setVisibility(View.VISIBLE);
                    } else {
                        layout_danger.setVisibility(View.GONE);
                    }
                }
            }
            int id = View.generateViewId();
            cb.setId(id);
            ITEM_MAP.put(itemBean.getKey() + bean.getValue(), id);
            layout.addView(cb);
            LinearLayout finalLayout_danger = layout_danger;
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", ""+isChecked);
                    String defaultVal = (String) buttonView.getTag();
                    if(finalLayout_danger != null){
                        if(isChecked && isDanger(itemBean,defaultVal)){
                            finalLayout_danger.setVisibility(View.VISIBLE);
                        } else {
                            finalLayout_danger.setVisibility(View.GONE);
                        }
                    }

                }

            });
        }
        mScrollCont.addView(layout);
        if(layout_danger != null){
            mScrollCont.addView(layout_danger);
        }

    }

    private OptionBean getValByItemId(String selectedVal,List<OptionBean> optionList){
        String  val = "";
        if(optionList != null){
            for(OptionBean optionBean : optionList){
                if(optionBean.getValue().equals(selectedVal)){
                  return optionBean;
                }
            }
        }
        return null;
    }

    private void initSelect(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean) throws JSONException {
        String option_str = itemBean.getItem_option();
        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
        TextView select_t;
        String defaultVal = "";
        if(itemBean.getRequired_flag() == 1) {
            // 必须 layout_need_input
            LinearLayout layout_need_select = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_need_select, null,false);
            RequiredTextView label_need = (RequiredTextView) layout_need_select.findViewById(R.id.label_select_need);
            select_t = (TextView) layout_need_select.findViewById(R.id.select_need);
            label_need.setText(itemBean.getItem_name() + ":");
            select_t.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请选择" : itemBean.getPlaceholder());
//            select_need.setId(new Long(itemBean.getId()).intValue());
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                OptionBean selectedBean = getValByItemId(itemBean.getDefault_value(),optionList);
                if(selectedBean != null){
                    select_t.setText(selectedBean.getLabel());
                    select_t.setTag(selectedBean.getValue());
                    defaultVal = selectedBean.getValue();
                }
            }
            int id = View.generateViewId();
            select_t.setId(id);
            ITEM_MAP.put(itemBean.getKey(), id);

            mScrollCont.addView(layout_need_select);
        } else {
            // 非必须 layout_unneed_radio
            LinearLayout layout_unneed_select = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_select, null,false);
            TextView label_unneed = (TextView) layout_unneed_select.findViewById(R.id.label_select_unneed);
            select_t = (TextView) layout_unneed_select.findViewById(R.id.select_unneed);
            label_unneed.setText(itemBean.getItem_name() + ":");
            select_t.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请选择" : itemBean.getPlaceholder());
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                OptionBean selectedBean = getValByItemId(itemBean.getDefault_value(),optionList);
                if(selectedBean != null){
                    select_t.setText(selectedBean.getLabel());
                    select_t.setTag(selectedBean.getValue());
                    defaultVal = selectedBean.getValue();
                }
            }
            int id = View.generateViewId();
            select_t.setId(id);
            ITEM_MAP.put(itemBean.getKey(), id);
            mScrollCont.addView(layout_unneed_select);
        }

        // 隐患
        LinearLayout layout_danger = null;
        if(itemBean.getHidden_danger_flag() !=null && itemBean.getHidden_danger_flag() == 1) {
            layout_danger = dangerMethodItem(view,itemBean);
            if(!TextUtils.isEmpty(defaultVal) && isDanger(itemBean,defaultVal) ){
                layout_danger.setVisibility(View.VISIBLE);
            } else {
                layout_danger.setVisibility(View.GONE);
            }
            mScrollCont.addView(layout_danger);
        }
        initOptionPicker(select_t,optionList,itemBean,layout_danger);
    }

    private void initDate(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean) throws JSONException {
        String option_str = itemBean.getItem_option();
        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
        TextView date_tx ;
        String defaultVal = "";
        if(itemBean.getRequired_flag() == 1) {
            // 必须 layout_need_date
            LinearLayout layout_need_date = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_need_date, null,false);
            RequiredTextView label_need = (RequiredTextView) layout_need_date.findViewById(R.id.label_date_need);
            date_tx = (TextView) layout_need_date.findViewById(R.id.date_need);
            label_need.setText(itemBean.getItem_name() + ":");
            date_tx.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请选择日期" : itemBean.getPlaceholder());
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                date_tx.setText(itemBean.getDefault_value());
                defaultVal = itemBean.getDefault_value();
            }
            int id = View.generateViewId();
            date_tx.setId(id);
            ITEM_MAP.put(itemBean.getKey(), id);

            mScrollCont.addView(layout_need_date);
        } else {
            // 非必须 layout_unneed_date
            LinearLayout layout_unneed_date = (LinearLayout)  LayoutInflater.from(this).inflate(R.layout.comp_unneed_date, null,false);
            TextView label_unneed = (TextView) layout_unneed_date.findViewById(R.id.label_date_unneed);
            date_tx = (TextView) layout_unneed_date.findViewById(R.id.date_unneed);
            label_unneed.setText(itemBean.getItem_name() + ":");
            date_tx.setHint(TextUtils.isEmpty(itemBean.getPlaceholder()) ? "请选择日期" : itemBean.getPlaceholder());
            if(!TextUtils.isEmpty(itemBean.getDefault_value())){
                date_tx.setText(itemBean.getDefault_value());
                defaultVal = itemBean.getDefault_value();
            }
            int id = View.generateViewId();
            date_tx.setId(id);
            ITEM_MAP.put(itemBean.getKey(), id);

            mScrollCont.addView(layout_unneed_date);
        }

        // 隐患
        LinearLayout layout_danger = null;
        if(itemBean.getHidden_danger_flag() !=null && itemBean.getHidden_danger_flag() == 1) {
            layout_danger = dangerMethodItem(view,itemBean);

            if(!TextUtils.isEmpty(defaultVal) && isDanger(itemBean,defaultVal) ){
                layout_danger.setVisibility(View.VISIBLE);
            } else {
                layout_danger.setVisibility(View.GONE);
            }
            mScrollCont.addView(layout_danger);
        }
        initDatePicker(date_tx,itemBean,layout_danger);
    }

    //初始化选择器
    private void initOptionPicker(TextView select,List<OptionBean> optionList,
                                  SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean,
                                  LinearLayout layout_danger) {
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
                select.setTag(val);
                if(layout_danger != null){
                    if(isDanger(itemBean,val) ){
                        layout_danger.setVisibility(View.VISIBLE);
                    } else {
                        layout_danger.setVisibility(View.GONE);
                    }
                }
            }
        })
                .setDecorView(activityRootview)//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                .setTitleText("")//标题文字
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
    private void initDatePicker(TextView dateView,
                                SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean,
                                LinearLayout layout_danger) {
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
                String val = DateTimeHelper.formatToString(date,"yyyy-MM-dd");
                dateView.setText(val);
                if(layout_danger != null){
                    if(isDanger(itemBean,val) ){
                        layout_danger.setVisibility(View.VISIBLE);
                    } else {
                        layout_danger.setVisibility(View.GONE);
                    }
                }
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
            openCamera(10);
        });
        tvChoosePicGallery.setOnClickListener(v -> {
            selectDialog.dismiss();
            // 打开相册
            openGallery(11);
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
        camera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.techen.smartgas.fileprovider", picture));
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
        if(requestCode == 0) {
            Toast.makeText(this, "权限获得", Toast.LENGTH_SHORT).show();
        } else if(requestCode > 0){
            if(curItemId > 0){ // 隐患
                generate_danger_img();
            } else{
                generate_img();
            }

            File file = null;
            if(requestCode == 10){//相机
                file = new File(imgPath);
                Glide.with(this).load(imgPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(curUploadImageView);
                compressImage(imgPath,curUploadImageView);
            }else if(requestCode == 11 && data != null){
                file = new File(UriToFile(data.getData()));
                compressImage(file.getPath(),curUploadImageView);
                Glide.with(this).load(data.getData()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(curUploadImageView);
            }

        }

    }

    /**
     * 压缩
     *
     * @param filePath 文件路径
     */
    private void compressImage(final String filePath,ImageView imageView) {
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                File file = null;
                Bitmap bitmap = ImageCompressUtils.compressImageFromFile(filePath, 1024f);// 按尺寸压缩图片
                if(bitmap == null){
                    file = new File(imgPath);
                }else{
                    int size = bitmap.getByteCount();
                    file = ImageCompressUtils.compressImage(bitmap);  //按质量压缩图片
//                File file = ImageUtils.bitmap2File(compressBitmap);
                    Long fileSize = FileUtils.getFileSize(file);
                }
                imgPath = "";
                if(file != null){
                    uploadImg(file,imageView);
                }
            }
        });
    }

    protected void uploadImg(File uploadFile,ImageView imageView){
        ArrayList<Map<String, Object>> curList = null;
        if(curItemId > 0){ // 隐患
            curList = dangerImgList.get(curItemId);
        } else{
            curList = imgList.get(curGroupId);
        }
        String url = HttpService.BASE_URL + "amiwatergas/mobile/fileUpload";
        Request request = ItheimaHttp.newUploadRequest(url, RequestMethod.POST);
        request.putHeader("Content-Type","multipart/form-data");
        request.putHeader("Authorization","Bearer " + Tool.getToken(SecurityAddActivity.this));
        request.putUploadFile("file",uploadFile).putMediaType(MediaType.parse("multipart/form-data"));
        ArrayList<Map<String, Object>> finalCurList = curList;
        ItheimaHttp.upload(request, new UploadListener() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) {
                if(response.body() != null){
                    FileUploadBean resultBean = null;
                    try {
                        resultBean = new Gson().fromJson(response.body().string(), FileUploadBean.class);
                        if(resultBean != null && resultBean.getCode() == 200){
                            Map<String, Object> img = new HashMap<>();
                            img.put("groupId", curGroupId);
                            img.put("photo_url",resultBean.getResult());
//                        Glide.with(SecurityAddActivity.this).load(HttpService.BASE_FILE_URL +  resultBean.getResult()).into(imageView);
                            finalCurList.add(img);
                        }else{

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onProgress(long l, long l1, boolean b) {

            }

            public void onFailure(okhttp3.Call call, IOException e) {
                System.out.println("出错了");
            }
        });
    }


    /**
     * todo  uri 转 file
     * @param uri
     * @return
     */
    public String UriToFile(Uri uri) {
        if(uri == null)
            return "";
        String[] filePc = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePc, null, null, null);
        cursor.moveToFirst();
        Log.i(TAG, "UriToFile: 22"+cursor);
        int col = cursor.getColumnIndex(filePc[0]);
        String pic = cursor.getString(col);
        cursor.close();
        return pic;
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