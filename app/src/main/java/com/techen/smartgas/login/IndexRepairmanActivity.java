package com.techen.smartgas.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.OrderInfoBean;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.views.statistics.StatisticsActivity;
import com.techen.smartgas.views.workorder.WorkOrderListActivity;

import java.util.ArrayList;
import java.util.HashMap;
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

            icon5.setOnClickListener(mListener);
            icon6.setOnClickListener(mListener);
            //  获取recyclerView
            courseRV = findViewById(R.id.index_Recycler);


            // here we have created new array list and added data to it.
            courseModelArrayList = new ArrayList<>();
            //测试 新增card数据
//            courseModelArrayList.add(new CourseModel("DSA in Java", "13011111111", "csscsccs","2022-05-08","2022-05-08","2022-05-08","2022-05-08"));
            //获取接口数据
            getData();

            // we are initializing our adapter class and passing our arraylist to it.
            CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);

            // below line is for setting a layout manager for our recycler view.
            // here we are creating vertical list so we will provide orientation as vertical
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            courseRV.setLayoutManager(linearLayoutManager);
            courseRV.setAdapter(courseAdapter);

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
    //                card内部循环赋值
                      for(OrderInfoBean.ResultBean.OrderInfoListBean item: testBean.getResult().getOrderInfoList()){
                         courseModelArrayList.add(new CourseModel(item.getName(), item.getMobile(), item.getOrder_code(),item.getReport_time(),item.getAccount_address()
                         ,item.getOrder_source(),item.getDisp_state()));
                      }
    //                外部数据赋值
                         handlingAmount.setText(testBean.getResult().getHandlingAmount());
                         handledAmount.setText(testBean.getResult().getHandledAmount());
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
