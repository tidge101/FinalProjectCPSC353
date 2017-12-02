/**
 * Collaborators:
 * Maika Fujii
 *  ID: 1935412
 *  fujii108@mail.chapman.edu /**
 * Thomas Madden
 *  ID: 2261821
 *  madde120@mail.chapman.edu /**
 * Dillon Tidgewell
 *  ID: 002285452
 *  tidge101@mail.chapman.edu /**
 *
 * Course: CPSC 353-01
 * Assignment: Final Project - Tic-Tac-Toe
 *
 *
 * GameServer.java
 */
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
        socketList.add(connectionSock);
        numberConnected++;
        // Output how many players have connected
        System.out.println("The number of connected players is " + numberConnected);
        // Output info of player who just connected
        for (int i = 0; i < playerList.size(); ++i) {
          if (playerList.get(i).getName() != "") {
            System.out.println("Player " + (i+1) + ": " + playerList.get(i).getName());
          }
        }

      }//  while (numberConnected < numberOfPlayers)

      System.out.println("All players have connected\n");
      //Wait until all clients have given us their names.
      /*
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
      }//while (ready == 0) */

      System.out.println("Ready!");


      // Ready for Tic Tac Toe Matchups
      ArrayList<Player> notMatchedUp = new ArrayList<Player>();
      for (int i = 0; i < playerList.size(); ++i) {
        notMatchedUp.add(playerList.get(i));
      }

      // Match waiting players
      while (notMatchedUp.size() != 0) {
        // Cannot match if there is only one player waiting
        if (notMatchedUp.size() == 1) {}

        // Randomly select 2 players from notMatchedUp
        int i = (int)(Math.random() * notMatchedUp.size());
        int j = (int)(Math.random() * notMatchedUp.size());
        // Ensure that the same player wasn't selected for both i and j
        if (i != j) {
          // Randomly select port to connect on
          int portToUse = (int)(Math.random() * 100) + 10000;
          // Create output streams for each client
          DataOutputStream iOut = new DataOutputStream(notMatchedUp.get(i).getConnectionSock().getOutputStream());
          DataOutputStream jOut = new DataOutputStream(notMatchedUp.get(j).getConnectionSock().getOutputStream());
          // Send each client's socket and port info to each other
          iOut.writeBytes("Host: " + notMatchedUp.get(j).getConnectionSock() + "\n");
          jOut.writeBytes("Client: " + notMatchedUp.get(i).getConnectionSock() + "\n");
          iOut.writeBytes("Port: " + portToUse + "\n");
          jOut.writeBytes("Port: " + portToUse + "\n");
          // Remove i and j from notMatchedUp now that they are matched
          notMatchedUp.remove(i);
          if (j > i) {
            notMatchedUp.remove(j - 1);
          } else {
            notMatchedUp.remove(j);
          }
        }
      }
      // Send each player their opponent's info

    } // End try
    catch (IOException e)
    {
      System.out.println(e.getMessage());
    } // End catch
  } // End getConnection

  public static void main(String[] args) {
    // Initialize GameServer
    GameServer server = new GameServer();

    // Get number of players from user input
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
    // Wait for n players to connect (where n = numPlayers)
    server.getConnections(numPlayers);
  }

}
