package com.techen.smartgas.views.workorder;

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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.SecurityTempBean;
import com.techen.smartgas.model.WorkOrderDetailBean;
import com.techen.smartgas.util.HttpService;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.RequestUtils;

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

public class WorkOrderDetailActivity extends AppCompatActivity {

    String workId = "";
    @BindView(R.id.w_username)
    TextView wUsername;
    @BindView(R.id.tag_handled)
    Button tagHandled;
    @BindView(R.id.tag_unhandle)
    Button tagUnhandle;
    @BindView(R.id.tag_closed)
    Button tagClosed;
    @BindView(R.id.w_code)
    TextView wCode;
    @BindView(R.id.w_source)
    TextView wSource;
    @BindView(R.id.w_worker)
    TextView wWorker;
    @BindView(R.id.w_report_time)
    TextView wReportTime;
    @BindView(R.id.w_reporter_mobile)
    TextView wReporterMobile;
    @BindView(R.id.w_order_time)
    TextView wOrderTime;
    @BindView(R.id.w_addr)
    TextView wAddr;
    @BindView(R.id.layout_work_cont)
    LinearLayout layoutWorkCont;
    @BindView(R.id.textarea)
    TextView textarea;
    @BindView(R.id.activity_rootview)
    ScrollView activity_rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_detail);
        ButterKnife.bind(this);
        setTitle("工单详情");

        //接受参数
        Intent intent = getIntent();
        workId = intent.getStringExtra("id");

        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        LoadingDialog.getInstance(WorkOrderDetailActivity.this).show();
        this.getDetail();
    }

    private void getDetail() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        String url = "";
        getParams.put("id", workId);
        url = "amiwatergas/mobile/workOrder/order_details";
        request.get(url, getParams, true, WorkOrderDetailActivity.this, new HttpResponseListener<WorkOrderDetailBean>() {
            @Override
            public void onResponse(WorkOrderDetailBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if (bean != null && bean.getCode() == 200) {
                    WorkOrderDetailBean.ResultBean resultBean = bean.getResult();
                    show(resultBean);
                    LoadingDialog.getInstance(WorkOrderDetailActivity.this).hide();
                }else{
                    LoadingDialog.getInstance(WorkOrderDetailActivity.this).hide();
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
                LoadingDialog.getInstance(WorkOrderDetailActivity.this).hide();
            }
        });
    }

    private void show(WorkOrderDetailBean.ResultBean resultBean) {
        if (resultBean != null) {
            if (resultBean.getState().equals("handling")) {
                tagHandled.setVisibility(View.GONE);
                tagUnhandle.setVisibility(View.VISIBLE);
                tagClosed.setVisibility(View.GONE);
            } else if (resultBean.getState().equals("handled")){
                tagHandled.setVisibility(View.VISIBLE);
                tagUnhandle.setVisibility(View.GONE);
                tagClosed.setVisibility(View.GONE);
            } else{
                tagHandled.setVisibility(View.GONE);
                tagUnhandle.setVisibility(View.GONE);
                tagClosed.setVisibility(View.VISIBLE);
            }
        }
        wCode.setText(resultBean.getOrder_code());
        wAddr.setText(resultBean.getAccount_address());
        String orderTime = resultBean.getRepair_time();
        orderTime = TextUtils.isEmpty(orderTime) ? "" : orderTime;
        wOrderTime.setText(orderTime);
        wWorker.setText(TextUtils.isEmpty(resultBean.getWorker_name())?"":resultBean.getWorker_name());
        wUsername.setText(resultBean.getReporter_name() + "");
        wReporterMobile.setText(resultBean.getAccount_phone() + "");
        wReportTime.setText(resultBean.getReport_time() + "");
        textarea.setText(resultBean.getWork_content() + "");
        generate_camp(findViewById(R.id.activity_rootview), resultBean.getWorkInfo());
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

    private void generate_camp(View view, List<WorkOrderDetailBean.ResultBean.WorkInfoBean> workInfoBeanList){
        // 照片
        // 上传后显示的图片
        for(WorkOrderDetailBean.ResultBean.WorkInfoBean workInfoBean : workInfoBeanList){
            LinearLayout layout_camera = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.comp_show_camera, (ViewGroup) view,false);
            TextView label = (TextView) layout_camera.findViewById(R.id.label);
            FlowLayout flowlayout = (FlowLayout) layout_camera.findViewById(R.id.flowlayout);

            label.setText(TextUtils.isEmpty(workInfoBean.getRepair_content())?"":workInfoBean.getRepair_content());
            if(!TextUtils.isEmpty( workInfoBean.getRepairUrl())){
                 String[] sList =  workInfoBean.getRepairUrl().split(",");
                for(int i = 0; i < sList.length;i++) {
                    RelativeLayout relative_img = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.comp_camera_show_img, flowlayout,false);
                    ImageView img = (ImageView) relative_img.findViewById(R.id.upload_img);
                    //设置网络图片
                    Glide.with(this).load(HttpService.BASE_FILE_URL + sList[i]).into(img);
                    flowlayout.addView(relative_img);
                }
            }
            layoutWorkCont.addView(layout_camera);
        }
    }
}