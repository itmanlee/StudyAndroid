package com.wangkeke.horirecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GalleryAdapterCopy extends RecyclerView.Adapter<GalleryAdapterCopy.ViewHolder> {

    private LayoutInflater mInflater;
    private List<StepToolModel> mDatas;

    OnStepItemCickListener listener;

    private List<String> listOpenPosition = new ArrayList<>();
    private Disposable startDispose;

    public GalleryAdapterCopy(Context context, List<StepToolModel> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        RelativeLayout layoutSelect;
        TextView tvBtnText, tvChildrenText;
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
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_recycler, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvBtnText = view.findViewById(R.id.tv_btn_text);
//        viewHolder.tvChildrenText = view.findViewById(R.id.tv_children_text);
        viewHolder.layoutSelect = view.findViewById(R.id.layout_select);
        viewHolder.layoutSelect.setVisibility(View.GONE);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.tvBtnText.setText(mDatas.get(i).getText());
//        viewHolder.tvChildrenText.setText(mDatas.get(i).getChildrenText());

        if (mDatas.get(i).isSelected()) {
            viewHolder.tvBtnText.setBackgroundColor(Color.parseColor("#111111"));
        } else {
            viewHolder.tvBtnText.setBackgroundColor(Color.parseColor("#888888"));
        }

        viewHolder.layoutSelect.setVisibility(mDatas.get(i).isOpen() ? View.VISIBLE : View.GONE);

        viewHolder.tvBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(i);

                startDispose = io.reactivex.Observable.timer(600, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                clearOtherOpen(i);
                                if (mDatas.get(i).isOpen()) {
                                    mDatas.get(i).setOpen(false);
                                    mDatas.get(i).setSelected(false);
                                    if (listOpenPosition.contains("" + i)) {
                                        listOpenPosition.remove("" + i);
                                    }
                                } else {
                                    mDatas.get(i).setOpen(true);
                                    mDatas.get(i).setSelected(true);
                                    if (!listOpenPosition.contains(i + "")) {
                                        listOpenPosition.add("" + i);
                                    }
                                }
                                updateItem(i);
                            }
                        });


            }
        });
    }

    private void clearOtherOpen(int i) {

        for (int j = 0; j < listOpenPosition.size(); j++) {
            if (Integer.parseInt(listOpenPosition.get(j)) != i) {
                if (mDatas.get(Integer.parseInt(listOpenPosition.get(j))).isOpen()) {
                    mDatas.get(Integer.parseInt(listOpenPosition.get(j))).setOpen(false);
                    mDatas.get(Integer.parseInt(listOpenPosition.get(j))).setSelected(false);
                    updateItem(Integer.parseInt(listOpenPosition.get(j)));
                }
            }
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
}
