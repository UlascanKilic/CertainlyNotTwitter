package com.example.ulas_.certainlynottwitter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME = "mylist.db";

    public  static final String TABLE_USERS = "USERS";
    public  static final String TABLE_MESSAGES = "MESSAGES";


    public  static final String COL_USERS_ID = "ID";
    public  static final String COL_USERS_USERNAME = "USERNAME";
    public  static final String COL_USERS_PASSWORD = "PASSWORD";
    public  static final String COL_USERS_AVATAR = "AVATAR";

    public  static final String COL_MESSAGES_ID = "ID";
    public  static final String COL_MESSAGES_MESSAGE= "MESSAGE";
    public  static final String COL_MESSAGES_USERID = "USER_ID";
    public  static final String COL_MESSAGES_DATE = "DATE";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE "+TABLE_USERS+" ("+COL_USERS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COL_USERS_USERNAME+" TEXT,"+
                COL_USERS_PASSWORD+" TEXT," +
                COL_USERS_AVATAR+" INTEGER)";

        String createTableMessages = "CREATE TABLE "+TABLE_MESSAGES+" ("+COL_MESSAGES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COL_MESSAGES_MESSAGE+" TEXT,"+
                COL_MESSAGES_USERID+" INTEGER,"+
                COL_MESSAGES_DATE+" TEXT)";
        Log.i("LogDeneme", "database create girildi.");
        db.execSQL(createTableMessages);
        db.execSQL(createTableUsers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.execSQL("DROP IF TABLE EXISTS "+TABLE_MESSAGES);
      //  db.execSQL("DROP IF TABLE EXISTS "+TABLE_USERS);
    }

    public boolean addUserData(String username,String password,int avatarIndex){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_USERS_USERNAME,username);
        contentValues.put(COL_USERS_PASSWORD,password);
        contentValues.put(COL_USERS_AVATAR,avatarIndex);

        long result = db.insert(TABLE_USERS,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public  boolean addTweety(String message,int user_id,String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_MESSAGES_MESSAGE,message);
        contentValues.put(COL_MESSAGES_USERID,user_id);
        contentValues.put(COL_MESSAGES_DATE,date);

        long result = db.insert(TABLE_MESSAGES,null,contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }

    }
    public Cursor getUserTweety(String user_name){
        int user_id;
      //  Cursor c = getUserID(user_name);
     //   user_id = c.getInt(c.getColumnIndex("ID"));
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor userTweeties = db.rawQuery("SELECT USERS.USERNAME,MESSAGES.MESSAGE,USERS.AVATAR,MESSAGES.ID,MESSAGES.DATE from USERS,MESSAGES where " +
                "USERS.ID=MESSAGES.USER_ID and USERS.USERNAME='"+ user_name +"' ORDER BY MESSAGES.ID DESC",null);

        return  userTweeties;
    }
    public Cursor getAllTweeties(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor allTweeties = db.rawQuery("SELECT USERS.USERNAME,MESSAGES.MESSAGE,USERS.AVATAR,MESSAGES.ID,MESSAGES.DATE from USERS,MESSAGES where " +
                "USERS.ID=MESSAGES.USER_ID ORDER BY MESSAGES.ID DESC ",null);

        return  allTweeties;
    }
    public  Cursor getUserID(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor userID = db.rawQuery("SELECT USERS.ID FROM USERS WHERE USERS.USERNAME='"+username+"'",null);

        return userID;
    }

    public Cursor checkUser(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor checkCursor = db.rawQuery("Select * from USERS where USERS.USERNAME='"+username+"' " +
                "and USERS.PASSWORD='"+password+"'",null);
        return  checkCursor;

    }
    public Cursor checkMessage(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor checkCursor = db.rawQuery("Select * from MESSAGES where ID="+id,null);
        return  checkCursor;

    }
    public void DropDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE  "+TABLE_USERS);
        db.execSQL("DROP TABLE  "+TABLE_MESSAGES);

    }
    public void DeleteMessage(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM MESSAGES WHERE MESSAGES.ID="+id);
    }
    public  void UpdateMessage(int id,String newMessage){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE MESSAGES SET MESSAGE='"+newMessage+"' WHERE ID="+id);
    }
}
