import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum CellState{
        Empty,
        X,
        O
    }

class Cell{
    private int row;
    private int col;
    private CellState state;

    public Cell(int row,int col){
        this.row = row;
        this.col = col;
        this.state = CellState.Empty;
    }

    public void setState(CellState state){
        this.state = state;
    }
    public CellState getState(){
        return state;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}

class Board{
    private Cell board[][];
    private int size;
    
    public Board(int size){
        this.size = size;
        board = new Cell[size][size];
        initializeBoard();
    }

    private void initializeBoard(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                board[i][j] = new Cell(i, j);
            }
        }
    }

    public boolean isFull(){
        for(Cell[] row: board){
            for(Cell cell : row){
                if(cell.getState()==CellState.Empty)return false;
            }
        }
        return true;
    }

    public boolean makeMove(int row,int col,CellState state){
        if(row<0 || row>=size || col<0 || col>=size){
            System.out.println("Invalid Move");
            return false;
        }
        if(board[row][col].getState() != CellState.Empty){
            System.out.println("Cell already occupied");
            return false;
        }
        board[row][col].setState(state);
        return true;
    }

    public void printBoard(){
        for(Cell[] row : board){
            for(Cell cell : row){
                switch(cell.getState()){
                    case Empty : System.out.print("_");break;
                    case X : System.out.print("X");break;
                    case O : System.out.print("O");break;
                }
            }
            System.out.println();
        }
    }

    public boolean checkWin(CellState state){
        for(int i=0;i<size;i++){
        if(checkRow(state,i))return true;
        if(checkCol(state,i))return true;
        }
        return checkDiagonal(state) || checkAntiDiagonal(state);
    }

    private boolean checkRow(CellState state,int index){
        for(int j=0;j<size;j++){
            if(board[index][j].getState() != state)return false;
        }
        return true;
    }

    private boolean checkCol(CellState state,int index){
        for(int j=0;j<size;j++){
            if(board[j][index].getState() != state)return false;
        }
        return true;
    }

    private boolean checkDiagonal(CellState state){
        for(int i=0;i<size;i++){
            if(board[i][i].getState() != state)return false;
        }
        return true;
    }

    private boolean checkAntiDiagonal(CellState state){
        for(int i=0;i<size;i++){
            if(board[i][size-1-i].getState() != state)return false;
        }
        return true;
    }
}

class Player{
    private String name;
    private CellState symbol;
    
    public Player(String name, CellState symbol){
        this.name = name;
        this.symbol = symbol;
    }

    public String getName(){return name;}
    public CellState getSymbol(){return symbol;}
}

class Game{
    private List<Player>Players;
    private Board Board;
    private int PlayerIndex;
    public Game(int size,List<Player>Players){
        Board = new Board(size);
        this.Players = Players;
        this.PlayerIndex = 0;
    }

    public void startGame(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            Player currentPlayer = Players.get(PlayerIndex);
            System.out.println(currentPlayer.getName()+"'s turn ("+currentPlayer.getSymbol()+")");
            System.out.print("Enter row and column (0 based) : ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            if(Board.makeMove(row, col, currentPlayer.getSymbol())){
                Board.printBoard();
                if(Board.checkWin(currentPlayer.getSymbol())){
                    System.out.println(currentPlayer.getName()+" Win's");
                    break;
                }
                if(Board.isFull()){
                    System.out.println("Game Tied");
                    break;
                }
            }
            PlayerIndex++;
            if(PlayerIndex>=Players.size())PlayerIndex = 0;
        }
        scanner.close();
    }
}
public class TicTacToeGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎮 Welcome to Tic Tac Toe Game! 🎮\n");

        System.out.print("Enter board size (e.g. 3 for 3x3): ");
        int boardSize = scanner.nextInt();
        scanner.nextLine();

        List<Player> players = new ArrayList<>();

        System.out.print("Enter name for Player 1 (X): ");
        String player1Name = scanner.nextLine();
        players.add(new Player(player1Name, CellState.X));

        System.out.print("Enter name for Player 2 (O): ");
        String player2Name = scanner.nextLine();
        players.add(new Player(player2Name, CellState.O));

        System.out.println("\nPlayers are ready:");
        for (Player player : players) {
            System.out.println("- " + player.getName() + " (" + player.getSymbol() + ")");
        }

        System.out.println("\nLet's start the game!\n");

        Game game = new Game(boardSize, players);
        game.startGame();

        System.out.println("\n🎉 Game Over. Thanks for playing! 🎉");

        scanner.close();
    }
}
