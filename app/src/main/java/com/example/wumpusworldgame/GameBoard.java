package com.example.wumpusworldgame;

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

    private int cellSize = getWidth()/10 ;

    private GameEngine gameEngine = new GameEngine() ;

    private Bitmap agentBitmap[] = new Bitmap[4] ;
    private Bitmap wumpusBitmap, pitBitmap, goldBitmap, breezeBitmap, stenchBitmap  ;

    private int ROW=10, COLUMN=10 ;

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

        int dimensions = Math.min(getMeasuredWidth(), getMeasuredHeight()) ;
        cellSize = dimensions/COLUMN ;
        setMeasuredDimension(dimensions, dimensions);
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
        breezeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.breeze) ;
        stenchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stench2) ;

        agentBitmap[0] = Bitmap.createScaledBitmap(agentBitmap[0],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        agentBitmap[1] = Bitmap.createScaledBitmap(agentBitmap[1],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        agentBitmap[2] = Bitmap.createScaledBitmap(agentBitmap[2],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        agentBitmap[3] = Bitmap.createScaledBitmap(agentBitmap[3],(int)(cellSize*0.5), (int)(cellSize*0.5), false) ;
        wumpusBitmap = Bitmap.createScaledBitmap(wumpusBitmap,(int)(cellSize*0.75), (int)(cellSize*0.7), false) ;
        pitBitmap = Bitmap.createScaledBitmap(pitBitmap,(int)(cellSize*0.8), (int)(cellSize*0.5), false) ;
        goldBitmap = Bitmap.createScaledBitmap(goldBitmap,(int)(cellSize*0.3), (int)(cellSize*0.40), false) ;
        breezeBitmap = Bitmap.createScaledBitmap(breezeBitmap,(int)(cellSize*0.3), (int)(cellSize*0.15), false) ;
        stenchBitmap = Bitmap.createScaledBitmap(stenchBitmap,(int)(cellSize*0.35), (int)(cellSize*0.2), false) ;

        drawGameBoard(canvas) ;
        drawMarkers(canvas) ;
    }

    private void drawGameBoard(Canvas canvas){
        paint.setColor(boardColor);
        paint.setStrokeWidth(5);

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
                if(gameEngine.getGameBoard()[r][c][0]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][0]==1){

                }

                if(gameEngine.getGameBoard()[r][c][1]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][1]==1){
                    drawBreeze(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][2]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][2]==1){
                    drawGold(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][3]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][3]==1){

                }

                if(gameEngine.getGameBoard()[r][c][4]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][4]==1){
                    drawPit(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][5]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][5]==1){
                    drawStench(canvas, r, c);
                }

                if(gameEngine.getGameBoard()[r][c][6]==0){
                }
                else if(gameEngine.getGameBoard()[r][c][6]==1){
                    drawWumpus(canvas, r, c);
                }
            }
        }

        drawAgent(canvas, gameEngine.getAgentCurrentRow(), gameEngine.getAgentCurrentColumn(), gameEngine.getAgentFaceDirection());

    }

    private void drawStench(Canvas canvas, int row, int col){
        canvas.drawBitmap(stenchBitmap, (float)(col*cellSize + cellSize*0.07),(float)(row*cellSize + cellSize*0.05), null);
    }

    private void drawBreeze(Canvas canvas, int row, int col){
        canvas.drawBitmap(breezeBitmap, (float)(col*cellSize + cellSize*0.63),(float)(row*cellSize + cellSize*0.1), paint);
    }

    private void drawGold(Canvas canvas, int row, int col){
        canvas.drawBitmap(goldBitmap,(float)(col*cellSize + cellSize*0.65),(float)(row*cellSize + cellSize*0.55), null);
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

    public void moveForward(){
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
    }

    public void turn90Clockwise(){
        if(gameEngine.getAgentFaceDirection()==3){
            gameEngine.setAgentFaceDirection(0);
        }else{
            gameEngine.setAgentFaceDirection(gameEngine.getAgentFaceDirection()+1);
        }
    }

    public void turn90AntiClockwise(){
        if(gameEngine.getAgentFaceDirection()==0){
            gameEngine.setAgentFaceDirection(3);
        }else{
            gameEngine.setAgentFaceDirection(gameEngine.getAgentFaceDirection()-1);
        }
    }
}
