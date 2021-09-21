package com.example.saveimagetosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity_List_Things extends AppCompatActivity {

    ListView listViewAllThings;
    Button btnAddToList, btnSearchInList;
    DatabaseHelper database;
    ArrayList<ThingModel> arrayThing;
    ArrayAdapter<ThingModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_things);

        btnAddToList = findViewById(R.id.btnAddToList);
        btnSearchInList = findViewById(R.id.btnSearch);
        listViewAllThings = findViewById(R.id.listViewAllThings);

        database = new DatabaseHelper(Activity_List_Things.this);

        refreshList();

        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_List_Things.this,
                        MainActivity.class);
                startActivityForResult(intent, 321);
            }
        });

        btnSearchInList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_List_Things.this, Activity_Search.class);
                startActivity(intent);
            }
        });

        listViewAllThings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ThingModel thing = arrayThing.get(i);
                Intent intent = new Intent(Activity_List_Things.this, Activity_Review_Image.class);
                ObjectHolder.currentThing = thing;
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }
    public void refreshList() {
        arrayThing = database.getAllToArrayList();
        adapter = new ArrayAdapter<ThingModel>(Activity_List_Things.this,
                android.R.layout.simple_list_item_1, arrayThing);
        listViewAllThings.setAdapter(adapter);
    }
}