package com.example.pcadministrator.fitnesstracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

/**@author Floralba Sulce */

public class FitnessDB {
    private DbHelper dbHelper;

    public FitnessDB(Context context){
        dbHelper=new DbHelper(context);
    }

    /**
     * Method that passes the information (steps, calories and distance) to the database.
     */
    public long savingStepCounter(int steps,float calories,float distance){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbHelper.Steps,steps);
        contentValues.put(DbHelper.Calories,calories);
        contentValues.put(DbHelper.Distance,distance);
        long id=db.insert(DbHelper.DB_Table_Name,null,contentValues);
        Log.i("FitnessDB","Saved Steps counter to Database!");
        return id;
    }

    /**
     * Method that gets the data stored in the database.
     */
    public Cursor getData(){
    SQLiteDatabase db=dbHelper.getWritableDatabase();
        String[] columns={
                DbHelper.ID,
                DbHelper.Steps,
                DbHelper.Calories,
                DbHelper.Distance};
        Cursor cursor=db.query(DbHelper.DB_Table_Name,columns,null,null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }

    /**
     * Class that structures the entire database.
     */
    static class DbHelper extends SQLiteOpenHelper{
        private static final String DB_Name = " StepsDb ";
        private static final String DB_Table_Name = " FitnessInfo ";
        private static final String ID = "_id";
        public static final String Steps = "steps";
        public static final String Calories = "calories";
        public static final String Distance = "distance";
        private static final String Create_Db_Table = " Create Table " + DB_Table_Name + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                Steps + " INTEGER NOT NULL, " + Calories + " REAL, " + Distance + " REAL)";
        private static final String Drop_Table = "DROP TABLE IF EXISTS " + DB_Table_Name;
        private Context context;

        public DbHelper(Context context) {
            super(context, "StepsDb", null, 1);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Create_Db_Table);
            Log.i(" FitnessDb " , Create_Db_Table + " table created!! ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(Drop_Table);
            Log.i(" FitnessDb " ,DB_Table_Name + " Dropped ");
            onCreate(db);
        }
    }
}
