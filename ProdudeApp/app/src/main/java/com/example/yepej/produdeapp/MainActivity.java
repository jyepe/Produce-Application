
package com.example.yepej.produdeapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    //final String serverIP = "192.168.1.220";
    final String serverIP = "192.168.1.109";
    //final String serverIP = "10.1.10.72";
    final String encodeFormat = "UTF-8";
    String userContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUpClicked(View control)
    {
        Intent myIntent = new Intent(this, Sign_up.class);
        startActivity(myIntent);
    }

    //region Login
    public void loginClick(View control)
    {
        TextView username = ((TextView) findViewById(R.id.usernameText));
        TextView password = ((TextView) findViewById(R.id.passwordText));

        if (username.getText().toString().equals("") || password.getText().toString().equals(""))
        {
            Toast.makeText(this, "Username or Password missing", Toast.LENGTH_LONG).show();
        }
        else
        {
            sendLoginInfo(username, password);
        }
    }

    private void sendLoginInfo(TextView username, TextView password)
    {
        PostSender sendPostData = new PostSender();

        try
        {
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username.getText().toString(), "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                    + URLEncoder.encode(password.getText().toString(), "UTF-8");

            data += "&" + URLEncoder.encode("method", "UTF-8") + "="
                    + URLEncoder.encode("login", "UTF-8");

            String serverResponse = sendPostData.execute("http://" + serverIP + "/ds.php", data).get();
            checkLoginResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void checkLoginResponse(String serverResponse)
    {
        if (serverResponse.toLowerCase().contains("connection failed"))
        {
            Toast.makeText(this, "Error Connecting to the server", Toast.LENGTH_LONG).show();
        }
        else if (serverResponse.contains("user already exists"))
        {
            Toast.makeText(this, "Username is already in use", Toast.LENGTH_LONG).show();
        }
        else if (serverResponse.contains("login successful"))
        {
            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
            getUser();
            Intent myIntent = new Intent(this, OrderOption.class);
            myIntent.putExtra("contactName", getUser());
            startActivity(myIntent);
        }
        else if (serverResponse.contains("wrong credentials") || serverResponse.contains("user does not exist"))
        {
            Toast.makeText(this, "Incorrect login information", Toast.LENGTH_LONG).show();
        }
    }

    private String getUser()
    {
        PostSender sendPostData = new PostSender();
        EditText uid = ((EditText) findViewById(R.id.usernameText));

        try
        {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getUser", "UTF-8");

            data += "&" + URLEncoder.encode("uid", "UTF-8") + "="
                    + URLEncoder.encode(uid.getText().toString(), "UTF-8");

            String serverResponse = sendPostData.execute("http://" + serverIP + "/ds.php", data).get();
            userContactName = serverResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return userContactName;
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
