
import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
  private ArrayList<Socket> socketList;
	private ArrayList<Player> playerList;
	private ServerSocket serverSock = null;
	private int numberConnected = 0;
  private int state = 0;

  public GameServer() {
    socketList = new ArrayList<Socket>();
    playerList = new ArrayList<Player>();
  }

  private void getConnections(int numberOfPlayers)
  {
    // Wait for a connection from the client
    try
    {
      serverSock = new ServerSocket(7654);
      // This is an infinite loop, the user will have to shut it down
      // using control-c
      while (numberConnected < numberOfPlayers)     // This should change to 'when someone hits the button'
      {
        Socket connectionSock = serverSock.accept();
        // Add this socket to the list
        Player newPlayer= new Player(connectionSock, "");   // we should find a name for player in gameclient
        //socketList.add(connectionSock);
        playerList.add(newPlayer);
        numberConnected++;
        System.out.println("The number of connected players is " + numberConnected);
        for (int i = 0; i < playerList.size(); ++i) {
          if (playerList.get(i).getName() != "") {
            System.out.println("Player " + (i+1) + ": " + playerList.get(i).getName());
          }
        }

      }//  while (numberConnected < numberOfPlayers)

      System.out.println("All players have connected\n");
      //Wait until all clients have given us their names.
      int ready = 0;
      while (ready == 0)
      {
        ready = 1;								//Wait for players to enter their names
        for (Player p:playerList)
        {
          if (p.getName().equals(""))
          {
            ready = 0;
          }
        }//for (Player p:playerList)
      }//while (ready == 0)


      // Ready for Tic Tac Toe Matchups

      // Send each player their opponent's info

    }//try
    catch (IOException e)
    {
      System.out.println(e.getMessage());
    }//catch
  }//getConnection

  public static void main(String[] args) {
    GameServer server = new GameServer();
    System.out.println("Please enter how many players are in your tournament!");
    int numPlayers = 0;
    try {
      while (numPlayers <= 0) {
        String numPlayersStr = new BufferedReader(new InputStreamReader(System.in)).readLine();
        numPlayers = Integer.parseInt(numPlayersStr);
      }
    } catch (IOException ioe) {
      System.out.println("IOE Exception while reading numPlayers: " + ioe.getMessage());
    }
    server.getConnections(numPlayers);
    server.state = 1;
  }

}
