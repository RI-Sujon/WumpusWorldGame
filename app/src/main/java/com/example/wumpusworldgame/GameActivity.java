package com.example.wumpusworldgame;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit ;

    GameBoard gameBoard ;
    TextView goldCounter, moveCounter, arrowCounter, gameLevelText ;

    int gameLevel = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferences = getSharedPreferences("WumpusWorldPreferences", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        gameBoard = findViewById(R.id.gameBoard) ;
        goldCounter = findViewById(R.id.goldCounter) ;
        moveCounter = findViewById(R.id.moves) ;
        arrowCounter = findViewById(R.id.arrows) ;
        gameLevelText = findViewById(R.id.game_level_text) ;

        String difficultyLevel = sharedPreferences.getString("currentDifficulty", "low") ;
        if(difficultyLevel=="medium"){
            gameLevel = 5 ;
        }
        else if(difficultyLevel=="high"){
            gameLevel = 11 ;
        }

        gameLevelText.setText("LEVEL:" + gameLevel);
        gameBoard.setEnvironment(gameLevel);

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

        TextView statusMessage = (TextView) dialog.findViewById(R.id.status);
        TextView gameOverTypeMessage = (TextView) dialog.findViewById(R.id.message);
        TextView score = (TextView) dialog.findViewById(R.id.score);
        Button backButton = (Button) dialog.findViewById(R.id.back) ;
        Button newGameButton = (Button) dialog.findViewById(R.id.new_game);
        Button nextLevelButton = (Button) dialog.findViewById(R.id.next_level);
        dialog.setCancelable(false);

        if(str=="wumpus"){
            statusMessage.setText("GAME OVER");
            gameOverTypeMessage.setText("Oops...You are killed by Wumpus");
            nextLevelButton.setVisibility(View.GONE);
        }
        else if(str=="pit"){
            statusMessage.setText("GAME OVER");
            gameOverTypeMessage.setText("Oops...You fall in a Pit");
            nextLevelButton.setVisibility(View.GONE);
        }
        else if(str=="win"){
            if(gameLevel==28) {
                statusMessage.setText("GAME OVER");
                gameOverTypeMessage.setText("Congratulations...GAME OVER.");
                nextLevelButton.setVisibility(View.GONE);
            }
            else{
                statusMessage.setText("WIN");
                gameOverTypeMessage.setText("Congratulations...You win.");
            }
        }

        score.setText("SCORE: " + (gameBoard.getNumberOfGoldCollected()*100 - gameBoard.getNumberOfMove() - gameBoard.getNumberOfArrowUsed()*10 + gameBoard.getNumberOfWumpusKilled()*50));

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameBoard.setEnvironment(gameLevel);
                goldCounter.setText(gameBoard.getNumberOfGoldRemaining()+"");
                arrowCounter.setText( gameBoard.getNumberOfArrowRemaining()+"");
                moveCounter.setText("0");
            }
        });

        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameLevel = gameLevel+1 ;
                gameBoard.setEnvironment(gameLevel);
                gameLevelText.setText("LEVEL:" + gameLevel);
                goldCounter.setText(gameBoard.getNumberOfGoldRemaining()+"");
                arrowCounter.setText( gameBoard.getNumberOfArrowRemaining()+"");
                moveCounter.setText("0");
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class) ;
                //startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

    private void storeCurrentState(){
        int ROW=0, COLUMN=0 ;
        if(gameLevel<=4){
            ROW = 6 ;
            COLUMN = 6 ;
        }
        else if(gameLevel<=10){
            ROW = 8 ;
            COLUMN = 8 ;
        }
        else if(gameLevel<=18){
            ROW = 10 ;
            COLUMN = 10 ;
        }
        else if(gameLevel<=28){
            ROW = 12 ;
            COLUMN = 12 ;
        }

        for(int i=0; i<ROW ; i++){
            for (int j=0; j<COLUMN ; j++){
                for(int k=0; k<8 ; k++){
                    myEdit.putInt(i+""+j+""+k+"", gameBoard.getGameBoard()[i][j][k]) ;
                }
            }
        }

        myEdit.putInt("AgentCurrentRow", gameBoard.getAgentLocation()[0]) ;
        myEdit.putInt("AgentCurrentColumn", gameBoard.getAgentLocation()[1]) ;
        myEdit.putInt("AgentCurrentFaceDirection", gameBoard.getAgentLocation()[2]) ;

        myEdit.putInt("NumberOfGoldCollected", gameBoard.getNumberOfGoldCollected()) ;
        myEdit.putInt("NumberOfArrowUsed", gameBoard.getNumberOfArrowUsed()) ;
        myEdit.putInt("NumberOfWumpusKilled", gameBoard.getNumberOfWumpusKilled()) ;
    }
}