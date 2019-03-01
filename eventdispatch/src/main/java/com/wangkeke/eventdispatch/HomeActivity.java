package com.wangkeke.eventdispatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatas = new ArrayList<>();
        mDatas.add("张三年");
        mDatas.add("13231");
        mDatas.add("房价肯");
        mDatas.add("发手动");
        mDatas.add("看是的");
        mDatas.add("08科技发达是");
        mDatas.add("反对发射点");
        mDatas.add("发射点发三个");
        mDatas.add("和地方豆腐干");

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NormalAdapter(mDatas));

    }
}
