package com.example.wumpusworldgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

class GameBoard extends View{
    private final int boardColor ;

    private final Paint paint = new Paint();

    private int cellSize, dimensions  ;

    private GameEngine gameEngine ;

    private Bitmap agentBitmap[] = new Bitmap[4] ;
    private Bitmap wumpusBitmap, pitBitmap, goldBitmap, glitterBitmap, breezeBitmap, stenchBitmap, grassBitmap, arrowBitmap  ;

    private int ROW = 0 , COLUMN = 0 ;

    private int gameOverFlag = 0 ;

    int Visited=0, Breeze=1, Gold=2, SafeSquare=3, Pit=4, Stench=5, Wumpus=6, Glitter=7;

    public GameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0) ;

        try{
            boardColor = a.getInteger(R.styleable.GameBoard_boardLineColor, 0) ;
        }finally {
            a.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        dimensions = Math.min(getMeasuredWidth(), getMeasuredHeight()) ;
        cellSize = dimensions/COLUMN ;
        setMeasuredDimension(dimensions, dimensions) ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE) ;
        paint.setAntiAlias(true);

        agentBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.agent) ;
        agentBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.agent1) ;
        agentBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.agent2) ;
        agentBitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.agent3) ;
        wumpusBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ganger) ;
        pitBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pit) ;
        goldBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gold3) ;
        glitterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.glitter) ;
        breezeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.breeze) ;
        stenchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stench2) ;
        grassBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grass) ;
        arrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow) ;

        agentBitmap[0] = Bitmap.createScaledBitmap(agentBitmap[0],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        agentBitmap[1] = Bitmap.createScaledBitmap(agentBitmap[1],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        agentBitmap[2] = Bitmap.createScaledBitmap(agentBitmap[2],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        agentBitmap[3] = Bitmap.createScaledBitmap(agentBitmap[3],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        wumpusBitmap = Bitmap.createScaledBitmap(wumpusBitmap,(int)(cellSize*0.75), (int)(cellSize*0.7), false) ;
        pitBitmap = Bitmap.createScaledBitmap(pitBitmap,(int)(cellSize*0.8), (int)(cellSize*0.5), false) ;
        goldBitmap = Bitmap.createScaledBitmap(goldBitmap,(int)(cellSize*0.3), (int)(cellSize*0.40), false) ;
        glitterBitmap = Bitmap.createScaledBitmap(glitterBitmap,(int)(cellSize*0.38), (int)(cellSize*0.38), false) ;
        breezeBitmap = Bitmap.createScaledBitmap(breezeBitmap,(int)(cellSize*0.3), (int)(cellSize*0.15), false) ;
        stenchBitmap = Bitmap.createScaledBitmap(stenchBitmap,(int)(cellSize*0.35), (int)(cellSize*0.2), false) ;
        grassBitmap = Bitmap.createScaledBitmap(grassBitmap,(int)(cellSize*1), (int)(cellSize*1), false) ;

        drawGameBoard(canvas) ;
        drawMarkers(canvas) ;

        //invalidate();
    }

    public int[][][] getGameBoard(){
        return gameEngine.getGameBoard() ;
    }

    private void drawGameBoard(Canvas canvas){
        paint.setColor(boardColor);
        paint.setStrokeWidth(1);

        for(int c=0; c<=COLUMN; c++){
            canvas.drawLine(cellSize*c, 0, cellSize*c, canvas.getWidth(), paint);
        }

        for(int r=0; r<=ROW; r++){
            canvas.drawLine(0, cellSize*r, canvas.getWidth(), cellSize*r, paint);
        }
    }

    private void drawMarkers(Canvas canvas){
        for(int r=0; r<ROW; r++){
            for(int c=0; c<COLUMN; c++){
                if(gameEngine.getGameBoard()[r][c][Visited]==0){
                    drawGrass(canvas, r, c);
                    continue;
                }
                else if(gameEngine.getGameBoard()[r][c][Visited]==1){
                }

                if(gameEngine.getGameBoard()[r][c][Breeze]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][Breeze]>=1){
                    drawBreeze(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][Gold]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][Gold]==1){
                    drawGold(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][SafeSquare]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][SafeSquare]==1){

                }

                if(gameEngine.getGameBoard()[r][c][Pit]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][Pit]==1){
                    drawPit(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][Stench]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][Stench]>=1){
                    drawStench(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][Wumpus]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][Wumpus]==1){
                    drawWumpus(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][Glitter]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][Glitter]>=1){
                    drawGlitter(canvas, r, c);
                }
            }
        }

        if(gameOverFlag==0) {
            drawAgent(canvas, gameEngine.getAgentCurrentRow(), gameEngine.getAgentCurrentColumn(), gameEngine.getAgentFaceDirection());
        }
        else{
            gameOverFlag = 0 ;
        }
    }

    private void drawGlitter(Canvas canvas, int row, int col){
        canvas.drawBitmap(glitterBitmap,(float)(col*cellSize + cellSize*0.6),(float)(row*cellSize + cellSize*0.6), null);
    }

    private void drawStench(Canvas canvas, int row, int col){
        canvas.drawBitmap(stenchBitmap, (float)(col*cellSize + cellSize*0.07),(float)(row*cellSize + cellSize*0.05), null);
    }

    private void drawBreeze(Canvas canvas, int row, int col){
        canvas.drawBitmap(breezeBitmap, (float)(col*cellSize + cellSize*0.63),(float)(row*cellSize + cellSize*0.1), paint);
    }

    private void drawGold(Canvas canvas, int row, int col){
        canvas.drawBitmap(goldBitmap,(float)(col*cellSize + cellSize*0.05),(float)(row*cellSize + cellSize*0.55), null);
    }

    private void drawWumpus(Canvas canvas, int row, int col){
        canvas.drawBitmap(wumpusBitmap,(float)(col*cellSize + cellSize*0.1),(float)(row*cellSize + cellSize*0.25), null);
    }

    private void drawPit(Canvas canvas, int row, int col){
        canvas.drawBitmap(pitBitmap,(float)(col*cellSize + cellSize*0.15),(float)(row*cellSize + cellSize*0.35), null);
    }

    private void drawAgent(Canvas canvas, int row, int col, int faceDirection){
        canvas.drawBitmap(agentBitmap[faceDirection],(float)(col*cellSize + cellSize*0.2),(float)(row*cellSize + cellSize*0.35), null);
    }

    private void drawGrass(Canvas canvas, int row, int col){
        canvas.drawBitmap(grassBitmap,(float)(col*cellSize),(float)(row*cellSize), null);
    }

    public void setEnvironment(int gameLevel){
        gameEngine = new GameEngine(gameLevel) ;
        ROW = gameEngine.ROW ;
        COLUMN = gameEngine.COLUMN ;
        gameEngine.setRandomEnvironment() ;
        cellSize = dimensions/COLUMN ;
        invalidate() ;
    }


    public int moveForward(){
        gameEngine.numberOfMove++ ;
        if(gameEngine.getAgentFaceDirection()==0 && gameEngine.getAgentCurrentColumn()!=COLUMN-1){
            gameEngine.setAgentCurrentColumn(gameEngine.getAgentCurrentColumn()+1);
        }
        else if(gameEngine.getAgentFaceDirection()==1 && gameEngine.getAgentCurrentRow()!=ROW-1){
            gameEngine.setAgentCurrentRow(gameEngine.getAgentCurrentRow()+1);
        }
        else if(gameEngine.getAgentFaceDirection()==2 && gameEngine.getAgentCurrentColumn()!=0){
            gameEngine.setAgentCurrentColumn(gameEngine.getAgentCurrentColumn()-1);
        }
        else if(gameEngine.getAgentFaceDirection()==3 && gameEngine.getAgentCurrentRow()!=0){
            gameEngine.setAgentCurrentRow(gameEngine.getAgentCurrentRow()-1);
        }

        gameEngine.setCellVisited();
        if(gameEngine.checkGameOverByWumpus()){
            gameOverFlag = -1 ;
            return -6 ;
        }
        if(gameEngine.checkGameOverByPit()){
            gameOverFlag = -1 ;
            return -4 ;
        }
        return 0;
    }

    public void turn90Clockwise(){
        gameEngine.numberOfMove++ ;
        if(gameEngine.getAgentFaceDirection()==3){
            gameEngine.setAgentFaceDirection(0);
        }else{
            gameEngine.setAgentFaceDirection(gameEngine.getAgentFaceDirection()+1);
        }
    }

    public void turn90AntiClockwise(){
        gameEngine.numberOfMove++ ;
        if(gameEngine.getAgentFaceDirection()==0){
            gameEngine.setAgentFaceDirection(3);
        }else{
            gameEngine.setAgentFaceDirection(gameEngine.getAgentFaceDirection()-1);
        }
    }

    public void collectGold(){
        gameEngine.numberOfMove++ ;
        gameEngine.collectGold() ;
    }

    public void shootByArrow(){
        gameEngine.deleteWumpusByArrowShooting();
    }

    public int getNumberOfGoldRemaining(){
        return gameEngine.numberOfGold - gameEngine.numberOfGoldCollected ;
    }

    public int getNumberOfArrowRemaining(){
        return gameEngine.numberOfArrow - gameEngine.numberOfArrowUsed ;
    }

    public int getNumberOfGoldCollected(){
        return gameEngine.numberOfGoldCollected ;
    }

    public int getNumberOfArrowUsed(){
        return gameEngine.numberOfArrowUsed ;
    }

    public int getNumberOfWumpusKilled(){
        return gameEngine.numberOfWumpusKilled ;
    }

    public int getNumberOfMove(){
        return gameEngine.numberOfMove ;
    }

    public int[] getAgentLocation(){
        int arr[] = new int[3] ;
        arr[0] = gameEngine.getAgentCurrentRow() ;
        arr[1] = gameEngine.getAgentCurrentColumn() ;
        arr[2] = gameEngine.getAgentFaceDirection() ;

        return arr ;
    }
}
