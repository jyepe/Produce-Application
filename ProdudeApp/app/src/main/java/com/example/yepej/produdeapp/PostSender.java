package com.example.yepej.produdeapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by yepej on 6/26/2018.
 */

public class PostSender extends AsyncTask<String, Void, String>
{

    @Override
    protected String doInBackground(String... params)
    {
        //Server response will be stored here
        String serverResponseString = "";

        //Data to pass to server
        String data = params[1];

        //Used to read server response
        BufferedReader reader = null;

        String url = params[0];

        try
        {
            URL address = new URL(url);
            HttpURLConnection conn = ((HttpURLConnection) address.openConnection());
            conn.setDoOutput(true);

            //Write data to server
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            //Reading server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            serverResponseString = sb.toString();
            conn.disconnect();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return serverResponseString;
    }
}
