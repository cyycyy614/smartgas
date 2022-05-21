package com.techen.smartgas.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techen.smartgas.R;
import com.techen.smartgas.model.SecurityBean;

import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;

import java.util.Random;

import butterknife.BindView;

public class SecurityRecyclerViewHolder extends BaseRecyclerViewHolder<SecurityBean.ResultBean.ItemsBean> {
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
        Random random_cus = new Random();
        Integer cindex =  random_cus.nextInt(2);
        if(cindex == 0 ) {
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.GONE);
        } else if(cindex == 1 ) {
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.VISIBLE);
        }
        stTitle.setText(mData.getName());
        stStartdate.setText("2022-10-10");
        stEnddate.setText("2022-10-10");
        stPlaycount.setText("10");
        stSecuritycount.setText("10");
        stRate.setText("10%");
        stState.setText("正常");
    }


    /**
     * 给按钮添加点击事件（button改成你要添加点击事件的id）
     * @param v
     */
//        @OnClick(R.id.button)
//        public void click(View v) {
//        }
}
