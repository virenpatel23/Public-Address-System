package com.example.viren.publicadresssystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
    }

    public void logout(View view) {


        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        finish();

        startActivity(new Intent(LogOutActivity.this,LoginActivity.class));


    }
}
