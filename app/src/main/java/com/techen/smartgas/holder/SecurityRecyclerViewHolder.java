package com.techen.smartgas.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techen.smartgas.R;
import com.techen.smartgas.model.SecurityBean;

import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;

import java.util.Random;

import butterknife.BindView;

public class SecurityRecyclerViewHolder extends BaseRecyclerViewHolder<SecurityBean.ResultBean.DataListBean> {
    @BindView(R.id.st_title)
    TextView stTitle;
    @BindView(R.id.tag_1)
    Button tag1;
    @BindView(R.id.tag_2)
    Button tag2;
    @BindView(R.id.st_startdate)
    TextView stStartdate;
    @BindView(R.id.st_enddate)
    TextView stEnddate;
    @BindView(R.id.st_playcount)
    TextView stPlaycount;
    @BindView(R.id.st_securitycount)
    TextView stSecuritycount;
    @BindView(R.id.st_rate)
    TextView stRate;
    @BindView(R.id.st_state)
    TextView stState;

    //换成你布局文件中的id
    public SecurityRecyclerViewHolder(ViewGroup parentView, int itemResId) {
        super(parentView, itemResId);
    }

    /**
     * 绑定数据的方法，在mData获取数据（mData声明在基类中）
     */
    @Override
    public void onBindRealData() {
        if(mData.getState().equals("enable")) {
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.GONE);
        } else if(mData.getState().equals("closed")) {
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.VISIBLE);
        }
        String startDate = mData.getStart_date();
        startDate = TextUtils.isEmpty(startDate) ? "" : startDate.replace(" 00:00:00","");
        String endDate = mData.getEnd_date();
        endDate = TextUtils.isEmpty(endDate) ? "" : endDate.replace(" 00:00:00","");
        stTitle.setText(mData.getPlan_name());
        stStartdate.setText(startDate);
        stEnddate.setText(endDate);
        stPlaycount.setText(mData.getTotal_amount() + "");
        stSecuritycount.setText(mData.getIn_amount() + "");
        stRate.setText(mData.getRate());
        stState.setText(mData.getDispstate());
    }


    /**
     * 给按钮添加点击事件（button改成你要添加点击事件的id）
     * @param v
     */
//        @OnClick(R.id.button)
//        public void click(View v) {
//        }
}
