/**
 * Collaborators:
 * Isabella Cedric
 * 	ID: 1879812
 * 	cedri100@mail.chapman.edu /**
 * Thomas Madden
 * 	ID: 2261821
 * 	madde120@mail.chapman.edu /**
 * Andre Perkins
 * 	ID: 2267349
 * 	perki128@mail.chapman.edu /**
 *
 * Course: CPSC 353-01
 * Assignment: PA04 - Network Game
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
	private static ClientListener listener;
	private static Thread theThread;

	//init private variables
	public static void main(String[] args)
	{
		try
		{

			connectionSock = new Socket(hostname, port);

			serverOutput = new DataOutputStream(connectionSock.getOutputStream());

			// Start a thread to listen and display data sent by the server
			listener = new ClientListener(connectionSock);
			theThread = new Thread(listener);
			theThread.start();

			//Prompt for the user's nme and send it to the server
			Scanner keyboard = new Scanner(System.in);
			while(true){
			String name = keyboard.nextLine();

			if(name.toString() != null){
			serverOutput.writeBytes(name + "\n");
			break;
			}

		}
			// Read input from the keyboard and send it to teh Game Server.

			while (true)
			{
				System.out.println("Loading......");

				String message = keyboard.nextLine();

				if(message == " "){
				System.out.println("Still Loading....");

				}
				serverOutput.writeBytes(message + "\n");

			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
} // GameClient
