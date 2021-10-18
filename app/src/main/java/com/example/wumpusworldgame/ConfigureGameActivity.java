package com.example.wumpusworldgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigureGameActivity extends AppCompatActivity {

    EditText numberOfGoldEditText, numberOfPitEditText, numberOfWumpusEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_game);

        numberOfGoldEditText = findViewById(R.id.numberOfGold) ;
        numberOfPitEditText = findViewById(R.id.numberOfPit) ;
        numberOfWumpusEditText = findViewById(R.id.numberOfWumpus) ;
    }

    public void playGame(View view){
        Intent intent = new Intent(getApplicationContext(),GameActivity.class) ;

        intent.putExtra("type", "random") ;
        intent.putExtra("numberOfGold", numberOfGoldEditText.getText().toString()) ;
        intent.putExtra("numberOfPit", numberOfPitEditText.getText().toString()) ;
        intent.putExtra("numberOfWumpus", numberOfWumpusEditText.getText().toString()) ;
        startActivity(intent) ;
        finish() ;
    }

    public void playGameByAI(View view){
        Intent intent = new Intent(getApplicationContext(),AIActivity.class) ;

        intent.putExtra("type", "random") ;
        intent.putExtra("numberOfGold", numberOfGoldEditText.getText().toString()) ;
        intent.putExtra("numberOfPit", numberOfPitEditText.getText().toString()) ;
        intent.putExtra("numberOfWumpus", numberOfWumpusEditText.getText().toString()) ;
        startActivity(intent) ;
        finish() ;
    }
}