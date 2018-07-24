package com.example.yepej.produdeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.os.Build.VERSION.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
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
    ServerInfo info;
    String[] itemList;
    int[] selectionList;


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
        info = ServerInfo.getInstance();
        //info.setServerIP("192.168.1.109");

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
        Pattern p = Pattern.compile("start(.*?)end");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            itemList = m.group(1).split(",");
        }

        selectionList = new int[itemList.length];
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
        public View getView(final int position, View convertView, ViewGroup parent)
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
            //Saves its position in listview as a tag
            holder.holderSpinner.setTag(position);

            //Sets the spinner that is being rendered to its saved selection
            if (((int) holder.holderSpinner.getTag()) == position)
            {
                holder.holderSpinner.setSelection(selectionList[position]);
            }

            //Allows for ViewHolder to be passed into spinner listner
            final ViewHolder finalHolder = holder;


            holder.holderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                // TODO: 7/20/2018 change text of corresponding selected spinner i.e if spinner value > 0 
                public void onItemSelected(AdapterView<?> parent, View view, int qtySelected, long id)
                {
                    int selectedItem = (int) finalHolder.holderSpinner.getTag();
                    selectionList[selectedItem] = qtySelected;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            return convertView;
        }
    }
    //endregion

    //region Custom Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);

        return true;
    }

    @Override // TODO: 7/20/2018 finish sending to db and reading response 
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            String data = URLEncoder.encode("method", encodeFormat) + "=" + URLEncoder.encode("newOrder", encodeFormat);
            int count = 0;


            for (int i = 0; i < selectionList.length; i++)
            {
                if (selectionList[i] != 0)
                {
                    count++;
                    data += "&" + URLEncoder.encode("item" + count, encodeFormat) + "=" + URLEncoder.encode(itemList[i], encodeFormat);
                    data += "&" + URLEncoder.encode("qty" + count, encodeFormat) + "=" + URLEncoder.encode(Integer.toString(selectionList[i]), encodeFormat);
                }
            }

            data += "&" + URLEncoder.encode("count", encodeFormat) + "=" + URLEncoder.encode(Integer.toString(count), encodeFormat);


            PostSender sendPostData = new PostSender();
            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();

            Log.i("Test", serverResponse);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return super.onOptionsItemSelected(item);
    }
    //endregion


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
