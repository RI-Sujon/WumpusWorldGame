package com.example.wumpusworldgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class AIActivity extends AppCompatActivity {

    GameBoard gameBoard ;
    TextView goldCounter, moveCounter, arrowCounter ;

    int numberOfGold, numberOfPit, numberOfWumpus ;

    int Visited=0,Breeze=1,Gold=2,SafeSquare=3,Pit=4,Stench=5,Wumpus=6,Glitter=7;
    int top=0,left=1,right=2,bottom=3;
    //EnvironmentSettingUp environmentSettingUp = new EnvironmentSettingUp();
    int[][][] board = new int[10][10][8];
    private int[][] pitPossibility, wumpusPossibility, nodesID, relationships;
    private int[] parent, checked;
    static final int sizeOfBoard = 10;
    int counter = 0;
    private int numOfArrows = 1;
    private int goldNode;
    int lastNode=90, sujonLastNode=90;
    int startNode = 90;
    private int agentRow = 9, agentCol = 0;
    private boolean gameOver = false;
    private boolean noSafePath = false;
    boolean isSafeCellInPreviousNode = false;
    private int sleep = 1200;

    String type = "" ;
    int pit[] = new int[10] ;
    int wumpus[] = new int[10] ;
    int goldLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_i);

        gameBoard = findViewById(R.id.gameBoard) ;
        goldCounter = findViewById(R.id.goldCounter) ;
        moveCounter = findViewById(R.id.moves) ;
        arrowCounter = findViewById(R.id.arrows) ;

//        Bundle extras = getIntent().getExtras() ;
//        numberOfGold = Integer.valueOf(extras.getString("numberOfGold"));
//        numberOfPit = Integer.valueOf(extras.getString("numberOfPit"));
//        numberOfWumpus = Integer.valueOf(extras.getString("numberOfWumpus"));

        Bundle extras = getIntent().getExtras() ;

        type = extras.getString("type") ;

        for (int i=0; i<10; i++){
            pit[i] = -1;
            wumpus[i] = -1;
        }

        if(type.equals("random")){
            numberOfGold = Integer.valueOf(extras.getString("numberOfGold"));
            numberOfPit = Integer.valueOf(extras.getString("numberOfPit"));
            numberOfWumpus = Integer.valueOf(extras.getString("numberOfWumpus"));

            gameBoard.setEnvironment(numberOfGold, numberOfPit, numberOfWumpus);
        }
        else if(type.equals("fixed")){
            pit[0] = extras.getInt("pit1", -1) ;
            pit[1] = extras.getInt("pit2", -1) ;
            pit[2] = extras.getInt("pit3", -1) ;
            pit[3] = extras.getInt("pit4", -1) ;
            wumpus[0] = extras.getInt("wumpus1", -1) ;
            wumpus[1] = extras.getInt("wumpus2", -1) ;
            wumpus[2] = extras.getInt("wumpus3", -1) ;
            wumpus[3] = extras.getInt("wumpus4", -1) ;

            goldLocation = Integer.valueOf(extras.getString("goldLocation")) ;

            System.out.println("============="+goldLocation);
            gameBoard.setEnvironment2(pit, wumpus, goldLocation);
        }

        goldCounter.setText(gameBoard.getNumberOfGoldRemaining()+"");
        arrowCounter.setText( gameBoard.getNumberOfArrowRemaining()+"");
        moveCounter.setText("0");

        board = gameBoard.getGameBoard() ;
        numOfArrows = gameBoard.getNumberOfArrowRemaining() ;

        try {
            //doInBackground() ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Arrows = " + numOfArrows);
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
                gameBoard.setEnvironment(numberOfGold, numberOfPit, numberOfWumpus);
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

    public void moveAgent(int row, int column){
        gameBoard.moveAgent(row, column);

        AIActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameBoard.invalidate();
                updateScoreBoard();
            }
        });
