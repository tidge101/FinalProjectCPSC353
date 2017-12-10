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
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;

public class GameClient
{

	public static String hostname = "localhost";
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
      JFrame frame = new JFrame("Menu");
      frame.setSize(300, 300);
      frame.setTitle("GameClient");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JPanel panel = new JPanel();
      frame.add(panel);

      JLabel label = new JLabel("IP Address to Connect To: ");
      panel.add(label);

      JTextField ipField = new JTextField(25);

      ipField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            JTextField ipField = (JTextField)(e.getSource());
            String selected = (String)ipField.getText();
            GameClient.hostname = selected;
          } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
          }
        }
      });
      panel.add(ipField);

      JButton button = new JButton("Connect to Server");
      button.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          // Wait for n players to connect (where n = numPlayers)
          GameClient.connect();
        }
      });
      panel.add(button);
      frame.setVisible(true);
	}

  public static void connect() {
    try {
      connectionSock = new Socket(GameClient.hostname, port);

      serverOutput = new DataOutputStream(connectionSock.getOutputStream());
      inFromServer =  new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));


      //Prompt for the user's name and send it to the server
      Scanner keyboard = new Scanner(System.in);
      String name;
      int mode = 1;

      // Read input from server of who they're playing
      String playerInfo = inFromServer.readLine();
      System.out.println(playerInfo);
      // If Client is the Host, create a socket and wait for the opponent to connect
      // Else(Client is not a host), receive Host info and connect to Host
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

      // Game loop
      while(true){
        // Receive Player name through user input
        System.out.println("Please enter a valid name!");
        name = keyboard.nextLine();
        if(name.toString() != null){
      //serverOutput.writeBytes(name + "\n");
          break;
        }
      }

      if(hostSocket != null) {
        while(true){
          System.out.println("Please enter 1 for a 3x3 board or 2 for a 4x4 board");
          mode = Integer.parseInt(keyboard.nextLine());
          if(mode == 1 || mode == 2){break;}
        }
      }


      // Start up TicTacToe client here
      TicTacToe currentGame;

      try {
        // Setup communication with opponent Client
        DataOutputStream out = new DataOutputStream(opponentSock.getOutputStream());
        BufferedReader in =  new BufferedReader(new InputStreamReader(opponentSock.getInputStream()));

        // If Client is Host, create new TicTacToe game where it is this Client's
        // turn. If Client is not the Host, create new TicTacToe game where it is
        // not this Client's turn. For both cases, output info about the status
        // and opponent Client.
        if (hostSocket != null) {
          System.out.println("Host mode: " + mode);
          currentGame = new TicTacToe(name, true, opponentSock, mode);
          // Send mode to opponent
          out.writeBytes("Mode: " + mode + "\n");
          out.writeBytes("Name: " + name + "\n");
          System.out.println("Is connected: " + opponentSock.isConnected());
          System.out.println("abouttowait host");
          String opponentName = in.readLine().substring(6);
          System.out.println("survived with " + opponentName);
        } else {
          // Get mode from host
          int mymode = Integer.parseInt(in.readLine().substring(6));
          mode = mymode;
          System.out.println("Client mode: " + mymode);
          currentGame = new TicTacToe(name, false, opponentSock, mymode);
          System.out.println("Is connected: " + opponentSock.isConnected());
          System.out.println("abouttowait client");
          String opponentName = in.readLine();
          System.out.println("survived with " + opponentName);
          out.writeBytes("Name: " + name + "\n");
        }

        currentGame.initialize(mode);
      } catch (IOException ioe) {
      System.out.println("something went really really wrong");
      }
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
  }
} // End GameClient
