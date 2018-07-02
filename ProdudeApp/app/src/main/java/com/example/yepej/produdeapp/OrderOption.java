package com.example.yepej.produdeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OrderOption extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_option);

        ListView optionView = ((ListView) findViewById(R.id.optionList));

        ArrayList<String> options = new ArrayList<String>();

        options.add("Create new order");
        options.add("View previous orders");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);

        optionView.setAdapter(adapter);
    }
}
