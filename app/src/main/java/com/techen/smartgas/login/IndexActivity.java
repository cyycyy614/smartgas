package com.techen.smartgas.login;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;

import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

import com.github.mikephil.charting.charts.PieChart;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.model.SecurityInfoBean;
import com.techen.smartgas.model.TestBean;
import com.techen.smartgas.util.AutoUpdater;
import com.techen.smartgas.util.DateTimeHelper;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.views.security.SecurityListActivity;
import com.techen.smartgas.views.security.UserListActivity;
import com.techen.smartgas.views.statistics.StatisticsActivity;
import com.techen.smartgas.views.workorder.WorkOrderListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {
    private PieChart chart;
    private PieChart chart2;
    private Button button;
    //顶部图标
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    //信息项
    private TextView planName;
    private TextView endDate;
    private TextView startDate;
    private TextView repetitionAccountAmount;
    private TextView dangerAccountAmount;
    private TextView totalAmount;
    private TextView inAmount;
    private Button dispState;

//复检信息项
    private TextView planName2;
    private TextView endDate2;
    private TextView startDate2;
    private TextView repetitionAccountAmount2;
    private TextView dangerAccountAmount2;
    private TextView totalAmount2;
    private TextView inAmount2;
    private Button dispState2;
    private CardView layoutPlan;
    private CardView layoutRePlan;

    SecurityInfoBean.ResultBean.LastSecurityPlanBean planBean;
    SecurityInfoBean.ResultBean.LastRepetitionPlanBean rePlanBean;
    private ArrayList<Integer> colors;
    private ArrayList<PieEntry> entries;
    private PieChartManager pieChartManager;
    private PieChartManager pieChartManager2;
    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "smartgas"), "smartgas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);
//        获得图表
        chart = (PieChart) findViewById(R.id.chart);
        chart2 = (PieChart) findViewById(R.id.chart2);


//        获取上端跳转按钮
        icon1 = (ImageView) findViewById(R.id.icon1);
        icon2 = (ImageView) findViewById(R.id.icon2);
        icon3 = (ImageView) findViewById(R.id.icon3);
        icon4 = (ImageView) findViewById(R.id.icon4);


        //获取信息项
        layoutPlan = (CardView) findViewById(R.id.layout_plan);
        planName = (TextView)findViewById(R.id.SecurityPlan_planName);
        endDate= (TextView)findViewById(R.id.SecurityPlan_endDate);
        startDate= (TextView)findViewById(R.id.SecurityPlan_startDate);
        repetitionAccountAmount= (TextView)findViewById(R.id.SecurityPlan_repetitionAccountAmount);
        dangerAccountAmount= (TextView)findViewById(R.id.SecurityPlan_dangerAccountAmount);
        totalAmount= (TextView)findViewById(R.id.SecurityPlan_totalAmount);
        inAmount= (TextView)findViewById(R.id.SecurityPlan_inAmount);
        dispState=(Button)findViewById(R.id.SecurityPlan_dispState) ;

        //获取复检信息项
        layoutRePlan = (CardView) findViewById(R.id.layout_replan);
        planName2 = (TextView)findViewById(R.id.RepetitionPlan_planName);
        endDate2= (TextView)findViewById(R.id.RepetitionPlan_endDate);
        startDate2= (TextView)findViewById(R.id.RepetitionPlan_startDate);
        repetitionAccountAmount2= (TextView)findViewById(R.id.RepetitionPlan_repetitionAccountAmount);
        dangerAccountAmount2= (TextView)findViewById(R.id.RepetitionPlan_dangerAccountAmount);
        totalAmount2= (TextView)findViewById(R.id.RepetitionPlan_totalAmount);
        inAmount2= (TextView)findViewById(R.id.RepetitionPlan_inAmount);
        dispState2=(Button)findViewById(R.id.RepetitionPlan_dispState) ;




        pieChartManager = new PieChartManager(chart, "");
        pieChartManager2 = new PieChartManager(chart2, "");
        ButterKnife.bind(this);
//        设置饼图的赋值
//        setPieChartData();
//        setPieChartData2();
        icon1.setOnClickListener(mListener);
        icon2.setOnClickListener(mListener);
        icon3.setOnClickListener(mListener);
        icon4.setOnClickListener(mListener);
        layoutPlan.setOnClickListener(mListener);
        layoutRePlan.setOnClickListener(mListener);

        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions( new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },100 );
        }

        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            requestPermissions( new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },100 );
        }
        askForPermissions();
