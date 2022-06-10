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
import org.itheima.recycler.widget.PullToLoadMoreRecyclerView;

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
    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.btn_miss)
    Button btnMiss;
    PullToLoadMoreRecyclerView mpullToLoadMoreRecyclerView;
    //换成你布局文件中的id
    public UserListRecyclerViewHolder(ViewGroup parentView, int itemResId, PullToLoadMoreRecyclerView pullToLoadMoreRecyclerView) {
        super(parentView, itemResId);
        mpullToLoadMoreRecyclerView = pullToLoadMoreRecyclerView;
    }

    /**
     * 绑定数据的方法，在mData获取数据（mData声明在基类中）
     */
    @Override
    public void onBindRealData() {
        if (mData.getState().equals("normal") || mData.getState().equals("danger")) {
            btnRecord.setVisibility(View.VISIBLE);
            btnEnter.setVisibility(View.GONE);
            btnMiss.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        } else if (mData.getState().equals("undo")) {
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.VISIBLE);
            btnMiss.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
        } else{
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.GONE);
            btnMiss.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }
        if (mData.getState().equals("closed") || mData.getPlan_state().equals("closed")) {
            btnRecord.setVisibility(View.GONE);
            btnEnter.setVisibility(View.GONE);
            btnMiss.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }
        title.setText(mData.getCons_addr());
        address.setText(mData.getCons_addr());
        mobile.setText(mData.getCons_tel());
        userno.setText(mData.getCons_no());
        username.setText(mData.getCons_name());
        userstatus.setText(mData.getDispstate());
    }


    @OnClick(R.id.btn_enter)
    public void enterClick(View v) {
        Intent intent = new Intent(v.getContext(), SecurityAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("plan_id", mData.getPlan_id() + "");
        intent.putExtra("template_id", mData.getTemplate_id() + "");
        intent.putExtra("account_id", mData.getCons_id() + "");
        intent.putExtra("repetition_flag", 0);
        mContext.startActivity(intent);
    }
    @OnClick(R.id.btn_miss)
    public void missClick(View v) {
        handleUnNormalState("miss","到访不遇");
    }
    @OnClick(R.id.btn_reject)
    public void rejectClick(View v) {
        handleUnNormalState("reject","拒绝安检");
    }
    @OnClick(R.id.btn_record)
    public void recordClick(View v) {
        // 打开详情页面
        Intent intent = new Intent(v.getContext(), SecurityDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("template_id", mData.getTemplate_id() + "");
        intent.putExtra("record_id", mData.getRecord_id() + "");
        mContext.startActivity(intent);
    }

    private void handleUnNormalState(String code, String name) {
        RequestUtils request = new RequestUtils();
        Long plan_id = mData.getPlan_id();
        Long account_id = mData.getCons_id();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("planId", plan_id);
            paramObject.put("accountId", account_id);
            paramObject.put("repetitionFlag", 1);
            paramObject.put("state", code);
            paramObject.put("elecSignature", "");
            paramObject.put("description", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.post("amiwatergas/mobile/securityRecord/save", paramObject, true, mContext, new HttpResponseListener<NormalBean>() {
            @Override
            public void onResponse(NormalBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                mData.setState(code);
                mData.setDispstate(name);

            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " + e);
            }
        });
    }
}
