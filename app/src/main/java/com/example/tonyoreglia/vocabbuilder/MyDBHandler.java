package com.example.tonyoreglia.vocabbuilder;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "words.db";
    private static final String TABLE_WORDS = "words";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_DEFINITION = "definition";
    private static final String COLUMN_PARTOFSPEECH = "partOfSpeech";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_WORDS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORD + " TEXT, " +
                COLUMN_DEFINITION + " TEXT, " +
                COLUMN_PARTOFSPEECH + " TEXT" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        onCreate(db);
    }

    //add new row to the database
    public void addWord(Word word) {

        //ContentValues values = new ContentValues();
        Log.i("INFO", "number of definitions: " + word.get_definition().size());
        for(int i=0; i < word.get_definition().size(); i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WORD, word.get_word());
            values.put(COLUMN_DEFINITION, word.get_definition().get(i));
            values.put(COLUMN_PARTOFSPEECH, word.get_partOfSpeech().get(i));
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_WORDS, null, values);
            values.clear();
            db.close();
        }
        if(word.get_definition().size() == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WORD, word.get_word());
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_WORDS, null, values);
            db.close();
        }
    }

    public void updateWord(Word word) {
        this.deleteWord(word.get_word());
        this.addWord(word);
    }

    //delete word from database
    public void deleteWord(String word) {
        SQLiteDatabase db = getWritableDatabase();
        String formatted_word = word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
        db.execSQL("DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORD + "=\"" + formatted_word + "\";");
    }

    public boolean checkForWord(String word) {
        SQLiteDatabase db = getWritableDatabase();
        String query  = "SELECT * FROM " + TABLE_WORDS + " WHERE 1";

        //cursor point to a location in you results
        Cursor c = db.rawQuery(query, null);
        //move to the first row in the results
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //try changing this to the constant COLUMN_WORD
            if(!c.getString(c.getColumnIndex(COLUMN_WORD)).equals(null)) {  ///THIS CHANGE UNCHECKED
                if(c.getString(c.getColumnIndex(COLUMN_WORD)).equals(word)) {
                    return true;
                }
            }
            c.moveToNext();
        }
        return false;
    }

    //print out the database words as a string
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query  = "SELECT * FROM " + TABLE_WORDS + " WHERE 1";

        //cursor point to a location in you results
        Cursor c = db.rawQuery(query, null);
        //move to the first row in the results
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //try changing this to the constant COLUMN_WORD
            if(c.getString(c.getColumnIndex(COLUMN_WORD)) != null) {
                dbString += c.getString(c.getColumnIndex(COLUMN_WORD));
                //dbString += c.getString(c.getColumnIndex(COLUMN_DEFINITION));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    //print out the word definitions
    public String databaseDefinitionToString(String word) {
        StringBuilder dbString = new StringBuilder();
        int definitionCounter = 1;
        SQLiteDatabase db = getWritableDatabase();
        String query  = "SELECT * FROM " + TABLE_WORDS + " WHERE 1";

        //cursor point to a location in you results
        Cursor c = db.rawQuery(query, null);
        //move to the first row in the results
        c.moveToFirst();

        while(!c.isAfterLast()) {
            //try changing this to the constant COLUMN_WORD
            if(c.getString(c.getColumnIndex(COLUMN_WORD)) != null) {
                Log.i("INFO", "Inside print db definition 1");
                Log.i("INFO", c.getString(c.getColumnIndex(COLUMN_WORD)));
                Log.i("INFO", word);

                if(c.getString(c.getColumnIndex(COLUMN_WORD)).equals(word)) {
                    Log.i("INFO", "Inside print db definition");
                    dbString.append(definitionCounter + ". ");
                    dbString.append(c.getString(c.getColumnIndex(COLUMN_DEFINITION)));
                    String partOfSpeech = c.getString(c.getColumnIndex(COLUMN_PARTOFSPEECH));
                    Log.i("INFO", "Part of Speech: " + partOfSpeech);
                    if(partOfSpeech.equals("null")) {
                        Log.i("INFO", "No part of speech being added to word string");
                    }
                    else {
                        dbString.append("; " + partOfSpeech);
                    }
                    dbString.append("\n");
                    definitionCounter++;
                }
//                dbString += c.getString(c.getColumnIndex("word"));
//                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString.toString();
    }
}









