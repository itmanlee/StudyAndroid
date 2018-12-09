package com.wangkeke.roomsqlitedemo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user")
    List<User> getAll();

    @Query("select * from user where userId in (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("select * from user where userId = (:userId)")
    User getUserById(int userId);

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);
}
