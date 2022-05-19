package com.techen.smartgas.views.security;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.techen.smartgas.R;
import com.techen.smartgas.holder.SecurityRecyclerViewHolder;
import com.techen.smartgas.model.SecurityBean;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String EXTRA_TYPE = "type";
    private String TAG = "SECURITY";

    BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder;
    PullToLoadMoreRecyclerView pullToLoadMoreRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout myswipeRefreshLayout;
    ItheimaRecyclerView myrecyclerView;
    @BindView(R.id.recycler_header)
    org.itheima.recycler.header.RecyclerViewHeader myrecyclerHeader;


    String handle;
    Integer pageIndex = 0;
    private int state = 0;
    private static final int STATE_FRESH = 1;
    private static final int STATE_MORE = 2;
    private int pageindex = 0;
    ArrayList<SecurityBean.ResultBean.ItemsBean> itemsBeanList = new ArrayList<>();
    private Unbinder unbinder;
    // TODO: Rename and change types of parameters
    private String type;

    public SecurityItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecurityItemFragment newInstance(String param1, String param2) {
        SecurityItemFragment fragment = new SecurityItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SecurityItemFragment newInstance(String type) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_TYPE, type);
        SecurityItemFragment securityItemFragment = new SecurityItemFragment();
        securityItemFragment.setArguments(arguments);
        return securityItemFragment;
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
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_security_item, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        myrecyclerView = contentView.findViewById(R.id.recycler_view);
        list();
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void list() {
        myrecyclerHeader.attachTo(myrecyclerView);

        ItemClickSupport itemClickSupport = new ItemClickSupport(myrecyclerView);
        //点击事件
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Integer id = itemsBeanList.get(position).getId();
                String name = itemsBeanList.get(position).getName();
//                Intent intent = new Intent(SecurityActivity.this, ReportDetailActivity.class);
//                intent.putExtra("id", id);
//                startActivity(intent);
//                Toast.makeText(recyclerView.getContext(), "我被点击了", Toast.LENGTH_SHORT).show();
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
                        pageIndex = 0;
                        break;
                    case STATE_MORE:
                        pageIndex++;
                        break;
                }
                //接口
                Log.i(TAG, "type is " + type);
                return "action/apiv2/banner?catalog=1&startrow=" + pageIndex + "&type=" + type + "&handle=" + handle;
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
                List<SecurityBean.ResultBean.ItemsBean> itemDatas = o.getItemDatas();
                if (itemDatas.size() == 0) {
                    holder.loadingFinish((String) null);
                    if (myswipeRefreshLayout != null) {
                        myswipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    for (SecurityBean.ResultBean.ItemsBean item : itemDatas) {
                        itemsBeanList.add(item);
                    }
                }
            }

            @Override
            public void onFailure() {
                //监听http请求失败，如果不需要监听可以不重新该方法
                L.i("setLoadingDataListener onFailure");
            }
        });

        pullToLoadMoreRecyclerView.requestData();
    }
}