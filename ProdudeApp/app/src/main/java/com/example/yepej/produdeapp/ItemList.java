package com.example.yepej.produdeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class ItemList extends AppCompatActivity
{

    final String encodeFormat = "UTF-8";

    //final String serverIP = "10.1.10.72";
    final String serverIP = "192.168.1.220";
    //final String serverIP = "192.168.1.109";

    ArrayList<String[]> cartList = new ArrayList<String[]>();


    String[] itemList;
    EditText itemsQTY;
    int itemSelected;

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

        itemsView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id)
            {
                String message = "How many " + parent.getItemAtPosition(position).toString() + " would you like to add to your cart?";

                AlertDialog.Builder builder1 = new AlertDialog.Builder(ItemList.this);
                builder1.setMessage(message);
                builder1.setCancelable(true);
                itemsQTY = new EditText(ItemList.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                itemsQTY.setLayoutParams(lp);
                itemsQTY.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder1.setView(itemsQTY);

                builder1.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        itemSelected = Integer.parseInt(itemsQTY.getText().toString());
                        Toast.makeText(ItemList.this, itemsQTY.getText().toString() + " " + parent.getItemAtPosition(position).toString() + " added to your cart.", Toast.LENGTH_LONG).show();

                        Boolean additem = true;

                        for(int i = 0; i < cartList.size(); i++)
                        {
                            if (cartList.get(i)[0].equals(parent.getItemAtPosition(position).toString()))
                            {
                                cartList.get(i)[1] = Integer.toString(Integer.parseInt(cartList.get(i)[1]) + Integer.parseInt(itemsQTY.getText().toString()));
                                additem = false;
                                break;
                            }
                        }

                        if(additem)
                        {
                            String[] item = new String[]{parent.getItemAtPosition(position).toString(), itemsQTY.getText().toString()};
                            cartList.add(item);
                        }
                    }
                });

                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ItemList.this, "Selection cancelled.", Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alert = builder1.create();
                alert.show();
            }
        });
    }

    private void showMessageBox(String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
