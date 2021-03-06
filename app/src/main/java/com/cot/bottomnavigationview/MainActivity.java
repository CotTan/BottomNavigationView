package com.cot.bottomnavigationview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cot.bottomnavigationview.adapter.ViewPagerFragmentAdapter;
import com.cot.bottomnavigationview.bean.BottomNavigationBean;
import com.cot.bottomnavigationview.fragment.CommunicationFragment;
import com.cot.bottomnavigationview.fragment.DiscoverFragment;
import com.cot.bottomnavigationview.fragment.DisplayFragment;
import com.cot.bottomnavigationview.fragment.HomeFragment;
import com.cot.bottomnavigationview.fragment.MineFragment;
import com.cot.bottomnavigationview.view.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author COT
 * @version 1.0
 * @since 2019-7-17
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;

    private List<Fragment> mFragmentList;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;

    private String tag = "BottomNavigationView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        mViewPager = findViewById(R.id.vp_main);
        mBottomNavigationView = findViewById(R.id.bnv_main);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(CommunicationFragment.newInstance());
        mFragmentList.add(DiscoverFragment.newInstance());
        mFragmentList.add(DisplayFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.setAdapter(mViewPagerFragmentAdapter);

        final List<BottomNavigationBean> list = new ArrayList<>();

        list.add(new BottomNavigationBean("首页", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round));
        list.add(new BottomNavigationBean("通讯", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round));
        list.add(new BottomNavigationBean("", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round));
        list.add(new BottomNavigationBean("炫耀", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round));
        list.add(new BottomNavigationBean("我的", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round));

        list.get(0).setBadgeStyle(BottomNavigationView.DOT);
        list.get(1).setBadgeStyle(BottomNavigationView.NUMBER).setBadgeValue("153");
        list.get(2).setBadgeStyle(BottomNavigationView.NUMBER).setBadgeValue("25");
        list.get(3).setBadgeStyle(BottomNavigationView.NUMBER).setBadgeValue("6");
        list.get(4).setBadgeStyle(BottomNavigationView.NUMBER).setBadgeValue("99+");

        mBottomNavigationView.setData(list)
                .setDefaultPosition(2)
                .setBadgeColor(Color.parseColor("#0EE90A"), 1)
                .bindViewPager(mViewPager)
                .setOnClickListener((curPosition, bottomNavigationList, prePosition) -> {
                    setBadge(list.get(curPosition), curPosition);
                    Log.e(tag, "单击当前位置：" + curPosition + "    上一个位置:" + prePosition);
                    Toast.makeText(MainActivity.this, "单击" + bottomNavigationList.get(curPosition).getLabelName(), Toast.LENGTH_SHORT).show();
                    return true;
                })
                .setOnDoubleClickListener((curPosition, bottomNavigationList, prePosition) -> {
                    setBadge(list.get(curPosition), curPosition);
                    Log.e(tag, "双击当前位置：" + curPosition + "    上一个位置:" + prePosition);
                    Toast.makeText(MainActivity.this, "双击" + bottomNavigationList.get(curPosition).getLabelName(), Toast.LENGTH_SHORT).show();
                    return true;
                })
                .setOnLongClickListener((curPosition, bottomNavigationList, prePosition) -> {
                    setBadge(list.get(curPosition), curPosition);
                    Log.e(tag, "长按当前位置：" + curPosition + "    上一个位置:" + prePosition);
                    Toast.makeText(MainActivity.this, "长按" + bottomNavigationList.get(curPosition).getLabelName(), Toast.LENGTH_SHORT).show();
                    return false;
                });
    }

    private void setBadge(BottomNavigationBean curPositionItem, int position) {
        Random random = new Random();
        if (curPositionItem.getBadgeStyle().equals(BottomNavigationView.DOT)) {
            curPositionItem.removeBadge();
        } else if (curPositionItem.getBadgeStyle().equals(BottomNavigationView.UNAVAILABLE)) {
            curPositionItem.setBadgeStyle(BottomNavigationView.DOT);
        }

        if (curPositionItem.getBadgeValue() == null || "".equals(curPositionItem.getBadgeValue())) {
            curPositionItem.setBadgeValue("" + (random.nextInt(900) + 1 + position));
        } else {
            curPositionItem.setBadgeValue("");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mBottomNavigationView != null) {
            mBottomNavigationView.setCurrentItem(2);
        }
    }
}
