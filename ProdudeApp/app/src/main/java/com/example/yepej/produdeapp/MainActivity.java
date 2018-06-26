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
