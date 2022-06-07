package com.techen.smartgas.views.security;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.techen.smartgas.R;
import com.techen.smartgas.holder.SecurityRecyclerViewHolder;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.SharedPreferencesUtil;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.views.workorder.WorkOrderDetailActivity;

import org.itheima.recycler.L;
import org.itheima.recycler.adapter.BaseLoadMoreRecyclerAdapter;
import org.itheima.recycler.listener.ItemClickSupport;
import org.itheima.recycler.widget.ItheimaRecyclerView;
import org.itheima.recycler.widget.PullToLoadMoreRecyclerView;
import org.itheima.recycler.widget.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecurityItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_FLAG = "flag";
    private String TAG = "SECURITY";
    private Integer repetition_flag = 0 ;
    BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder;
    PullToLoadMoreRecyclerView pullToLoadMoreRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout myswipeRefreshLayout;
    @BindView(R.id.layout_nodata)
    LinearLayout layout_NoData;
    ItheimaRecyclerView myrecyclerView;
    @BindView(R.id.recycler_header)
    org.itheima.recycler.header.RecyclerViewHeader myrecyclerHeader;


    String handle;
    Integer pageIndex = 1;
    private int state = 0;
    private static final int STATE_FRESH = 1;
    private static final int STATE_MORE = 2;
    View contentView;
    ArrayList<SecurityBean.ResultBean.DataListBean> itemsBeanList = new ArrayList<>();
    private Unbinder unbinder;
    // TODO: Rename and change types of parameters
    private String type;

    public SecurityItemFragment() {
        // Required empty public constructor
    }

    public static SecurityItemFragment newInstance(String type,Integer flag) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_TYPE, type);
        arguments.putInt(EXTRA_FLAG, flag);
        SecurityItemFragment securityItemFragment = new SecurityItemFragment();
        securityItemFragment.setArguments(arguments);
        return securityItemFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(EXTRA_TYPE);
            repetition_flag = getArguments().getInt(EXTRA_FLAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_security_item, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        myrecyclerView = contentView.findViewById(R.id.recycler_view);
        LoadingDialog.getInstance(contentView.getContext()).show();
        list();
        return contentView;
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
                Long id = itemsBeanList.get(position).getId();
                String stateCode = itemsBeanList.get(position).getState();
                Intent intent = new Intent(contentView.getContext(), UserListActivity.class);
                intent.putExtra("id", id+"");
                intent.putExtra("repetition_flag", repetition_flag);
                intent.putExtra("code", stateCode);
                startActivity(intent);
            }
        });

        pullToLoadMoreRecyclerView = new PullToLoadMoreRecyclerView<SecurityBean>(myswipeRefreshLayout, myrecyclerView, SecurityRecyclerViewHolder.class) {
            @Override
            public int getItemResId() {
                //recylerview item资源id
                return R.layout.item_security;
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
                Log.i(TAG, "type is " + type);
                return "amiwatergas/mobile/securityPlan/qryList?currentPage=" + pageIndex;
            }

            //            //是否加载更多的数据，根据业务逻辑自行判断，true表示有更多的数据，false表示没有更多的数据，如果不需要监听可以不重写该方法
            @Override
            public boolean isMoreData(BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder1) {
                System.out.println("isMoreData---------------------" + holder1);
                holder = holder1;
                state = STATE_MORE;
                return true;
            }
        };

        pullToLoadMoreRecyclerView.setLoadingDataListener(new PullToLoadMoreRecyclerView.LoadingDataListener<SecurityBean>() {

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
            public void onSuccess(SecurityBean o, Headers headers) {
                //监听http请求成功，如果不需要监听可以不重新该方法
                L.i("setLoadingDataListener onSuccess: " + o);
                List<SecurityBean.ResultBean.DataListBean> itemDatas = o.getItemDatas();
                if (itemDatas.size() == 0) {
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
                    for (SecurityBean.ResultBean.DataListBean item : itemDatas) {
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
        pullToLoadMoreRecyclerView.putHeader("Authorization","Bearer " + Tool.getToken(contentView.getContext()));
        pullToLoadMoreRecyclerView.putHeader("lang","en");
        //添加请求参数
        pullToLoadMoreRecyclerView.putParam("sidx", "id");
        pullToLoadMoreRecyclerView.putParam("sord", "desc");
        pullToLoadMoreRecyclerView.putParam("currentPage", pageIndex);
        pullToLoadMoreRecyclerView.putParam("pageSize", 10);
        pullToLoadMoreRecyclerView.putParam("repetition_flag", repetition_flag);
        pullToLoadMoreRecyclerView.putParam("app_state", type);
        pullToLoadMoreRecyclerView.requestData();
    }
}