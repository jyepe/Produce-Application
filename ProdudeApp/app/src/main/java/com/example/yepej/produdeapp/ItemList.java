package com.example.yepej.produdeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemList extends AppCompatActivity
{

    final String encodeFormat = "UTF-8";

    final String serverIP = "10.1.10.72";
    //final String serverIP = "192.168.1.220";
    //final String serverIP = "192.168.1.109";

    String[] itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        getItems();
        setListView();
    }


    //Gets all items from DB
    private void getItems()
    {
        PostSender sendPostData = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", encodeFormat) + "=" + URLEncoder.encode("getItems", encodeFormat);
            String serverResponse = sendPostData.execute("http://" + serverIP + "/ds.php", data).get();
            parseResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Puts the data returned into an array
    private void parseResponse(String serverResponse)
    {
        Pattern p = Pattern.compile("start(.*?)end");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            itemList = m.group(1).split(",");
        }
    }

    //sets the listview with items in the itemList array
    private void setListView()
    {
        ListView itemsView = ((ListView) findViewById(R.id.items));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);

        itemsView.setAdapter(adapter);
    }
}