//        new Thread(new Runnable() {
//            public void run() {
//            }
//        }).start();
    }

    public void next(View view){
        new Thread(new Runnable() {
            public void run() {
                try {
                    doInBackground() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void doInBackground() throws Exception
    {
        checked = new int[sizeOfBoard * sizeOfBoard];
        nodesID = new int[sizeOfBoard][sizeOfBoard];
        relationships = new int[sizeOfBoard * sizeOfBoard][4];
        parent = new int[sizeOfBoard * sizeOfBoard];

        int nodeCounter = 0;
        int currentNodeID = agentRow * 10 + agentCol;

        for (int i = 0; i < sizeOfBoard * sizeOfBoard; i++ )
        {
            parent[i] = -1;
        }

        for (int row = 0; row < sizeOfBoard; row++ )
        {
            for (int col = 0; col < sizeOfBoard; col++ )
            {
                nodesID[row][col] = nodeCounter;
                //System.out.println("Node Count = "+nodeCounter);
                nodeCounter += 1;
            }
        }

        for (int row = 0; row < sizeOfBoard; row++ )
        {
            for (int col = 0; col < sizeOfBoard; col++ )
            {
                if(row -1 >= 0) {
                    relationships[ nodesID[row][col] ][ top ] = nodesID[row -1][col];
                }
                else relationships[ nodesID[row][col] ][ top ] = -1;

                if(col -1 >= 0) {
                    relationships[ nodesID[row][col] ][ left ] = nodesID[row][col-1];
                }
                else relationships[ nodesID[row][col] ][ left ] = -1;

                if(col +1 <= 9) {
                    relationships[ nodesID[row][col] ][ right ] = nodesID[row][col+1];
                }
                else relationships[ nodesID[row][col] ][ right ] = -1;

                if(row +1 <= 9) {
                    relationships[ nodesID[row][col] ][ bottom ] = nodesID[row+1][col];
                }
                else relationships[ nodesID[row][col] ][ bottom ] = -1;
            }
        }

        for (int row = 0; row < sizeOfBoard * sizeOfBoard; row++ )
        {
            System.out.print("Node: " + row + "\t| ");
            for ( int col = 0; col < 4; col++ )
            {
                System.out.print(relationships[row][col] + " ");
            }
            System.out.println();
        }

        pitPossibility = new int[sizeOfBoard][sizeOfBoard];
        wumpusPossibility = new int[sizeOfBoard][sizeOfBoard];
        //cell_OK = new int[sizeOfBoard][sizeOfBoard];
        int nearestNodeId;

        for (int row = 0; row < sizeOfBoard; row++ )
        {
            for (int col = 0; col < sizeOfBoard; col++ )
            {
                pitPossibility[row][col] = -1;
                wumpusPossibility[row][col] = -1;
                board[row][col][Visited] = 0;
            }
        }

        board[9][0][Visited] = 1;
        dfsExploringTheWorld(currentNodeID);


        return;
    }

    public void dfsExploringTheWorld(int currentNodeID)
    {

        String sense = "";
        System.out.println();
        if(gameOver) {
            //gameOverMessage("Game Over");
            System.out.println("Game Over");
            return;
        }else{
            moveAgent(currentNodeID/10, currentNodeID%10);
            System.out.println("===========Current Node:" + currentNodeID);
        }

        if(board[currentNodeID / 10][currentNodeID % 10][Pit] == 1 || board[currentNodeID / 10][currentNodeID % 10][Wumpus] == 1) {
            System.out.println("Node: "+currentNodeID+"\n Agent is dead!");
            sense = " Dead! Game Lost!";
            goldNode = currentNodeID;
            gameOver = true;
            return;
        }

        if(board[currentNodeID / 10][currentNodeID % 10][Gold] == 1) {
            System.out.println("Node: "+currentNodeID+"\n  Got Gold");
            System.out.println("Game Won!");
            sense = " Got Gold";
            gameOver = true;
            return;
        }

        boolean isBreezeHere = false, isStenchHere = false,isGlitterHere=false;
        if(board[currentNodeID / 10][currentNodeID % 10][Breeze] == 1) {
            isBreezeHere = true;
        }
        if(board[currentNodeID / 10][currentNodeID % 10][Stench] == 1) {
            isStenchHere = true;
        }
        if(board[currentNodeID / 10][currentNodeID % 10][Glitter] == 1) {
            isGlitterHere = true;
        }

        if(isBreezeHere)
            sense += "Breeze ";
        if(isStenchHere)
            sense += "Stench ";
        if(isGlitterHere)
            sense += "Glitter. ";
        if(!isBreezeHere && !isStenchHere)
            sense += " OK, there is no risk.";

        System.out.println("Node: "+currentNodeID+"\nSense: " +sense);

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int nearestNodeId;
        ArrayList<Integer> unsafeNodeList = new ArrayList<Integer>();
        ArrayList<Integer> safeNodeList = new ArrayList<Integer>();

        for(int i = 0; i < 4; i++)
        {
            nearestNodeId = relationships[currentNodeID][i];
            if(nearestNodeId == -1 || board[nearestNodeId / 10][nearestNodeId % 10][Visited] == 1) continue;

            boolean pitMaxPossibilityInThisMove = false;

            if(isBreezeHere && pitPossibility[nearestNodeId / 10][nearestNodeId % 10] != 0)
            {
                if(pitPossibility[nearestNodeId / 10][nearestNodeId % 10] == -1 && wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] == 1)
                {
                    pitPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;
                    wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;
                }

                else {
                    if (pitPossibility[nearestNodeId / 10][nearestNodeId % 10] == 1)
                        pitMaxPossibilityInThisMove = false;

                    else
                        pitMaxPossibilityInThisMove = true;

                    if(pitMaxPossibilityInThisMove)
                        pitPossibility[nearestNodeId / 10][nearestNodeId % 10] = 1;

                    else {
                        pitPossibility[nearestNodeId / 10][nearestNodeId % 10] = 2;

                        for(int x =0; x< 4; x++)
                        {
                            int tempNodeID = relationships[currentNodeID][x];
                            if(tempNodeID == -1 || tempNodeID == nearestNodeId)
                                continue;
                            pitPossibility[tempNodeID / 10][tempNodeID % 10] = -1;
                        }
                    }
                }
            }

            else {
                pitPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;
            }


            if(isStenchHere && wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] != 0)
            {
                if(wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] == -1 && pitPossibility[nearestNodeId / 10][nearestNodeId % 10] == 1)
                {
                    if(pitMaxPossibilityInThisMove)
                    {
                        wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 1;
                    }

                    else {
                        pitPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;
                        wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;

                    }
                }

                else {
                    if(wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] == 1)
                    {
                        wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 2;

                        for(int x =0; x< 4; x++) {
                            int tempNodeID = relationships[currentNodeID][x];
                            if(tempNodeID == -1 || tempNodeID == nearestNodeId) continue;
                            wumpusPossibility[tempNodeID / 10][tempNodeID % 10] = -1;
                        }
                    }

                    else
                        wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 1;
                }
            }

            else {
                wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;
            }

            if((!isBreezeHere && !isStenchHere) ||
                    (wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] <=0 && pitPossibility[nearestNodeId / 10][nearestNodeId % 10] <=0))
            {
                safeNodeList.add(nearestNodeId);
            }

            else {
                unsafeNodeList.add(nearestNodeId);
            }
        }

        int numOfSafeCell = safeNodeList.size();
        int numOfUnsafeCell = unsafeNodeList.size();
        int[] safeCell = new int[numOfSafeCell], unsafeCell = new int[numOfUnsafeCell];

        for(int j = 0; j < numOfSafeCell; j++)
        {
            safeCell[j] = safeNodeList.get(j);
            System.out.println("Safe Node: " +safeCell[j]);
        }

        for(int j = 0; j < numOfUnsafeCell; j++) {
            unsafeCell[j] = unsafeNodeList.get(j);
            System.out.println("UnSafe Node: " +unsafeCell[j]);
        }

        if(numOfSafeCell > 0) {
            for(int k = 0; k < numOfSafeCell; k++) {
                parent[safeCell[k]] = currentNodeID;
                board[ safeCell[k] / 10 ][ safeCell[k] % 10 ][Visited] = 1;
                sujonLastNode = lastNode ;
                lastNode = safeCell[k];

                if(k != numOfSafeCell-1) {
                    isSafeCellInPreviousNode = true;
                }

                dfsExploringTheWorld(safeCell[k]);

                if(k == numOfSafeCell - 1   ) {
                    noSafePath = true;
                }
                if(!gameOver)
                {
//                    try {
//                        Thread.sleep(sleep);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    System.out.println("Decision: Stepping Back");
                    moveAgent(sujonLastNode/10, sujonLastNode%10);
                    //System.out.println("===========Current Node111111:" + sujonLastNode);


                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(noSafePath && numOfUnsafeCell > 0)
        {
            int riskyNodeID = -1;
            for(int j = 0; j < numOfUnsafeCell; j++) {
                if(wumpusPossibility[ unsafeCell[j] / 10 ][ unsafeCell[j] % 10 ] == 2) {
                    riskyNodeID = unsafeCell[j];
                    String side = "";
                    if(riskyNodeID == currentNodeID-1) side += "Left";
                    else if(riskyNodeID == currentNodeID+1) side += "Right";
                    else if(riskyNodeID == currentNodeID-10) side += "Top";
                    else if(riskyNodeID == currentNodeID+10) side += "Bottom";
                    break;
                }
            }

            if(riskyNodeID == -1)
            {
                do {
                    Random rand = new Random();
                    int x = rand.nextInt( numOfUnsafeCell );
                    riskyNodeID = unsafeCell[x];
                }while(pitPossibility[ riskyNodeID / 10 ][ riskyNodeID % 10 ] == 2);

                String side = "";
                if(riskyNodeID == currentNodeID-1) side += "Left";
                else if(riskyNodeID == currentNodeID+1) side += "Right";
                else if(riskyNodeID == currentNodeID-10) side += "Top";
                else if(riskyNodeID == currentNodeID+10) side += "Bottom";

                if(isStenchHere)
                {
                    numOfArrows--;
                    System.out.println("Decision: Moving "+side+", Throwing arrow for the safety");

                    try {
                        Thread.sleep(sleep - 200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(board[ riskyNodeID / 10 ][ riskyNodeID % 10 ][Wumpus] == 1)
                    {
                        board[ riskyNodeID / 10 ][ riskyNodeID % 10 ][Wumpus] = 0;

                        if((riskyNodeID % 10)-1!=-1)
                            board[riskyNodeID / 10][(riskyNodeID % 10)-1][Stench] = 0;

                        if((riskyNodeID / 10)-1!=-1)
                            board[(riskyNodeID / 10)-1][riskyNodeID % 10][Stench] = 0;

                        if((riskyNodeID % 10)+1!=10)
                            board[riskyNodeID / 10][(riskyNodeID % 10)+1][Stench] = 0;

                        if((riskyNodeID / 10)+1!=-1)
                            board[(riskyNodeID / 10)+1][riskyNodeID % 10][Stench] = 0;

                        System.out.println("Node: "+riskyNodeID+"\nResult:WUMPUS is dead! ");
                    }
                }

                else {
                    System.out.println("Decision: Taking risk, moving "+side);
                }
            }

            try {
                Thread.sleep(sleep - 150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parent[riskyNodeID] = currentNodeID;
            board[ riskyNodeID / 10 ][ riskyNodeID % 10 ][Visited] = 1;
            lastNode = riskyNodeID;

            dfsExploringTheWorld(riskyNodeID);
        }
    }

    public void back(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class) ;
        startActivity(intent) ;
        finish() ;
    }
}