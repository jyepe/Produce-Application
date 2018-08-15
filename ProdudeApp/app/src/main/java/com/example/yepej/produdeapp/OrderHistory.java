package com.example.yepej.produdeapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderHistory extends AppCompatActivity {


    final String encodeFormat = "UTF-8";
    private ListView ordersView;
    InstanceInfo info;
    List<String> prevOrders;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        info = InstanceInfo.getInstance();
        ordersView = (ListView) findViewById(R.id.PastOrders);

        getPreviousOrders();

        final Intent singleOrder = new Intent(this, SingleOrder.class);

        ordersView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String str = prevOrders.get(position);
                Pattern pattern = Pattern.compile(": (.*?)   ");
                Matcher matcher = pattern.matcher(str);
                String orderID = "";
                while (matcher.find()) {
                    orderID = matcher.group(1);
                }
                singleOrder.putExtra("orderID", orderID);
                singleOrder.putExtra("Header", prevOrders.get(position));
                //start item list activity
                startActivity(singleOrder);
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prevOrders);

        ordersView.setAdapter(arrayAdapter);
    }


    //Gets all inventory items from DB
    private void getPreviousOrders()
    {
        PostSender sendPostData = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", encodeFormat) + "=" + URLEncoder.encode("getUserOrders", encodeFormat);
            data += "&" + URLEncoder.encode("ID", encodeFormat) + "=" + URLEncoder.encode(info.getUser_ID(), encodeFormat);
            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();
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
        //Searches for everything between start and end of the server response
        Pattern p = Pattern.compile("start:INFO(.*?)end:INFO");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            prevOrders = new ArrayList<String>(Arrays.asList(m.group(1).split("-")));
        }
    }

    private void showMessageBox(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
