package com.techen.smartgas.holder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techen.smartgas.R;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.views.security.UserListFragment;

import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class UserListRecyclerViewHolder extends BaseRecyclerViewHolder<SecurityBean.ResultBean.ItemsBean> {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userno)
    TextView userno;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.userstatus)
    TextView userstatus;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_enter)
    Button btnEnter;

    //换成你布局文件中的id
    public UserListRecyclerViewHolder(ViewGroup parentView, int itemResId) {
        super(parentView, itemResId);
    }

    /**
     * 绑定数据的方法，在mData获取数据（mData声明在基类中）
     */
    @Override
    public void onBindRealData() {
        Random random_cus = new Random();
        Integer cindex = random_cus.nextInt(2);
        if (cindex == 0) {
            btnRecord.setVisibility(View.VISIBLE);
            btnEnter.setVisibility(View.GONE);
        } else if (cindex == 1) {
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.VISIBLE);
        }
        title.setText(mData.getName());
        address.setText("青岛市李沧区**街道**号春和景园1期3号楼1单元101户");
        mobile.setText("18955556666");
        userno.setText("10%");
        username.setText("正常");
        userstatus.setText("正常");
    }


    @OnClick(R.id.btn_enter)
    public void click(View v) {
//        Intent intent = new Intent(v.getContext(), UserListFragment.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);

    }
}
