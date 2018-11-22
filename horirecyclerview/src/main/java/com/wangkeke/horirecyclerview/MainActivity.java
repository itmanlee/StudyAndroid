package com.wangkeke.horirecyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnStepItemCickListener {

    private RecyclerView recyclerView;

    private GalleryAdapter mAdapter;

    private RelativeLayout rootLayout;

    private LinearLayout rootToolsLayout;

    private List<StepToolModel> listData = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private Button btnStart,btnOpen,btnInsert,btnDelete,btnUpdateData;

    private float defaultAnimValueX = 0;
    private boolean isToolsOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler);
        rootLayout = findViewById(R.id.root_view);
        rootToolsLayout = findViewById(R.id.root_tools_layout);
        btnStart = findViewById(R.id.btn_start);
        btnOpen = findViewById(R.id.btn_open);
        btnInsert = findViewById(R.id.btn_insert);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdateData = findViewById(R.id.btn_update_data);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToolsOpen) {
                    startCloseToolsViewAnim();
                } else {
                    startOpenToolsViewAnim(false);
                }
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isToolsOpen){
                    startOpenToolsViewAnim(true);
//                    mAdapter.setCurrentSelectItem(1,true);
                }else {
                    mAdapter.setCurrentSelectItem(1,true);
                }
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepToolModel stepToolModel = new StepToolModel();
                stepToolModel.setText("动态插入的按钮");
                stepToolModel.setChildrenText("我是动态插入后的子数据");
                stepToolModel.setOpen(false);
                stepToolModel.setSelected(false);
                stepToolModel.setType(4);
                listData.add(2,stepToolModel);
                mAdapter.notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.remove(2);
                mAdapter.notifyDataSetChanged();
            }
        });

        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.get(2).setChildrenText("我被动态改了，我刷新出来了");
                mAdapter.updateItem(2);
            }
        });

        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(this, listData);
        mAdapter.setOnStepItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        initData();
    }

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int left = mRecyclerView.getChildAt(movePosition).getLeft();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(left, 0);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    private void initData() {

        for (int i = 0; i < 6; i++) {
            StepToolModel stepToolModel = new StepToolModel();
            stepToolModel.setText("按钮" + i);
            stepToolModel.setChildrenText("我是打开后的数据" + i);
            stepToolModel.setOpen(false);
            stepToolModel.setSelected(false);
            stepToolModel.setType(i);
            listData.add(stepToolModel);
        }


        View footView = LayoutInflater.from(this).inflate(R.layout.foot_view, rootLayout, false);
        mAdapter.setFooterView(footView);
//        View headView = LayoutInflater.from(this).inflate(R.layout.head_view,rootLayout,false);
//        mAdapter.setHeaderView(headView);
    }

    @Override
    public void onItemClick(int position) {
        smoothMoveToPosition(recyclerView, position);
    }

    private void startCloseToolsViewAnim() {
        if(isAnimRunning){
            return;
        }
        float curTranslationX = rootToolsLayout.getTranslationX();
        defaultAnimValueX = curTranslationX;
        // 获得当前按钮的位置
        ObjectAnimator animator;
        if (defaultAnimValueX == 0) {
            animator = ObjectAnimator.ofFloat(rootToolsLayout, "translationX", defaultAnimValueX, 1580);
        } else {
            animator = ObjectAnimator.ofFloat(rootToolsLayout, "translationX", curTranslationX, defaultAnimValueX);
        }
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        isToolsOpen = false;
    }

    private boolean isAnimRunning = false;

    private void startOpenToolsViewAnim(final boolean flag) {
        if(isAnimRunning){
            return;
        }
        float curTranslationX = rootToolsLayout.getTranslationX();
        // 获得当前按钮的位置
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootToolsLayout, "translationX", curTranslationX, defaultAnimValueX);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimRunning = false;
                if(flag){
                    mAdapter.setCurrentSelectItem(1,true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        isToolsOpen = true;
    }
}
