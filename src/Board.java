import java.util.Scanner;

public class Board{

    Piece[][] board;

    Player player1;
    Player player2;
    Minimax player3;

    boolean turn;
    boolean gameOver;

    private final int height = 4;
    private final int width = 3;

    public Board(){
        board = new Piece[height][width];

        reset(1);
    }

    public Board duplicateBoard(){

        Board boardObj = new Board();

        boardObj.setGameOver(gameOver);
        
        Piece[][] board = new Piece[height][width];
        for(int x = 0; x<height; x++){
            for(int y = 0; y<width; y++){
                if(this.board[x][y] != null)
                    board[x][y] = this.board[x][y].duplicatePiece();
                else
                    board[x][y] = null;
            }
        }

        boardObj.setBoard(board.clone());
        boardObj.setPlayer1(player1.duplicatePlayer());
        if(player2 != null)
            boardObj.setPlayer2(player2.duplicatePlayer());
        if(player3 != null)
            boardObj.setPlayer3(player3.duplicatePlayer());

        return boardObj;
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public void changeTurn(){
        turn = !turn;
    }

    public void setPlayer1(Player player1){
        this.player1 = player1;
    }

    public void setPlayer3(Minimax player3){
        this.player3 = player3;
    }

    public void setPlayer2(Player player2){
        this.player2 = player2;
    }

    public void setBoard(Piece[][] board){
        this.board = board;
    }

    public Piece[][] getBoard(){
        return board;
    }

    public boolean getGameover(){
        return gameOver;
    }

    public boolean getTurn(){
        return turn;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Minimax getPlayer3(){
        return player3;
    }

    public Player getPlayer2(){
        return player2;
    }

    //n for next
    public boolean canMove(int x, int y, int n_x, int n_y){

        if(turn){
            //System.out.println(player1.canMove(board, x, y, n_x, n_y));
            return player1.canMove(board, x, y, n_x, n_y);
        }
        else{
            return player2.canMove(board, x, y, n_x, n_y);
        }

    }

    private void move(int x, int y, int n_x, int n_y){

        if(canMove(x, y, n_x, n_y)){

            if(turn)
                gameOver = player1.move(board, x, y, n_x, n_y, turn);
            else
                gameOver = player2.move(board, x, y, n_x, n_y, turn);

            turn = !turn;
            //add history
        }
        else{
            System.out.println("Illegal Move!");
        }
        
    }


    private void reset(int modes){

        //Empties/Initialises all the positions
        for(int x = 0; x < height; x++){
            for(int y = 0; y < width; y++){
                board[x][y] = null;
            }
        }

        player1 = null;
        player2 = null;
        player3 = null;

        if(modes == 1){
            player1 = new Player(true, height, width);
            player2 = new Player(false, height, width);
        }
        else if(modes == 2){
            //System.out.println("HIIIIIIIIIIIIIIIII");
            player1 = new Player(true, height, width);
            player3 = new Minimax(false, height, width);
        }
        
        turn = true;

        //For the beginning of the game
        board[0][0] = new Piece(Animal.GIRAFFE, false);
        board[0][1] = new Piece(Animal.LION, false);
        board[0][2] = new Piece(Animal.ELEPHANT, false);
        board[1][1] = new Piece(Animal.CHICK, false);

        board[3][0] = new Piece(Animal.ELEPHANT, true);
        board[3][1] = new Piece(Animal.LION, true);
        board[3][2] = new Piece(Animal.GIRAFFE, true);
        board[2][1] = new Piece(Animal.CHICK, true);

    }

    private void drop(char animalInitial, int x, int y){

        if(turn){
            if(player1.drop(board, animalInitial, x, y, turn))
                turn = !turn;
        }
        else{
            if(player2.drop(board, animalInitial, x, y, turn))
                turn = !turn;
        }

    }

    public void showCurrentBoard(){

        System.out.println("    a     b     c  ");
        System.out.println("  ------------------");
        for(int x = 0; x < height; x++){
            System.out.print(x+1);
            for(int y = 0; y < width; y++){
                System.out.print("|  ");
                if(board[x][y] == null){
                    System.out.print(" ");
                }
                else if(!board[x][y].getSide()){

                    if(board[x][y].getAnimal() == Animal.ELEPHANT){
                        System.out.print("E");
                    }
                    else if(board[x][y].getAnimal() == Animal.GIRAFFE){
                        System.out.print("G");
                    }
                    else if(board[x][y].getAnimal() == Animal.LION){
                        System.out.print("L");
                    }
                    else if(board[x][y].getAnimal() == Animal.CHICK){
                        System.out.print("C");
                    }
                    else if(board[x][y].getAnimal() == Animal.ROOSTER){
                        System.out.print("R");
                    }

                }
                else{

                    if(board[x][y].getAnimal() == Animal.ELEPHANT){
                        System.out.print("e");
                    }
                    else if(board[x][y].getAnimal() == Animal.GIRAFFE){
                        System.out.print("g");
                    }
                    else if(board[x][y].getAnimal() == Animal.LION){
                        System.out.print("l");
                    }
                    else if(board[x][y].getAnimal() == Animal.CHICK){
                        System.out.print("c");
                    }
                    else if(board[x][y].getAnimal() == Animal.ROOSTER){
                        System.out.print("r");
                    }

                }
                System.out.print("  ");
            }
            System.out.print("|");
            System.out.println();
            System.out.println("  ------------------");
        }

        System.out.println();
        if(turn)
            System.out.println("Player 1 to move");
        else
            System.out.println("Player 2 to move");
        //Holding
        System.out.println();
        System.out.println("Piece(s) on hand for player 1: " + player1.showPieces());
        if(player2 != null)
            System.out.println("Piece(s) on hand for player 2: " + player2.showPieces());
        if(player3 != null)
            System.out.println("Piece(s) on hand for player 2: " + player3.showPieces());
        System.out.println();

        System.out.println("Press M for menu");
        System.out.println();

    }

    private void play(){
        Scanner scanner = new Scanner(System.in);
        //while someone wins or if user exits

        String input;

        while(!gameOver){
            showCurrentBoard();
            System.out.println("Enter the piece's initial position and final position: ");
            input = scanner.nextLine();
            parseInput(input, scanner);
        }
        showCurrentBoard();
        menu(scanner);
        
    }

    private void playVE(){
        Scanner scanner = new Scanner(System.in);
        //while someone wins or if user exits

        String input;

        while(!gameOver){
            showCurrentBoard();
            if(turn){
                System.out.println("Enter the piece's initial position and final position: ");
                input = scanner.nextLine();
                parseInput(input, scanner);
            }
            else{
                player3.useEngine(this, 1);
                turn = !turn;
            }
        }
        showCurrentBoard();
        menu(scanner);
        
    }

    //Instead of this make a method called game over which will run once game over and rerun menu
    public void parseInput(String input, Scanner scanner){

        if(input.equals("M")){
            menu(scanner);
        }
        else{
            if(input.length() == 5){

                int x, y, n_x, n_y;

                y = input.charAt(0) - 'a';
                x = Character.getNumericValue(input.charAt(1)) -1;
                n_y = input.charAt(3) - 'a';
                n_x = Character.getNumericValue(input.charAt(4)) -1;

                move(x, y, n_x, n_y);

            //Drop
            }
            else if(input.length() == 3){

                int x, y;
                y = input.charAt(1) - 'a';
                x = Character.getNumericValue(input.charAt(2)) -1;

                drop(input.charAt(0), x, y);

            }
            else{
                System.out.println("Invalid input!");
                System.out.println("<char><number> <char><number>");
            }
        }

    }

    private void gameModes(Scanner scanner){

        System.out.println();
        System.out.println("1. PvP ");
        System.out.println("2. PvE ");
        System.out.println();
        System.out.println(" Enter a number: ");
        System.out.println();

        int input = scanner.nextInt();

        if(input == 1 || input == 2){
            reset(input);
            if(input == 1)
                play();
            if(input == 2)
                playVE();
        }

    }

    private void menu(Scanner scanner){

        System.out.println();
        System.out.println("What do you want to do? \n");
        System.out.println("1. Play a new game ");
        System.out.println("2. Continue ");
        System.out.println("3. Exit ");
        System.out.println();
        System.out.println(" Enter a number: ");
        System.out.println();

        int input = scanner.nextInt();
        //What if something thats not int

        switch(input){
            case 1: 
                //option of modes
                if(gameOver){
                    gameOver = false;
                }
                gameModes(scanner);
                break;
            case 2:
                //Do nothing
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println(" Invalid input! ");
        }

    }

    public static void main(String[] args){

        Board game = new Board();
        game.play();

    }

}
