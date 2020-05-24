import java.util.ArrayList;
import java.util.List;

public class Minimax extends Player{
    
    private final int valLION = 5;
    private final int valGIRAFFE = 2;
    private final int valELEPHANT = 2;
    private final int valCHICK = 1;
    private final int valROOSTER = 4;

    Board tempBoard;
    int depth;

    public Minimax(boolean side, int height, int width){
        super(side, height, width);
    }

    @Override
    public Minimax duplicatePlayer(){
        
        Minimax minimaxObj = new Minimax(side, height, width);
        minimaxObj.setPieceList(new ArrayList<>(pieceOnHand));

        return minimaxObj;
    }

    //Only a chick should get a position advantage because its value increases as it promotes (because a rooster has more possible moves)
    private int positionEval(Board boardObj){

        Piece[][] board = boardObj.getBoard();
        int positionVal = 0;
        for(int x = 0; x < board.length; x++){
            for (int y = 0; y < board[x].length; y++){
                if(board[x][y] == null)
                    continue;
                Animal animal = board[x][y].getAnimal();
                boolean sideOfPiece = board[x][y].getSide();

                positionVal += score(animal, sideOfPiece);

                if(side == sideOfPiece){
                    if(animal == Animal.CHICK && side && x == 1)
                        positionVal++;
                    else if(animal == Animal.CHICK && !side && x == 2)
                        positionVal++;
                }
                else{
                    if(animal == Animal.CHICK && side && x == 1)
                        positionVal--;
                    else if(animal == Animal.CHICK && !side && x == 2)
                        positionVal--;
                }
            }
        }

        List<Piece> pieceOnHand1 = boardObj.getPlayer1().getPieceList();
        List<Piece> pieceOnHand2 = boardObj.getPlayer3().getPieceList();
        
        for(Piece piece: pieceOnHand1){
            Animal animal = piece.getAnimal();
            boolean sideOfPiece = piece.getSide();
            positionVal += score(animal, sideOfPiece);
        }

        for(Piece piece: pieceOnHand2){
            Animal animal = piece.getAnimal();
            boolean sideOfPiece = piece.getSide();
            positionVal += score(animal, sideOfPiece);
        }


        return positionVal;
    }

    private int score(Animal animal, boolean sideOfPiece){
        //side is maximizing thats why
        if(side == sideOfPiece){
            if(animal == Animal.LION){
                return valLION;
            }
            else if(animal == Animal.GIRAFFE){
                return valGIRAFFE;
            }
            else if(animal == Animal.ELEPHANT){
                return valELEPHANT;
            }
            else if(animal == Animal.ROOSTER){
                return valROOSTER;
            }
            else if(animal == Animal.CHICK){
                //the chick gets a point advantage as it approaches the last rank
                return valCHICK;
            }
        }
        else{
            if(animal == Animal.LION){
                return -valLION;
            }
            else if(animal == Animal.GIRAFFE){
                return -valGIRAFFE;
            }
            else if(animal == Animal.ELEPHANT){
                return -valELEPHANT;
            }
            else if(animal == Animal.ROOSTER){
                return -valROOSTER;
            }
            else if(animal == Animal.CHICK){
                return -valCHICK;
            }
        }
        return 0;
    }

