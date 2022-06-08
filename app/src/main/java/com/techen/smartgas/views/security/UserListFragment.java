package com.techen.smartgas.views.security;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.itheima.retrofitutils.listener.HttpResponseListener;
import com.techen.smartgas.R;
import com.techen.smartgas.model.NormalBean;
import com.techen.smartgas.model.SecurityDetailBean;
import com.techen.smartgas.model.SecurityUserBean;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.RequestUtils;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.widget.IosPopupWindow;

import org.itheima.recycler.L;
import org.itheima.recycler.adapter.BaseLoadMoreRecyclerAdapter;
import org.itheima.recycler.header.RecyclerViewHeader;
import org.itheima.recycler.listener.ItemClickSupport;
import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;
import org.itheima.recycler.widget.ItheimaRecyclerView;
import org.itheima.recycler.widget.PullToLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment {

    private static final String EXTRA_CODE = "code";
    private static final String EXTRA_FLAG = "flag";
    private static final String EXTRA_SID = "securityId";

    @BindView(R.id.plan_address)
    TextView planAddress;
    @BindView(R.id.plan_state)
    TextView planState;
    @BindView(R.id.plan_date)
    TextView planDate;
    @BindView(R.id.layout_nodata)
    LinearLayout layout_NoData;
    private String TAG = "USERLIST";
//    private AlertView mAlertView;

    BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder;
    static PullToLoadMoreRecyclerView pullToLoadMoreRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout myswipeRefreshLayout;
    ItheimaRecyclerView myrecyclerView;
    @BindView(R.id.recycler_header)
    RecyclerViewHeader myrecyclerHeader;

    String handle;
    Integer pageIndex = 1;
    private int state = 0;
    private static final int STATE_FRESH = 1;
    private static final int STATE_MORE = 2;
    View contentView;
    ArrayList<SecurityUserBean.ResultBean.DataListBean> itemsBeanList = new ArrayList<>();
    private Unbinder unbinder;
    private String code;
    private String securityId;
    private String type;
    int mPosition = 0;
    private Integer repetition_flag = 0 ;

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(String securityId, String code, Integer flag) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_CODE, code);
        arguments.putString(EXTRA_SID, securityId);
        arguments.putInt(EXTRA_FLAG, flag);
        UserListFragment userListFragment = new UserListFragment();
        userListFragment.setArguments(arguments);
        return userListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString(EXTRA_CODE);
            securityId = getArguments().getString(EXTRA_SID);
            repetition_flag = getArguments().getInt(EXTRA_FLAG, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        myrecyclerView = contentView.findViewById(R.id.recycler_view);
        LoadingDialog.getInstance(contentView.getContext()).show();
        list();
        getPlanDetail();
        return contentView;
    }

    // 获取安检计划详情
    private void getPlanDetail() {
        RequestUtils request = new RequestUtils();
        // get请求
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", securityId);
        request.get("amiwatergas/mobile/securityPlan/qryDetails", getParams, true, contentView.getContext(), new HttpResponseListener<SecurityDetailBean>() {
            @Override
            public void onResponse(SecurityDetailBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                if(bean != null && bean.getCode() == 200){
                    String startDate = bean.getResult().getStart_date();
                    String endDate = bean.getResult().getEnd_date();
                    String date = "";
                    if(!TextUtils.isEmpty(startDate) && startDate.length() > 10){
                        date = startDate.replace(" 00:00:00","") + " 至 ";
                    }
                    if(!TextUtils.isEmpty(endDate) && endDate.length() > 10){
                        date += endDate.replace(" 00:00:00","");
                    }
                    planAddress.setText(bean.getResult().getPlan_name());
                    planDate.setText(date);
                    planState.setText(bean.getResult().getDispstate());
                }else{
                    LoadingDialog.getInstance(contentView.getContext()).hide();
                }
            }

            /**
             * 可以不重写失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                System.out.println("print data -- " + e);
                LoadingDialog.getInstance(contentView.getContext()).hide();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadingDialog.setInstance(null);
        unbinder.unbind();
    }

    private void list() {
        myrecyclerHeader.attachTo(myrecyclerView);

        ItemClickSupport itemClickSupport = new ItemClickSupport(myrecyclerView);
        //点击事件
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SecurityUserBean.ResultBean.DataListBean mData = itemsBeanList.get(position);
                mPosition = position;
                String plan_state = mData.getPlan_state();
                if(plan_state != null && !plan_state.equals("closed")) {
                    handleItemClick(mData, position);
                }else if(plan_state != null && plan_state.equals("closed")){
                    Toast.makeText(contentView.getContext(), "该计划已经关闭！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pullToLoadMoreRecyclerView = new PullToLoadMoreRecyclerView<SecurityUserBean>(myswipeRefreshLayout, myrecyclerView, UserListRecyclerViewHolder.class) {
            @Override
            public int getItemResId() {
                //recylerview item资源id
                return R.layout.item_user;
            }

            @Override
            public String getApi() {
                switch (state) {
                    case STATE_FRESH:
                        pageIndex = 1;
                        break;
                    case STATE_MORE:
                        pageIndex++;
                        break;
                }
                //接口
                return "amiwatergas/mobile/securityPlan/qryConsList?currentPage=" + pageIndex;
            }

            //是否加载更多的数据，根据业务逻辑自行判断，true表示有更多的数据，false表示没有更多的数据，如果不需要监听可以不重写该方法
            @Override
            public boolean isMoreData(BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder1) {
                System.out.println("isMoreData---------------------" + holder1);
                holder = holder1;
                state = STATE_MORE;
                return true;
            }
        };

        pullToLoadMoreRecyclerView.setLoadingDataListener(new PullToLoadMoreRecyclerView.LoadingDataListener<SecurityUserBean>() {

            @Override
            public void onRefresh() {
                //监听下啦刷新，如果不需要监听可以不重新该方法
                L.i("setLoadingDataListener onRefresh");
                state = STATE_FRESH;
                itemsBeanList = new ArrayList<>();
            }

            @Override
            public void onStart() {
                //监听http请求开始，如果不需要监听可以不重新该方法
                L.i("setLoadingDataListener onStart");
            }

            @Override
            public void onSuccess(SecurityUserBean o, Headers headers) {
                //监听http请求成功，如果不需要监听可以不重新该方法

                L.i("setLoadingDataListener onSuccess: " + o);
                List<SecurityUserBean.ResultBean.DataListBean> itemDatas = o.getItemDatas();
                if (itemDatas == null || itemDatas.size() == 0) {
                    if(holder != null){
                        holder.loadingFinish((String) null);
                    }
                    if (myswipeRefreshLayout != null) {
                        myswipeRefreshLayout.setRefreshing(false);
                    }
                    if(itemsBeanList.size() == 0){
                        layout_NoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (SecurityUserBean.ResultBean.DataListBean item : itemDatas) {
                        itemsBeanList.add(item);
                    }
                }
                if(itemsBeanList.size() > 0){
                    layout_NoData.setVisibility(View.GONE);
                }
                LoadingDialog.getInstance(contentView.getContext()).hide();
            }

            @Override
            public void onFailure() {
                //监听http请求失败，如果不需要监听可以不重新该方法
                LoadingDialog.getInstance(contentView.getContext()).hide();
                if(itemsBeanList.size() == 0){
                    layout_NoData.setVisibility(View.VISIBLE);
                }
                L.i("setLoadingDataListener onFailure");
            }
        });
        pullToLoadMoreRecyclerView.setPageSize(10);
        //添加头
        pullToLoadMoreRecyclerView.putHeader("Authorization", "Bearer " + Tool.getToken(contentView.getContext()));
        pullToLoadMoreRecyclerView.putHeader("lang", "en");
        //添加请求参数
        pullToLoadMoreRecyclerView.putParam("id", securityId);
        pullToLoadMoreRecyclerView.putParam("security_record_state", code);
        pullToLoadMoreRecyclerView.putParam("sidx", "area_name");
        pullToLoadMoreRecyclerView.putParam("sord", "desc");
        pullToLoadMoreRecyclerView.putParam("currentPage", pageIndex);
        pullToLoadMoreRecyclerView.putParam("pageSize", 10);
        pullToLoadMoreRecyclerView.requestData();
    }

    private List<Map<String, Object>> initDatas() {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        Map map1 = new HashMap();
        map1.put("img", "");
        map1.put("name", "待安检");
        map1.put("code", "undo");
//        Map map2 = new HashMap();
//        map2.put("img", "");
////        map2.put("img",R.mipmap.icon_correct);
//        map2.put("name", "正常");
//        map2.put("code", "normal");
        Map map3 = new HashMap();
        map3.put("img", "");
        map3.put("name", "到访不遇");
        map3.put("code", "miss");
        Map map4 = new HashMap();
        map4.put("img", "");
        map4.put("name", "拒绝安检");
        map4.put("code", "reject");
        datas.add(map1);
//        datas.add(map2);
        datas.add(map3);
        datas.add(map4);
        return datas;
    }

    private void handleItemClick(SecurityUserBean.ResultBean.DataListBean mData,int position) {
        String plan_id = securityId;
        Long template_id = mData.getTemplate_id();
        Long account_id = mData.getCons_id();
        Long record_id = mData.getRecord_id();
        String state = mData.getState();
        //
        if(state.equals("normal") || state.equals("danger")){
            // 打开详情页面
            Intent intent = new Intent(contentView.getContext(), SecurityDetailActivity.class);
            intent.putExtra("template_id", template_id + "");
            intent.putExtra("record_id", record_id + "");
            startActivity(intent);
        }else if(state.equals("undo")){
            // 打开状态弹窗
            openWindow(mData,plan_id,template_id,account_id);
        }
    }

    private void openWindow(SecurityUserBean.ResultBean.DataListBean mData,String plan_id,Long template_id,Long account_id){
        List<Map<String, Object>> datas = initDatas();

        IosPopupWindow mPopupWindow = new IosPopupWindow(getActivity(), datas, new IosPopupWindow.OnClickListener() {
            @Override
            public void onItemClick(Object o, int position, Map<String, Object> data) {
                //弹出alipay码
                Integer id = (Integer) data.get("id");
                String code = (String) data.get("code");
                String name = (String) data.get("name");
                if (code == "undo") {//待安检
                    Log.i(TAG, "弹框位置：" + data);
                    Intent intent = new Intent(contentView.getContext(), SecurityAddActivity.class);
                    intent.putExtra("plan_id", plan_id);
                    intent.putExtra("template_id", template_id + "");
                    intent.putExtra("account_id", account_id + "");
                    intent.putExtra("code", code);
                    intent.putExtra("repetition_flag", repetition_flag);
                    startActivity(intent);
                } else {
                    handleUnNormalState(mData, code, name);
                }
                Toast.makeText(contentView.getContext(), "当前用户状态：" + data.get("name").toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
                //取消

            }
        });

        mPopupWindow.show(contentView);
    }

    // 非正常时调动接口保存
    private void handleUnNormalState(SecurityUserBean.ResultBean.DataListBean mData, String code, String name) {
        RequestUtils request = new RequestUtils();
        String plan_id = securityId;
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

        request.post("amiwatergas/mobile/securityRecord/save", paramObject, true, contentView.getContext(), new HttpResponseListener<NormalBean>() {
            @Override
            public void onResponse(NormalBean bean, Headers headers) {
                System.out.println("print data");
                System.out.println("print data -- " + bean);
                mData.setState(code);
                mData.setDispstate(name);
                itemsBeanList.get(mPosition).setState(code);
                itemsBeanList.get(mPosition).setDispstate(name);
                state = STATE_FRESH;
                pullToLoadMoreRecyclerView.onRefresh();
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

    public static class UserListRecyclerViewHolder extends BaseRecyclerViewHolder<SecurityUserBean.ResultBean.DataListBean> {
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
            if (mData.getState().equals("closed")) {
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
                    pullToLoadMoreRecyclerView.onRefresh();
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
}