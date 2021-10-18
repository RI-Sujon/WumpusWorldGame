package com.example.wumpusworldgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FixedEnvironment extends AppCompatActivity {

    EditText pit1, pit2, pit3, pit4, wumpus1, wumpus2, wumpus3, wumpus4, goldLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_environment);

        pit1 = findViewById(R.id.Pit1) ;
        pit2 = findViewById(R.id.Pit2) ;
        pit3 = findViewById(R.id.Pit3) ;
        pit4 = findViewById(R.id.Pit4) ;
        wumpus1 = findViewById(R.id.wumpus1) ;
        wumpus2 = findViewById(R.id.wumpus2) ;
        wumpus3 = findViewById(R.id.wumpus3) ;
        wumpus4 = findViewById(R.id.wumpus4) ;
        goldLocation = findViewById(R.id.goldLocation) ;

    }

    public void playGame(View view){
        Intent intent = new Intent(getApplicationContext(),GameActivity.class) ;

        intent.putExtra("type", "fixed") ;
        intent.putExtra("pit1", pit1.getText().toString()) ;
        intent.putExtra("pit2", pit2.getText().toString()) ;
        intent.putExtra("pit3", pit3.getText().toString()) ;
        intent.putExtra("pit4", pit4.getText().toString()) ;
        intent.putExtra("wumpus1", wumpus1.getText().toString()) ;
        intent.putExtra("wumpus2", wumpus2.getText().toString()) ;
        intent.putExtra("wumpus3", wumpus3.getText().toString()) ;
        intent.putExtra("wumpus4", wumpus4.getText().toString()) ;

        startActivity(intent) ;
        finish() ;
    }

    public void playGameByAI(View view){
        Intent intent = new Intent(getApplicationContext(),AIActivity.class) ;

        intent.putExtra("type", "fixed") ;

        if(pit1.getText().toString().equals("")){
            intent.putExtra("pit1", -1) ;
        }else{
            intent.putExtra("pit1", Integer.valueOf(pit1.getText().toString())) ;
        }

        if(pit2.getText().toString().equals("")){
            intent.putExtra("pit2", -1) ;
        }else{
            intent.putExtra("pit2", Integer.valueOf(pit2.getText().toString())) ;
        }

        if(pit3.getText().toString().equals("")){
            intent.putExtra("pit3", -1) ;
        }else{
            intent.putExtra("pit3", Integer.valueOf(pit3.getText().toString())) ;
        }

        if(pit4.getText().toString().equals("")){
            intent.putExtra("pit4", -1) ;
        }else{
            intent.putExtra("pit4", Integer.valueOf(pit4.getText().toString())) ;
        }


        if(wumpus1.getText().toString().equals("")){
            intent.putExtra("wumpus1", -1) ;
        }else{
            intent.putExtra("wumpus1", Integer.valueOf(wumpus1.getText().toString())) ;
        }

        if(wumpus2.getText().toString().equals("")){
            intent.putExtra("wumpus2", -1) ;
        }else{
            intent.putExtra("wumpus2", Integer.valueOf(wumpus2.getText().toString())) ;
        }

        if(wumpus3.getText().toString().equals("")){
            intent.putExtra("wumpus3", -1) ;
        }else{
            intent.putExtra("wumpus3", Integer.valueOf(wumpus3.getText().toString())) ;
        }

        if(wumpus4.getText().toString().equals("")){
            intent.putExtra("wumpus4", -1) ;
        }else{
            intent.putExtra("wumpus4", Integer.valueOf(wumpus4.getText().toString())) ;
        }

        intent.putExtra("goldLocation", goldLocation.getText().toString()) ;
        startActivity(intent) ;
        finish() ;
    }
}