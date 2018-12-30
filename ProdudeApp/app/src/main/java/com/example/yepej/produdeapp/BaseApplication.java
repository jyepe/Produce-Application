package com.example.yepej.produdeapp;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BaseApplication extends Application
{
    InstanceInfo info;

    @Override
    public void onCreate()
    {
        info = InstanceInfo.getInstance();
        //info.setServerIP("192.168.1.109");
        info.setServerIP("10.1.10.73");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference clientToken = ref.child("Client Token");

        clientToken.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                info.setToken(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        super.onCreate();
    }
}
