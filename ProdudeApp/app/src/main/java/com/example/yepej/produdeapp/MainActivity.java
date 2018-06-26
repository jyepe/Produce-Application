<<<<<<< HEAD
package com.example.yepej.produdeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void signUpClicked(View control)
    {
        setContentView(R.layout.sign_up);
    }

    public void finishClicked(View control)
    {
        EditText uid = ((EditText) findViewById(R.id.uid));
        EditText pw = ((EditText) findViewById(R.id.pw));
        EditText compName = ((EditText) findViewById(R.id.compName));

        PostSender postSender = new PostSender();

        try
        {
            String data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid.getText().toString(), "UTF-8");

            data += "&" + URLEncoder.encode("pw", "UTF-8") + "=" + URLEncoder.encode(pw.getText().toString(), "UTF-8");
            data += "&" + URLEncoder.encode("comp", "UTF-8") + "=" + URLEncoder.encode(uid.getText().toString(), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

            String serverResponse = sendPostData.execute("http://192.168.1.109/ds.php", data).get();
            Log.i("test", serverResponse);
            checkLoginResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void checkLoginResponse(String serverResponse)
    {
        if (serverResponse.contains("false"))
        {
            Toast.makeText(this, "Incorrect login information", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
        }
    }
    //endregion


}
=======
package com.example.yepej.produdeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void signUpClicked(View control)
    {
        setContentView(R.layout.sign_up);
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

            //String serverResponse = sendPostData.execute("http://192.168.1.109/ds.php", data).get();
            String serverResponse = sendPostData.execute("http://192.168.1.220/ds.php", data).get();

            Log.i("test", serverResponse);
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
            return;
        }
        if (serverResponse.contains("login successful"))
        {
            Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show();
        }
        else if (serverResponse.contains("wrong credentials") || serverResponse.contains("user does not exist"))
        {
            Toast.makeText(this, "Incorrect login information", Toast.LENGTH_LONG).show();
        }
    }
    //endregion


}
>>>>>>> 11bf89f59428e45d9727bb8e5d771d5d4dce3968
