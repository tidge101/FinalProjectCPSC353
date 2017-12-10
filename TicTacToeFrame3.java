/**
 * Collaborators:
 * Maika Fujii
 *  ID: 1935412
 *  fujii108@mail.chapman.edu /**
 * Thomas Madden
 *  ID: 2261821
 *  madde120@mail.chapman.edu /**
 * Dillon Tidgewell
 *  ID:
 *  tidge101@mail.chapman.edu /**
 *
 * Course: CPSC 353-01
 * Assignment: Final Project - Tic-Tac-Toe
 *
 *
 * TicTacToeFrame.java
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * JFrame to hold TicTacToe board.
 */
public class TicTacToeFrameMode3 extends JFrame
{
    // Indicate whose turn it is
    private char whoseTurn = 'X';
    private boolean gameOver = false;
    private boolean myTurn;
    private Socket connectionSock;
    private String name;
    private DataOutputStream out;
    private BufferedReader in;
    private String opponentName;
    private Thread theThread;
    private ClientListener listener;

    // Create cell grid
    private Cell[][] cells = new Cell[4][4];

    // Create a status label
    JLabel jlblStatus;

    /**
     * No-argument Constructor
     */
    public TicTacToeFrameMode3()
    {
        // Panel to hold cells
        JPanel panel = new JPanel(new GridLayout(15, 15, 0, 0));
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                panel.add(cells[i][j] = new Cell(i, j));

        panel.setBorder(new LineBorder(Color.red, 1));
        jlblStatus.setBorder(new LineBorder(Color.yellow, 1));

        add(panel, BorderLayout.CENTER);
        add(jlblStatus, BorderLayout.SOUTH);
        this.myTurn = true;
        jlblStatus = new JLabel("Your turn to mess around!");
    }

    public TicTacToeFrameMode3(boolean myTurn, Socket connectionSock, String name) {
      // Panel to hold cells
      JPanel panel = new JPanel(new GridLayout(15, 15, 0, 0));
      for (int i = 0; i < 15; i++)
          for (int j = 0; j < 15; j++)
              panel.add(cells[i][j] = new Cell(i, j));

      if (myTurn) {
        jlblStatus = new JLabel("X's turn to play!");
      } else {
        jlblStatus = new JLabel("O's turn to play!");
      }

      panel.setBorder(new LineBorder(Color.red, 1));
      jlblStatus.setBorder(new LineBorder(Color.yellow, 1));

      add(panel, BorderLayout.CENTER);

      add(jlblStatus, BorderLayout.SOUTH);
      this.myTurn = myTurn;
      this.connectionSock = connectionSock;
      this.name = name;

      if (myTurn) {
        listener = new ClientListener(connectionSock, cells, 'O');
      } else {
        listener = new ClientListener(connectionSock, cells, 'X');
      }
			theThread = new Thread(listener);
			theThread.start();

      try {
        this.out = new DataOutputStream(connectionSock.getOutputStream());
        this.in =  new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
        if (myTurn) {
          this.whoseTurn = 'X';
        } else {
          this.whoseTurn = 'O';
        }
      } catch (IOException ioe) {
        System.out.println("something went really wrong");
      }
    }

    /**
     * Determine if game board is full.
     * @return True, if game board is full. Otherwise, false.
     */
    public boolean isFull()
    {
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                if (cells[i][j].getToken() == ' ')
                    return false;
        return true;
    }

    /**
     * Determines if a given token has won.
     * @param token Token to test for winning
     * @return True, if the token has won. Otherwise, false.
     */
    public boolean isWon(char token)
    {
        // check rows
        for (int i = 0; i < 15; i++)
            if ((cells[i][0].getToken() == token)
                    && (cells[i][1].getToken() == token)
                    && (cells[i][2].getToken() == token)
                    && (cells[i][3].getToken() == token))
            {
                return true;
            }

        // check columns
        for (int j = 0; j < 15; j++)
            if ((cells[0][j].getToken() == token)
                    && (cells[1][j].getToken() == token)
                    && (cells[2][j].getToken() == token)
                    && (cells[3][j].getToken() == token))
            {
                return true;
            }
        // check diagonal
        if ((cells[0][0].getToken() == token)
                && (cells[1][1].getToken() == token)
                && (cells[2][2].getToken() == token)
                && (cells[3][3].getToken() == token))
        {
            return true;
        }

        if ((cells[0][3].getToken() == token)
                && (cells[1][2].getToken() == token)
                && (cells[2][1].getToken() == token)
                && (cells[3][0].getToken() == token))
        {
            return true;
        }

        return false;
    }

