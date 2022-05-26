package com.techen.smartgas.views.security;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techen.smartgas.R;
import com.techen.smartgas.holder.UserListRecyclerViewHolder;
import com.techen.smartgas.model.SecurityBean;
import com.techen.smartgas.widget.IosPopupWindow;

import org.itheima.recycler.L;
import org.itheima.recycler.adapter.BaseLoadMoreRecyclerAdapter;
import org.itheima.recycler.listener.ItemClickSupport;
import org.itheima.recycler.widget.ItheimaRecyclerView;
import org.itheima.recycler.widget.PullToLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment {

    private static final String EXTRA_ID = "id";
    private String TAG = "USERLIST";
//    private AlertView mAlertView;

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
    View contentView;
    ArrayList<SecurityBean.ResultBean.ItemsBean> itemsBeanList = new ArrayList<>();
    private Unbinder unbinder;
    private String id;

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_ID, id);
        UserListFragment userListFragment = new UserListFragment();
        userListFragment.setArguments(arguments);
        return userListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(EXTRA_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_user_list, container, false);
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
                SecurityBean.ResultBean.ItemsBean mData =  itemsBeanList.get(position);
                handleItemClick(mData);
            }
        });

        pullToLoadMoreRecyclerView = new PullToLoadMoreRecyclerView<SecurityBean>(myswipeRefreshLayout, myrecyclerView, UserListRecyclerViewHolder.class) {
            @Override
            public int getItemResId() {
                //recylerview item资源id
                return R.layout.item_user;
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
                Log.i(TAG, "id is " + id);
                return "action/apiv2/banner?catalog=1&startrow=" + pageIndex + "&id=" + id + "&handle=" + handle;
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

    private List<Map<String, Object>> initDatas() {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        Map map1=new HashMap();
        map1.put("img","");
        map1.put("name","正常");
        Map map2=new HashMap();
        map2.put("img",R.mipmap.icon_correct);
        map2.put("name","隐患");
        Map map3=new HashMap();
        map3.put("img","");
        map3.put("name","到访不遇");
        Map map4=new HashMap();
        map4.put("img","");
        map4.put("name","拒绝入户");
        datas.add(map1);
        datas.add(map2);
        datas.add(map3);
        datas.add(map4);
        return datas;
    }

    private void handleItemClick(SecurityBean.ResultBean.ItemsBean mData){
        Integer id = mData.getId();
        String name = mData.getName();
        List<Map<String, Object>> datas = initDatas();
//        Intent intent = new Intent(contentView.getContext(), UserListActivity.class);
//        intent.putExtra("id", id);
//        startActivity(intent);

        IosPopupWindow mPopupWindow = new IosPopupWindow(getActivity(), datas, new IosPopupWindow.OnClickListener() {
            @Override
            public void onItemClick(Object o, int position, Map<String, Object> data) {
                //弹出alipay码
                Log.i(TAG,"弹框位置：" + data);
                Integer id = (Integer) data.get("id");
                String name = (String) data.get("name");
                Intent intent = new Intent(contentView.getContext(), SecurityAddActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                Toast.makeText(contentView.getContext(), "您单击了" + data.get("name").toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
                //取消

            }
        });

        mPopupWindow.show(contentView);

    }
}