package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //String[] list_values = {"TU Munich", "TU Vienna", "TU Dresden"};
        String[] list_values = getResources().getStringArray(R.array.list_values);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter listAdapter = new ListAdapter(list_values);
        recyclerView.setAdapter(listAdapter);
    }


    public void onClickStartMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
