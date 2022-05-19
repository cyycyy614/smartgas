package com.techen.smartgas.views.security;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.itheima.retrofitutils.L;
import com.techen.smartgas.R;
import com.techen.smartgas.holder.SecurityRecyclerViewHolder;
import com.techen.smartgas.model.SecurityBean;

import org.itheima.recycler.adapter.BaseLoadMoreRecyclerAdapter;
import org.itheima.recycler.header.RecyclerViewHeader;
import org.itheima.recycler.listener.ItemClickSupport;
import org.itheima.recycler.viewholder.BaseRecyclerViewHolder;
import org.itheima.recycler.widget.ItheimaRecyclerView;
import org.itheima.recycler.widget.PullToLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;

public class SecurityActivity extends AppCompatActivity {
    BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder;
    PullToLoadMoreRecyclerView pullToLoadMoreRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout myswipeRefreshLayout;
    private ItheimaRecyclerView myrecyclerView;
    String handle;
    Integer pageIndex = 0;
    private int state = 0;
    private static final int STATE_FRESH = 1;
    private static final int STATE_MORE = 2;
    private int pageindex = 0;
    ArrayList<SecurityBean.ResultBean.ItemsBean> itemsBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        ButterKnife.bind(this);
        setTitle("安检计划");

        // 调用列表初始化及接口
        list();
    }

    private void list() {
        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.recycler_header);
        myrecyclerView = (ItheimaRecyclerView) findViewById(R.id.recycler_view);
        header.attachTo(myrecyclerView);

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
                return "action/apiv2/banner?catalog=1&startrow=" + pageIndex + "&handle=" + handle;
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