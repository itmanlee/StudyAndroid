package com.wangkeke.horirecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<StepToolModel> mDatas;

    OnStepItemCickListener listener;

    // item 的三种类型
    public static final int ITEM_TYPE_NORMAL = 0X1111; // 正常的item类型
    public static final int ITEM_TYPE_HEADER = 0X1112; // header
    public static final int ITEM_TYPE_FOOTER = 0X1113; // footer

    private Disposable startDispose;

    private Context mContext;

    public GalleryAdapter(Context context, List<StepToolModel> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        mContext = context;
    }

    // 头部控件
    private View mHeaderView;

    // 底部控件
    private View mFooterView;


    private boolean isHasHeader = false;

    private boolean isHasFooter = false;


    /**
     * 添加头部视图
     *
     * @param header
     */
    public void setHeaderView(View header) {
        this.mHeaderView = header;
        isHasHeader = true;
        notifyDataSetChanged();
    }

    /**
     * 添加底部视图
     *
     * @param footer
     */
    public void setFooterView(View footer) {
        this.mFooterView = footer;
        isHasFooter = true;
        notifyDataSetChanged();
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        public NormalViewHolder(View arg0) {
            super(arg0);
        }

        LinearLayout layoutSelect;
        TextView tvBtnText, tvChildrenText;
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    //底部 ViewHolder
    public static class BottomViewHolder extends RecyclerView.ViewHolder {

        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setData(List<StepToolModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void updateItem(int position) {
        notifyItemChanged(position);
    }

    public void setOnStepItemClickListener(OnStepItemCickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        // 根据索引获取当前View的类型，以达到复用的目的

        // 根据位置的索引，获取当前position的类型
        if (isHasHeader && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (isHasHeader && isHasFooter && position == mDatas.size() + 1) {
            // 有头部和底部时，最后底部的应该等于size+!
            return ITEM_TYPE_FOOTER;
        } else if (!isHasHeader && isHasFooter && position == mDatas.size()) {
            // 没有头部，有底部，底部索引为size
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == ITEM_TYPE_FOOTER) {
            // 如果是底部类型，返回底部视图
            return new BottomViewHolder(mFooterView);
        }

        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        }


        View view = mInflater.inflate(R.layout.item_recycler, viewGroup, false);
        NormalViewHolder viewHolder = new NormalViewHolder(view);
        viewHolder.tvBtnText = view.findViewById(R.id.tv_btn_text);
//        viewHolder.tvChildrenText = view.findViewById(R.id.tv_children_text);
        viewHolder.layoutSelect = view.findViewById(R.id.layout_select);
        viewHolder.layoutSelect.setVisibility(View.GONE);
        return viewHolder;
    }

    public void setCurrentSelectItem(int position, boolean state) {
        clearOtherOpen(position);
        if (state) {
            listener.onItemClick(position);
        }
        mDatas.get(position).setSelected(state);
        mDatas.get(position).setOpen(state);
        updateItem(position);
    }

    @Override
    public int getItemCount() {
        int size = mDatas.size();
        if (isHasFooter)
            size++;
        if (isHasHeader)
            size++;
        return size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder instanceof HeaderViewHolder) {

        } else if (viewHolder instanceof NormalViewHolder) {

            final int j = i;

            ((NormalViewHolder) viewHolder).tvBtnText.setText(mDatas.get(j).getText());

            if (mDatas.get(j).isSelected()) {
                ((NormalViewHolder) viewHolder).tvBtnText.setBackgroundColor(Color.parseColor("#111111"));
            } else {
                ((NormalViewHolder) viewHolder).tvBtnText.setBackgroundColor(Color.parseColor("#888888"));
            }

            ((NormalViewHolder) viewHolder).layoutSelect.setVisibility(mDatas.get(j).isOpen() ? View.VISIBLE : View.GONE);

            showViewByType(j, ((NormalViewHolder) viewHolder));

            ((NormalViewHolder) viewHolder).tvBtnText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(j);

                    startDispose = io.reactivex.Observable.timer(600, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    clearOtherOpen(j);
                                    if (mDatas.get(j).isOpen()) {
                                        mDatas.get(j).setOpen(false);
                                        mDatas.get(j).setSelected(false);
                                    } else {
                                        mDatas.get(j).setOpen(true);
                                        mDatas.get(j).setSelected(true);
                                    }
                                    updateItem(j);
                                }
                            });


                }
            });

        } else if (viewHolder instanceof BottomViewHolder) {

        }
    }

    private void showViewByType(int position, NormalViewHolder normalViewHolder) {
        LinearLayout childViewRoot = normalViewHolder.layoutSelect;
        childViewRoot.removeAllViews();
        switch (mDatas.get(position).getType()) {
            case 0:
                View viewTypeText = mInflater.inflate(R.layout.view_type_text, null);
                TextView tvTypeText = viewTypeText.findViewById(R.id.tv_text_show);
                tvTypeText.setText(mDatas.get(position).getChildrenText());
                childViewRoot.addView(viewTypeText);
                break;
            case 1:
                for (int n = 0; n < 5; n++) {
                    ImageView viewImage = (ImageView) mInflater.inflate(R.layout.view_type_imge, null);
                    if (n == 0) {
                        viewImage.setImageResource(R.mipmap.one);
                    } else if (n == 1) {
                        viewImage.setImageResource(R.mipmap.two);
                    } else if (n == 2) {
                        viewImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mContext, "点击了第三个", Toast.LENGTH_SHORT).show();
                            }
                        });
                        viewImage.setImageResource(R.mipmap.three);
                    } else if (n == 3) {
                        viewImage.setImageResource(R.mipmap.four);
                    } else if (n == 4) {
                        viewImage.setImageResource(R.mipmap.five);
                    }
                    childViewRoot.addView(viewImage);
                }
                break;
            case 2:
                View viewTypeText1 = mInflater.inflate(R.layout.view_type_text, null);
                TextView tvTypeText1 = viewTypeText1.findViewById(R.id.tv_text_show);
                tvTypeText1.setText(mDatas.get(position).getChildrenText());
                childViewRoot.addView(viewTypeText1);
                break;
            case 3:
                View viewTypeText2 = mInflater.inflate(R.layout.view_type_text, null);
                TextView tvTypeText2 = viewTypeText2.findViewById(R.id.tv_text_show);
                tvTypeText2.setText(mDatas.get(position).getChildrenText());
                childViewRoot.addView(viewTypeText2);
                break;
            case 4:
                View viewTypeText3 = mInflater.inflate(R.layout.view_type_text, null);
                TextView tvTypeText3 = viewTypeText3.findViewById(R.id.tv_text_show);
                tvTypeText3.setText(mDatas.get(position).getChildrenText());
                childViewRoot.addView(viewTypeText3);
                break;
            case 5:
                View viewTypeText4 = mInflater.inflate(R.layout.view_type_text, null);
                TextView tvTypeText4 = viewTypeText4.findViewById(R.id.tv_text_show);
                tvTypeText4.setText(mDatas.get(position).getChildrenText());
                childViewRoot.addView(viewTypeText4);
                break;
        }

    }

    private void clearOtherOpen(int i) {

        for (int m = 0; m < mDatas.size(); m++) {
            if (mDatas.get(m).isOpen() && m != i) {
                mDatas.get(m).setOpen(false);
                mDatas.get(m).setSelected(false);
                updateItem(m);
            }
        }

    }

}
