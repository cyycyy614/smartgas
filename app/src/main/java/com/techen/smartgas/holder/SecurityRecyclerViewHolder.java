package com.techen.smartgas.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techen.smartgas.R;
import com.techen.smartgas.model.SecurityBean;

import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;

import butterknife.BindView;

public class SecurityRecyclerViewHolder  extends BaseRecyclerViewHolder<SecurityBean.ResultBean.ItemsBean> {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public SecurityRecyclerViewHolder(ViewGroup parentView, int itemResId) {
        super(parentView, itemResId);
    }

    /**
     * 绑定数据的方法，在mData获取数据（mData声明在基类中）
     */
    @Override
    public void onBindRealData() {
        tvTitle.setText(mData.getName());
    }


    /**
     * 给按钮添加点击事件（button改成你要添加点击事件的id）
     * @param v
     */
//        @OnClick(R.id.button)
//        public void click(View v) {
//        }
}
