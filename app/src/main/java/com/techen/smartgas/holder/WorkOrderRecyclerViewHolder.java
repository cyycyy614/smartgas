package com.techen.smartgas.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techen.smartgas.R;
import com.techen.smartgas.model.WorkOrderBean;

import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;

import java.util.Random;

import butterknife.BindView;

public class WorkOrderRecyclerViewHolder extends BaseRecyclerViewHolder<WorkOrderBean.ResultBean.DataListBean> {


    @BindView(R.id.w_code)
    TextView wCode;
    @BindView(R.id.tag_1)
    Button tag1;
    @BindView(R.id.tag_2)
    Button tag2;
    @BindView(R.id.tag_3)
    Button tag3;
    @BindView(R.id.w_reporter)
    TextView wReporter;
    @BindView(R.id.w_reporter_mobile)
    TextView wReporterMobile;
    @BindView(R.id.w_report_time)
    TextView wReportTime;
    @BindView(R.id.w_source)
    TextView wSource;
    @BindView(R.id.w_repairer)
    TextView wRepairer;
    @BindView(R.id.w_repairer_time)
    TextView wRepairerTime;
    @BindView(R.id.w_repair_addr)
    TextView wRepairAddr;

    public WorkOrderRecyclerViewHolder(ViewGroup parentView, int itemResId) {
        super(parentView, itemResId);
    }


    /**
     * 绑定数据的方法，在mData获取数据（mData声明在基类中）
     */
    @Override
    public void onBindRealData() {
        if (mData.getState().equals("handling")) {
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
        } else if (mData.getState().equals("handled")){
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.GONE);
        } else{
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.VISIBLE);
        }

        wCode.setText(mData.getCode());
        wReportTime.setText(mData.getReport_time());
        wSource.setText(mData.getDisporder_source());
        wReporter.setText(mData.getReporter_name());
        wReporterMobile.setText(mData.getAccount_phone() + "");
        wRepairer.setText(mData.getWorker_name());
        wRepairerTime.setText(mData.getAppointment_time());
        wRepairAddr.setText(mData.getAccount_address());
    }


    /**
     * 给按钮添加点击事件（button改成你要添加点击事件的id）
     * @param v
     */
//        @OnClick(R.id.button)
//        public void click(View v) {
//        }
}
