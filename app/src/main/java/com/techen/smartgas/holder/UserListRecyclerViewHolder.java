package com.techen.smartgas.holder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.NormalBean;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.model.SecurityUserBean;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.views.security.SecurityAddActivity;
import com.techen.smartgas.views.security.SecurityDetailActivity;
import com.techen.smartgas.views.security.UserListFragment;
import com.techen.smartgas.widget.IosPopupWindow;

import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserListRecyclerViewHolder extends BaseRecyclerViewHolder<SecurityUserBean.ResultBean.DataListBean> {
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
        if (mData.getState().equals("normal") || mData.getState().equals("danger")) {
            btnRecord.setVisibility(View.VISIBLE);
            btnEnter.setVisibility(View.GONE);
        } else if (mData.getState().equals("undo")) {
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.VISIBLE);
        } else{
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.GONE);
        }
        if (mData.getState().equals("closed")) {
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.GONE);
        }
        title.setText(mData.getCons_addr());
        address.setText(mData.getCons_addr());
        mobile.setText(mData.getCons_tel());
        userno.setText(mData.getCons_no());
        username.setText(mData.getCons_name());
        userstatus.setText(mData.getDispstate());
    }


    @OnClick(R.id.btn_enter)
    public void click(View v) {

    }

}
