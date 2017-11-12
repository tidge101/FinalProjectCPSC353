

public GameServer {
  private ArrayList<Socket> socketList;
	private ArrayList<Player> playerList;
	private ServerSocket serverSock = null;
	private int numberOfPlayers = 2;
	private int numberConnected = 0;
  private int state = 0;

  private Game game = null;

  public GameServer() {
    socketList = new ArrayList<Socket>();
    playerList = new ArrayList<Player>();
    game = new Game(playerList);
  }

  private void getConnection()
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
        Player newplayer= new Player(connectionSock, "");   // we should find a name for player in gameclient
        //socketList.add(connectionSock);
        playerList.add(newplayer);
        numberConnected++;
        System.out.print("The number of connected players is ");
        System.out.println(numberConnected);
        // Send to ClientHandler the socket and arraylist of all sockets
        ClientHandler handler = new ClientHandler(newplayer, this.playerList,mygame);
        Thread theThread = new Thread(handler);
        theThread.start();
      }//  while (numberConnected < numberOfPlayers)

      System.out.println("All players have connected\n");
      //Wait until all clients have given us their names.
      int ready = 0;
      while (ready == 0)
      {
        ready = 1;								//Wait for players to enter their names
        for (Player p:playerList)
        {
          if (p.name.equals(""))
          {
            ready = 0;
          }
        }//for (Player p:playerList)
      }//while (ready == 0)

      showScores();
    }//try
    catch (IOException e)
    {
      System.out.println(e.getMessage());
    }//catch
  }//getConnection

  public static void main(String[] args) {
    GameServer server = new GameServer();
    server.getConnection();
    server.state = 1;
  }


}
