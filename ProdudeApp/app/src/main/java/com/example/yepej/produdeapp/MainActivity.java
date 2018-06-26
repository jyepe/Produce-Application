package com.example.yepej.produdeapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    public class SendPostData extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            // TODO: 6/25/2018 fix this messy code 
            
            try
            {
                String text = "";
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode("Julian", "UTF-8");
                BufferedReader reader = null;
                URL url = new URL("http://192.168.1.109/ds.php");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                text = sb.toString();
                Log.i("test", text);

            } catch (Exception e) 
            {
                System.out.println(e.getMessage());
            }
            
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buttonClicked(View control) throws IOException
    {
        SendPostData sendPostData = new SendPostData();

        sendPostData.execute("http://192.168.1.109/ds.php");
    }
}