//        获取数据安检员页面数据
        String Token = getToken();
        getData(Token);
        checkUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createDir();
            }
        }
    }

    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            createDir();
        }
    }

    public void createDir(){
        if (!dir.exists()){
            dir.mkdirs();
        }
    }

    private void checkUpdate(){
        //检查更新
        try {
            //6.0才用动态权限
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.INTERNET};
                List<String> permissionList = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }
                if (permissionList.size() <= 0) {
                    //说明权限都已经通过，可以做你想做的事情去
                    //自动更新
                    AutoUpdater manager = new AutoUpdater(IndexActivity.this);
                    manager.CheckUpdate();
                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(this, permissions, 100);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(IndexActivity.this, "自动更新异常：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean haspermission = false;
        if (100 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    haspermission = true;
                }
            }
            if (haspermission) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                permissionDialog();
            } else {
                //全部权限通过，可以进行下一步操作
                AutoUpdater manager = new AutoUpdater(IndexActivity.this);
                manager.CheckUpdate();
            }
        }
    }

    AlertDialog alertDialog;

    //打开手动设置应用权限
    private void permissionDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        alertDialog.show();
    }

    private void cancelPermissionDialog() {
        alertDialog.cancel();
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon1:
                    goSecurityList();
                    //安检计划列表
                    break;
                case R.id.icon2:
                    goSecurityRecheckList();
                    //复检计划列表
                    break;
                case R.id.icon3:
                    goWorkOrderList();
                    //维修工单
                    break;
                case R.id.icon4:
                    goStatisticsList();
                    //统计分析
                    break;
                case R.id.layout_plan:
                    if(planBean != null && planBean.getId() != null){
                       goUserList(planBean.getId() + "",0);
                    }
                    //下发
                    break;
                case R.id.layout_replan:
                    if(rePlanBean != null && rePlanBean.getId() != null){
                       goUserList(rePlanBean.getId() + "",1);
                    }
                    //下发
                    break;
            }
        }
    };

     private String getToken(){
         SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
         String token = preferences.getString("token", "获取token");//读取username
         return token;
     }


    //设置饼图  安检计划
    private void setPieChartData(SecurityInfoBean.ResultBean.LastSecurityPlanBean.RecordStateInfoBean recordStateInfoBean){
        colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#6AE5B0"));
        colors.add(Color.parseColor("#FD754B"));
        colors.add(Color.parseColor("#845FFD"));
        colors.add(Color.parseColor("#24C4D4"));
        colors.add(Color.parseColor("#FFB03F"));

        //模拟数据
        entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(recordStateInfoBean.getNormal(),"正常"));
        entries.add(new PieEntry(recordStateInfoBean.getUndo(),"待安检"));
        entries.add(new PieEntry(recordStateInfoBean.getReject(),"拒绝安检"));
        entries.add(new PieEntry(recordStateInfoBean.getDanger(),"隐患"));
        entries.add(new PieEntry(recordStateInfoBean.getMiss(),"到访不遇"));
        pieChartManager.showRingPieChart(entries, colors);
    }
    //设置饼图    复检计划
    private void setPieChartData2(SecurityInfoBean.ResultBean.LastRepetitionPlanBean.RecordStateInfoBean recordStateInfoBean2) {
        colors = new ArrayList<Integer>();
        colors.add(Color.parseColor("#6AE5B0"));
        colors.add(Color.parseColor("#FD754B"));
        colors.add(Color.parseColor("#845FFD"));
        colors.add(Color.parseColor("#24C4D4"));
        colors.add(Color.parseColor("#FFB03F"));

        //模拟数据
        entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(recordStateInfoBean2.getNormal(),"正常"));
        entries.add(new PieEntry(recordStateInfoBean2.getUndo(),"待安检"));
        entries.add(new PieEntry(recordStateInfoBean2.getReject(),"拒绝安检"));
        entries.add(new PieEntry(recordStateInfoBean2.getDanger(),"隐患"));
        entries.add(new PieEntry(recordStateInfoBean2.getMiss(),"到访不遇"));
        pieChartManager2.showRingPieChart(entries, colors);
    }

    //跳转方法
    private void goSecurityList(){
         // 跳转安检计划，增加参数为0
        Intent intent = new Intent(IndexActivity.this, SecurityListActivity.class);
        intent.putExtra("repetition_flag", 0);
        startActivity(intent);
    }
    private void goSecurityRecheckList(){
        // 跳转安检计划，增加参数为1
        Intent intent = new Intent(IndexActivity.this, SecurityListActivity.class);
        intent.putExtra("repetition_flag", 1);
        startActivity(intent);
    }
    private void goWorkOrderList(){
        // 跳转维修工单计划
        Intent intent = new Intent(IndexActivity.this, WorkOrderListActivity.class);
//        intent.putExtra("repetition_flag", 1);
        startActivity(intent);
    }
    private void goStatisticsList(){
        // 跳转统计分析计划
        Intent intent = new Intent(IndexActivity.this, StatisticsActivity.class);
//        intent.putExtra("repetition_flag", 1);
        startActivity(intent);
    }
    private void goUserList(String id,Integer repetition_flag){
        // 跳转用户列表
        Intent intent = new Intent(IndexActivity.this, UserListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("repetition_flag", repetition_flag);
        startActivity(intent);
    }

    public void getData(String Token){
        RequestUtils request = new RequestUtils();
        Map<String,Object> getParams = new HashMap<>();
        request.get("amiwatergas/mobile/homePage/getSecurityInfo",getParams,true,IndexActivity.this,new HttpResponseListener<SecurityInfoBean>() {
            @Override
            public void onResponse(SecurityInfoBean testBean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + testBean);
                if(testBean != null && testBean.getCode() == 200){
                    SecurityInfoBean.ResultBean.LastSecurityPlanBean lastSecurityPlanBean =  testBean.getResult().getLastSecurityPlan();
                    SecurityInfoBean.ResultBean.LastRepetitionPlanBean lastRepetitionPlanBean = testBean.getResult().getLastRepetitionPlan();
                    if(lastSecurityPlanBean != null){
                        // 安检计划基础数据赋值
                        planName.setText(lastSecurityPlanBean.getPlanName());
                        endDate.setText(DateTimeHelper.formatToString(lastSecurityPlanBean.getEndDate(),"yyyy-MM-dd"));
                        String startDateStr = lastSecurityPlanBean.getStartDate();
                        String formartStartDate = DateTimeHelper.formatToString(startDateStr,"yyyy-MM-dd");
                        startDate.setText(formartStartDate);
                        repetitionAccountAmount.setText(lastSecurityPlanBean.getRepetitionAccountAmount().toString());
                        dangerAccountAmount.setText(lastSecurityPlanBean.getDangerAccountAmount().toString());
                        totalAmount.setText(lastSecurityPlanBean.getTotalAmount().toString());
                        inAmount.setText(lastSecurityPlanBean.getInAmount().toString());
                        dispState.setText(lastSecurityPlanBean.getDispState());
                        if(lastSecurityPlanBean.getRecordStateInfo() != null){
                            SecurityInfoBean.ResultBean.LastSecurityPlanBean.RecordStateInfoBean recordStateInfoBean =   lastSecurityPlanBean.getRecordStateInfo();
                            setPieChartData(recordStateInfoBean);
                        }
                        dispState.setOnClickListener(mListener);
                        planBean = lastSecurityPlanBean;
                    }
                    //赋值方法
//                安检计划和复检计划赋值图表赋值
                    if(lastRepetitionPlanBean != null){
                        //复检计划基础数据赋值
                        planName2.setText(lastRepetitionPlanBean.getPlanName());
                        endDate2.setText(DateTimeHelper.formatToString(lastRepetitionPlanBean.getEndDate(),"yyyy-MM-dd"));
                        String startDateStr = lastRepetitionPlanBean.getStartDate();
                        String formartStartDate = DateTimeHelper.formatToString(startDateStr,"yyyy-MM-dd");
                        startDate2.setText(formartStartDate);
                        repetitionAccountAmount2.setText(lastRepetitionPlanBean.getRepetitionAccountAmount().toString());
                        dangerAccountAmount2.setText(lastRepetitionPlanBean.getDangerAccountAmount().toString());
                        totalAmount2.setText(lastRepetitionPlanBean.getTotalAmount().toString());
                        inAmount2.setText(lastRepetitionPlanBean.getInAmount().toString());
                        dispState2.setText(lastRepetitionPlanBean.getDispState());
                        if(lastRepetitionPlanBean.getRecordStateInfo() != null){
                            SecurityInfoBean.ResultBean.LastRepetitionPlanBean.RecordStateInfoBean recordStateInfoBean2 = lastRepetitionPlanBean.getRecordStateInfo();
                            setPieChartData2(recordStateInfoBean2);
                        }
                        dispState2.setOnClickListener(mListener);
                        rePlanBean = lastRepetitionPlanBean;
                    }

                }
            }
            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                Toast.makeText(IndexActivity.this, "获取数据出错了！", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
