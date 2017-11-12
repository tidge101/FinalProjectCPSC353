

public class Player {
  private int gamesWon;
  private String name;
  private Socket connectionSock = null;

  public Player() {
    id = 0;
  }

  public Player(Socket connectionSock, String name) {
    this.name = name;
    this.connectionSock = connectionSock;
    gamesWon = 0;
  }

  public int getName() {
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
}
