package com.example.wumpusworldgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goTo(View view){
        Intent intent = new Intent(getApplicationContext(),GameActivity.class) ;
        startActivity(intent);
        finish();
    }
}