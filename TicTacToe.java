
//import javax.*;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionListener;

/**
 * This is a simple little TicTacToe game.
 */
public class TicTacToe
{
    private char[][] board;

    public TicTacToe()
    {
      board = new char[3][3];
    }

    public void initialize()
    {
    	JFrame frame = new JFrame("Menu");
		frame.setVisible(true);
		frame.setTitle("Tic-Tac-Toe TCP Game!");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		JButton button = new JButton("Play");
		panel.add(button);

		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		JMenu help = new JMenu("Help");
		menubar.add(help);
		JMenuItem about = new JMenuItem("About");
		help.add(about);

		class playAction implements ActionListener{
			public void actionPerformed (ActionEvent e){
				JFrame ticTacToe = new TicTacToeFrame();
		        ticTacToe.setTitle("Lets Play");
		        ticTacToe.setSize(600, 600);
		        ticTacToe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        ticTacToe.setLocationRelativeTo(null);
		        ticTacToe.setVisible(true);
			}
		}








    }
} // end class TicTacToe