    /**
     * Defines a cell in a TicTacToe game board.
     */
    public class Cell extends JPanel
    {
        // token of this cell
        private char token = ' ';

        // location of cell
        private int row;
        private int col;

        /**
         * Constructor
         */
        public Cell()
        {
            setBorder(new LineBorder(Color.black, 1));
            addMouseListener(new MyMouseListener(0, 0));
        }

        public Cell(int row, int col) {
          setBorder(new LineBorder(Color.black, 1));
          addMouseListener(new MyMouseListener(row, col));
          this.row = row;
          this.col = col;
        }

        /**
         * Gets the token of the cell.
         * @return The token value of the cell.
         */
        public char getToken()
        {
            return token;
        }

        /**
         * Sets the token of the cell.
         * @param c Character to use as token value.
         */
        public void setToken(char c)
        {
            token = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            if (token == 'X')
            {
                g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
                g.drawLine(getWidth() - 10, 10, 10, getHeight() - 10);
            }

            else if (token == 'O')
            {
                g.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
            }
        }

        private class MyMouseListener extends MouseAdapter
        {
            private int row;
            private int col;

            public MyMouseListener(int row, int col) {
              this.row = row;
              this.col = col;
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (gameOver || !myTurn) {
                    System.out.println("click ignored");
                    return;
                }

                try {
                  // if the cell is empty and the game is not over
                  if (token == ' ' && whoseTurn != ' ')
                      setToken(whoseTurn);

                  // Check game status
                  if (isWon(whoseTurn))
                  {
                      jlblStatus.setText(whoseTurn + " won! Game over!");
                      whoseTurn = ' ';
                      gameOver = true;
                      out.writeBytes("Win");
                  }
                  else if (isFull())
                  {
                      jlblStatus.setText("Tie game! Game over!");
                      whoseTurn = ' ';
                      gameOver = true;
                  }
                  else
                  {
                      out.writeBytes("Row: " + row + "\n");
                      out.writeBytes("Col: " + col + "\n");
                      if (whoseTurn == 'X') {
                        jlblStatus.setText("X's turn to play!");
                      } else {
                        jlblStatus.setText("O's turn to play!");
                      }
                  }

                  myTurn = false;


                } catch (IOException ioe) {
                  System.out.println("something went really wrong");
                }


            }
        } // end class MyMouseListener
    } // end class Cell

    public class ClientListener implements Runnable
    {
    	private Socket connectionSock = null;
    	private BufferedReader serverInput;
    	private String fromOpponent;
      private Cell[][] cells;
      private char mySign;

    	ClientListener(Socket sock, Cell[][] cells, char mySign)
    	{
    		this.connectionSock = sock;
        this.cells = cells;
        this.mySign = mySign;
    	}
    /**
     * run method in this class receives messages from the server
     * after the connection is made, and returns this data
     *
     */
    	public void run()
    	{
           		 // Wait for data from the server.  If received, output it.
    		try
    		{
    			serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
    			while (true)
    			{
    				// Get data sent from the server
    				fromOpponent = serverInput.readLine();
    				if (serverInput != null)
    				{
              int row = Integer.parseInt(fromOpponent.substring(5));
              fromOpponent = serverInput.readLine();
              int col = Integer.parseInt(fromOpponent.substring(5));
              cells[row][col].setToken(mySign);

              myTurn = true;
              if (mySign == 'X') {
                jlblStatus.setText("O's turn to play!");
              } else {
                jlblStatus.setText("X's turn to play!");
              }

    					if (fromOpponent.indexOf("Win") != -1){
                myTurn = false;
                if (mySign == 'X') {
                  jlblStatus.setText("O won the game! Game over.");
                } else {
                  jlblStatus.setText("X won the game! Game over.");
                }
    						connectionSock.close();    //prepare to exit the while loop
    						break;
    					}
    				}
    				else
    				{
    					// Connection was lost
    					connectionSock.close();
    					break;
    				}
    			}
    		}
    		catch (Exception e)
    		{
    			System.out.println("Error: " + e.toString());
    		}
    	}
    } // ClientListener for GameClient
} // end class TicTacToeFrame
