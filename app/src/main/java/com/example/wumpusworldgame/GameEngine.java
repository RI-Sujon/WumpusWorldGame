package com.example.wumpusworldgame;

public class GameEngine {
    private int[][][] gameBoard;

    private int ROW = 10 ;
    private int COLUMN = 10 ;
    private int nDATA = 7 ;

    private int agentCurrentRow = 9 ;
    private int agentCurrentColumn = 0 ;
    private int agentFaceDirection = 0 ;

    private int suggestedNextMoveRow, suggestedNextMoveColumn;

    GameEngine(){
        gameBoard = new int[ROW][COLUMN][nDATA] ;

        for (int r=0; r<ROW; r++){
            for (int c=0; c<COLUMN; c++){
                for (int k=0; k<nDATA ; k++){
                    gameBoard[r][c][k] = 0 ;
                }
            }
        }

        setTheGameBoard();
    }

    public void setTheGameBoard(){
        //Nadim will developed it.....
        //1..breeze
        //2..gold
        //4..pit
        //5..stench
        //6..wumpus
        //Dummy data here

        gameBoard[5][5][4] = 1 ;
        gameBoard[5][4][1] = 1 ;
        gameBoard[5][6][1] = 1 ;
        gameBoard[6][5][1] = 1 ;
        gameBoard[4][5][1] = 1 ;

        gameBoard[4][4][6] = 1 ;
        gameBoard[5][4][5] = 1 ;
        gameBoard[3][4][5] = 1 ;
        gameBoard[4][5][5] = 1 ;
        gameBoard[4][3][5] = 1 ;

        gameBoard[3][4][2] = 1 ;
        gameBoard[5][4][2] = 1 ;
    }

    public boolean deleteWumpus(int row, int col, String agentFaceDirection){
        if(gameBoard[row-1][col-1][6]==0){
            gameBoard[row-1][col-1][6] = 0;
        }
        else{
            return false;
        }
        return true;
    }

    public int[][][] getGameBoard(){
        return gameBoard ;
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
