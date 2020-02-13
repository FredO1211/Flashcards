package com.example.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Flashcards_database.db";
    private static int databaseVersion=1;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table users(" +
                        "user_id integer primary key autoincrement," +
                        " name text," +
                        " login text," +
                        " password text);" +
                        "");
        db.execSQL(
                "create table collections(" +
                        "collection_id integer primary key autoincrement," +
                        " collection_name text," +
                        " quantity integer," +
                        " image_resources integer," +
                        " user_id integer);" +
                        "");
        db.execSQL(
                "create table flashcards(" +
                        " flashcard_id integer primary key autoincrement," +
                        " word_pl text," +
                        " word_en text," +
                        " collection_id integer);" +
                        "");
    }
    public void addUser(String name, String login, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("login",login);
        values.put("password",password);
        db.insert("users",null,values);
        addCollection("Main",0,R.drawable.ic_cancel_red_40dp,0);
        addCollection("Main",0,R.drawable.ic_cancel_red_40dp,0);
        db.close();
    }
    public void addCollection(String collectionName, int quantity,int imageResources, int userId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("collection_name",collectionName);
        values.put("quantity",quantity);
        values.put("image_resources",imageResources);
        values.put("user_id",userId);
        db.insert("collections",null,values);
    }
    public void addFlashcard(String wordPl, String wordEn, int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word_pl",wordPl);
        values.put("word_en",wordEn);
        values.put("collection_id",collectionId);
        db.insert("flashcards",null,values);
    }

    /*
        Get user data
     */
    private Cursor getAllRecordsFromUsersByLogin(String login){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE login ='"+login+"'ORDER BY login;",null);
        return cursor;
    }


    public int getUserId(int user_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id FROM collections WHERE user_id ='"+user_id+"';",null);
        return cursor.getInt(0);
    }


    public String getUserName(String login){
        return getAllRecordsFromUsersByLogin(login).getString(1);
    }
    public boolean doesTheLoginExist(String login){
        return getAllRecordsFromUsersByLogin(login).moveToFirst();
    }
    public String getUserPassword(String login){
        Cursor cursor = getAllRecordsFromUsersByLogin(login);
        cursor.moveToFirst();
        return cursor.getString(3);
    }
    /*
        Get collections data
     */
    private Cursor getAllRecordsFromCollectionsByUserId(int userId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM collections" +
                " WHERE user_id='"+userId +
                "' ORDER BY collection_name;",null);
        return cursor;
    }
    public int numbersOfCollectionElements(int userId){
        return getAllRecordsFromCollectionsByUserId(userId).getCount();
    }


    public String getUserCollectionNameUsingIndex(String login, int index){
       if(getAllRecordsFromUsersByLogin("a").moveToFirst()){
           return "Number" + 0;
       }
       else
           return "Dain";
    }


    public int getCollectionQuantityNameUsingIndex(int userId, int index){
        getAllRecordsFromCollectionsByUserId(userId).moveToPosition(index);
        return getAllRecordsFromCollectionsByUserId(userId).getInt(2);
    }
    public int getCollectionImageResourcesNameUsingIndex(int userId, int index){
        getAllRecordsFromCollectionsByUserId(userId).moveToPosition(index);
        return getAllRecordsFromCollectionsByUserId(userId).getInt(3);
    }
    public ArrayList<FlashcardMenuItem> returnArrayListOfFlashcardMenuItem(String login){
        ArrayList<FlashcardMenuItem> exampleList = new ArrayList<>();
        for(int i =0 ; i<10; i++) {
            exampleList.add(new FlashcardMenuItem(R.drawable.ic_cancel_red_40dp, getUserCollectionNameUsingIndex("admin",0), "0"));
        }
        return exampleList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
