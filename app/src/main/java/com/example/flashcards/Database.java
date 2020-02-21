package com.example.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;

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
                        " favorite_collection boolean," +
                        " user_id integer," +
                        " FOREIGN KEY(user_id) REFERENCES users(user_id));" +
                        "");
        db.execSQL(
                "create table flashcards(" +
                        " flashcard_id integer primary key autoincrement," +
                        " word_pl text," +
                        " word_en text," +
                        " points int," +
                        " favorite_flashcard boolean," +
                        " last_using_date date," +
                        " last_reviews_counter int," +
                        " collection_id integer," +
                        " FOREIGN KEY(collection_id) REFERENCES collections(collection_id));" +
                        "");
    }
    public void addCollection(String collectionName, int quantity, int userId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("collection_name",collectionName);
        values.put("quantity",quantity);
        values.put("favorite_collection",false);
        values.put("user_id",userId);
        db.insert(" collections",null,values);
    }
    public void addUser(String name, String login, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("login",login);
        values.put("password",password);
        db.insert("users",null,values);
        addCollection("main",0,getUserId(login));
        db.close();
    }
    public void addFlashcard(String wordPl, String wordEn, int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word_pl",wordPl);
        values.put("word_en",wordEn);
        values.put("points",20);
        values.put("favorite_flashcard",false);
        values.put("last_reviews_counter", 0);
        values.put("last_using_date",new Date().getYear()+"-"+new Date().getMonth()+"-"+new Date().getDay());
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
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM users" +
                " WHERE login ='"+login +
                "'ORDER BY login;",null);
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
                "' ORDER BY favorite_collection DESC, collection_name ASC;",null);
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
    public boolean getFavouriteCollection(String login, int index){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        cursor.moveToPosition(index);
        return cursor.getInt(3)>0;
    }
    public int getFavouriteCollection(int collectionId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM collections" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY collection_name;",null);
        cursor.moveToFirst();
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

    public void setFavourite(int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE collections " +
                "SET favorite_collection = '"+ (getFavouriteCollection(collectionId)-1)*(-1) +
                "' WHERE collection_id = '"+collectionId+"';");
    }

    public ArrayList<FlashcardMenuItem> returnCollectionsArrayList(String login){
        ArrayList<FlashcardMenuItem> exampleList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM collections" +
                " WHERE user_id='"+getUserId(login) +
                "' ORDER BY favorite_collection DESC, collection_name ASC;",null);
        for(int i =0 ; i<numberOfCollectionElements(login); i++) {
            cursor.moveToPosition(i);
            exampleList.add(new FlashcardMenuItem(
                    cursor.getInt(3)>0,
                    cursor.getString(1),
                    numberItemOfCollection(cursor.getInt(0))+" elements",
                    cursor.getInt(0)));
        }
        return exampleList;
    }

    /*
            Get items data
     */

    private Cursor getAllRecordsCursorFromFlashcardsByCollectionId(int collectionId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY word_pl;",null);
        return cursor;
    }

    public int getItemIdUsingIndex(Cursor cursor){
        return cursor.getInt(0);
    }
    public String getPolishMining(Cursor cursor){
        return cursor.getString(1);
    }

    public String getEnglishMining(Cursor cursor){
        return cursor.getString(2);
    }

    public int numberItemOfCollection(int collectionId){
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionId(collectionId);
        return  cursor.getCount();
    }

    public boolean getFavouriteFlashcard(Cursor cursor,int i){
        cursor.moveToPosition(i);
        return cursor.getInt(4)>0;
    }

    public int getFavouriteFlashcard(int flashcardId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE flashcard_id='"+flashcardId +
                "' ORDER BY flashcard_id;",null);
        cursor.moveToFirst();
        return cursor.getInt(4);
    }

    public void setFlashcardFavourite(int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE flashcards " +
                "SET favorite_flashcard = '"+ (getFavouriteFlashcard(flashcardId)-1)*(-1) +
                "' WHERE flashcard_id = '"+flashcardId+"';");
    }

    public void setPoints(int points, int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE flashcards " +
                "SET points = '"+ points +
                "' WHERE flashcard_id = '"+flashcardId+"';");
    }

    public void setReviewCounter(int counter, int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE flashcards " +
                "SET last_reviews_counter = '"+ counter +
                "' WHERE flashcard_id = '"+flashcardId+"';");
    }

    public void setLastUsingDate(Date date,int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE flashcards " +
                "SET last_reviews_counter = '"+ date +
                "' WHERE flashcard_id = '"+flashcardId+"';");
    }


    public ArrayList<FlashcardItem> returnItemsArrayListOfCollection(int collectionId){
        ArrayList<FlashcardItem> collectionItemsArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY word_pl;",null);
        for(int i =0 ; i<numberItemOfCollection(collectionId); i++) {
            cursor.moveToPosition(i);
            collectionItemsArrayList.add(new FlashcardItem(
                    getPolishMining(cursor),
                    getEnglishMining(cursor),
                    getItemIdUsingIndex(cursor),
                    getFavouriteFlashcard(cursor,i)));
        }
        return collectionItemsArrayList;
    }

    public ArrayList<FlashcardItem> returnItemsArrayListOfCollectionSortedByPoints(int collectionId,int size){
        ArrayList<FlashcardItem> collectionItemsArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY word_pl;",null);
        if(cursor.getCount()>size){
            for (int i=0; i<size;i++){
                cursor.moveToPosition(i);
                collectionItemsArrayList.add(new FlashcardItem(
                        getPolishMining(cursor),
                        getEnglishMining(cursor),
                        getItemIdUsingIndex(cursor),
                        getFavouriteFlashcard(cursor,i)));;
            }
        }
        else {
            for (int i=0; i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                collectionItemsArrayList.add(new FlashcardItem(
                        getPolishMining(cursor),
                        getEnglishMining(cursor),
                        getItemIdUsingIndex(cursor),
                        getFavouriteFlashcard(cursor,i)));
            }
        }
        return collectionItemsArrayList;
    }

    public void deleteCurrentItem(int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE" +
                " FROM flashcards" +
                " WHERE flashcard_id = '"+flashcardId+"';");
    }
}
