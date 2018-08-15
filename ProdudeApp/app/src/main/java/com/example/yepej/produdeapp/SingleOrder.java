package com.example.yepej.produdeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleOrder extends AppCompatActivity {


    final String encodeFormat = "UTF-8";
    private ListView subItems;
    String orderID;
    InstanceInfo info;
    List<String> orderItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_order);
        info = InstanceInfo.getInstance();
        subItems = (ListView) findViewById(R.id.subItems);

        orderID = getIntent().getStringExtra("orderID");

        TextView headerView = (TextView) findViewById(R.id.headerView);

        headerView.setText(getIntent().getStringExtra("Header"));

        showMessageBox(orderID);

        getOrderItems();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderItems);
        subItems.setAdapter(arrayAdapter);
    }

    public void getOrderItems()
    {
        PostSender sendPostData = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", encodeFormat) + "=" + URLEncoder.encode("getSingleOrder", encodeFormat);
            data += "&" + URLEncoder.encode("ID", encodeFormat) + "=" + URLEncoder.encode(orderID, encodeFormat);
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
        Pattern p = Pattern.compile("start:ITEM(.*?)end:ITEM");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            orderItems = new ArrayList<String>(Arrays.asList(m.group(1).split("-")));
        }


        Pattern totalP = Pattern.compile("start:TOTAL(.*?)end:TOTAL");
        Matcher totalM = totalP.matcher(serverResponse);
        TextView totalView = (TextView) findViewById(R.id.totalView);
        if (totalM.find())
        {
            totalView.setText("Order Total = $" + totalM.group(1));
        }
        else
        {
            totalView.setText("Error retrieving order total.");
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
