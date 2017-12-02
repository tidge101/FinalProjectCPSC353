/**
 * Collaborators:
 * Maika Fujii
 * 	ID: 1935412
 * 	fujii108@mail.chapman.edu /**
 * Thomas Madden
 * 	ID: 2261821
 * 	madde120@mail.chapman.edu /**
 * Dillon Tidgewell
 * 	ID: 002285452
 * 	tidge101@mail.chapman.edu /**
 *
 * Course: CPSC 353-01
 * Assignment: Final Project - Tic-Tac-Toe
 *
 *
 * GameClient.java
 *
 * This program implements a simple multithreaded chat client.  It connects to the
 * server (assumed to be localhost on port 7654) and starts two threads:
 * one for listening for data sent from the server, and another that waits
 * for the user to type something in that will be sent to the server.
 * Anything sent to the server is broadcast to all clients.
 *
 * The GameClient uses a ClientListener whose code is in a separate file.
 * The ClientListener runs in a separate thread, recieves messages form the server,
 * and displays them on the screen.
 *
 * Data received is sent to the output screen, so it is possible that as
 * a user is typing in information a message from the server will be
 * inserted.
 *
 * Created private variables hostname and port to protect port details
 *
 *
 *
 */
 import java.io.*;
 import java.net.*;
 import java.util.*;

public class GameClient
{

	private static final String hostname = "localhost";
	private static final int port = 7654;
	private static Socket connectionSock;
	private static DataOutputStream serverOutput;
	private static BufferedReader inFromServer;
	private static Thread theThread;
	private static ServerSocket hostSocket;
  private static Socket opponentSock;

	//init private variables
	public static void main(String[] args)
	{
		try
		{

			connectionSock = new Socket(hostname, port);

			serverOutput = new DataOutputStream(connectionSock.getOutputStream());
			inFromServer =  new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));


			//Prompt for the user's nme and send it to the server
			Scanner keyboard = new Scanner(System.in);
			String name;

			// Read input from server of who they're playing
			String playerInfo = inFromServer.readLine();
      System.out.println(playerInfo);
      // If Client is the Host,
			if (playerInfo.indexOf("Host") >= 0) {
				String opponentIp = playerInfo.substring(playerInfo.indexOf("/") + 1, playerInfo.indexOf(","));
				System.out.println(opponentIp);
				int hostPort = Integer.parseInt(inFromServer.readLine().substring(6));
        System.out.println(hostPort);
				hostSocket = new ServerSocket(hostPort);
				opponentSock = hostSocket.accept();
			} else {
        try {
          Thread.sleep(1000);
        } catch (Exception e) {
          System.out.println("problem!");
        }
				String opponentIp = playerInfo.substring(playerInfo.indexOf("/") + 1, playerInfo.indexOf(","));
				System.out.println(opponentIp);
				int hostPort = Integer.parseInt(inFromServer.readLine().substring(6));
        System.out.println(hostPort);
				opponentSock = new Socket("localhost", hostPort);
			}

      while(true){
        System.out.println("Please enter a valid name!");
        name = keyboard.nextLine();
        if(name.toString() != null){
    //			serverOutput.writeBytes(name + "\n");
          break;
        }

      }

			// Start up TicTacToe client here
			TicTacToe currentGame;

      try {
        DataOutputStream out = new DataOutputStream(opponentSock.getOutputStream());
        BufferedReader in =  new BufferedReader(new InputStreamReader(opponentSock.getInputStream()));

  			if (hostSocket != null) {
  				currentGame = new TicTacToe(name, true, opponentSock);

          out.writeBytes("Name: " + name);
          System.out.println("abouttowait host");
          String opponentName = in.readLine().substring(6);
  			} else {
  				currentGame = new TicTacToe(name, false, opponentSock);
          System.out.println("abouttowait client");
          try {
            Thread.sleep(1000);
          } catch (Exception e) {
            System.out.println("problem!");
          }

          String opponentName = in.readLine();
          out.writeBytes("Name: " + name);
  			}
        currentGame.initialize();
      } catch (IOException ioe) {
      System.out.println("something went really really wrong");
      }
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
} // GameClient
