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
public class TicTacToeFrame extends JFrame
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

    // Create cell grid
    private Cell[][] cells = new Cell[3][3];

    // Create a status label
    JLabel jlblStatus = new JLabel("X's turn to play");

    /**
     * No-argument Constructor
     */
    public TicTacToeFrame()
    {
        // Panel to hold cells
        JPanel panel = new JPanel(new GridLayout(3, 3, 0, 0));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                panel.add(cells[i][j] = new Cell(i, j));

        panel.setBorder(new LineBorder(Color.red, 1));
        jlblStatus.setBorder(new LineBorder(Color.yellow, 1));

        add(panel, BorderLayout.CENTER);
        add(jlblStatus, BorderLayout.SOUTH);
        this.myTurn = true;
    }

    public TicTacToeFrame(boolean myTurn, Socket connectionSock, String name) {
      // Panel to hold cells
      JPanel panel = new JPanel(new GridLayout(3, 3, 0, 0));
      for (int i = 0; i < 3; i++)
          for (int j = 0; j < 3; j++)
              panel.add(cells[i][j] = new Cell(i, j));

      panel.setBorder(new LineBorder(Color.red, 1));
      jlblStatus.setBorder(new LineBorder(Color.yellow, 1));

      add(panel, BorderLayout.CENTER);
      add(jlblStatus, BorderLayout.SOUTH);
      this.myTurn = myTurn;
      this.connectionSock = connectionSock;
      this.name = name;

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
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
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
        for (int i = 0; i < 3; i++)
            if ((cells[i][0].getToken() == token)
                    && (cells[i][1].getToken() == token)
                    && (cells[i][2].getToken() == token))
            {
                return true;
            }

        // check columns
        for (int j = 0; j < 3; j++)
            if ((cells[0][j].getToken() == token)
                    && (cells[1][j].getToken() == token)
                    && (cells[2][j].getToken() == token))
            {
                return true;
            }
        // check diagonal
        if ((cells[0][0].getToken() == token)
                && (cells[1][1].getToken() == token)
                && (cells[2][2].getToken() == token))
        {
            return true;
        }

        if ((cells[0][2].getToken() == token)
                && (cells[1][1].getToken() == token)
                && (cells[2][0].getToken() == token))
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
                if (gameOver || !myTurn)
                    return;

                try {
                  // if the cell is empty and the game is not over
                  if (token == ' ' && whoseTurn != ' ')
                      setToken(whoseTurn);

                  // Check game status
                  if (isWon(whoseTurn))
                  {
                      jlblStatus.setText(name + " won! Game over!");
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
                      jlblStatus.setText(opponentName + "'s turn.");
                  }

                  String fromOpponent = in.readLine();
                  if (fromOpponent.indexOf("Win") == -1) {
                    int row = Integer.parseInt(fromOpponent.substring(5));
                  } else {
                    jlblStatus.setText(opponentName + " won! Game over!");
                    whoseTurn = ' ';
                    gameOver = true;
                  }
                  fromOpponent = in.readLine();
                  int col = Integer.parseInt(fromOpponent.substring(5));
                } catch (IOException ioe) {
                  System.out.println("something went really wrong");
                }

                if (whoseTurn == 'X') {
                  cells[row][col].setToken('O');
                } else {
                  cells[row][col].setToken('X');
                }
            }
        } // end class MyMouseListener
    } // end class Cell
} // end class TicTacToeFrame
