
import java.util.Scanner;


enum Color{
    WHITE,
    BLACK;
}


abstract class Piece{
    private  Color color;
    public Piece(Color color){
        this.color = color;
    }
    public Color getColor(){return this.color;}
    public abstract boolean isValidMove(Cell start,Cell end);
}


abstract class DirectionalPiece extends Piece{
    protected int[][] direction;
    public DirectionalPiece(Color color){
        super(color);
    }
    public abstract int[] getDirection(Cell start,Cell end);
}


class Queen extends DirectionalPiece{
    public Queen(Color color){
        super(color);
        this.direction = new int[][] {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {-1,-1}, {1,1}, {-1,1}, {1,-1}
        };
    }
    @Override
    public int[] getDirection(Cell start, Cell end) {
        int rowDiff = end.getRow() - start.getRow();
        int colDiff = end.getCol() - start.getCol();

        if (rowDiff == 0) {
            return (colDiff > 0) ? direction[2] : direction[3];
        } else if (colDiff == 0) {
            return (rowDiff > 0) ? direction[0] : direction[1];
        } else if (Math.abs(rowDiff) == Math.abs(colDiff)) {
            if (rowDiff > 0 && colDiff > 0) return direction[5];  
            if (rowDiff < 0 && colDiff < 0) return direction[4];  
            if (rowDiff > 0 && colDiff < 0) return direction[7];  
            if (rowDiff < 0 && colDiff > 0) return direction[6];  
        }
       return direction[0];
    }
    @Override
    public boolean  isValidMove(Cell start,Cell end){
        if(end.getPiece()!=null && end.getPiece().getColor() == getColor())return false;
        if(start.getCol() == end.getCol() && start.getRow() != end.getRow())return true;
        if(start.getCol() != end.getCol() && start.getRow() == end.getRow())return true;
        if(Math.abs(start.getCol() - end.getCol()) == Math.abs(start.getRow() - end.getRow()))return true;
        return false;
    }
}

class Rook extends DirectionalPiece{
    public Rook(Color color){
        super(color);
        this.direction = new int[][] {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };
    }
    @Override
    public int[] getDirection(Cell start,Cell end){
        if(start.getRow() != end.getRow()){
            if(start.getRow()<end.getRow()){
                return direction[0];
            }
            return direction[1];
        }
        else{
            if(start.getCol()<end.getCol()){
                return direction[2];
            }
            return direction[3];
        }
    }
    @Override
    public boolean  isValidMove(Cell start,Cell end){
        if(end.getPiece()!=null && end.getPiece().getColor() == getColor())return false;
        if(start.getCol() != end.getCol() && start.getRow() == end.getRow())return true;
        if(start.getCol() == end.getCol() && start.getRow() != end.getRow())return true;
        return false;
    }
}

class Bishop extends DirectionalPiece{
    public Bishop(Color color){
        super(color);
        this.direction = new int[][] {
            {-1,-1}, {1,1}, {-1,1}, {1,-1}
        };
    }
    @Override
    public int[] getDirection(Cell start, Cell end) {
        int rowDiff = end.getRow() - start.getRow();
        int colDiff = end.getCol() - start.getCol();
        if (rowDiff > 0 && colDiff > 0) return direction[1]; 
        if (rowDiff < 0 && colDiff < 0) return direction[0]; 
        if (rowDiff > 0 && colDiff < 0) return direction[3]; 
        return direction[2];
    }
    @Override
    public boolean  isValidMove(Cell start,Cell end){
        if(end.getPiece()!=null && end.getPiece().getColor() == getColor())return false;
        if(Math.abs(start.getCol() - end.getCol()) == Math.abs(start.getRow() - end.getRow()))return true;
        return false;
    }
}

