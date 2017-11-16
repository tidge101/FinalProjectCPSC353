/**
 * Collaborators:
 * Maika Fujii
 * 	ID: 1935412
 * 	fujii108@mail.chapman.edu /**
 * Thomas Madden
 * 	ID: 2261821
 * 	madde120@mail.chapman.edu /**
 * Dillon Tidgewell
 * 	ID:
 * 	tidge@mail.chapman.edu /**
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
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class GameClient
{


	private static final String hostname = "localhost";
	private static final int port = 7654;
	private static Socket connectionSock;
	private static DataOutputStream serverOutput;
	private static BufferedReader inFromServer;
	private static Thread theThread;

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
			while(true){
				System.out.println("Please enter a valid name!");
				String name = keyboard.nextLine();
				if(name.toString() != null){
					serverOutput.writeBytes(name + "\n");
					break;
				}

			}
			// Read input from server of who they're playing
			String playerInfo = inFromServer.readLine();

			// Connect to other player using playerInfo

			// Start up TicTacToe client here
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
} // GameClient
