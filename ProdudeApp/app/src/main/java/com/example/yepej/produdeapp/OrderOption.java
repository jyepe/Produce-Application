package com.example.yepej.produdeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderOption extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_option);

        String user = getIntent().getStringExtra("contactName");

        setTitle("Welcome " + user);
        showOptions();
    }

    private void showOptions()
    {
        final Intent myIntent = new Intent(this, ItemList.class);


        ListView optionView = ((ListView) findViewById(R.id.optionList));

        ArrayList<String> options = new ArrayList<String>();

        options.add("Create new order");
        options.add("View previous orders");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);

        optionView.setAdapter(adapter);

        optionView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    //start item list activity
                    startActivity(myIntent);
                }
            }
        });
    }
}
