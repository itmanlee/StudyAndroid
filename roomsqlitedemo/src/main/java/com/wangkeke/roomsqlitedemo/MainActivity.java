package com.wangkeke.roomsqlitedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnInsert,btnSelect;

    private EditText etName,etSex;

    private AppDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        database = AppDataBase.getInstance(this);
    }

    private void initView() {

        btnInsert = findViewById(R.id.btn_insert);
        btnSelect = findViewById(R.id.btn_select);
        etName = findViewById(R.id.et_name);
        etSex = findViewById(R.id.et_sex);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllUser();
            }
        });
    }

    private void insertUser() {

        String name = etName.getText().toString();
        String sex = etSex.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(sex)){
            Toast.makeText(this, "姓名和性别不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUserName(name);
        user.setSex(sex);
        database.userDao().insertUser(user);
    }

    private void selectAllUser() {

        List<User> userList = database.userDao().getAll();

        for (int i = 0; i < userList.size(); i++) {
            Log.e("wangkeke","user("+i+") = "+userList.get(i).toString());
        }
    }
}
