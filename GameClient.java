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
      GameClient.gui.gui();
	}
  public static class gui{
    static String name;
    static String mode;
    static void gui(){

      JFrame frame = new JFrame("Menu");
      frame.setSize(500, 500);
      frame.setTitle("GameClient");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JPanel panel = new JPanel();
      frame.add(panel);

      //The folliwing code would be used if we were not running on the local servers

      /*JLabel label = new JLabel("IP Address to Connect To: ");
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
      */

      JButton button = new JButton("Connect to Server");
      button.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          // Wait for n players to connect (where n = numPlayers)
          GameClient.connect();
        }
      });
      //enter name
      JLabel label = new JLabel("Enter Name: ");
      JTextField nameField = new JTextField(25);
      nameField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            JTextField nameField = (JTextField)(e.getSource());
            name = (String)nameField.getText();
          } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
          }
        }
      });
      //enter game mode
      /*
      JLabel label2 = new JLabel("Enter game mode");
      JTextField gamemode = new JTextField(25);
      gamemode.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            JTextField gamemode = (JTextField)(e.getSource());
            //String selected2 = (String)nameField.getText();
          } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
          }
        }
      });
      */
      //mode = (String)gamemode.getText();
      name = (String)nameField.getText();
      JLabel instruction = new JLabel("Choose game mode in terminal");
      panel.add(label);
      panel.add(nameField);
      //panel.add(label2);
      //panel.add(gamemode);
      panel.add(button);
      panel.add(instruction);
      //JLabel instruction = new JLabel("Enter '1' for 3x3\nEnter '2' for 4x4\nEnter '3' for 10x10");
      //panel.add(instruction);
      frame.setVisible(true);
    }
    public static String getName(){
      return name;
    }
    public static void setName(String name){
      name = name;
    }
    /*
    public static String getMode(){
      return mode;
    }
    public static void setMode(String mode){
      mode = mode;
    }
    */
};
  public static void connect(){
    GameClient.gui gui = new GameClient.gui();
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
        //System.out.println("Please enter a valid name!");
        //name = keyboard.nextLine();
        name = gui.getName();
        if(name.toString() != null){
      //serverOutput.writeBytes(name + "\n");
          break;
        }
      }

      if(hostSocket != null) {
        while(true){
          System.out.println("Please enter 1 for a 3x3 board or 2 for a 4x4 board \nor 3 for a fun mode");
          mode = Integer.parseInt(keyboard.nextLine());
          if(mode == 1 || mode == 2 || mode == 3){break;}
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
