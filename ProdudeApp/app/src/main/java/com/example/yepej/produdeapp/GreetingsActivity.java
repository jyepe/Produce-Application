package com.example.yepej.produdeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class GreetingsActivity extends AppCompatActivity
{
    InstanceInfo info;
    final String encodeFormat = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);


        animateActivity();
    }

    private void animateActivity()
    {
        View view = findViewById(android.R.id.content);
        Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        mLoadAnimation.setDuration(5000);
        view.startAnimation(mLoadAnimation);
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

            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();
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
            myIntent.putExtra("contactName", info.getContactName());
            startActivity(myIntent);
        }
        else if (serverResponse.contains("wrong credentials") || serverResponse.contains("user does not exist"))
        {
            Toast.makeText(this, "Incorrect login information", Toast.LENGTH_LONG).show();
        }
    }

    private void getUser()
    {
        PostSender sendPostData = new PostSender();
        EditText uid = ((EditText) findViewById(R.id.usernameText));

        try
        {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getUser", "UTF-8");

            data += "&" + URLEncoder.encode("uid", "UTF-8") + "="
                    + URLEncoder.encode(uid.getText().toString(), "UTF-8");

            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();

            String userContactName = serverResponse.substring(serverResponse.indexOf("start:CONTACT_NAME") + 18, serverResponse.indexOf("end:CONTACT_NAME"));
            info.setContactName(userContactName);

            String user_ID = serverResponse.substring(serverResponse.indexOf("start:ID") + 8, serverResponse.indexOf("end:ID"));
            info.setUser_ID(user_ID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //endregion
}
