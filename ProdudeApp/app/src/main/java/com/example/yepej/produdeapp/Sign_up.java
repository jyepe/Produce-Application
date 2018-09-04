package com.example.yepej.produdeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.URLEncoder;

public class Sign_up extends AppCompatActivity
{

    String selectedState = "";
    InstanceInfo info;
    final String encodeFormat = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        info = InstanceInfo.getInstance();

        Spinner spinner = (Spinner) findViewById(R.id.state);

        AdapterView.OnItemSelectedListener hi = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long text) {
                if (!parent.getItemAtPosition(pos).toString().equals("State..."))
                { selectedState = parent.getItemAtPosition(pos).toString(); }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinner.setOnItemSelectedListener(hi);
    }

    //region Sign up


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
                data += "&" + URLEncoder.encode("state", encodeFormat) + "=" + URLEncoder.encode(selectedState, encodeFormat);
                data += "&" + URLEncoder.encode("zip", encodeFormat) + "=" + URLEncoder.encode(zip.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("phone", encodeFormat) + "=" + URLEncoder.encode(phone.getText().toString(), encodeFormat);
                data += "&" + URLEncoder.encode("email", encodeFormat) + "=" + URLEncoder.encode(email.getText().toString(), encodeFormat);


                //showMessageBox(selectedState);
                String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();
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

    private void checkLoginResponse(String serverResponse)
    {
        if (serverResponse.toLowerCase().contains("connection failed"))
        {
            Toast.makeText(this, "Error Connecting to the server", Toast.LENGTH_LONG).show();
        }
        else if (serverResponse.contains("user created successfully")) {
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this, OrderOption.class);
            startActivity(myIntent);
        }
        else if (serverResponse.contains("user already exists"))
        {
            Toast.makeText(this, "Username is already in use", Toast.LENGTH_LONG).show();
        }
    }

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