class Knight extends Piece{
    public Knight(Color color){
        super(color);
    }
    @Override
    public boolean  isValidMove(Cell start,Cell end){
        if(end.getPiece()!=null && end.getPiece().getColor() == getColor())return false;
        boolean flag1 = false,flag2 = false;
        if(Math.abs(start.getCol() - end.getCol()) == 1 && Math.abs(start.getRow() - end.getRow()) == 2)flag1 = true;
        if(Math.abs(start.getCol() - end.getCol()) == 2 && Math.abs(start.getRow() - end.getRow()) == 1)flag2 = true;
        if(!flag1 && !flag2)return false;
        return true;
    }
}

class King extends Piece{
    public King(Color color){
        super(color);
    }
    @Override
    public boolean  isValidMove(Cell start,Cell end){
        if(end.getPiece()!=null && end.getPiece().getColor() == getColor())return false;
        if(Math.abs(start.getRow()-end.getRow())>1 || Math.abs(start.getCol()-end.getCol())>1)return false; 
        return true;
    }
}

class Pawn extends Piece{
    public Pawn(Color color){
        super(color);
    }
    @Override
    public boolean  isValidMove(Cell start,Cell end){
        if(Math.abs(start.getCol()-end.getCol())>1)return false;
        if(getColor()==Color.WHITE){
            if(start.getRow() - end.getRow()!=1)return false;
        }
        else{
            if(start.getRow() - end.getRow()!=-1)return false;
        }
        if(Math.abs(start.getCol()-end.getCol())==1){
            if(end.getPiece()==null || end.getPiece().getColor()==start.getColor())return false;
            return true;
        }
        if(end.getPiece()!=null)return false;
        return true;
    }
}

class Cell{
    private int row;
    private int col;
    private Piece piece;
    private Color color;
    public Cell(int row,int col,Piece piece,Color color){
        this.row = row;
        this.col = col;
        this.piece = piece;
        this.color = color;
    }
    public int getRow(){return this.row;}
    public int getCol(){return this.col;}
    public Color getColor(){return this.color;}
    public Piece getPiece(){return this.piece;}
    public void setPiece(Piece piece){
        this.piece = piece;
    }
}


