package com.example.pcadministrator.fitnesstracking;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/** @author  Floralba Sulce */

public class DatabaseViewer extends AppCompatActivity {
    private FitnessDB fitnessDB;
    private SimpleCursorAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_viewer);
        displayListView();
    }

    /**
     * Method that displays the database entries in ListView.
     */

    private void displayListView(){
        fitnessDB = new FitnessDB(this);
        Cursor cursor = fitnessDB.getData();

        String[] columns = new String[]{
                FitnessDB.DbHelper.Steps,
                FitnessDB.DbHelper.Calories,
                FitnessDB.DbHelper.Distance
        };

        int[] data = new int[]{
                R.id.steps_text,
                R.id.calories_text,
                R.id.distance_text
        };

        infoAdapter = new SimpleCursorAdapter(
                this, R.layout.listviewtext,
                cursor,
                columns,
                data,
                0);

        ListView listview = (ListView)findViewById(R.id.list_view);
        listview.setAdapter(infoAdapter);
    }
}
