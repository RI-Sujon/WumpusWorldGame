package com.example.wumpusworldgame;

public class GameEngine {
    private int[][][] gameBoard;

    private int ROW = 10 ;
    private int COLUMN = 10 ;
    private int nDATA = 7 ;

    private int agentCurrentRow = 9 ;
    private int agentCurrentColumn = 0 ;
    private int agentFaceDirection = 0 ;

    public int numberOfGoldCollected = 0 ;
    public int numberOfArrowUsed = 0 ;
    public int numberOfMove = 0 ;

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
        //Nadim will develop it.....
        //1..breeze
        //2..gold
        //4..pit
        //5..stench
        //6..wumpus
        //Dummy data here

        gameBoard[agentCurrentRow][agentCurrentColumn][0] = 1 ;

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

    public void deleteWumpusByArrowShooting(){
        if(agentFaceDirection==0){
            for (int c=agentCurrentColumn; c<COLUMN ; c++){
                if(gameBoard[agentCurrentRow][c][6]==1){
                    gameBoard[agentCurrentRow][c][6] = 0 ;
                    gameBoard[agentCurrentRow][c][0] = 1 ;

                    if(c+1!=COLUMN)
                        gameBoard[agentCurrentRow][c+1][5] = 0 ;

                    if(c-1!=-1)
                        gameBoard[agentCurrentRow][c-1][5] = 0 ;

                    if(agentCurrentRow+1!=ROW)
                        gameBoard[agentCurrentRow+1][c][5] = 0 ;

                    if(agentCurrentRow-1!=-1)
                        gameBoard[agentCurrentRow-1][c][5] = 0 ;

                    break;
                }
            }
        }
        else if(agentFaceDirection==2){
            for (int c=agentCurrentColumn; c>=0 ; c--){
                if(gameBoard[agentCurrentRow][c][6]==1){
                    gameBoard[agentCurrentRow][c][6] = 0 ;
                    gameBoard[agentCurrentRow][c][0] = 1 ;

                    if(c+1!=COLUMN)
                        gameBoard[agentCurrentRow][c+1][5] = 0 ;

                    if(c-1!=-1)
                        gameBoard[agentCurrentRow][c-1][5] = 0 ;

                    if(agentCurrentRow+1!=ROW)
                        gameBoard[agentCurrentRow+1][c][5] = 0 ;

                    if(agentCurrentRow-1!=-1)
                        gameBoard[agentCurrentRow-1][c][5] = 0 ;

                    break;
                }
            }
        }
        else if(agentFaceDirection==1){
            for (int r=agentCurrentRow; r<ROW ; r++){
                if(gameBoard[r][agentCurrentColumn][6]==1){
                    gameBoard[r][agentCurrentColumn][6] = 0 ;
                    gameBoard[r][agentCurrentColumn][0] = 1 ;

                    if(agentCurrentColumn+1!=COLUMN)
                        gameBoard[r][agentCurrentColumn+1][5] = 0 ;

                    if(agentCurrentColumn-1!=-1)
                        gameBoard[r][agentCurrentColumn-1][5] = 0 ;

                    if(r+1!=ROW)
                        gameBoard[r+1][agentCurrentColumn][5] = 0 ;

                    if(r-1!=-1)
                        gameBoard[r-1][agentCurrentColumn][5] = 0 ;

                    break;
                }
            }
        }
        else if(agentFaceDirection==3){
            for (int r=agentCurrentRow; r>=0 ; r--){
                if(gameBoard[r][agentCurrentColumn][6]==1){
                    gameBoard[r][agentCurrentColumn][6] = 0 ;
                    gameBoard[r][agentCurrentColumn][0] = 1 ;

                    if(agentCurrentColumn+1!=COLUMN)
                        gameBoard[r][agentCurrentColumn+1][5] = 0 ;

                    if(agentCurrentColumn-1!=-1)
                        gameBoard[r][agentCurrentColumn-1][5] = 0 ;

                    if(r+1!=ROW)
                        gameBoard[r+1][agentCurrentColumn][5] = 0 ;

                    if(r-1!=-1)
                        gameBoard[r-1][agentCurrentColumn][5] = 0 ;

                    break;
                }
            }
        }
    }

    public boolean collectGold(){
        if(gameBoard[agentCurrentRow][agentCurrentColumn][2] == 1){
            numberOfGoldCollected++ ;
            gameBoard[agentCurrentRow][agentCurrentColumn][2] = 0 ;

            return true;
        }

        return false;
    }

    public void setCellViewed(){
        gameBoard[agentCurrentRow][agentCurrentColumn][0] = 1 ;
    }

    public boolean checkGameOverByPit(){
        if(gameBoard[agentCurrentRow][agentCurrentColumn][4] == 1){
            return true;
        }

        return false;
    }

    public boolean checkGameOverByWumpus(){
        if(gameBoard[agentCurrentRow][agentCurrentColumn][6] == 1){
            return true;
        }

        return false;
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
