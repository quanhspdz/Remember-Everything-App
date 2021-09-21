package com.example.saveimagetosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class Activity_Search extends AppCompatActivity {

    ListView listViewResult;
    ImageButton btnSearch;
    EditText edtName;
    ArrayList<ThingModel> arrayResult;
    ArrayAdapter<ThingModel> adapter;
    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listViewResult = findViewById(R.id.listViewSearchResult);
        btnSearch = findViewById(R.id.btnImgSearch);
        edtName = findViewById(R.id.edtNameSearch);

        database = new DatabaseHelper(Activity_Search.this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = "";
                if (edtName.getText().toString().length() > 0)
                    name = edtName.getText().toString();
                findItem(name);
            }
        });

        listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Activity_Search.this, Activity_Review_Image.class);
                ObjectHolder.currentThing = arrayResult.get(i);
                startActivity(intent);
            }
        });


    }

    private void findItem(String name) {
        arrayResult = database.findAnItem(name);
        adapter = new ArrayAdapter<ThingModel>(Activity_Search.this,
                android.R.layout.simple_list_item_1,
                arrayResult);
        listViewResult.setAdapter(adapter);
    }
}