import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Player{

    protected boolean side;
    protected int height, width;

    protected List<Piece> pieceOnHand;

    public Player(boolean side, int height, int width){
        this.side = side;
        this.height = height;
        this.width = width;
        pieceOnHand = new ArrayList<Piece>();
    }

    public Player duplicatePlayer(){

        Player playerObj = new Player(side, height, width);
        playerObj.setPieceList(new ArrayList<Piece>(pieceOnHand));

        return playerObj;

    }

    public void setPieceList(List<Piece> pieceOnHand){
        this.pieceOnHand = pieceOnHand;
    }

    public List<Piece> getPieceList(){
        return pieceOnHand;
    }

    public void addPiece(Piece piece){
        pieceOnHand.add(piece);
    }

    public String showPieces(){
        String allPieces = " ";
        for(Piece piece: pieceOnHand){
            allPieces += piece.getAnimal() + " ";
        }
        return allPieces;
    }

    public void removeAllPieces(){
        pieceOnHand.clear();
    }

    public void removePiece(Piece piece){
        pieceOnHand.remove(piece);
    }

    public boolean pieceExists(Animal animal){
        for(Piece piece: pieceOnHand){
            if(piece.getAnimal().equals(animal)){
                return true;
            }
        }
        return false;
    }

    public Piece getPiece(Animal animal){
        for(Piece piece: pieceOnHand){
            if(piece.getAnimal().equals(animal)){
                return piece;
            }
        }
        return null;
    }

    public boolean canMove(Piece[][] board, int x, int y, int n_x, int n_y){

        //System.out.println(height+" "+width);

        if(x > height || y > width){
            return false;
        }

        if(x < 0 || y < 0){
            return false;
        }

        if(board[x][y] == null){
            return false;
        }

        boolean side = board[x][y].getSide();

        if(side != this.side){
            return false;
        }

        for(int[] pair: possibleMoves(board[x][y])){

            //possible moves
            int p_x = x + pair[0]; 
            int p_y = y + pair[1]; 

            if(p_x >= height || p_y >= width){
                continue;
            }

            if(p_x < 0 || p_y < 0){
                continue;
            }

            if(p_x == n_x && p_y == n_y){

                if(board[p_x][p_y] != null){
                    if((board[p_x][p_y].getSide() == board[x][y].getSide())){
                        return false;
                    }
                }

                return true;
            }

        }
        return false;
    }

    public boolean move(Piece[][] board, int x, int y, int n_x, int n_y, boolean turn){

        Piece piece = board[n_x][n_y];
        if(piece != null){
            piece.capture();
            addPiece(piece);
        }
        board[n_x][n_y] = board[x][y];
        board[x][y] = null;
        if(n_x == 0 && turn){
            board[n_x][n_y].promote();
        }else if(n_x == (height-1) && !turn){
            board[n_x][n_y].promote();
        }

        if(piece != null){
            if(piece.getAnimal() == Animal.LION){
                return true;
            }
        }
        return false;
    }

    public boolean drop(Piece[][] board, char animalInitial, int x, int y, boolean turn){

        Animal dropAnimal = identifyCharacter(animalInitial);
        
        if(x < 0||x > height||y < 0||y > width){
            System.out.println("No such block!");
        }
        else if(board[x][y]!=null){
            System.out.println("Cannot drop on another piece!");
        }
        else if(dropAnimal.equals(null)){
            System.out.println("No such animal!");
        }
        else if(turn == side && pieceExists(dropAnimal)){
            board[x][y] = getPiece(dropAnimal);
            removePiece(getPiece(dropAnimal));
            return true;
        }
        return false;

    }

    private Animal identifyCharacter(char animal){

        switch(animal){
            case 'l':
                return Animal.LION;
            case 'e':
                return Animal.ELEPHANT;
            case 'g':
                return Animal.GIRAFFE;
            case 'c':
                return Animal.CHICK;
            default:
                return null;
        }

    }

    boolean contains(int[][] combinations, int[] combination){
        for(int[] pair: combinations){
            if(Arrays.equals(pair, combination)){
                return true;
            }
        }
        return false;
    }

    int[][] possibleMoves(Piece piece){
        switch(piece.getAnimal()){
            case LION:
                return new int[][] { { 1, 0 },
                                     { -1, 0 },
                                     { 1, 1 },
                                     { 0, 1 },
                                     { -1, 1 },
                                     { 1, -1 },
                                     { 0, -1 },
                                     { -1, -1 } };
            case ELEPHANT:
                return new int[][] { { 1, 1 },
                                    { 1, -1 },
                                    { -1, -1 },
                                    { -1, 1 }};
            case GIRAFFE:
                return new int[][] { { 0, 1 },
                                    { 1, 0 },
                                    { 0, -1 },
                                    { -1, 0 }};
            case CHICK:
                if(side)
                    return new int[][] { { -1, 0 } };
                else
                    return new int[][] { { 1, 0 } };
            case ROOSTER:
                if(side)
                    return new int[][] { { 1, 0 },
                                     { -1, 0 },
                                     { -1, -1 },
                                     { 0, 1 },
                                     { -1, 1 },
                                     { 0, -1 }};
                else
                    return new int[][] { { 1, 0 },
                                     { -1, 0 },
                                     { 0, 1 },
                                     { 1, -1 },
                                     { 0, -1 },
                                     { 1, 1 } };
            default:
                return null;

        }
    }

}
