package com.example.wumpusworldgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    GameBoard gameBoard ;
    TextView goldCounter, moveCounter, arrowCounter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = findViewById(R.id.gameBoard) ;
        goldCounter = findViewById(R.id.goldCounter) ;
        moveCounter = findViewById(R.id.moves) ;
        arrowCounter = findViewById(R.id.arrows) ;

        goldCounter.setText(gameBoard.getNumberOfGoldRemaining()+"");
        arrowCounter.setText( gameBoard.getNumberOfArrowRemaining()+"");
        moveCounter.setText("0");
    }

    public void moveForward(View view){
        int flag = gameBoard.moveForward() ;
        if(flag==-6){
            gameOverMessage("wumpus");
        }
        else if(flag==-4){
            gameOverMessage("pit");
        }
        gameBoard.invalidate() ;
        updateScoreBoard();
    }

    public void turn90Clockwise(View view){
        gameBoard.turn90Clockwise(); ;
        gameBoard.invalidate() ;
        updateScoreBoard();
    }

    public void turn90AntiClockwise(View view){
        gameBoard.turn90AntiClockwise(); ;
        gameBoard.invalidate() ;
        updateScoreBoard();
    }

    public void collectGold(View view){
        gameBoard.collectGold();
        gameBoard.invalidate();
        updateScoreBoard();

        if(gameBoard.getNumberOfGoldRemaining()==0){
            gameOverMessage("win");
        }
    }

    public void shootByArrow(View view){
        if(gameBoard.getNumberOfArrowRemaining()>0){
            gameBoard.shootByArrow();
            gameBoard.invalidate();
            updateScoreBoard();
        }
    }

    public void updateScoreBoard(){
        goldCounter.setText(gameBoard.getNumberOfGoldRemaining() + "");
        arrowCounter.setText(gameBoard.getNumberOfArrowRemaining() + "");
        moveCounter.setText(gameBoard.getNumberOfMove() + "");
    }

    public void gameOverMessage(String str){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_adapter_for_dialog_box_tutor_rating);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView gameOverTypeMessage = (TextView) dialog.findViewById(R.id.message);
        TextView score = (TextView) dialog.findViewById(R.id.score);
        Button newGameButton = (Button) dialog.findViewById(R.id.new_game);
        Button backButton = (Button) dialog.findViewById(R.id.back) ;
        dialog.setCancelable(false);

        if(str=="wumpus"){
            gameOverTypeMessage.setText("Oops...You are killed by Wumpus");
        }
        else if(str=="pit"){
            gameOverTypeMessage.setText("Oops...You fall in a Pit");
        }
        else if(str=="win"){
            gameOverTypeMessage.setText("Congratulations...You win.");
        }

        score.setText("SCORE: " + (gameBoard.getNumberOfGoldCollected()*100 - gameBoard.getNumberOfMove() - gameBoard.getNumberOfArrowUsed()*10 + gameBoard.getNumberOfWumpusKilled()*50));

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameBoard.resetGameBoard();
                goldCounter.setText(gameBoard.getNumberOfGoldRemaining()+"");
                arrowCounter.setText( gameBoard.getNumberOfArrowRemaining()+"");
                moveCounter.setText("0");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class) ;
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

}