class Board{
    private Cell[][] board;
    public Board(){
        board = new Cell[8][8];
        colorBoard();
        setupPawns();
        setupMainPieces(0,Color.BLACK);
        setupMainPieces(7,Color.WHITE);
    }
    private void colorBoard(){
         for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(i%2 != j%2){
                    board[i][j] = new Cell(i, j, null, Color.WHITE);
                }
                else{
                    board[i][j] = new Cell(i, j, null, Color.BLACK);
                }
            }
        }
    }
    private void setupPawns(){
        for(int i=0;i<8;i++){
            board[1][i].setPiece(new Pawn(Color.BLACK));
            board[6][i].setPiece(new Pawn(Color.WHITE));
        }
    }
    private void setupMainPieces(int row,Color color){
        board[row][0].setPiece(new Rook(color));
        board[row][1].setPiece(new Knight(color));
        board[row][2].setPiece(new Bishop(color));
        board[row][3].setPiece(new Queen(color));
        board[row][4].setPiece(new King(color));
        board[row][5].setPiece(new Bishop(color));
        board[row][6].setPiece(new Knight(color));
        board[row][7].setPiece(new Rook(color));
    }
    public void reset(){
        colorBoard();
        setupPawns();
        setupMainPieces(0,Color.BLACK);
        setupMainPieces(7,Color.WHITE);
    }
    public boolean validPlacement(int row,int col){
        if(row>=0 && row<8 && col>=0 && col<8)return true;
        return false;
    };  
    public Cell getCell(int row,int col){
        return board[row][col];
    } 
    public boolean inBetween(Cell start,Cell end){
        DirectionalPiece piece = (DirectionalPiece) start.getPiece();
        int[] direction = piece.getDirection(start, end);
        int row = start.getRow(),col = start.getCol();
        while(true){
            row += direction[0];
            col += direction[1];
            if(row == end.getRow() && col == end.getCol())break;
            Cell cell = board[row][col];
            if(cell.getPiece()!=null)return true;
        }
        return false;
    }
    public boolean validMove(Cell start,Cell end){
        if(!start.getPiece().isValidMove(start, end))return false;
        if(start.getPiece() instanceof DirectionalPiece){
            if(inBetween(start,end))return false;
        }
        return true;
    }
    public boolean makeMove(Cell start,Cell end){
        if(end.getPiece() instanceof King){
            return true;
        }
        end.setPiece(start.getPiece());
        start.setPiece(null);
        return false;
    }
    public void printBoard() {
    System.out.println("  0 1 2 3 4 5 6 7");
    for (int i = 0; i < 8; i++) {
        System.out.print(i + " ");
        for (int j = 0; j < 8; j++) {
            Piece piece = board[i][j].getPiece();
            if (piece == null) {
                System.out.print("- ");
            } else {
                char symbol = ' ';
                if (piece instanceof King) symbol = 'K';
                else if (piece instanceof Queen) symbol = 'Q';
                else if (piece instanceof Rook) symbol = 'R';
                else if (piece instanceof Bishop) symbol = 'B';
                else if (piece instanceof Knight) symbol = 'N';
                else if (piece instanceof Pawn) symbol = 'P';
                if (piece.getColor() == Color.BLACK)
                    symbol = Character.toLowerCase(symbol);
                System.out.print(symbol + " ");
            }
        }
        System.out.println();
    }
}
}
class Player {
    private String name;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }
}
class Game{
    private Board board;
    private Player white;
    private Player black;
    public Game(Board board, Player A, Player B) {
        if (A.getColor() == B.getColor()) {
            throw new IllegalArgumentException("Both players cannot have the same color.");
        }

        this.board = board;

        if (A.getColor() == Color.WHITE) {
            white = A;
            black = B;
        } else {
            black = A;
            white = B;
        }
    }
    public void start(){
        Color turn = Color.WHITE;
        Scanner in = new Scanner(System.in);
        while(true){
            Player inTurn = (turn==white.getColor()?white : black);
            System.out.println("\n" + inTurn.getName() + "'s turn (" + inTurn.getColor() + ")");
            board.printBoard();
            System.out.print("Enter source cell (row col): ");
            int row = in.nextInt();int col = in.nextInt();
            if(!board.validPlacement(row, col)){
                System.out.println("Selected cell is not on the board, please try again.");
                continue;
            }
            Cell start = board.getCell(row, col);
            if(start.getPiece()==null || start.getPiece().getColor()!=turn){
                System.out.println("You have to select your piece, please try again.");
                continue;
            }
            System.out.print("Enter destination cell (row col): ");
            row = in.nextInt();col = in.nextInt();
             if(!board.validPlacement(row, col)){
                System.out.println("Selected cell is not on the board, please try again.");
                continue;
            }
            Cell end = board.getCell(row, col);
            if(!board.validMove(start,end)){
                System.out.println("Invalid move!!");
                continue;
            }
            if(board.makeMove(start,end)){
                System.out.println(inTurn.getName()+" Wins");
                System.out.println("Thanks for playing");
                board.reset();
                break;
            }
            else{
                System.out.println("Move completed!\n");
                if(turn == Color.BLACK){
                    turn = Color.WHITE;
                }
                else{
                    turn = Color.BLACK;
                }
            }
        }
        in.close();
    }
}
public class Chess {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner in = new Scanner(System.in);

        System.out.print("Enter name for Player 1 (White): ");
        String p1Name = in.nextLine();
        Player player1 = new Player(p1Name, Color.WHITE);

        System.out.print("Enter name for Player 2 (Black): ");
        String p2Name = in.nextLine();
        Player player2 = new Player(p2Name, Color.BLACK);

        Game game = new Game(board, player1, player2);
        game.start();
    }
}
