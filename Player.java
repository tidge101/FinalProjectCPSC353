import java.net.*;

public class Player {
  private int gamesWon;
  private String name;
  private Socket connectionSock = null;

  public Player() {
    this.name = "";
  }

  public Player(Socket connectionSock, String name) {
    this.name = name;
    this.connectionSock = connectionSock;
    gamesWon = 0;
  }

  public String getName() {
    return name;
  }

  public Socket getConnectionSock() {
    return connectionSock;
  }

  public int getGamesWon() {
    return gamesWon;
  }

  public void addGameWon() {
    gamesWon++;
  }

  public String toString() {
    String player = "Name: " + name + "\n";
    player += "Connection Socket: " + connectionSock.toString() + "\n";
    player += "Games Won: " + gamesWon;
    return player;
  }
}
