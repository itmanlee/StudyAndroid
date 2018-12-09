package com.wangkeke.roomsqlitedemo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {User.class}, version = 3)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    private static final Object sLock = new Object();

    public abstract UserDao userDao();

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE 'user' ADD COLUMN 'user_id' INTEGER NOT NULL");
        }
    };

    public static AppDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "user.db")
                        .addMigrations(MIGRATION_2_3)
                        //Room不允许在主线程中访问数据库，除非在buid的时候使用allowMainThreadQueries()方法
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }
}
