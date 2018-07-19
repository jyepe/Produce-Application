package com.example.yepej.produdeapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class ItemList extends AppCompatActivity
{

    final String encodeFormat = "UTF-8";
    //final String serverIP = "10.1.10.72";
    //final String serverIP = "192.168.1.220";
    final String serverIP = "192.168.1.109";


    String[] itemList;

    //I have the item list activity set to the default activity to skip the login. In your IDE go to
    //run menu and edit configurations and select this activity as default for testing
    // TODO: 7/12/2018 When spinner is scrolled off screen it resets the value of spinner (This is due to the getView method in the custom adapter class getting called and resetting values. We need to find a way to save the state of the spinner)
    // TODO: 7/12/2018 Any other bugs in here you find


    static class ViewHolder
    {
        TextView holderText;
        Spinner holderSpinner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        ListView listView = ((ListView) findViewById(R.id.itemListView));


        getItems();
        CustomAdapter adapter = new CustomAdapter(itemList);
        listView.setAdapter(adapter);
    }

    //Gets all inventory items from DB
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
        //Searches for everything between start and end of the server response
        Pattern p = Pattern.compile("start(.*?)end");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            itemList = m.group(1).split(",");
        }
    }

    //region Custom Adapter
    //This class is used to create a custom listview
    public class CustomAdapter extends BaseAdapter
    {

        String[] items;

        public CustomAdapter(String[] list)
        {
            items = list;
        }

        @Override
        public int getCount()
        {
            return items.length;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;

            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);
                holder = new ViewHolder();

                holder.holderText = ((TextView) convertView.findViewById(R.id.text));
                holder.holderSpinner = ((Spinner) convertView.findViewById(R.id.spinner));

                convertView.setTag(holder);
            }
            else
            {
                holder = ((ViewHolder) convertView.getTag());
            }

            holder.holderText.setText(items[position]);

            return convertView;
        }


    }
    //endregion
}
