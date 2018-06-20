package com.example.bot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bot.model.Msg;

import java.util.ArrayList;

public class ChatMsgDao {
    private final DBHelper helper;

    public ChatMsgDao(Context context) {
        helper = new DBHelper(context);
    }


    /**
     * 添加新信息
     *
     * @param msg
     */
    public int insert(Msg msg) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBcolumns.MSG_TYPE, msg.getType());
        values.put(DBcolumns.MSG_CONTENT, msg.getContent());
        values.put(DBcolumns.MSG_ISCOMING, msg.getIsComing());
        values.put(DBcolumns.MSG_DATE, msg.getDate());
        db.insert(DBcolumns.TABLE_MSG, null, values);
        int id=queryTheLastMsgId();
        db.close();
        return id;
    }


    /**
     * 清空所有聊天记录
     */
    public void deleteTableData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DBcolumns.TABLE_MSG, null, null);
        db.close();
    }


    /**
     * 根据msgid，删除对应聊天记录
     *
     * @return
     */
    public void deleteMsgById(int msgid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long row = db.delete(DBcolumns.TABLE_MSG, DBcolumns.MSG_ID + " = ?", new String[]{"" + msgid});
        db.close();
    }

    /**
     * 查询列表,每页返回15条,依据id逆序查询，将时间最早的记录添加进list的最前面
     *
     * @return
     */
    public ArrayList<Msg> queryMsg(int offset) {
        ArrayList<Msg> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSG + " order by " + DBcolumns.MSG_ID + " desc limit ?,?";
        String[] args = new String[]{String.valueOf(offset), "15"};
        Cursor cursor = db.rawQuery(sql, args);
        Msg msg;
        while (cursor.moveToNext()) {
            msg = new Msg();
            msg.setMsgId(cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ID)));
            msg.setType(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_TYPE)));
            msg.setContent(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_CONTENT)));
            msg.setIsComing(cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ISCOMING)));
            msg.setDate(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_DATE)));
            list.add(0, msg);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询最新一条记录
     *
     * @return
     */
    public Msg queryTheLastMsg() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSG + " order by " + DBcolumns.MSG_ID + " desc limit 1";
        String[] args = new String[]{};
        Cursor cursor = db.rawQuery(sql, args);

        Msg msg = null;
        while (cursor.moveToNext()) {
            msg = new Msg();
            msg.setMsgId(cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ID)));
            msg.setType(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_TYPE)));
            msg.setContent(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_CONTENT)));
            msg.setIsComing(cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ISCOMING)));
            msg.setDate(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_DATE)));
        }
        cursor.close();
        db.close();
        return msg;
    }

    /**
     * 查询最新一条记录的id
     *
     * @return
     */
    private int queryTheLastMsgId() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select " + DBcolumns.MSG_ID + " from " + DBcolumns.TABLE_MSG + " order by " + DBcolumns.MSG_ID + " desc limit 1";
        String[] args = new String[]{};
        Cursor cursor = db.rawQuery(sql, args);
        int id = -1;
        if (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ID));
        }
        cursor.close();
        db.close();
        return id;
    }

}
