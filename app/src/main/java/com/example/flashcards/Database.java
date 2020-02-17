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
                        " user_id integer primary key autoincrement," +
                        " name text," +
                        " login text," +
                        " password text);" +
                        "");
        db.execSQL(
                "create table collections(" +
                        " collection_id integer primary key autoincrement," +
                        " collection_name text," +
                        " quantity integer," +
                        " image_resources integer," +
                        " user_id integer," +
                        " FOREIGN KEY(user_id) REFERENCES users(user_id));" +
                        "");
        db.execSQL(
                "create table flashcards(" +
                        " flashcard_id integer primary key autoincrement," +
                        " word_pl text," +
                        " word_en text," +
                        " collection_id integer," +
                        " FOREIGN KEY(collection_id) REFERENCES collections(collection_id));" +
                        "");
    }
    public void addCollection(String collectionName, int quantity,int imageResources, int userId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(" collection_name",collectionName);
        values.put(" quantity",quantity);
        values.put(" image_resources",imageResources);
        values.put(" user_id",userId);
        db.insert(" collections",null,values);
    }
    public void addUser(String name, String login, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("login",login);
        values.put("password",password);
        db.insert("users",null,values);
        addCollection("main",0,R.drawable.ic_cancel_red_40dp,getUserId(login));
        addCollection("main1",0,R.drawable.ic_cancel_red_40dp,getUserId(login));
        db.close();
    }
    public void addFlashcard(String wordPl, String wordEn, int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word_pl",wordPl);
        values.put("word_en",wordEn);
        values.put("collection_id",collectionId);
        db.insert("flashcards",null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
        Get user data
     */
    private Cursor getAllRecordsFromUsersByLogin(String login){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE login ='"+login+"'ORDER BY login;",null);
        cursor.moveToFirst();
        return cursor;
    }
    public int getUserId(String login){
        Cursor cursor = getAllRecordsFromUsersByLogin(login);
        return cursor.getInt(0);
    }
    public String getUserName(String login){
        Cursor cursor = getAllRecordsFromUsersByLogin(login);
        return cursor.getString(1);
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

    private Cursor getAllRecordsCursorFromCollectionsByUserId(int userId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM collections" +
                " WHERE user_id='"+userId +
                "' ORDER BY collection_name;",null);
        return cursor;
    }
    public int getCollectionIdUsingIndex(String login, int index){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        cursor.moveToPosition(index);
        return cursor.getInt(0);
    }
    public String getUserCollectionNameUsingIndex(String login, int index){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        cursor.moveToPosition(index);
        return cursor.getString(1);
    }
    public int getCollectionQuantityNameUsingIndex(String login, int index){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        cursor.moveToPosition(index);
        return  cursor.getInt(2);
    }
    public int getCollectionImageResourcesNameUsingIndex(String login, int index){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        cursor.moveToPosition(index);
        return cursor.getInt(3);
    }
    public int numberOfCollectionElements(String login){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        return  cursor.getCount();
    }

    public void deleteCurrentCollection(int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE" +
                " FROM collections" +
                " WHERE collection_id = '"+collectionId+"';");
    }

    public ArrayList<FlashcardMenuItem> returnCollectionsArrayList(String login){
        ArrayList<FlashcardMenuItem> exampleList = new ArrayList<>();
        for(int i =0 ; i<numberOfCollectionElements(login); i++) {
            exampleList.add(new FlashcardMenuItem(
                    getCollectionImageResourcesNameUsingIndex(login,i),
                    getUserCollectionNameUsingIndex(login,i),
                    getCollectionQuantityNameUsingIndex(login,i)+" elements",
                    getCollectionIdUsingIndex(login,i)));
        }
        return exampleList;
    }

    /*
            get items data
     */

    private Cursor getAllRecordsCursorFromFlashcardsByCollectionId(int collectionId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY word_pl;",null);
        return cursor;
    }

    public int getCollectionId(int collectionId){
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionId(collectionId);
        return cursor.getInt(0);
    }

    public int numberItemOfCollection(int collectionId){
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionId(collectionId);
        return  cursor.getCount();
    }

    public ArrayList<FlashcardItem> returnItemsArrayListOfCollection(int collectionId){
        ArrayList<FlashcardItem> exampleList = new ArrayList<>();
        for(int i =0 ; i<numberItemOfCollection(collectionId); i++) {
            exampleList.add(new FlashcardItem("dobry","good"));
        }
        return exampleList;
    }
}
