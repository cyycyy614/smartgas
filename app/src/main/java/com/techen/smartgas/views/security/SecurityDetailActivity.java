package com.techen.smartgas.views.security;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.itheima.retrofitutils.ItheimaHttp;
import com.itheima.retrofitutils.Request;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.OptionBean;
import com.techen.smartgas.model.SecurityDangerBean;
import com.techen.smartgas.model.SecurityRecordDetailBean;
import com.techen.smartgas.model.SecurityTempBean;
import com.techen.smartgas.util.HttpService;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.Tool;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 页面：安检计划详情页面
 * 描述：显示安检详情
 */
public class SecurityDetailActivity extends AppCompatActivity {

    String template_id;
    String plan_id;
    String record_id;
    String account_id;
    SecurityTempBean tempBean;
    SecurityRecordDetailBean detailBean;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userno)
    TextView userno;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.d_state)
    TextView dState;
    @BindView(R.id.d_time)
    TextView dTime;
    @BindView(R.id.d_repetition)
    TextView dRepetition;
    @BindView(R.id.scrollvie_ll)
    LinearLayout dynamicCont;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_detail);
        ButterKnife.bind(this);
        setTitle("安检详情");

        // 接受参数
        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        template_id = intent.getStringExtra("template_id");
        record_id = intent.getStringExtra("record_id");
        account_id = intent.getStringExtra("account_id");

        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        LoadingDialog.getInstance(SecurityDetailActivity.this).show();
        // 调用初始化及接口
        getTempData();
    }

    private void getTempData() {

        //开始请求
        Request request = ItheimaHttp.newGetRequest("amiwatergas/mobile/securityTemplate/load");//apiUrl格式："xxx/xxxxx"
        Map<String, Object> map = new HashMap<>();
        map.put("id", template_id);

        request.putParamsMap(map);
        request.putHeader("Authorization", "Bearer " + Tool.getToken(SecurityDetailActivity.this));
        request.putHeader("lang", "en");
        Call call = ItheimaHttp.send(request, new HttpResponseListener<SecurityTempBean>() {

            @Override
            public void onResponse(SecurityTempBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if (bean != null && bean.getCode() == 200) {
                    tempBean = bean;
                    getDetail();
                } else {
                    LoadingDialog.getInstance(SecurityDetailActivity.this).hide();
                    Toast.makeText(SecurityDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                LoadingDialog.getInstance(SecurityDetailActivity.this).hide();
                Toast.makeText(SecurityDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDetail() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        String url = "";
        if(record_id.equals("-999")){//上次安检记录
            getParams.put("accountId", account_id);
            url = "amiwatergas/mobile/securityRecord/queryAccountSecondRecord";
        }else{
            getParams.put("id", record_id);
            url = "amiwatergas/mobile/securityRecord/querySecurityRecordDetail";
        }
        request.get(url, getParams, true, SecurityDetailActivity.this, new HttpResponseListener<SecurityRecordDetailBean>() {
            @Override
            public void onResponse(SecurityRecordDetailBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if (bean != null && bean.getCode() == 200) {
                    detailBean = bean;
                    show();
                }
                LoadingDialog.getInstance(SecurityDetailActivity.this).hide();
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                LoadingDialog.getInstance(SecurityDetailActivity.this).hide();
                System.out.println("print data -- " + e);
            }
        });
    }

    private void initUserDetail() {
        SecurityRecordDetailBean.ResultBean resultBean = detailBean.getResult();
        if(resultBean == null){
            return;
        }
        SecurityRecordDetailBean.ResultBean.AccountBean accountBean = resultBean.getAccount();
        if(accountBean == null){
            return;
        }
       title.setText(accountBean.getCons_addr());
       username.setText(accountBean.getCons_name());
       userno.setText(accountBean.getCons_no());
       mobile.setText(accountBean.getCons_tel());
    }
    private void initBasicDetail() {
        SecurityRecordDetailBean.ResultBean resultBean = detailBean.getResult();
        if(resultBean == null){
            return;
        }
        SecurityRecordDetailBean.ResultBean.AccountBean accountBean = resultBean.getAccount();
        if(accountBean == null){
            return;
        }
        dState.setText(resultBean.getDisp_state());
        dRepetition.setText(resultBean.getRepetition_flag() == 1 ? "需要" : "不需要");
        dTime.setText(resultBean.getOperate_time());
    }
    private void show() {
        initUserDetail();
        initBasicDetail();
        generate_Comp();
    }


    private void generate_Comp(){
        // 获取
        View view = LayoutInflater.from(this).inflate(R.layout.comp_title, null,false);
        for(SecurityTempBean.ResultBean.GroupInfoListBean groupInfoListBean: tempBean.getItemDatas()){
            String groupName = groupInfoListBean.getGroup_name();
            // 标题-- 管道
            generate_title(view,groupName);
            // 生成内容
            for(SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean:groupInfoListBean.getItemList()){
                if(isDanger(itemBean)){
                    generate_danger_cont(view,itemBean);
                }else{
                    generate_cont(view,itemBean);
                }
            }
            generate_camp(view,groupInfoListBean);
        }
    }

    private void generate_title(View view, String groupName){
        // 标题-- 管道
        LinearLayout linearLayout_group = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_title, (ViewGroup) view,false);
        TextView group_name = (TextView) linearLayout_group.findViewById(R.id.group_name);
        group_name.setText(groupName);
        dynamicCont.addView(linearLayout_group);
    }

    private void generate_cont(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        LinearLayout layout_detail = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_detail, (ViewGroup) view,false);
        TextView label_item = (TextView) layout_detail.findViewById(R.id.label_item);
        TextView item_val = (TextView) layout_detail.findViewById(R.id.item_val);
        label_item.setText(itemBean.getItem_name() + ":");
        String val = getValByItemBean(itemBean);
        item_val.setText(TextUtils.isEmpty(val) ? "" : val);
        dynamicCont.addView(layout_detail);
    }

    private void generate_camp(View view, SecurityTempBean.ResultBean.GroupInfoListBean groupBean){
        LinearLayout layout_camera = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_show_camera, (ViewGroup) view,false);
        FlowLayout flowlayout = (FlowLayout) layout_camera.findViewById(R.id.flowlayout);

        // 照片
        // 上传后显示的图片
        ArrayList sList = getPhotoListByGroupId(groupBean.getId());
        for(int i = 0; i < sList.size();i++){
            RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_show_img, flowlayout,false);
            ImageView img = (ImageView) relative_img.findViewById(R.id.upload_img);
            //设置网络图片
            Glide.with(this).load(HttpService.BASE_FILE_URL +  sList.get(i)).into(img);
            flowlayout.addView(relative_img);
        }
        if(sList.size() == 0){
            flowlayout.setVisibility(View.GONE);
        }else{
            flowlayout.setVisibility(View.VISIBLE);
        }
        dynamicCont.addView(layout_camera);
    }


    private void generate_danger_cont(View view, SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        LinearLayout layout_danger = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_detail_show_danger, (ViewGroup) view,false);
        TextView label_item = (TextView) layout_danger.findViewById(R.id.label_item);
        TextView item_val = (TextView) layout_danger.findViewById(R.id.item_val);
        TextView text_method = (TextView) layout_danger.findViewById(R.id.text_method);
        TextView select_report = (TextView) layout_danger.findViewById(R.id.select_report);
        EditText textarea = (EditText) layout_danger.findViewById(R.id.textarea);
        FlowLayout flowlayout = (FlowLayout) layout_danger.findViewById(R.id.flowlayout);
        label_item.setText(itemBean.getItem_name() + ":");
        String val = getValByItemBean(itemBean);
        item_val.setText(TextUtils.isEmpty(val) ? "" : val);
        SecurityRecordDetailBean.ResultBean.HiddenDangerInfoListBean dangerBean = getDangerByItemId(itemBean.getId());
        String report = "现场整改";
        if(dangerBean != null && dangerBean.getId() > 0){
            SecurityRecordDetailBean.ResultBean.WorkOrderListBean workOrderBean = getWorkOrderByDangerId(dangerBean.getId());
            if(workOrderBean != null && workOrderBean.getId() > 0) {
                report= "上报维修";
            }
            textarea.setText(dangerBean.getDescription());
            textarea.setFocusable(false);
            textarea.setFocusableInTouchMode(false);
        }
        // 整改部分
        text_method.setText(itemBean.getRectification_method());
        select_report.setText(report);
        // 照片
        // 上传后显示的图片
        if(dangerBean.getPhotolist() != null && dangerBean.getPhotolist().size() == 0){
            flowlayout.setVisibility(View.GONE);
        }else{
            flowlayout.setVisibility(View.VISIBLE);
        }
        for(SecurityRecordDetailBean.ResultBean.HiddenDangerInfoListBean.PhotolistBean imgListBean: dangerBean.getPhotolist()){
            RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_show_img, flowlayout,false);
            ImageView img = (ImageView) relative_img.findViewById(R.id.upload_img);
            //设置网络图片
            Glide.with(this).load(HttpService.BASE_FILE_URL + imgListBean.getPhoto_url()).into(img);
            flowlayout.addView(relative_img);
        }
        dynamicCont.addView(layout_danger);
    }

    private Boolean isDanger(SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean itemBean){
        Integer isDanger =  itemBean.getHidden_danger_flag() == null ? 0 : itemBean.getHidden_danger_flag();
        String dangerVal =  itemBean.getHidden_danger_value() == null ? "" : itemBean.getHidden_danger_value();
        String dangerJudge =  itemBean.getHidden_danger_judge() == null ? "" : itemBean.getHidden_danger_judge();
        String curVal = getValByItemId(itemBean.getId());
        try {
            if(isDanger == 1){
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
            }
        }catch (Exception e){

        }
        return false;
    }

    private String getValByItemId(Long itemid){
        String  val = "";
        List<SecurityRecordDetailBean.ResultBean.SecurityCheckItemValueListBean> checkItemList =  detailBean.getResult().getSecurityCheckItemValueList();
        for(SecurityRecordDetailBean.ResultBean.SecurityCheckItemValueListBean itemBean : checkItemList){
            if(itemBean.getItem_id().equals(itemid)){
                val = itemBean.getItem_value();
                continue;
            }
        }
        return val;
    }

    private String getValByItemBean(SecurityTempBean.ResultBean.GroupInfoListBean.ItemListBean bean){
        String  val = "";
        String  realVal = "";
        ArrayList types = new ArrayList();
        types.add("select");
        types.add("checkbox");
        types.add("radio");

        List<SecurityRecordDetailBean.ResultBean.SecurityCheckItemValueListBean> checkItemList =  detailBean.getResult().getSecurityCheckItemValueList();
        for(SecurityRecordDetailBean.ResultBean.SecurityCheckItemValueListBean itemBean : checkItemList){
            if(itemBean.getItem_id().equals(bean.getId())){
                val = itemBean.getItem_value();
                if(bean.getItem_type().equals("checkbox")){
                    String option_str = bean.getItem_option();
                    String show_str = "";
                    if(val != null && !TextUtils.isEmpty(val)){
                        List<String > valList =  Tool.toArray(val, String.class);
                        if(option_str != null){
                            List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
                            for(OptionBean optionBean : optionList){
                                if(valList.indexOf(optionBean.getValue().toString()) > -1){
                                    realVal = optionBean.getLabel();
                                    show_str += realVal + "、";
                                }
                            }
                            if(show_str.length() > 0){
                                show_str = show_str.substring(0,show_str.length() - 1);
                            }
                            return show_str;
                        }
                    }
                }else if(types.indexOf(bean.getItem_type()) > -1){
                    String option_str = bean.getItem_option();
                    if(option_str != null){
                        List<OptionBean> optionList = Tool.toArray(option_str, OptionBean.class);
                        for(OptionBean optionBean : optionList){
                            if(optionBean.getValue().equals(val)){
                                realVal = optionBean.getLabel();
                                return realVal;
                            }
                        }
                    }

                }
                continue;
            }
        }
        return val;
    }
    private ArrayList getPhotoListByGroupId(Long groupid){
        ArrayList pList = new ArrayList();
        List<SecurityRecordDetailBean.ResultBean.SecurityCheckRecordPhotoListBean> checkItemList =  detailBean.getResult().getSecurityCheckRecordPhotoList();
        for(SecurityRecordDetailBean.ResultBean.SecurityCheckRecordPhotoListBean itemBean : checkItemList){
            if(itemBean.getGroup_id().equals(groupid)){
                pList.add(itemBean.getPhoto_url());
                continue;
            }
        }
        return pList;
    }

    private SecurityRecordDetailBean.ResultBean.HiddenDangerInfoListBean getDangerByItemId(Long itemid){
        List<SecurityRecordDetailBean.ResultBean.HiddenDangerInfoListBean> dangerInfoList =  detailBean.getResult().getHiddenDangerInfoList();
        for(SecurityRecordDetailBean.ResultBean.HiddenDangerInfoListBean itemBean : dangerInfoList){
            if(itemBean.getItem_id().equals(itemid)){
               return itemBean;
            }
        }
        return  null;
    }

    private SecurityRecordDetailBean.ResultBean.WorkOrderListBean getWorkOrderByDangerId(Long dangerId){
        List<SecurityRecordDetailBean.ResultBean.WorkOrderListBean> dangerInfoList =  detailBean.getResult().getWorkOrderList();
        for(SecurityRecordDetailBean.ResultBean.WorkOrderListBean itemBean : dangerInfoList){
            if(itemBean.getDanger_id().equals(dangerId)){
               return itemBean;
            }
        }
        return  null;
    }
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


    @Override
    protected void onDestroy() {
        LoadingDialog.setInstance(null);
        super.onDestroy();
    }
}