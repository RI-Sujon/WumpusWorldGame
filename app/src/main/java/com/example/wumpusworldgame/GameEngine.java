package com.example.wumpusworldgame;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine {
    private int[][][] board;

    private int[][] goldPositionForBfs;

    private int ROW = 10 ;
    private int COLUMN = 10 ;
    private int nDATA = 8 ;

    private int agentCurrentRow = 9 ;
    private int agentCurrentColumn = 0 ;
    private int agentFaceDirection = 0 ;

    public int numberOfGoldCollected = 0 ;
    public int numberOfGold = 0 ;
    public int numberOfPit = 0 ;
    public int numberOfWumpus = 0 ;
    public int numberOfArrowUsed = 0 ;
    public int numberOfArrow = 0 ;
    public int numberOfMove = 0 ;
    public int numberOfWumpusKilled = 0 ;

    private int suggestedNextMoveRow, suggestedNextMoveColumn;

    int Visited=0,Breeze=1,Gold=2,SafeSquare=3,Pit=4,Stench=5,Wumpus=6,Glitter=7;

    GameEngine(){
        board = new int[ROW][COLUMN][nDATA] ;
    }

    public void setRandomEnvironment(int numberOfGold, int numberOfPit, int numberOfWumpus){

        this.numberOfGold = numberOfGold ;
        this.numberOfPit = numberOfPit ;
        this.numberOfWumpus = numberOfWumpus ;
        this.numberOfArrow = numberOfWumpus ;
        //board[agentCurrentRow][agentCurrentColumn][0] = 1 ;
        generateRandomBoard(numberOfGold, numberOfPit, numberOfWumpus);
    }

    private void generateRandomBoard(int numOfGold, int numOfPit, int numOfWumpus) {
        //System.out.println(numOfGold+" "+numOfPit+" "+numOfWumpus);
        //agent_placement = new int [10][10];
        //agent_placement[9][0] = 1;

        do {
            goldPositionForBfs = new int[ROW][COLUMN];
            for (int r=0; r<ROW; r++){
                for (int c=0; c<COLUMN; c++){
                    goldPositionForBfs[r][c] = 0 ;
                }
            }

            for (int r=0; r<ROW; r++){
                for (int c=0; c<COLUMN; c++){
                    for (int k=0; k<nDATA ; k++){
                        board[r][c][k] = 0 ;
                    }
                }
            }

            board[agentCurrentRow][agentCurrentColumn][0] = 1 ;

            Random rand = new Random();
            for (int i = 0; i < numOfPit; i++) {
                int x = rand.nextInt(100);
                int row = x / 10;
                int col = x % 10;
                if ((row == ROW - 1 && (col == 0 || col == 1)) || (row == ROW - 2 && col == 0)) {
                    i--;
                    continue;
                }

                if(board[row][col][Pit] == 1){
                    i--;
                    continue;
                }

                    //board[row][col][Pit] = 1;
                setWumpusOrPitOrGoldAndWarning(row, col, Pit);
            }

            for(int i = 0; i< numOfWumpus; i++) {
                int x = rand.nextInt( 100 );
                int row = x / 10;
                int col = x % 10;
                if (  (row == ROW -1 && (col == 0 || col == 1)) || (row == ROW -2  && col == 0 )  ){
                    i --;
                    continue;
                }

                if(board[row][col][Pit] == 1 || board[row][col][Wumpus] == 1){
                    i--;
                    continue;
                }
                    //board[row][col][Wumpus] = 1;
                setWumpusOrPitOrGoldAndWarning(row, col, Wumpus);
            }

            for(int i = 0; i< numOfGold; i++) {
                int x = rand.nextInt( 100 );
                int row = x / 10;
                int col = x % 10;
                if (row > Stench || ( row == ( ROW - 1 ) && col == 0) ){
                    i--;
                    continue;
                }
                if ( board[row][col][Pit] == 1 || board[row][col][Gold] == 1) {
                    i--;
                    continue;
                }

                //board[row][col][Gold] = 1;
                goldPositionForBfs[row][col] = 1 ;
                setWumpusOrPitOrGoldAndWarning(row, col, Gold);
            }

        } while(!bfs());
    }

//    private boolean bfs( )
//    {
//        ArrayList<Integer> queue = new ArrayList<Integer>();
//        boolean isSolutionExist = false;
//
//        int[][] checked = new int[ROW][COLUMN];
//        int[][] nodesID = new int[ROW][COLUMN];
//        int[][] relationships = new int[ROW * COLUMN][ROW * COLUMN];
//
//        int node_counter = 0;
//
//        for (int row = 0; row < ROW; row++ ) {
//            for (int col = 0; col < COLUMN; col++ ) {
//                checked[row][col] = 0;
//            }
//        }
//
//        for (int row = 0; row < ROW; row++ ) {
//            for (int col = 0; col < COLUMN; col++ ) {
//                nodesID[row][col] = node_counter;
//                node_counter += 1;
//            }
//        }
//
//        for (int row = 0; row < ROW * COLUMN; row++ ) {
//            for (int col = 0; col < ROW * COLUMN; col++ ) {
//                relationships[row][col] = 0;
//            }
//        }
//
//        for (int row = 0; row < ROW; row++ ) {
//            for (int col = 0; col < COLUMN; col++ ) {
//                try {
//                    relationships[ nodesID[row][col] ][ nodesID[row][col-1]  ] = 1;
//                }
//                catch( ArrayIndexOutOfBoundsException e ){  }
//
//                try {
//                    relationships[ nodesID[row][col] ][ nodesID[row-1][col]  ] = 1;
//                }
//                catch( ArrayIndexOutOfBoundsException e ){  }
//
//                try {
//                    relationships[ nodesID[row][col] ][ nodesID[row][col+1]  ] = 1;
//                }
//                catch( ArrayIndexOutOfBoundsException e ){  }
//
//                try {
//                    relationships[ nodesID[row][col] ][ nodesID[row+1][col]  ] = 1;
//                }
//                catch( ArrayIndexOutOfBoundsException e ){  }
//            }
//        }
//
//        queue.add( nodesID[ROW -1][0 ] );
//        checked[ROW -1][0 ] = 1;
//
//        while( !queue.isEmpty() ) {
//            int node = queue.remove( 0 );
//
//            if(board[(int)node/10][(int)node%10][Gold] == 1) {
//                isSolutionExist = true;
//                break;
//            }
//            else {
//                for (int i = 0; i<ROW*COLUMN; i++ ) {
//                    if ( relationships[node][i] == 1 && board[(int)i/10][(int)i%10][Pit] != 1 && checked[(int)i/10][(int)i%10] != 1 ) {
//                        queue.add(nodesID[(int)i/10][(int)i%10]);
//                        checked[(int)i/10][(int)i%10] = 1;
//                    }
//                }
//            }
//        }
//
//        return isSolutionExist;
//    }

    private boolean bfs( ) {
        boolean isSolutionExist = false;

        for(int g=0 ; g<numberOfGold ; g++){
            ArrayList<Integer> queue = new ArrayList<Integer>();
            isSolutionExist = false ;

            int[][] checked = new int[ROW][COLUMN];
            int[][] nodesID = new int[ROW][COLUMN];
            int[][] relationships = new int[ROW * COLUMN][ROW * COLUMN];

            int node_counter = 0;

            for (int row = 0; row < ROW; row++ ) {
                for (int col = 0; col < COLUMN; col++ ) {
                    checked[row][col] = 0;
                }
            }

            for (int row = 0; row < ROW; row++ ) {
                for (int col = 0; col < COLUMN; col++ ) {
                    nodesID[row][col] = node_counter;
                    node_counter += 1;
                }
            }

            for (int row = 0; row < ROW * COLUMN; row++ ) {
                for (int col = 0; col < ROW * COLUMN; col++ ) {
                    relationships[row][col] = 0;
                }
            }

            for (int row = 0; row < ROW; row++ ) {
                for (int col = 0; col < COLUMN; col++ ) {
                    try {
                        relationships[nodesID[row][col]][nodesID[row][col-1]] = 1;
                    }
                    catch( ArrayIndexOutOfBoundsException e ){  }

                    try {
                        relationships[nodesID[row][col]][nodesID[row-1][col]] = 1;
                    }
                    catch( ArrayIndexOutOfBoundsException e ){  }

                    try {
                        relationships[nodesID[row][col]][nodesID[row][col+1]] = 1;
                    }
                    catch( ArrayIndexOutOfBoundsException e ){  }

                    try {
                        relationships[nodesID[row][col]][nodesID[row+1][col]] = 1;
                    }
                    catch( ArrayIndexOutOfBoundsException e ){  }
                }
            }

            queue.add( nodesID[ROW -1][0 ] );
            checked[ROW -1][0 ] = 1;

            while(!queue.isEmpty()) {
                int node = queue.remove( 0 );

                if(goldPositionForBfs[(int)node/10][(int)node%10] == 1) {
                    isSolutionExist = true;
                    goldPositionForBfs[(int)node/10][(int)node%10] = 0 ;
                    break;
                }
                else {
                    for (int i = 0; i<ROW*COLUMN; i++ ) {
                        if ( relationships[node][i] == 1 && board[(int)i/10][(int)i%10][Pit] != 1 && checked[(int)i/10][(int)i%10] != 1 ) {
                            queue.add(nodesID[(int)i/10][(int)i%10]);
                            checked[(int)i/10][(int)i%10] = 1;
                        }
                    }
                }
            }

            if(isSolutionExist==false)
                break;
        }

        return isSolutionExist;
    }


    private void setWumpusOrPitOrGoldAndWarning(int row, int column, int type){
        board[row][column][type] = 1 ;

        int warningType ;
        if(type==Pit)
            warningType = Breeze;

        else if(type==Wumpus)
            warningType = Stench;

        else
            warningType = Glitter;


        if(column+1!=COLUMN)
            board[row][column+1][warningType]++ ;

        if(column-1!=-1)
            board[row][column-1][warningType]++ ;

        if(row+1!=ROW)
            board[row+1][column][warningType]++ ;

        if(row-1!=-1)
            board[row-1][column][warningType]++ ;
    }

    public void deleteWumpusByArrowShooting(){
        numberOfArrowUsed++;

        if(agentFaceDirection==0){
            for (int c=agentCurrentColumn; c<COLUMN ; c++){
                if(board[agentCurrentRow][c][Wumpus]==1){
                    board[agentCurrentRow][c][Wumpus] = 0 ;
                    board[agentCurrentRow][c][0] = 1 ;
                    numberOfWumpusKilled++ ;

                    if(c+1!=COLUMN)
                        board[agentCurrentRow][c+1][Stench]-- ;

                    if(c-1!=-1)
                        board[agentCurrentRow][c-1][Stench]-- ;

                    if(agentCurrentRow+1!=ROW)
                        board[agentCurrentRow+1][c][Stench]-- ;

                    if(agentCurrentRow-1!=-1)
                        board[agentCurrentRow-1][c][Stench]-- ;

                    break;
                }
            }
        }
        else if(agentFaceDirection==2){
            for (int c=agentCurrentColumn; c>=0 ; c--){
                if(board[agentCurrentRow][c][Wumpus]==1){
                    board[agentCurrentRow][c][Wumpus] = 0 ;
                    board[agentCurrentRow][c][0] = 1 ;
                    numberOfWumpusKilled++ ;

                    if(c+1!=COLUMN)
                        board[agentCurrentRow][c+1][Stench]-- ;

                    if(c-1!=-1)
                        board[agentCurrentRow][c-1][Stench]-- ;

                    if(agentCurrentRow+1!=ROW)
                        board[agentCurrentRow+1][c][Stench]-- ;

                    if(agentCurrentRow-1!=-1)
                        board[agentCurrentRow-1][c][Stench]-- ;

                    break;
                }
            }
        }
        else if(agentFaceDirection==1){
            for (int r=agentCurrentRow; r<ROW ; r++){
                if(board[r][agentCurrentColumn][Wumpus]==1){
                    board[r][agentCurrentColumn][Wumpus] = 0 ;
                    board[r][agentCurrentColumn][0] = 1 ;
                    numberOfWumpusKilled++ ;

                    if(agentCurrentColumn+1!=COLUMN)
                        board[r][agentCurrentColumn+1][Stench]-- ;

                    if(agentCurrentColumn-1!=-1)
                        board[r][agentCurrentColumn-1][Stench]-- ;

                    if(r+1!=ROW)
                        board[r+1][agentCurrentColumn][Stench]-- ;

                    if(r-1!=-1)
                        board[r-1][agentCurrentColumn][Stench]-- ;

                    break;
                }
            }
        }
        else if(agentFaceDirection==3){
            for (int r=agentCurrentRow; r>=0 ; r--){
                if(board[r][agentCurrentColumn][Wumpus]==1){
                    board[r][agentCurrentColumn][Wumpus] = 0 ;
                    board[r][agentCurrentColumn][0] = 1 ;
                    numberOfWumpusKilled++ ;

                    if(agentCurrentColumn+1!=COLUMN)
                        board[r][agentCurrentColumn+1][Stench]-- ;

                    if(agentCurrentColumn-1!=-1)
                        board[r][agentCurrentColumn-1][Stench]-- ;

                    if(r+1!=ROW)
                        board[r+1][agentCurrentColumn][Stench]-- ;

                    if(r-1!=-1)
                        board[r-1][agentCurrentColumn][Stench]-- ;

                    break;
                }
            }
        }
    }

    public boolean collectGold(){
        if(board[agentCurrentRow][agentCurrentColumn][Gold] == 1){
            numberOfGoldCollected++ ;
            board[agentCurrentRow][agentCurrentColumn][Gold] = 0 ;

            if(agentCurrentColumn+1!=COLUMN)
                board[agentCurrentRow][agentCurrentColumn+1][Glitter]-- ;

            if(agentCurrentColumn-1!=-1)
                board[agentCurrentRow][agentCurrentColumn-1][Glitter]-- ;

            if(agentCurrentRow+1!=ROW)
                board[agentCurrentRow+1][agentCurrentColumn][Glitter]-- ;

            if(agentCurrentRow-1!=-1)
                board[agentCurrentRow-1][agentCurrentColumn][Glitter]-- ;

            return true;
        }

        return false;
    }

    public void setCellViewed(){
        board[agentCurrentRow][agentCurrentColumn][Visited] = 1 ;
    }

    public boolean checkGameOverByPit(){
        if(board[agentCurrentRow][agentCurrentColumn][Pit] == 1){
            return true;
        }

        return false;
    }

    public boolean checkGameOverByWumpus(){
        if(board[agentCurrentRow][agentCurrentColumn][Wumpus] == 1){
            return true;
        }

        return false;
    }

    public int[][][] getGameBoard(){
        return board ;
    }

    public int getAgentCurrentRow() {
        return agentCurrentRow;
    }

    public void setAgentCurrentRow(int agentCurrentRow) {
        this.agentCurrentRow = agentCurrentRow;
    }

    public int getAgentCurrentColumn() {
        return agentCurrentColumn;
    }

    public void setAgentCurrentColumn(int agentCurrentColumn) {
        this.agentCurrentColumn = agentCurrentColumn;
    }

    public int getAgentFaceDirection() {
        return agentFaceDirection;
    }

    public void setAgentFaceDirection(int agentFaceDirection) {
        this.agentFaceDirection = agentFaceDirection;
    }
}
