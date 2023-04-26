package com.example.wumpusworldgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SetDifficultyLevelActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_difficulty_level);

        sharedPreferences = getSharedPreferences("WumpusWorldPreferences", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
    }

    public void setLow(View view){
        myEdit.putString("currentDifficulty", "low");
        
        myEdit.commit();
        Toast.makeText(getApplicationContext(), "Set Difficulty Level to Low", Toast.LENGTH_SHORT).show();

        finish();
    }
    public void setMedium(View view){
        myEdit.putString("currentDifficulty", "medium");
        myEdit.commit();
        Toast.makeText(getApplicationContext(), "Set Difficulty Level to Medium", Toast.LENGTH_SHORT).show();

        finish();
    }
    public void setHigh(View view){
        myEdit.putString("currentDifficulty", "high");
        myEdit.commit();
        Toast.makeText(getApplicationContext(), "Set Difficulty Level to High", Toast.LENGTH_SHORT).show();

        finish();
    }
}