    //Possibility of returning a map, which map to gameOver
    //Might have to also return the two lists
    private List<Board> possibleStates(Board boardObj){
        
        List<Board> possibleStates = new ArrayList<>();

        final Piece[][] currentBoard = boardObj.getBoard();
        final boolean turn = boardObj.getTurn();

        Board tempBoardObj;
        Player player1;
        Minimax player3;
        Piece[][] tempBoard;

        for(int x = 0; x < height; x++){
            for(int y = 0; y < width; y++){
                if(currentBoard[x][y] != null){

                    if(turn != currentBoard[x][y].getSide())
                        continue;


                    for(int[] pair: possibleMoves(currentBoard[x][y])){
                        //possible moves
                        int p_x = x + pair[0]; 
                        int p_y = y + pair[1]; 

                        tempBoardObj = boardObj.duplicateBoard();
                        player1 = tempBoardObj.getPlayer1();
                        player3 = tempBoardObj.getPlayer3();
                        tempBoard = tempBoardObj.getBoard();
                        if(turn){
                            if(player1.canMove(currentBoard, x, y, p_x, p_y)){
                                tempBoardObj.setGameOver(player1.move(tempBoard, x, y, p_x, p_y, turn));
                                tempBoardObj.setBoard(tempBoard);
                                tempBoardObj.setPlayer1(player1);
                                tempBoardObj.setPlayer3(player3);
                                tempBoardObj.changeTurn();
                                possibleStates.add(tempBoardObj);
                            }
                        }
                        else{
                            if(player3.canMove(currentBoard, x, y, p_x, p_y)){
                                tempBoardObj.setGameOver(player3.move(tempBoard, x, y, p_x, p_y, turn));
                                //System.out.println("hi");
                                tempBoardObj.setBoard(tempBoard);
                                tempBoardObj.setPlayer1(player1);
                                tempBoardObj.setPlayer3(player3);
                                tempBoardObj.changeTurn();
                                possibleStates.add(tempBoardObj);
                            }

                        }
                    }
                }
                else{
                    tempBoardObj = boardObj.duplicateBoard();
                    player1 = tempBoardObj.getPlayer1();
                    player3 = tempBoardObj.getPlayer3();
                    tempBoard = tempBoardObj.getBoard();
                    if(turn){
                        if(player1.pieceExists(Animal.CHICK)){
                            tempBoardObj = boardObj.duplicateBoard();
                            player1 = tempBoardObj.getPlayer1();
                            player3 = tempBoardObj.getPlayer3();
                            tempBoard = tempBoardObj.getBoard();
                            tempBoard = tempBoardObj.getBoard();
                            player1.drop(tempBoard, 'c', x, y, turn);
                            tempBoardObj.setBoard(tempBoard);
                            tempBoardObj.setPlayer1(player1);
                            tempBoardObj.setPlayer3(player3);
                            tempBoardObj.changeTurn();
                            possibleStates.add(tempBoardObj);
                        }
                        if(player1.pieceExists(Animal.GIRAFFE)){
                            tempBoardObj = boardObj.duplicateBoard();
                            player1 = tempBoardObj.getPlayer1();
                            player3 = tempBoardObj.getPlayer3();
                            tempBoard = tempBoardObj.getBoard();
                            tempBoard = tempBoardObj.getBoard();
                            player1.drop(tempBoard, 'g', x, y, turn);
                            tempBoardObj.setBoard(tempBoard);
                            tempBoardObj.setPlayer1(player1);
                            tempBoardObj.setPlayer3(player3);
                            tempBoardObj.changeTurn();
                            possibleStates.add(tempBoardObj);
                        }
                        if(player1.pieceExists(Animal.ELEPHANT)){ tempBoardObj = boardObj.duplicateBoard();
                            player1 = tempBoardObj.getPlayer1();
                            player3 = tempBoardObj.getPlayer3();
                            tempBoard = tempBoardObj.getBoard();
                            tempBoard = tempBoardObj.getBoard();
                            player1.drop(tempBoard, 'e', x, y, turn);
                            tempBoardObj.setBoard(tempBoard);
                            tempBoardObj.setPlayer1(player1);
                            tempBoardObj.setPlayer3(player3);
                            tempBoardObj.changeTurn();
                            possibleStates.add(tempBoardObj);
                        }
                    }                    
                    else{
                        if(player3.pieceExists(Animal.CHICK)){
                            tempBoardObj = boardObj.duplicateBoard();
                            player1 = tempBoardObj.getPlayer1();
                            player3 = tempBoardObj.getPlayer3();
                            tempBoard = tempBoardObj.getBoard();
                            tempBoard = tempBoardObj.getBoard();
                            player3.drop(tempBoard, 'c', x, y, turn);
                            tempBoardObj.setBoard(tempBoard);
                            tempBoardObj.setPlayer1(player1);
                            tempBoardObj.setPlayer3(player3);
                            tempBoardObj.changeTurn();
                            possibleStates.add(tempBoardObj);
                        }
                        if(player3.pieceExists(Animal.GIRAFFE)){
                            tempBoardObj = boardObj.duplicateBoard();
                            player1 = tempBoardObj.getPlayer1();
                            player3 = tempBoardObj.getPlayer3();
                            tempBoard = tempBoardObj.getBoard();
                            tempBoard = tempBoardObj.getBoard();
                            player3.drop(tempBoard, 'g', x, y, turn);
                            tempBoardObj.setBoard(tempBoard);
                            tempBoardObj.setPlayer1(player1);
                            tempBoardObj.setPlayer3(player3);
                            tempBoardObj.changeTurn();
                            possibleStates.add(tempBoardObj);
                        }
                        if(player3.pieceExists(Animal.ELEPHANT)){
                            tempBoardObj = boardObj.duplicateBoard();
                            player1 = tempBoardObj.getPlayer1();
                            player3 = tempBoardObj.getPlayer3();
                            tempBoard = tempBoardObj.getBoard();
                            tempBoard = tempBoardObj.getBoard();
                            player3.drop(tempBoard, 'e', x, y, turn);
                            tempBoardObj.setBoard(tempBoard);
                            tempBoardObj.setPlayer1(player1);
                            tempBoardObj.setPlayer3(player3);
                            tempBoardObj.changeTurn();
                            possibleStates.add(tempBoardObj);
                        }
                    }                    
                }
            }
        }
        
        //for(Board board: possibleStates){
            //board.showCurrentBoard();
        //}
        
        return possibleStates;
    }

    private int changeState(Board boardObj, int depth, boolean player){

        if(boardObj.getGameover() || depth == 0)
            return positionEval(boardObj);

        //Maximizing player
        if(player){
            int eval;
            int maxEval = Integer.MIN_VALUE;
            for(Board board: possibleStates(boardObj)){
                eval = changeState(board, depth-1, false);
                if(maxEval < eval){
                    maxEval = eval;
                    if(depth == this.depth)
                        tempBoard = board;
                }
            }
            return maxEval;
        }
        //Minimizing player
        else{
            int eval;
            int minEval = Integer.MAX_VALUE;
            for(Board board: possibleStates(boardObj)){
                eval = changeState(board, depth-1, true);
                if(minEval > eval){
                    minEval = eval;
                }
            }
            return minEval;
        }

    }

    public void useEngine(Board board, int depth){
        this.depth = depth;
        changeState(board, depth, true);
        //System.out.println(eval);
        /*Board nextBoard = findBoard(eval, board);
        board.setBoard(nextBoard.getBoard());
        board.setGameOver(nextBoard.getGameover());
        board.setPlayer1(nextBoard.getPlayer1());
        board.setPlayer3(nextBoard.getPlayer3());*/

        board.setBoard(tempBoard.getBoard());
        board.setGameOver(tempBoard.getGameover());
        board.setPlayer1(tempBoard.getPlayer1());
        board.setPlayer3(tempBoard.getPlayer3());
    }

}
