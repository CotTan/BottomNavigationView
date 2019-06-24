package com.gztckj.bottomnavview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gztckj.bottomnavview.R;
import com.gztckj.bottomnavview.bean.BottomNavigationBean;

import java.util.List;

public class BottomNavigationViewAdapter extends RecyclerView.Adapter<BottomNavigationViewAdapter.ViewHolder> {

    private static int TIMEOUT = 300;//双击间四百毫秒延时
    private int firstPosition = 0;//用来区分是不是快速点击两个不同的item
    private int lastPosition = 0;//用来区分是不是快速点击两个不同的item
    private int clickCount = 0;//记录连续点击次数
    private Handler handler;

    private int curPosition;
    private int prePosition;

    private Context mContext;
    private onItemClickListener itemClickListener;
    private onItemDoubleClickListener itemDoubleClickListener;
    private onItemLongClickListener itemLongClickListener;

    private List<BottomNavigationBean> bottomNavigationList;

    public BottomNavigationViewAdapter(Context context, List<BottomNavigationBean> bottomNavigationList) {
        this.mContext = context;
        this.bottomNavigationList = bottomNavigationList;
    }

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bottom_navigation_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BottomNavigationBean item = bottomNavigationList.get(i);

        if (item.getLabelName() == null || "".equals(item.getLabelName())) {
            viewHolder.label.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewHolder.icon.getLayoutParams();

            layoutParams.height = 144;
            layoutParams.width = 144;
            viewHolder.icon.setLayoutParams(layoutParams);
        } else {
            viewHolder.label.setVisibility(View.VISIBLE);
            viewHolder.label.setText(item.getLabelName());
        }

        if (item.isCheck()) {
            viewHolder.label.setTextColor(mContext.getResources().getColor(BottomNavigationBean.CHECKED_COLOR));
            viewHolder.label.setTextSize(TypedValue.COMPLEX_UNIT_SP, BottomNavigationBean.CHECKED_TEXT_SIZE);

            if (item.getLabelCheckedIcon() != 0) {
                viewHolder.icon.setImageResource(item.getLabelCheckedIcon());
            }
        } else if (!item.isCheck()) {
            viewHolder.label.setTextColor(mContext.getResources().getColor(BottomNavigationBean.UNCHECKED_COLOR));
            viewHolder.label.setTextSize(TypedValue.COMPLEX_UNIT_SP, BottomNavigationBean.UNCHECKED_TEXT_SIZE);

            if (item.getLabelUncheckedIcon() != 0) {
                viewHolder.icon.setImageResource(item.getLabelUncheckedIcon());
            }
        }
    }

    @Override
    public int getItemCount() {
        return bottomNavigationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView label;
        ImageView icon;

        @SuppressLint("HandlerLeak")
        ViewHolder(View itemView) {
            super(itemView);

            firstPosition = getCurPosition();
            lastPosition = getCurPosition();
            prePosition = lastPosition;

            label = itemView.findViewById(R.id.tv_item_bottom_navigation);
            icon = itemView.findViewById(R.id.iv_item_bottom_navigation);

            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        //1,3 单击  2 双击  4 长按
                        case 1://单击
                        case 3:
                            if (itemClickListener != null) {
                                itemClickListener.onClickListener(msg.arg1, bottomNavigationList, msg.arg2);
                            }
                            break;
                        case 2://双击
                            if (itemDoubleClickListener != null) {
                                itemDoubleClickListener.onDoubleClickListener(msg.arg1, bottomNavigationList, msg.arg2);
                            }
                            break;
                        case 4://长按
                            if (itemLongClickListener != null) {
                                itemLongClickListener.onLongClickListener(msg.arg1, bottomNavigationList, msg.arg2);
                            }
                            break;
                    }

                    handler.removeCallbacksAndMessages(null);
                    //清空handler延时，并防内存泄漏
                    clickCount = 0;//计数清零
                }
            };

            //item单机事件和双击事件处理
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int type = 0;//1,3 单击  2 双击  4 长按

                    prePosition = firstPosition;

                    clickCount++;
                    if (clickCount == 1) {
                        firstPosition = getAdapterPosition();
                    } else if (clickCount == 2) {
                        lastPosition = getAdapterPosition();
                    }

                    if (clickCount == 1) {
                        type = 1;
                    } else if (clickCount == 2 && firstPosition == lastPosition) {
                        type = 2;
                        prePosition = curPosition;
                    } else if (firstPosition != lastPosition) {
                        type = 3;
                        prePosition = curPosition;
                    }

                    Message msg = new Message();
                    switch (type) {
                        case 1:
                            msg.what = type;
                            msg.arg1 = firstPosition;
                            msg.arg2 = prePosition;
                            handler.sendMessageDelayed(msg, TIMEOUT);
                            break;
                        case 2:
                            msg.what = type;
                            msg.arg1 = lastPosition;
                            msg.arg2 = prePosition;
                            handler.sendMessageDelayed(msg, TIMEOUT);
                            handler.removeMessages(1);
                            break;
                        case 3:
                            msg.what = type;
                            msg.arg1 = lastPosition;
                            msg.arg2 = prePosition;
                            handler.sendMessageDelayed(msg, TIMEOUT);
                            handler.removeMessages(1);
                            break;
                    }
                }
            });

            //item 长按事件处理
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int type = 4;//1,3 单击  2 双击  4 长按

                    prePosition = curPosition;
                    firstPosition = getAdapterPosition();

                    Message msg = new Message();
                    msg.what = type;
                    msg.arg1 = firstPosition;
                    msg.arg2 = prePosition;
                    handler.sendMessageDelayed(msg, TIMEOUT);

                    return true;
                }
            });
        }
    }

    /**
     * 单击事件
     */
    public void setOnClickListener(onItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    /**
     * 双击事件
     */
    public void setOnDoubleClickListener(onItemDoubleClickListener doubleClickListener) {
        this.itemDoubleClickListener = doubleClickListener;
    }

    /**
     * 长按事件
     */
    public void setOnLongClickListener(onItemLongClickListener longClickListener) {
        this.itemLongClickListener = longClickListener;
    }

    //单击事件
    public interface onItemClickListener {
        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return  true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }

    //双击事件
    public interface onItemDoubleClickListener {
        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return  true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onDoubleClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }

    //长按事件
    public interface onItemLongClickListener {
        /**
         * @param curPosition          当前位置
         * @param bottomNavigationList 数据源
         * @param prePosition          之前位置
         * @return  true 默认实现跳转  false 自己需要实现跳转 默认 true
         */
        boolean onLongClickListener(int curPosition, List<BottomNavigationBean> bottomNavigationList, int prePosition);
    }
}
