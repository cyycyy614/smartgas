package com.techen.smartgas.views.security;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.techen.smartgas.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecurityActivity extends AppCompatActivity {
    @BindView(R.id.tl_tab)
    TabLayout mTabTl;
    @BindView(R.id.vp_content)
    ViewPager2 mContentVp;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        ButterKnife.bind(this);
        setTitle("安检计划");

        // 返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 调用列表初始化及接口
        initContent();
        initTab();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTab(){
        mTabTl.setTabMode(TabLayout.MODE_SCROLLABLE);
//        mTabTl.setTabTextColors(ContextCompat.getColor(this, R.color.gray), ContextCompat.getColor(this, R.color.white));
//        mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(mTabTl, 10);
//        mTabTl.setupWithViewPager(mContentVp);
        new TabLayoutMediator(mTabTl, mContentVp,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabIndicators.get(position));
                    }
                }).attach();
        mTabTl.getTabAt(0).select();
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabIndicators.add("全部");
        tabIndicators.add("下发");
        tabIndicators.add("关闭");
        tabFragments = new ArrayList<>();
        for(int i = 0;i < tabIndicators.size(); i ++){
            tabFragments.add(SecurityItemFragment.newInstance(i + ""));
        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mContentVp.setAdapter(contentAdapter);
    }

    class ContentPagerAdapter extends FragmentStateAdapter {

        public ContentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @Override
        public Fragment createFragment(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getItemCount() {
            return tabIndicators.size();
        }

    }



}