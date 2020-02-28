package com.example.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /*
         New records adding methods
     */
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
    public void addCollection(String collectionName, int quantity, int userId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("collection_name",collectionName);
        values.put("quantity",quantity);
        values.put("favorite_collection",false);
        values.put("user_id",userId);
        db.insert(" collections",null,values);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addFlashcard(String wordPl, String wordEn, int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word_pl",wordPl);
        values.put("word_en",wordEn);
        values.put("points",20);
        values.put("favorite_flashcard",false);
        values.put("last_reviews_counter", 3);
        values.put("last_using_date",getCurrentDate());
        values.put("collection_id",collectionId);
        db.insert("flashcards",null,values);
    }

    /*
            Other methods
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return format.format(date).toString();
    }

    public int dayCounterBetweenTwoDates(String date1, String date2){
        long difference=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateStart = format.parse(date1);
            Date dateEnd= format.parse(date2);
            difference = dateEnd.getTime()-dateStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) (difference/(1000*60*60*24));
    }

    /*
        Get user data
     */

    // Cursors

    private Cursor getAllRecordsFromUsersByLogin(String login){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM users" +
                " WHERE login ='"+login +
                "'ORDER BY login;",null);
        cursor.moveToFirst();
        return cursor;
    }

    // Getters
    public int getUserId(String login){
        Cursor cursor = getAllRecordsFromUsersByLogin(login);
        return cursor.getInt(0);
    }
    public String getUserName(String login){
        Cursor cursor = getAllRecordsFromUsersByLogin(login);
        return cursor.getString(1);
    }
    public String getUserPassword(String login){
        Cursor cursor = getAllRecordsFromUsersByLogin(login);
        cursor.moveToFirst();
        return cursor.getString(3);
    }

    // Other

    public boolean doesTheLoginExist(String login){
        return getAllRecordsFromUsersByLogin(login).moveToFirst();
    }

    /*
        Get collections data
     */

    // Cursors

    private Cursor getAllRecordsCursorFromCollectionsByUserId(int userId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM collections" +
                " WHERE user_id='"+userId +
                "' ORDER BY favorite_collection DESC, collection_name ASC;",null);
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getAllRecordsCursorFromCollectionUsingLogin(String login){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        cursor.moveToFirst();
        return cursor;
    }

    private Cursor getCollectionCursorUsingCollectionId(int collectionId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM collections" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY collection_name;",null);
        cursor.moveToFirst();
        return cursor;
    }

    //Getters

    public int getCollectionId(String login, int position){
        Cursor cursor= getAllRecordsCursorFromCollectionUsingLogin(login);
        cursor.moveToPosition(position);
        return getCollectionId(cursor);
    }

    public int getCollectionId(Cursor cursor){
        return cursor.getInt(0);
    }
    public String getUserCollectionName(Cursor cursor){
        return cursor.getString(1);
    }
    public int getCollectionQuantityName(Cursor cursor){
        return  cursor.getInt(2);
    }
    public int getFavouriteCollection(Cursor cursor){
        return cursor.getInt(3);
    }
    public int getCountOfUserCollections(String login){
        Cursor cursor = getAllRecordsCursorFromCollectionsByUserId(getUserId(login));
        return  cursor.getCount();
    }

    // Setters

    public void setFavourite(int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE collections " +
                "SET favorite_collection = '"+ ((getFavouriteCollection(getCollectionCursorUsingCollectionId(collectionId))-1)*(-1)) +
                "' WHERE collection_id = '"+collectionId+"';");
    }

    // Other

    public void deleteCurrentCollection(int collectionId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE" +
                " FROM collections" +
                " WHERE collection_id = '"+collectionId+"';");

        db.execSQL("DELETE" +
                " FROM flashcards" +
                " WHERE collection_id = '"+collectionId+"';");
    }

    // ArrayLists

    public ArrayList<FlashcardMenuItem> returnCollectionsArrayList(String login){
        ArrayList<FlashcardMenuItem> exampleList = new ArrayList<>();
        Cursor cursor = getAllRecordsCursorFromCollectionUsingLogin(login);
        for(int i =0 ; i<getCountOfUserCollections(login); i++) {
            cursor.moveToPosition(i);
            exampleList.add(new FlashcardMenuItem(
                    getFavouriteCollection(cursor)>0,
                    getUserCollectionName(cursor),
                    getCountOfCollectionItems(getCollectionId(cursor))+" elements",
                    getCollectionId(cursor)));
        }
        return exampleList;
    }

    /*
            Get items data
     */

    // Cursors

    private Cursor getAllRecordsCursorFromFlashcardsByCollectionId(int collectionId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY favorite_flashcard DESC, word_pl ASC;",null);
        return cursor;
    }

    private Cursor getAllRecordsCursorFromFlashcardsByCollectionIdOrderByPoints(int collectionId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE collection_id='"+collectionId +
                "' ORDER BY points DESC;",null);
        return cursor;
    }

    private Cursor getRecordCursorFromFlashcardsUsingCollectionId(int flashcardId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *" +
                " FROM flashcards" +
                " WHERE flashcard_id='"+flashcardId +
                "' ORDER BY flashcard_id;",null);
        cursor.moveToFirst();
        return cursor;
    }

    // Getters

    public int getItemIdUsingIndex(Cursor cursor){
        return cursor.getInt(0);
    }

    public String getPolishMining(Cursor cursor){
        return cursor.getString(1);
    }

    public String getEnglishMining(Cursor cursor){
        return cursor.getString(2);
    }

    public int getPoints(Cursor cursor){
        return  cursor.getInt(3);
    }

    public boolean getFavouriteFlashcard(Cursor cursor){
        return cursor.getInt(4)>0;
    }

    public int getFavouriteFlashcardInt(Cursor cursor){
        return cursor.getInt(4);
    }

    public String getLastUsingDate(Cursor cursor){
        return  cursor.getString(5);
    }

    public int getLastReviewingCounter(Cursor cursor){
        return cursor.getInt(6);
    }

    public int getCountOfCollectionItems(int collectionId){
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionId(collectionId);
        return  cursor.getCount();
    }

    // Setters

    public void setFlashcardFavourite(int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE flashcards " +
                "SET favorite_flashcard = '"+ (getFavouriteFlashcardInt(getRecordCursorFromFlashcardsUsingCollectionId(flashcardId))-1)*(-1) +
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

    public void increaseReviewCounterByOne(int flashcardId){
        setReviewCounter(
                getLastReviewingCounter(
                        getRecordCursorFromFlashcardsUsingCollectionId(flashcardId))+1,
                flashcardId);
    }

    public void setLastUsingDate(String date, int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE flashcards " +
                "SET last_reviews_counter = '"+ date +
                "' WHERE flashcard_id = '"+flashcardId+"';");
    }

    // Other

    public void deleteCurrentItem(int flashcardId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE" +
                " FROM flashcards" +
                " WHERE flashcard_id = '"+flashcardId+"';");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recalculatePoints(Cursor cursor){
        setPoints(
                dayCounterBetweenTwoDates(getLastUsingDate(cursor),getCurrentDate())*2
                        +getLastReviewingCounter(cursor)*3
                        +getFavouriteFlashcardInt(cursor)*5,
                getItemIdUsingIndex(cursor));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recalculatePointsForRecordsUsingCollectionId(int collectionId){
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionId(collectionId);
        for(int i = 0; i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            recalculatePoints(cursor);
        }
    }

    // ArrayLists

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<FlashcardItem> returnItemsArrayListOfCollection(int collectionId){
        ArrayList<FlashcardItem> collectionItemsArrayList = new ArrayList<>();
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionId(collectionId);
        for(int i =0 ; i<getCountOfCollectionItems(collectionId); i++) {
            cursor.moveToPosition(i);
            collectionItemsArrayList.add(new FlashcardItem(
                    getPolishMining(cursor),
                    getEnglishMining(cursor),
                    getItemIdUsingIndex(cursor),
                    getFavouriteFlashcard(cursor)));
        }
        return collectionItemsArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<FlashcardItem> returnItemsArrayListOfCollectionSortedByPoints(int collectionId, int size){
        ArrayList<FlashcardItem> collectionItemsArrayList = new ArrayList<>();
        recalculatePointsForRecordsUsingCollectionId(collectionId);
        Cursor cursor = getAllRecordsCursorFromFlashcardsByCollectionIdOrderByPoints(collectionId);
        if(cursor.getCount()>size){
            for (int i=0; i<size;i++){
                cursor.moveToPosition(i);
                collectionItemsArrayList.add(new FlashcardItem(
                        getPolishMining(cursor),
                        getEnglishMining(cursor),
                        getItemIdUsingIndex(cursor),
                        getFavouriteFlashcard(cursor)));;
            }
        }
        else {
            for (int i=0; i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                collectionItemsArrayList.add(new FlashcardItem(
                        getPolishMining(cursor),
                        getEnglishMining(cursor),
                        getItemIdUsingIndex(cursor),
                        getFavouriteFlashcard(cursor)));
            }
        }
        return collectionItemsArrayList;
    }
}
