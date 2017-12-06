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
 * TicTacToe.java
 */
//import javax.*;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

/**
* This is a simple little TicTacToe game.
*/
public class TicTacToe
{
  private String name;
  private boolean myTurn;
  private Socket connectionSock;
  private int mode;

  // Constructors
  public TicTacToe()
  {
    this.name = "";
    myTurn = true;
  }

  public TicTacToe(String name, boolean initTurn, Socket opponentSocket, int m)
  {
    this.name = name;
    this.myTurn = initTurn;
    this.connectionSock = opponentSocket;
    this.mode = m;
  }

  public void setMyTurn(boolean myTurn) {
    this.myTurn = myTurn;
  }

  public boolean getMyTurn() {
    return myTurn;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public int getMode() {
    return mode;
  }

  // Initializer - set GUI parameters for main menu
  public void initialize(int mode)
  {
    JFrame frame = new JFrame("Menu");
    frame.setVisible(true);
    frame.setTitle("Tic-Tac-Toe TCP Game: " + name);
    if(mode == 1){frame.setSize(600,600);}
    if(mode == 2){frame.setSize(700,700);}
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    frame.add(panel);
    JButton button = new JButton("Play");
    button.addActionListener(new playAction(myTurn, connectionSock, name, mode));
    panel.add(button);

    JMenuBar menubar = new JMenuBar();
    frame.setJMenuBar(menubar);
    JMenu help = new JMenu("Help");
    menubar.add(help);
    JMenuItem about = new JMenuItem("About");
    help.add(about);


  }
} // end class TicTacToe

// Create game window
class playAction implements ActionListener{
  private boolean myTurn;
  private Socket connectionSock;
  private String name;
  private int mode;

  public playAction() { this.myTurn = true; }

  public playAction(boolean myTurn, Socket connectionSock, String name, int mode) {
    this.myTurn = myTurn;
    this.connectionSock = connectionSock;
    this.name = name;
    this.mode = mode;
  }

  public void actionPerformed (ActionEvent e){
        JFrame ticTacToe;
        if (mode == 2) {
          ticTacToe = new TicTacToeFrameMode2(myTurn, connectionSock, name);
        } else {
          ticTacToe = new TicTacToeFrame(myTurn, connectionSock, name, mode);
        }
        ticTacToe.setTitle("Lets Play");
        ticTacToe.setSize(600, 600);
        ticTacToe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ticTacToe.setLocationRelativeTo(null);
        ticTacToe.setVisible(true);
  }
}
