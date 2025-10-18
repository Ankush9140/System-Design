
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

class Player{
    private final String userId;
    private final String userName;
    private int score;
    public Player(String userId,String userName){
        this.userId = userId;
        this.userName = userName;
        this.score = 0;
    }
    public String getUserName(){ return userName;}
    public String getUserId(){ return userId; }
    public int getScore(){ return score; }
    public void setScore(int score){ this.score = score; }
}
class LeaderBoard {
    private HashMap<String, Player> playerMap;
    private PriorityQueue<Player> players;

    public LeaderBoard() {
        playerMap = new HashMap<>();
        players = new PriorityQueue<>((a, b) -> (b.getScore() - a.getScore()));
    }

    public List<Player> getTopPlayers(int n) {
        List<Player> playerList = new ArrayList<>();
        int count = 0;
        List<Player> temp = new ArrayList<>();

        while (count < n && !players.isEmpty()) {
            Player player = players.poll();
            if (player != null) {
                playerList.add(player);
                temp.add(player);
                count++;
            }
        }
        players.addAll(temp);
        return playerList;
    }

    public void addScore(String playerId, int score) {
        if (playerMap.containsKey(playerId)) {
            Player player = playerMap.get(playerId);
            players.remove(player);
            player.setScore(player.getScore() + score);
            players.add(player);
        }
    }

    public void subtractScore(String playerId, int score) {
        if (playerMap.containsKey(playerId)) {
            Player player = playerMap.get(playerId);
            players.remove(player);
            player.setScore(player.getScore() - score);
            players.add(player);
        }
    }

    public void addPlayer(Player player) {
        if (playerMap.containsKey(player.getUserId())) {
            System.out.println("Player is already on leaderboard");
        } else {
            playerMap.put(player.getUserId(), player);
            players.add(player);  // ✅ Add to priority queue
            System.out.println("Player added successfully");
        }
    }

    public void removePlayer(Player player) {
        if (playerMap.containsKey(player.getUserId())) {
            players.remove(player);
            playerMap.remove(player.getUserId());
            System.out.println("Player removed from leaderboard");
        } else {
            System.out.println("Player not on leaderboard");
        }
    }
}


public class LeaderboardSystem {
    public static void main(String[] args) {
        LeaderBoard leaderboard = new LeaderBoard();

        Player ankush = new Player("U001", "Ankush");
        Player ram = new Player("U002", "Ram");
        Player shyam = new Player("U003", "Shyam");

        leaderboard.addPlayer(ankush);
        leaderboard.addPlayer(ram);
        leaderboard.addPlayer(shyam);

        leaderboard.addScore("U001", 150);
        leaderboard.addScore("U002", 200);
        leaderboard.addScore("U003", 180);

        System.out.println("\n🏆 Top 2 Players:");
        for (Player p : leaderboard.getTopPlayers(2)) {
            System.out.println(p.getUserName() + " : " + p.getScore());
        }

        leaderboard.subtractScore("U002", 50);

        System.out.println("\n🏆 Leaderboard after deducting 50 from Ram:");
        for (Player p : leaderboard.getTopPlayers(3)) {
            System.out.println(p.getUserName() + " : " + p.getScore());
        }

        leaderboard.removePlayer(shyam);

        System.out.println("\n🏆 Leaderboard after removing Shyam:");
        for (Player p : leaderboard.getTopPlayers(3)) {
            System.out.println(p.getUserName() + " : " + p.getScore());
        }
    }
}

