package com.example.wumpusworldgame;

import java.util.ArrayList;
import java.util.Random;

    public class ExploringTheWorld
    {
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
        int lastNode;
        int startNode = 90;
        private int agentRow = 9, agentCol = 0;
        private boolean gameOver = false;
        private boolean noSafePath = false;
        boolean isSafeCellInPreviousNode = false;
        private int sleep = 1200;

        public void setGameBoard(int board[][][], int numOfArrows) throws Exception {
            //environmentSettingUp.setupEnvironment();
            doInBackground();
            board=board;
            numOfArrows=numOfArrows;
            //System.out.println("Arrows = "+environmentSettingUp.numOfArrow);
        }

        public Void doInBackground() throws Exception
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

            return null;
        }

        public void dfsExploringTheWorld(int currentNodeID)
        {
            String sense = "";
            System.out.println();
            if(gameOver) {

                return;
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

                boolean pitEntailedInThisMove = false, wumpusEntailedInThisMove = false;

                if(isBreezeHere && pitPossibility[nearestNodeId / 10][nearestNodeId % 10] != 0)
                {
                    if(pitPossibility[nearestNodeId / 10][nearestNodeId % 10] == -1 && wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] == 1)
                    {
                        pitPossibility[nearestNodeId / 10][nearestNodeId % 10] = 0;
                        wumpusPossibility[nearestNodeId / 10][nearestNodeId % 10] = 1;
                    }

                    else {
                        if (pitPossibility[nearestNodeId / 10][nearestNodeId % 10] == 1)
                            pitEntailedInThisMove = false;

                        else
                            pitEntailedInThisMove = true;

                        if(pitEntailedInThisMove)
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
                        if(pitEntailedInThisMove)
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
                        System.out.println("Decision: Stepping Back");

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
                            board[riskyNodeID / 10][(riskyNodeID % 10)-1][Stench] = 0;
                            board[(riskyNodeID / 10)-1][riskyNodeID % 10][Stench] = 0;
                            board[riskyNodeID / 10][(riskyNodeID % 10)+1][Stench] = 0;
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
    }

