package com.alford.apmevaluation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK = "create table book (" +
            "id integer primary key autoincrement, " +
            "author text, " +
            "price real," +
            "pages integer, " +
            "name text)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK); // 执行这句才会创建表
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在这里面可以把旧的表 drop掉 从新创建新表，
        // 但如果数据比较重要更好的做法还是把旧表的数据迁移到新表上，比如升级qq聊天记录被删掉肯定招骂
    }
}
