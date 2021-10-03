package com.example.wumpusworldgame;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GameBoard gameBoard ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = findViewById(R.id.gameBoard) ;
    }

    public void moveForward(View view){
        gameBoard.moveForward() ;
        gameBoard.invalidate() ;
    }

    public void turn90Clockwise(View view){
        gameBoard.turn90Clockwise(); ;
        gameBoard.invalidate() ;
    }

    public void turn90AntiClockwise(View view){
        gameBoard.turn90AntiClockwise(); ;
        gameBoard.invalidate() ;
    }
}