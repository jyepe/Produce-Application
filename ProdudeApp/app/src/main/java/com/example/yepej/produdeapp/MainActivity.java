
package com.example.yepej.produdeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    // TODO: 6/27/2018 state text field should be dropdown with all states

    final String serverIP = "192.168.1.220";
    //final String serverIP = "192.168.1.109";
    final String encodeFormat = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a list of items for the spinner.
        String[] states = new String[]{"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC",
                "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA",
                "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE",
                "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC",
                "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
    }





    //region Sign up
    public void signUpClicked(View control)
    {
        setContentView(R.layout.sign_up);
    }

    public void formatPhone (View control)
    {
        EditText phone = ((EditText) findViewById(R.id.phone));
        String phoneNumber = phone.getText().toString();

        String text = String.valueOf( android.telephony.PhoneNumberUtils.formatNumber(phoneNumber) ); //formatted: 123-456-7890

        phone.setText(text); //set editText text equal to new formatted number

        phone.setSelection(text.length()); //move cursor to end of editText view
    }

    public void finishClicked (View control)
    {
        EditText compName = ((EditText) findViewById(R.id.compName));
        EditText uid = ((EditText) findViewById(R.id.uid));
        EditText pw = ((EditText) findViewById(R.id.pw));
        EditText name = ((EditText) findViewById(R.id.name));
        EditText address1 = ((EditText) findViewById(R.id.address1));
        EditText address2 = ((EditText) findViewById(R.id.address2));
        EditText city = ((EditText) findViewById(R.id.city));
        EditText county = ((EditText) findViewById(R.id.county));
        EditText state = ((EditText) findViewById(R.id.state));
        EditText zip = ((EditText) findViewById(R.id.zip));
        EditText phone = ((EditText) findViewById(R.id.phone));
        EditText email = ((EditText) findViewById(R.id.email));
        PostSender sendPostData = new PostSender();
        Boolean sendData = checkRequiredFields(compName, uid, pw);

        if (sendData)
        {

            try
            {
                String data = URLEncoder.encode("method", encodeFormat) + "=" + URLEncoder.encode("addUser", encodeFormat);

                data += "&" + URLEncoder.encode("compName", encodeFormat) + "=" + URLEncoder.encode(compName.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("username", encodeFormat) + "=" + URLEncoder.encode(uid.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("password", encodeFormat) + "=" + URLEncoder.encode(pw.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("name", encodeFormat) + "=" + URLEncoder.encode(name.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("address1", encodeFormat) + "=" + URLEncoder.encode(address1.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("address2", encodeFormat) + "=" + URLEncoder.encode(address2.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("county", encodeFormat) + "=" + URLEncoder.encode(county.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("city", encodeFormat) + "=" + URLEncoder.encode(city.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("state", encodeFormat) + "=" + URLEncoder.encode(state.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("zip", encodeFormat) + "=" + URLEncoder.encode(zip.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("phone", encodeFormat) + "=" + URLEncoder.encode(phone.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("email", encodeFormat) + "=" + URLEncoder.encode(email.getText().toString(), encodeFormat);


                String serverResponse = sendPostData.execute("http://" + serverIP + "/ds.php", data).get();
                Log.i("test", serverResponse);
                checkLoginResponse(serverResponse);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private Boolean checkRequiredFields(EditText compName, EditText uid, EditText pw)
    {
        String comp = compName.getText().toString();
        String username = uid.getText().toString();
        String password = pw.getText().toString();

        if (comp.equals("") || username.equals("") || password.equals(""))
        {
            Toast.makeText(this, "Fill out required fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //endregion

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
        else if (serverResponse.contains("user created successfully")) {
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
        }
        else if (serverResponse.contains("user already exists"))
        {
            Toast.makeText(this, "Username is already in use", Toast.LENGTH_LONG).show();
        }
        else if (serverResponse.contains("login successful"))
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
