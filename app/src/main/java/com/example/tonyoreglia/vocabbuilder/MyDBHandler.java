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

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_WORDS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORD + " TEXT, " +
                COLUMN_DEFINITION + " TEXT" +
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
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word.get_word());
        values.put(COLUMN_DEFINITION, word.get_definition());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_WORDS, null, values);
        db.close();
    }

    public void updateWord(Word word) {
        this.deleteWord(word.get_word());
        this.addWord(word);
    }

    //delete word from database
    public void deleteWord(String word) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORD + "=\"" + word + "\";");
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

    public String databaseDefinitionToString(String word) {
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
                Log.i("INFO", "Inside print db definition 1");
                Log.i("INFO", c.getString(c.getColumnIndex(COLUMN_WORD)));
                Log.i("INFO", word);

                if(c.getString(c.getColumnIndex(COLUMN_WORD)).equals(word)) {
                    Log.i("INFO", "Inside print db definition 2");
                    dbString += c.getString(c.getColumnIndex(COLUMN_DEFINITION));
                }
//                dbString += c.getString(c.getColumnIndex("word"));
//                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
}









