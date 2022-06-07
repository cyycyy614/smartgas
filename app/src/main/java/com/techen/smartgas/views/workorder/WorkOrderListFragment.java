package com.techen.smartgas.views.workorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techen.smartgas.R;
import com.techen.smartgas.holder.SecurityRecyclerViewHolder;
import com.techen.smartgas.holder.WorkOrderRecyclerViewHolder;
import com.techen.smartgas.model.WorkOrderBean;
import com.techen.smartgas.util.LoadingDialog;
import com.techen.smartgas.util.Tool;
import com.techen.smartgas.views.security.SecurityDetailActivity;
import com.techen.smartgas.views.security.SecurityItemFragment;
import com.techen.smartgas.views.security.UserListActivity;

import org.itheima.recycler.L;
import org.itheima.recycler.adapter.BaseLoadMoreRecyclerAdapter;
import org.itheima.recycler.listener.ItemClickSupport;
import org.itheima.recycler.widget.ItheimaRecyclerView;
import org.itheima.recycler.widget.PullToLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkOrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkOrderListFragment extends Fragment {

    private static final String EXTRA_TYPE = "type";
    private String TAG = "WORKORDER";
    BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder;
    PullToLoadMoreRecyclerView pullToLoadMoreRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout myswipeRefreshLayout;
    ItheimaRecyclerView myrecyclerView;
    @BindView(R.id.recycler_header)
    org.itheima.recycler.header.RecyclerViewHeader myrecyclerHeader;
    @BindView(R.id.layout_nodata)
    LinearLayout layout_NoData;

    String handle;
    Integer pageIndex = 1;
    private int state = 0;
    private static final int STATE_FRESH = 1;
    private static final int STATE_MORE = 2;
    private String type;
    private Unbinder unbinder;
    View contentView;
    ArrayList<WorkOrderBean.ResultBean.DataListBean> itemsBeanList = new ArrayList<>();

    public WorkOrderListFragment() {
        // Required empty public constructor
    }

    public static WorkOrderListFragment newInstance(String type) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_TYPE, type);
        WorkOrderListFragment workOrderListFragment = new WorkOrderListFragment();
        workOrderListFragment.setArguments(arguments);
        return workOrderListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(EXTRA_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_work_order_list, container, false);
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
                if(stateCode.equals("handling")){
                    Intent intent = new Intent(contentView.getContext(), WorkOrderAddActivity.class);
                    intent.putExtra("id", id + "");
                    startActivityForResult(intent,GET_CODE);
                }else{
                    Intent intent = new Intent(contentView.getContext(), WorkOrderDetailActivity.class);
                    intent.putExtra("id", id + "");
                    startActivityForResult(intent,GET_CODE);
                }
            }
        });

        pullToLoadMoreRecyclerView = new PullToLoadMoreRecyclerView<WorkOrderBean>(myswipeRefreshLayout, myrecyclerView, WorkOrderRecyclerViewHolder.class) {
            @Override
            public int getItemResId() {
                //recylerview item资源id
                return R.layout.item_workorder;
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
                return "amiwatergas/mobile/workOrder/list?currentPage=" + pageIndex;
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

        pullToLoadMoreRecyclerView.setLoadingDataListener(new PullToLoadMoreRecyclerView.LoadingDataListener<WorkOrderBean>() {

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
            public void onSuccess(WorkOrderBean o, Headers headers) {
                //监听http请求成功，如果不需要监听可以不重新该方法
                L.i("setLoadingDataListener onSuccess: " + o);
                List<WorkOrderBean.ResultBean.DataListBean> itemDatas = o.getItemDatas();
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
                    for (WorkOrderBean.ResultBean.DataListBean item : itemDatas) {
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
                LoadingDialog.getInstance(contentView.getContext()).hide();
                if(itemsBeanList.size() == 0){
                    layout_NoData.setVisibility(View.VISIBLE);
                }
                //监听http请求失败，如果不需要监听可以不重新该方法
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
        pullToLoadMoreRecyclerView.putParam("state", type);
        pullToLoadMoreRecyclerView.requestData();
    }
    static final private int GET_CODE = 0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == GET_CODE && resultCode == Activity.RESULT_OK) {
            //加载数据
            itemsBeanList = new ArrayList<>();
            pullToLoadMoreRecyclerView.onRefresh();
        }
    }
}