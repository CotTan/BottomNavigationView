package com.cot.bottomnavigationview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cot.bottomnavigationview.R;
import com.cot.bottomnavigationview.adapter.BottomNavigationViewAdapter;
import com.cot.bottomnavigationview.bean.BottomNavigationBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * @author COT
 * @version 1.0
 * @since 2019-7-17
 */
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

    private String mBadgeStyle;// 小红点 默认显示方式（默认不显示）
    private int mBadgeColor;// 小红点颜色(默认红色)

    private int mTextCheckedColor;// 选中项的文字颜色
    private int mTextUncheckedColor;// 未选中项的文字颜色
    private int mTextCheckedSize;// 选中项的文字大小
    private int mTextUncheckedSize;// 未选中项的文字大小

    private int defaultPosition = 0; //默认为0

    private OnClickListener onClickListener;
    private OnDoubleClickListener onDoubleClickListener;
    private OnLongClickListener onLongClickListener;
    private OnPageChangeListener onPageChangeListener;

    private boolean clickStatus;
    private boolean doubleClickStatus;
    private boolean longClickStatus;
    private int listenerType;//0滑动切换 1 单机 2双击 3长按

    public static final String UNAVAILABLE = "0";
    public static final String DOT = "1";
    public static final String NUMBER = "2";

    //限定传入的参数
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    @StringDef({UNAVAILABLE, DOT, NUMBER})

    public @interface BadgeStyle {
    }

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

        //加载自定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView);
        mBadgeStyle = typedArray.getString(R.styleable.BottomNavigationView_badgeStyle);
        mBadgeColor = typedArray.getColor(R.styleable.BottomNavigationView_badgeColor, context.getResources().getColor(R.color.red));
        mTextCheckedColor = typedArray.getColor(R.styleable.BottomNavigationView_textCheckedColor, context.getResources().getColor(R.color.textColor));
        mTextUncheckedColor = typedArray.getColor(R.styleable.BottomNavigationView_textUncheckedColor, context.getResources().getColor(R.color.textGrayColor));
        mTextCheckedSize = (int) typedArray.getDimension(R.styleable.BottomNavigationView_textCheckedSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 14, context.getResources().getDisplayMetrics()));
        mTextUncheckedSize = (int) typedArray.getDimension(R.styleable.BottomNavigationView_textUncheckedSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 14, context.getResources().getDisplayMetrics()));

        //回收资源，这一句必须调用
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 设置数据源  默认第0项选中 最小2项，最大5项（超出不显示）
     */
    public BottomNavigationView setData(List<BottomNavigationBean> list) {
        if (list == null || list.size() < 2) {
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

        mBottomNavigationAdapter = new BottomNavigationViewAdapter(mContent, list, this);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mBottomNavigationAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewGridDivider(1, 0, true, RecycleViewGridDivider.HORIZONTAL));

        setCurrentItem(getDefaultPosition());

        mBottomNavigationAdapter.notifyDataSetChanged();

        setTextSize(mTextCheckedSize, mTextUncheckedSize);
        setTextColor(mTextCheckedColor, mTextUncheckedColor);
        setBadgeDefaultStyle(mBadgeStyle);

        if (onClickListener == null) {//默认点击事件
            setOnClickListener(new BottomNavigationView.OnClickListener() {
                @Override
                public boolean onClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                    listenerType = 1;
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(curPosition, false);
                    }
                    return true;
                }
            });
        }

        //单击事件
        mBottomNavigationAdapter.setOnClickListener(new BottomNavigationViewAdapter.onItemClickListener() {
            @Override
            public boolean onClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                listenerType = 1;
                if (onClickListener != null) {
                    clickStatus = onClickListener.onClickListener(curPosition, bottomNavigationList,
                            prePosition);
                    setCurrentItem(curPosition, clickStatus ? 0 : 1);
                }
                return clickStatus;
            }
        });

        //双击事件
        mBottomNavigationAdapter.setOnDoubleClickListener(new BottomNavigationViewAdapter.onItemDoubleClickListener() {
            @Override
            public boolean onDoubleClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                listenerType = 2;
                if (onDoubleClickListener != null) {
                    doubleClickStatus = onDoubleClickListener.onDoubleClickListener(curPosition, bottomNavigationList,
                            prePosition);
                    setCurrentItem(curPosition, doubleClickStatus ? 0 : 1);
                }
                return doubleClickStatus;
            }
        });

        //长按事件
        mBottomNavigationAdapter.setOnLongClickListener(new BottomNavigationViewAdapter.onItemLongClickListener() {
            @Override
            public boolean onLongClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition) {
                listenerType = 3;
                if (onLongClickListener != null) {
                    longClickStatus = onLongClickListener.onLongClickListener(curPosition, bottomNavigationList,
                            prePosition);
                    setCurrentItem(curPosition, longClickStatus ? 0 : 1);
                }
                return longClickStatus;
            }
        });
        return this;
    }

    /**
     * 设置当前选中
     */
    public BottomNavigationView setCurrentItem(int position) {
        setCurrentItem(position, 0);
        return this;
    }

    /**
     * 设置当前选中
     *
     * @param toggle 0点击并切换 1 点击不切换 2 viewpager的切换
     */
    private BottomNavigationView setCurrentItem(int position, int toggle) {
        if (mBottomNavigationList == null || mBottomNavigationList.size() == 0) {
            return null;
        }
        if (mBottomNavigationList.size() <= position) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (mViewPager != null && toggle == 0) {
            mViewPager.setCurrentItem(position, false);
        }
        if (toggle == 0 || toggle == 2) {
            for (int i = 0; i < mBottomNavigationList.size(); i++) {
                if (i == position) {

                    if (mBottomNavigationAdapter != null) {
                        mBottomNavigationAdapter.setCurPosition(i);
                    }
                    mBottomNavigationList.get(i).setCheck(true);
                } else {
                    mBottomNavigationList.get(i).setCheck(false);
                }
            }
        }
        mBottomNavigationAdapter.notifyDataSetChanged();

        return this;
    }

    public int getTextCheckedColor() {
        return mTextCheckedColor;
    }

    public BottomNavigationView setTextCheckedColor(int mTextCheckedColor) {
        this.mTextCheckedColor = mTextCheckedColor;
        return this;
    }

    public int getTextUncheckedColor() {
        return mTextUncheckedColor;
    }

    public BottomNavigationView setTextUncheckedColor(int mTextUncheckedColor) {
        this.mTextUncheckedColor = mTextUncheckedColor;
        return this;
    }

    public int getTextCheckedSize() {
        return mTextCheckedSize;
    }

    public BottomNavigationView setTextCheckedSize(int mTextCheckedSize) {
        this.mTextCheckedSize = mTextCheckedSize;
        return this;
    }

    public int getTextUncheckedSize() {
        return mTextUncheckedSize;
    }

    public BottomNavigationView setTextUncheckedSize(int mTextUncheckedSize) {
        this.mTextUncheckedSize = mTextUncheckedSize;
        return this;
    }

    /**
     * 设置选择的字体颜色和未选择的颜色
     */
    public BottomNavigationView setTextColor(int checkedColor, int uncheckedColor) {
        setTextCheckedColor(checkedColor);
        setTextUncheckedColor(uncheckedColor);

        return this;
    }

    /**
     * 设置选择的字体大小和未选择的字体大小  sp
     */
    public BottomNavigationView setTextSize(int checkedTextSize, int uncheckedTextSize) {
        setTextCheckedSize(checkedTextSize);
        setTextUncheckedSize(uncheckedTextSize);
        return this;
    }

    /**
     * 关联viewpager 默认第0项
     */
    public BottomNavigationView bindViewPager(ViewPager viewPager) {
        return bindViewPager(viewPager, getDefaultPosition());
    }

    /**
     * 关联viewpager 指定默认项
     */
    public BottomNavigationView bindViewPager(ViewPager viewPager, int position) {
        this.mViewPager = viewPager;
        if (this.mViewPager != null) {
            this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }

                @Override
                public void onPageSelected(int i) {
                    if (i < mBottomNavigationList.size()) {
                        switch (listenerType) {
                            case 0://滑动切换
                                setCurrentItem(i, 2);
                                if (onClickListener != null) {
                                    clickStatus = onClickListener.onClickListener(i, mBottomNavigationAdapter.getData(),
                                            mBottomNavigationAdapter.getPrePosition());
                                }
                                break;
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    listenerType = 0;
                }
            });
        }
        setDefaultPosition(position);
        return this;
    }

    public String getBadgeDefaultStyle() {
        return mBadgeStyle;
    }

    public BottomNavigationView setBadgeDefaultStyle(@BadgeStyle String badgeStatus) {
        this.mBadgeStyle = badgeStatus;
        return this;
    }

    /**
     * 设置badge
     *
     * @param badgeContent 小红点内容
     * @param position     小红点位置
     */
    public BottomNavigationView setBadge(String badgeContent, int position) {
        if (mBottomNavigationList == null || mBottomNavigationList.size() == 0) {
            return null;
        }
        if (mBottomNavigationList.size() <= position) {
            throw new ArrayIndexOutOfBoundsException();
        }

        for (int i = 0; i < mBottomNavigationList.size(); i++) {
            if (i == position) {
                switch (mBadgeStyle) {
                    case UNAVAILABLE:
                        mBottomNavigationList.get(i).setBadgeStyle(UNAVAILABLE);
                        break;
                    case DOT:
                        mBottomNavigationList.get(i).setBadgeStyle(DOT);
                        break;
                    case NUMBER:
                        mBottomNavigationList.get(i).setBadgeStyle(NUMBER);
                        mBottomNavigationList.get(i).setBadgeValue(badgeContent);
                        break;
                }
            }
        }

        mBottomNavigationAdapter.notifyDataSetChanged();
        return this;
    }

    /**
     * 移除badge
     *
     * @param position badge位置
     */
    public BottomNavigationView removeBadge(int position) {
        if (mBottomNavigationList == null || mBottomNavigationList.size() == 0) {
            return null;
        }
        if (mBottomNavigationList.size() <= position) {
            throw new ArrayIndexOutOfBoundsException();
        }

        for (int i = 0; i < mBottomNavigationList.size(); i++) {
            if (i == position) {
                mBottomNavigationList.get(i).setBadgeStyle(UNAVAILABLE);
            }
        }

        mBottomNavigationAdapter.notifyDataSetChanged();
        return this;
    }

    /**
     * //todo 颜色错位 尚未解决  请勿使用该方法 配置颜色 请去XML中配置
     */
    @Deprecated
    public BottomNavigationView setBadgeColor(int badgeColor, int position) {

        this.mBadgeColor = badgeColor;

        if (mBottomNavigationList == null || mBottomNavigationList.size() == 0) {
            return null;
        }
        if (mBottomNavigationList.size() <= position) {
            throw new ArrayIndexOutOfBoundsException();
        }

        for (int i = 0; i < mBottomNavigationList.size(); i++) {
            if (i == position) {
                mBottomNavigationList.get(position).setBadgeColor(badgeColor);
            }
        }

        mBottomNavigationAdapter.notifyDataSetChanged();

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

    /**
     * 滑动切换事件
     */
    public BottomNavigationView addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
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

    //滑动切换事件
    public interface OnPageChangeListener {
        /**
         * @param position 当前页面，及你点击滑动的页面
         * @param v        当前页面偏移的百分比
         * @param i1       当前页面偏移的像素位置
         */
        void onPageScrolled(int position, float v, int i1);

        /**
         * @param position 此方法是页面跳转完后得到调用，position是你当前选中的页面的Position（位置编号）
         */
        void onPageSelected(int position);

        /**
         * @param state 此方法是在状态改变的时候调用，其中state这个参数有三种状态：
         *              <p>
         *              SCROLL_STATE_DRAGGING（1）表示用户手指“按在屏幕上并且开始拖动”的状态
         *              （手指按下但是还没有拖动的时候还不是这个状态，只有按下并且手指开始拖动后log才打出。）
         *              SCROLL_STATE_IDLE（0）滑动动画做完的状态。
         *              SCROLL_STATE_SETTLING（2）在“手指离开屏幕”的状态
         */
        void onPageScrollStateChanged(int state);

        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onLongClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }

}
