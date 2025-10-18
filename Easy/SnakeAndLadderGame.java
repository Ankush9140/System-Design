import java.util.*;
class Dice{
    public int roll(){
        return (int)(Math.random() * 6) + 1;
    }
}
class Player{
    private String name;
    private int position;
    public Player(String name){
        this.name = name;
        this.position = 1;
    }
    public String getName(){
        return this.name;
    }
    public int getPosition(){
        return this.position;
    }
    public void setPosition(int currentPosition){
        this.position = currentPosition;
    }
}
class Jump{
    private int start;
    private int end;
    public Jump(int start,int end){
        this.start = start;
        this.end = end;
    }
    public int getStart(){
        return start;
    }
    public int getEnd(){
        return end;
    }
}
class Board{
    private List<Jump>ladder;
    private List<Jump>snake;
    private int size;
    public Board(int size){
        this.ladder = new ArrayList<>();
        this.snake = new ArrayList<>();
        this.size = size;
    }
    public void addLadder(int start,int end){
        if(start<=0 || end>size){
            System.out.println("Cannot place ladder outside the board");
            return;
        }
        if(end<=start){
            System.out.println("Ladder ending will be always greater than start");
            return;
        }
        ladder.add(new Jump(start, end));
    }
    public void addSnake(int start,int end){
        if(start<=0 || end>size){
            System.out.println("Cannot place snake outside the board");
            return;
        }
        if(end>=start){
            System.out.println("Snake ending will be always smaller than start");
            return;
        }
        snake.add(new Jump(start, end));
    }
    public int getSize(){
        return this.size;
    }
    public int getNextPosition(int position){
        int nextPosition = position;
        for(Jump jump : ladder){
            if(jump.getStart() == position){
                nextPosition = jump.getEnd();
                 System.out.println("Ladder from " + jump.getStart() + " to " + jump.getEnd());
                break;
            }
        }
        for(Jump jump : snake){
            if(jump.getStart() == position){
                System.out.println("Snake from " + jump.getStart() + " to " + jump.getEnd());
                nextPosition = jump.getEnd();
                break;
            }
        }
        return nextPosition;
    }
}
class Game{
    private Dice dice;
    private Board board;
    private Queue<Player>players;
    public Game(Dice dice,Board board,List<Player>playersList){
        this.dice = dice;
        this.board = board;
        this.players = new LinkedList<>(playersList);
    }
    public void play(){
        if(players.isEmpty()){
            System.out.println("Please insert players");
            return;
        }
        while(true){
           try {
                Player currPlayer = players.poll();
                int diceRoll = dice.roll();
                System.out.println(currPlayer.getName() + " rolled a " + diceRoll);

                int nextPosition = currPlayer.getPosition() + diceRoll;
                if(nextPosition > board.getSize()){
                    System.out.println("Roll exceeds the board size.");
                    players.add(currPlayer);
                    Thread.sleep(500);
                    continue;
                }

                nextPosition = board.getNextPosition(nextPosition);
                currPlayer.setPosition(nextPosition);
                System.out.println(currPlayer.getName() + " moved to " + nextPosition);

                if(currPlayer.getPosition() == board.getSize()){
                    System.out.println(currPlayer.getName() + " Wins!!");
                    break;
                }

                players.add(currPlayer);
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class SnakeAndLadderGame {
    public static void main(String[] args){
        System.out.println("🎲 Welcome to Snake and Ladder Game! 🎲\n");

        Board board = new Board(100);

        board.addSnake(99, 10);
        board.addSnake(90, 50);
        board.addSnake(70, 30);

        board.addLadder(5, 25);
        board.addLadder(40, 89);
        board.addLadder(60, 95);

        System.out.println("Snakes and ladders placed on the board.\n");

        Dice dice = new Dice();

        List<Player> players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        players.add(new Player("Charlie"));

        System.out.println("Players in the game:");
        for (Player p : players) {
            System.out.println("- " + p.getName());
        }

        System.out.println("\nLet the game begin!\n");

        Game game = new Game(dice, board, players);
        game.play();

        System.out.println("\n🎉 Game Over 🎉");
    }
}
