package com.techen.smartgas.views.statistics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.login.LoginActivity;
import com.techen.smartgas.model.DangerBean;
import com.techen.smartgas.model.InDoorBean;
import com.techen.smartgas.model.OptionBean;
import com.techen.smartgas.model.SecurityDetailBean;
import com.techen.smartgas.model.SecurityResultBean;
import com.techen.smartgas.model.SecurityTempBean;
import com.techen.smartgas.model.StateBean;
import com.techen.smartgas.util.BarChartUtils;
import com.techen.smartgas.util.DateTimeHelper;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.PieChartUtils;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.SharedPreferencesUtil;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.views.security.SecurityAddActivity;
import com.techen.smartgas.views.security.SecurityConfirmActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class StatisticsActivity extends AppCompatActivity {
    @BindView(R.id.barChart)
    BarChart barChart;
    @BindView(R.id.pieChart)
    PieChart pieChart;
    @BindView(R.id.tv_danger_number)
    TextView dangerNumber;
    @BindView(R.id.startMonth)
    TextView startMonth;
    @BindView(R.id.endMonth)
    TextView endMonth;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.activity_rootview)
    RelativeLayout activityRootview;
    private List<BarEntry> netBarList = new ArrayList<>();
    private List<String> netDateList = new ArrayList<>();
    Date startMonthDate = new Date();
    Date endMonthDate = new Date();

    TimePickerView mStartDatePickerView;
    TimePickerView mEndDatePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);
        setTitle("统计分析");

        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        startMonth.setOnClickListener(mListener);
        btnSearch.setOnClickListener(mListener);
        initStartDatePicker(startMonth);
        initEndDatePicker(endMonth);
        show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show(){
        LoadingDialog.getInstance(StatisticsActivity.this).show();
        try {
            getInDoor();
            getDangerNumber();
            getState();
        }catch (Exception ex){
            LoadingDialog.getInstance(StatisticsActivity.this).hide();
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    if(valid()){
                        show();
                    }
                    break;
            }
        }
    };

    //初始化选择器
    private void initStartDatePicker(TextView dateView) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        selectedDate = formatMonthDatas(3);
        startMonthDate = selectedDate.getTime();
        dateView.setText(DateTimeHelper.formatToString(startMonthDate,"yyyy-MM"));
        //设置最小日期和最大日期
        Calendar startDate = Calendar.getInstance();
        try {
            startDate.setTime(DateTimeHelper.parseStringToDate("1970-01-01"));//设置为2006年4月28日
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endDate = Calendar.getInstance();//最大日期是今天

        //时间选择器
        mStartDatePickerView = new TimePickerBuilder(StatisticsActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                String val = DateTimeHelper.formatToString(date,"yyyy-MM");
                dateView.setText(val);
                startMonthDate = date;
            }
        })
                .setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
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
                .setRangDate(startDate, endDate)//设置最小和最大日期
                .setDate(selectedDate)//设置选中的日期
                .build();
        dateView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mStartDatePickerView.show();
            }
        });
    }


    //初始化选择器
    private void initEndDatePicker(TextView dateView) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        endMonthDate = selectedDate.getTime();
        dateView.setText(DateTimeHelper.formatToString(endMonthDate,"yyyy-MM"));
        //设置最小日期和最大日期
        Calendar startDate = Calendar.getInstance();
        try {
            startDate.setTime(DateTimeHelper.parseStringToDate("1970-01-01"));//设置为2006年4月28日
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar endDate = Calendar.getInstance();//最大日期是今天

        //时间选择器
        mEndDatePickerView = new TimePickerBuilder(StatisticsActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                String val = DateTimeHelper.formatToString(date,"yyyy-MM");
                dateView.setText(val);
                endMonthDate = date;
            }
        })
                .setDecorView((RelativeLayout)findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
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
                .setRangDate(startDate, endDate)//设置最小和最大日期
                .setDate(selectedDate)//设置选中的日期
                .build();
        dateView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mEndDatePickerView.show();
            }
        });
    }

    private Boolean valid(){
        if(startMonthDate.getTime() > endMonthDate.getTime()){
            Toast.makeText(StatisticsActivity.this, "请重新选择日期！", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    // 获取入户安检率
    private void getInDoor() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("startDate", startMonth.getText());
        getParams.put("endDate", endMonth.getText());
        request.get("amiwatergas/mobile/statistics/securityPlanIndoor", getParams, true, StatisticsActivity.this, new HttpResponseListener<InDoorBean>() {
            @Override
            public void onResponse(InDoorBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if(bean != null && bean.getCode() == 200){
                    //柱状图初始化
                    BarChartUtils.initChart(barChart, true, false, false);
                    List<InDoorBean.ResultBean> resultBeanList = bean.getResult();
                    setData(resultBeanList);
                } else{
                    BarChartUtils.NotShowNoDataText(barChart);
                }
                LoadingDialog.getInstance(StatisticsActivity.this).hide();
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                LoadingDialog.getInstance(StatisticsActivity.this).hide();
                System.out.println("print data -- " + e);
            }
        });
    }


    // 获取隐患数量
    private void getDangerNumber() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("startDate", startMonth.getText());
        getParams.put("endDate", endMonth.getText());
        request.get("amiwatergas/mobile/statistics/hiddenDangerAmount", getParams, true, StatisticsActivity.this, new HttpResponseListener<DangerBean>() {
            @Override
            public void onResponse(DangerBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if(bean != null && bean.getCode() == 200){
                    dangerNumber.setText(bean.getResult() + "");
                }
                LoadingDialog.getInstance(StatisticsActivity.this).hide();
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " + e);
                LoadingDialog.getInstance(StatisticsActivity.this).hide();
            }
        });
    }

    // 获取记录状态
    private void getState() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("startDate", startMonth.getText());
        getParams.put("endDate", endMonth.getText());
        request.get("amiwatergas/mobile/statistics/securityRecordState", getParams, true, StatisticsActivity.this, new HttpResponseListener<StateBean>() {
            @Override
            public void onResponse(StateBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if(bean != null && bean.getCode() == 200){
                    //柱状图初始化
                    PieChartUtils.initChart(pieChart, true, false, false);
                    // 饼图
                    StateBean.ResultBean resultBean = bean.getResult();
                    List<PieEntry> strings = new ArrayList<>();
                    strings.add(new PieEntry(resultBean.getNormal(),"正常"));
                    strings.add(new PieEntry(resultBean.getUndo(),"待安检"));
                    strings.add(new PieEntry(resultBean.getDanger(),"隐患"));
                    strings.add(new PieEntry(resultBean.getMiss(),"到访不遇"));
                    strings.add(new PieEntry(resultBean.getReject(),"拒绝安检"));
                    PieChartUtils.notifyDataSetChanged(pieChart,strings);
                } else{
                    PieChartUtils.NotShowNoDataText(pieChart);
                }
                LoadingDialog.getInstance(StatisticsActivity.this).hide();
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " + e);
                LoadingDialog.getInstance(StatisticsActivity.this).hide();
            }
        });
    }
    /**
     * 获取默认查询的时间(当前时间的前day天)
     */
    public static String formatDatas(int day) {
        Date dNow = new Date(); // 当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.setTime(dNow);// 把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -day); // 设置为前三天
        dBefore = calendar.getTime(); // 得到前三天的时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd"); // 设置时间格式
        String defaultStartDate = sdf.format(dBefore); // 格式化
        return defaultStartDate;

    }
    /**
     * 获取默认查询的时间(当前时间的前day天)
     */
    public static Calendar formatMonthDatas(int month) {
        Date dNow = new Date(); // 当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.setTime(dNow);// 把当前时间赋给日历
        calendar.add(Calendar.MONTH, -month); // 设置为前x月
        dBefore = calendar.getTime(); // 得到前三天的时间
        calendar.setTime(dBefore);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); // 设置时间格式
        String defaultStartDate = sdf.format(dBefore); // 格式化
        return calendar;

    }


    /**
     * new Entry(x,y) x: 折线图中数据值的位置索引 y: 具体数据值
     */
    private int xLableCount = 7;
    private int xRangeMaximum = xLableCount - 1;

    private void setData(List<InDoorBean.ResultBean> resultBeanList) {
        netBarList.clear();
        netDateList.clear();
        if(resultBeanList.size() == 0){
            //无数据
            BarChartUtils.NotShowNoDataText(barChart);
            return;
        }
        int i = 0;
        for(InDoorBean.ResultBean bean : resultBeanList){
            String planName = bean.getPlan_name();
            if(planName.length() > 10){
                planName = planName.substring(0,10);
            }
            netDateList.add(planName);
            netBarList.add(new BarEntry((float) i, (float)bean.getRate() * 100));
            i++;
        }

        xLableCount = (netDateList.size() + 3) > 7 ? 7 : (netDateList.size() + 3);
        xRangeMaximum = xLableCount - 1;

        BarChartUtils.setXAxis(barChart, xLableCount, netDateList.size(), xRangeMaximum);
        BarChartUtils.notifyDataSetChanged(barChart, netBarList, netDateList);

    }
}