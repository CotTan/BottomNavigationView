package com.gztckj.bottomnavview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gztckj.bottomnavview.R;
import com.gztckj.bottomnavview.adapter.BottomNavigationViewAdapter;
import com.gztckj.bottomnavview.bean.BottomNavigationBean;

import java.util.List;

public class BottomNavigationView extends FrameLayout {

    /**
     * 支持 图 图文 文 三种模式的底部导航的组合控件，可以混搭
     */
    private Context mContent;
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;

    private BottomNavigationViewAdapter mBottomNavigationAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<BottomNavigationBean> mBottomNavigationList;

    private int defaultPosition = 0; //默认为0

    private OnClickListener onClickListener;
    private OnDoubleClickListener onDoubleClickListener;
    private OnLongClickListener onLongClickListener;

    public BottomNavigationView(@NonNull Context context) {
        this(context, null);
    }

    public BottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContent = context;

        View view = LayoutInflater.from(context).inflate(R.layout.bottom_navigation_view, this);

        mRecyclerView = view.findViewById(R.id.rv_bottom_navigation_view);

        setOnClickListener(new BottomNavigationView.OnClickListener() {
            @Override
            public boolean onClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                setCurrentItem(curPosition);
                return true;
            }
        });
    }

    /**
     * 设置数据源  默认第0项选中
     */
    public BottomNavigationView setData(List<BottomNavigationBean> list) {
        if (list.size() < 2) {
            return null;
        } else if (list.size() > 5) {
            this.mBottomNavigationList = list;
            for (int i = 5; i < mBottomNavigationList.size(); ) {
                this.mBottomNavigationList.remove(i);
            }
        } else {
            this.mBottomNavigationList = list;
        }

        mGridLayoutManager = new GridLayoutManager(mContent, mBottomNavigationList.size());

        mBottomNavigationAdapter = new BottomNavigationViewAdapter(mContent, list);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mBottomNavigationAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewGridDivider(1, 0, true, RecycleViewGridDivider.HORIZONTAL));

        setCurrentItem(getDefaultPosition());

        mBottomNavigationAdapter.notifyDataSetChanged();

        //单击事件
        mBottomNavigationAdapter.setOnClickListener(new BottomNavigationViewAdapter.onItemClickListener() {
            @Override
            public boolean onClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                if (onClickListener != null) {
                    boolean event = onClickListener.onClickListener(curPosition, bottomNavigationList, prePosition);
                    if (event) {
                        setCurrentItem(curPosition);
                    }
                    return event;
                }
                return true;
            }
        });

        //双击事件
        mBottomNavigationAdapter.setOnDoubleClickListener(new BottomNavigationViewAdapter.onItemDoubleClickListener() {
            @Override
            public boolean onDoubleClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                if (onDoubleClickListener != null) {
                    boolean event = onDoubleClickListener.onDoubleClickListener(curPosition, bottomNavigationList, prePosition);
                    if (event) {
                        setCurrentItem(curPosition);
                    }
                    return event;
                }
                return true;
            }
        });

        //长按事件
        mBottomNavigationAdapter.setOnLongClickListener(new BottomNavigationViewAdapter.onItemLongClickListener() {
            @Override
            public boolean onLongClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                if (onLongClickListener != null) {
                    boolean event = onLongClickListener.onLongClickListener(curPosition, bottomNavigationList, prePosition);
                    if (event) {
                        setCurrentItem(curPosition);
                    }
                    return event;
                }
                return true;
            }
        });
        return this;
    }

    /**
     * 设置当前选中
     */
    public BottomNavigationView setCurrentItem(int position) {
        if (mBottomNavigationList == null || mBottomNavigationList.size() == 0) {
            return null;
        }
        if (mBottomNavigationList.size() <= position) {
            throw new ArrayIndexOutOfBoundsException();
        }

        for (int i = 0; i < mBottomNavigationList.size(); i++) {
            if (i == position) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(i, false);
                }
                if (mBottomNavigationAdapter != null) {
                    mBottomNavigationAdapter.setCurPosition(i);
                }
                mBottomNavigationList.get(i).setCheck(true);
            } else {
                mBottomNavigationList.get(i).setCheck(false);
            }
        }

        mBottomNavigationAdapter.notifyDataSetChanged();

        return this;
    }

    /**
     * 设置选择的字体颜色和未选择的颜色
     */
    public BottomNavigationView setTextColor(int checkedColor, int uncheckedColor) {
        BottomNavigationBean.CHECKED_COLOR = checkedColor;
        BottomNavigationBean.UNCHECKED_COLOR = uncheckedColor;

        mBottomNavigationAdapter.notifyDataSetChanged();

        return this;
    }

    /**
     * 设置选择的字体大小和未选择的字体大小  sp
     */
    public BottomNavigationView setTextSize(int checkedTextSize, int uncheckedTextSize) {
        BottomNavigationBean.CHECKED_TEXT_SIZE = checkedTextSize;
        BottomNavigationBean.UNCHECKED_TEXT_SIZE = uncheckedTextSize;

        mBottomNavigationAdapter.notifyDataSetChanged();
        return this;
    }


    /**
     * 关联viewpager 默认第0项
     */
    public BottomNavigationView setViewPager(ViewPager viewPager) {
        return setViewPager(viewPager, 0);
    }

    /**
     * 关联viewpager 指定默认项
     */
    public BottomNavigationView setViewPager(ViewPager viewPager, int position) {
        this.mViewPager = viewPager;
        if (this.mViewPager != null) {
            this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }

                @Override
                public void onPageSelected(int i) {
                    if (i <= mBottomNavigationList.size()) {
                        setCurrentItem(i);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
        }
        setDefaultPosition(position);
        return this;
    }

    public int getDefaultPosition() {
        return defaultPosition;
    }

    public BottomNavigationView setDefaultPosition(int defaultPosition) {
        this.defaultPosition = defaultPosition;
        setCurrentItem(defaultPosition);
        return this;
    }

    /**
     * 单击事件
     */
    public BottomNavigationView setOnClickListener(OnClickListener clickListener) {
        this.onClickListener = clickListener;
        return this;
    }

    /**
     * 双击事件
     */
    public BottomNavigationView setOnDoubleClickListener(OnDoubleClickListener doubleClickListener) {
        this.onDoubleClickListener = doubleClickListener;
        return this;
    }

    /**
     * 长按事件
     */
    public BottomNavigationView setOnLongClickListener(OnLongClickListener longClickListener) {
        this.onLongClickListener = longClickListener;
        return this;
    }

    //单击事件
    public interface OnClickListener {
        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }

    //双击事件
    public interface OnDoubleClickListener {
        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onDoubleClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }

    //长按事件
    public interface OnLongClickListener {
        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onLongClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }

}
