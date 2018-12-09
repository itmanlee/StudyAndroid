package com.wangkeke.roomsqlitedemo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/*默认情况下，Room使用类名作为表名，使用字段名作为列名*/
@Entity(tableName = "user",indices = {@Index(value = {"user_name"},unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private Integer userId;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "sex")
    private String sex;

    @Ignore
    private String introduce;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}
