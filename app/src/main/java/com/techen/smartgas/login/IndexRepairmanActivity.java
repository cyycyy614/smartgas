package com.techen.smartgas.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.OrderInfoBean;
import com.techen.smartgas.util.AutoUpdater;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.views.statistics.StatisticsActivity;
import com.techen.smartgas.views.workorder.WorkOrderAddActivity;
import com.techen.smartgas.views.workorder.WorkOrderDetailActivity;
import com.techen.smartgas.views.workorder.WorkOrderListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class IndexRepairmanActivity extends AppCompatActivity{
    private RecyclerView courseRV;
    //顶部图标
    private ImageView icon5;
    private ImageView icon6;
    private TextView handlingAmount;
    private TextView handledAmount;
    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "smartgas"), "smartgas");

    // Arraylist for storing data
    private ArrayList<CourseModel> courseModelArrayList;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            ButterKnife.bind(this);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.index2_main);
            //        获取上端跳转按钮
            icon5 = (ImageView) findViewById(R.id.icon5);
            icon6 = (ImageView) findViewById(R.id.icon6);

            handlingAmount= (TextView) findViewById(R.id.handlingAmount);
            handledAmount= (TextView) findViewById(R.id.handledAmount);
            handlingAmount.setText("0");
            handledAmount.setText("0");
            icon5.setOnClickListener(mListener);
            icon6.setOnClickListener(mListener);
            //  获取recyclerView
            courseRV = findViewById(R.id.index_Recycler);


            // here we have created new array list and added data to it.
            courseModelArrayList = new ArrayList<>();

            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions( new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },100 );
            }

            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions( new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },100 );
            }
            askForPermissions();
            checkUpdate();
            //测试 新增card数据
//            courseModelArrayList.add(new CourseModel("DSA in Java", "13011111111", "csscsccs","2022-05-08","2022-05-08","2022-05-08","2022-05-08"));
            //获取接口数据
            getData();
        }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon5:
                    goWorkOrderList();
                    //维修工单
                    break;
                case R.id.icon6:
                    goStatisticsList();
                    //统计分析
                    break;
            }
        }
    };


    public void getData(){
        RequestUtils request = new RequestUtils();
        Map<String,Object> getParams = new HashMap<>();
        request.get("amiwatergas/mobile/homePage/getWorkOrderInfo",getParams,true,IndexRepairmanActivity.this, new HttpResponseListener<OrderInfoBean>() {
            @Override
            public void onResponse(OrderInfoBean testBean, Headers headers) {
                if(testBean.getCode()==200){
                    OrderInfoBean.ResultBean resultBean =  testBean.getResult();
                    List<OrderInfoBean.ResultBean.OrderInfoListBean> orderInfoListBeanList =  resultBean.getOrderInfoList();
                    if(orderInfoListBeanList != null && orderInfoListBeanList.size() > 0){
                        // card内部循环赋值
                        for(OrderInfoBean.ResultBean.OrderInfoListBean item: resultBean.getOrderInfoList()){
                            courseModelArrayList.add(new CourseModel(item.getId(),item.getState(), item.getName(), item.getMobile(), item.getOrder_code(),item.getReport_time(),item.getAccount_address()
                                    ,item.getDisp_order_source(),item.getDisp_state()));
                        }
                        // we are initializing our adapter class and passing our arraylist to it.
                        CourseAdapter courseAdapter = new CourseAdapter(IndexRepairmanActivity.this, courseModelArrayList, new CourseAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(CourseModel item) {
                                Long id = item.getWork_id();
                                String stateCode = item.getState();
                                if(stateCode.equals("handling")){
                                    Intent intent = new Intent(IndexRepairmanActivity.this, WorkOrderAddActivity.class);
                                    intent.putExtra("id", id + "");
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(IndexRepairmanActivity.this, WorkOrderDetailActivity.class);
                                    intent.putExtra("id", id + "");
                                    startActivity(intent);
                                }
                            }
                        });

                        // below line is for setting a layout manager for our recycler view.
                        // here we are creating vertical list so we will provide orientation as vertical
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IndexRepairmanActivity.this, LinearLayoutManager.VERTICAL, false);

                        // in below two lines we are setting layoutmanager and adapter to our recycler view.
                        courseRV.setLayoutManager(linearLayoutManager);
                        courseRV.setAdapter(courseAdapter);
                    }
    //
                      try {
                          // 外部数据赋值
                          handlingAmount.setText(resultBean.getHandlingAmount() + "");
                          handledAmount.setText(resultBean.getHandledAmount() + "");
                      }catch (Exception ex){
                          Log.i("Index",ex.toString());
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
                System.out.println("print data -- " +  e);
//                Toast.makeText(IndexRepairmanActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
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
                    AutoUpdater manager = new AutoUpdater(IndexRepairmanActivity.this);
                    manager.CheckUpdate();
                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(this, permissions, 100);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(IndexRepairmanActivity.this, "自动更新异常：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                AutoUpdater manager = new AutoUpdater(IndexRepairmanActivity.this);
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



    private void goWorkOrderList(){
        // 跳转维修工单计划
        Intent intent = new Intent(IndexRepairmanActivity.this, WorkOrderListActivity.class);
//        intent.putExtra("repetition_flag", 1);
        startActivity(intent);
    }
    private void goStatisticsList(){
        // 跳转统计分析计划
        Intent intent = new Intent(IndexRepairmanActivity.this, StatisticsActivity.class);
//        intent.putExtra("repetition_flag", 1);
        startActivity(intent);
    }
}
