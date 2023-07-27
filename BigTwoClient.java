import java.io.*;
import javax.swing.JOptionPane;
import java.net.Socket;
import java.util.ArrayList;
/**
 * BigTwoClient used to model a BigTwo game client. Responsible for establishing connection and communication
 * @author Udaria
 *
 */

public class BigTwoClient implements NetworkGame, CardGame {
	
	/**
	 * Constructor for BigTwoClient
	 * @param game - Game Obkect
	 * @param gui - User interface
	 */
	@SuppressWarnings("unused")
	public BigTwoClient(BigTwo game, BigTwoGUI gui)
	{
		this.game = game;
		this.gui = gui;
		
		playerID = -1;
				
		String name = (String) JOptionPane.showInputDialog("Enter your name: ");
		setPlayerName(name);
		
		if(name=="" || name.trim()=="" || name==null) 
		{
			setPlayerName("Guest");
			
		}
		
		
		serverIP = "127.0.0.1";
		serverPort = 2396;
		
		gui.disable();
		gui.repaint();
		makeConnection();
		
	}
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentPlayerIdx;

	
	private BigTwo game;
	private BigTwoGUI gui;
	private Socket sock;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	/**
	 *  method for getting playerID
	 */
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	
	public int getNumOfPlayers()
	{
		return this.numOfPlayers;
		
	}

	/**
	 * Returns the deck of cards being used in this card game.
	 * 
	 * @return the deck of cards being used in this card game
	 */

	public Deck getDeck()
	{
		return this.deck;
	}
	/**
	 * Returns the list of players in this card game.
	 * 
	 * @return the list of players in this card game
	 */

	 
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return this.playerList;
		
	}
	/**
	 * Returns the list of hands played on the gui.
	 * 
	 * @return the list of hands played on the table
	 */

	public ArrayList<Hand> getHandsOnTable()
	{
		return this.handsOnTable;
		
	}
	/**
	 * Returns the index of the current player.
	 * 
	 * @return the index of the current player
	 */

	public int getCurrentPlayerIdx()
	{
		return this.currentPlayerIdx;
	}
	
	
	public int getPlayerID()
	{
		return playerID;
	}
	
	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}
	
	public String getPlayerName(String playerName)
	{
		return playerName;
	}
	
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}
	
	public String getServerIP()
	{
		return serverIP;
	}
	
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}
	
	public void setServerPort(int serverPort)
	{
		this.serverPort= serverPort;
	}
	
	public boolean isConnected()
	{
		if(sock.isClosed())
			return false;
		
		return true;
	}
	
	public void disconnect()
	{
		try {
				if(sock!=null)
				{
					sock.close();
				}
			sock=null;
		}
		
		catch(Exception e)
		{
			gui.printMsg("Error from disconnecting from the server!\n");
			e.printStackTrace();
		}
	}
	
	public void makeConnection() 
	{
		try 
		{
			sock = new Socket(getServerIP(), getServerPort());
			
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			
			
			Thread thread = new Thread(new ServerHandler());
			thread.start();
	
			gui.reset();
			gui.disableTable();
			gui.printMsg("Connected to server at /" + serverIP + ":" + serverPort + "\n");
			gui.repaint();		
			
		}
		
		catch(Exception e) 
		{
			gui.printMsg("Unable to connect to game server!");
			e.printStackTrace();
		}	
	}

	public void parseMessage(GameMessage message) 
	{
		if(message.getType()==CardGameMessage.FULL) 
		{
			gui.printMsg("Game is Full\n");
			gui.displaymsg("Game is Full!\n");
			disconnect();

		}
		
		else if(message.getType()==CardGameMessage.JOIN) 
		{
			getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			gui.repaint();
			gui.printMsg("Player " + playerList.get(message.getPlayerID()).getName() + " joined the game!");
		}
		
		else if(message.getType()==CardGameMessage.MOVE) 
		{
			game.checkMove(message.getPlayerID(),(int[])message.getData());
			gui.repaint();
		}
		
		else if(message.getType()==CardGameMessage.MSG) 
		{
			gui.printMsg((String)message.getData());
		}
		
		else if(message.getType()==CardGameMessage.PLAYER_LIST) 
		{
			setPlayerID(message.getPlayerID());
			
			gui.setActivePlayer(message.getPlayerID());
			
			for(int i=0;i<getNumOfPlayers();i++)	
			{
				if(((String[])message.getData())[i]!=null) {
					getPlayerList().get(i).setName(((String[])message.getData())[i]);
				}
			}
		}
		
		else if(message.getType()==CardGameMessage.QUIT) 
		{
			gui.printMsg("Player " + message.getPlayerID() + " " + playerList.get(message.getPlayerID()).getName() + " left the game.");
			getPlayerList().get(message.getPlayerID()).setName("");
			if(!endOfGame()) 
			{
				gui.disable();
				CardGameMessage msg = new CardGameMessage(CardGameMessage.READY, -1, null);
				sendMessage(msg);
				for(int i=0;i<4;i++) 
				{
					getPlayerList().get(i).removeAllCards();
				}
				gui.repaint();
			}
			gui.repaint();
		}
		
		else if(message.getType()==CardGameMessage.READY) 
		{
			gui.printMsg("Player " + message.getPlayerID() + " is ready.");
		}
		
		else if(message.getType()==CardGameMessage.START) 
		{
			deck = (BigTwoDeck) message.getData();
			start(deck);
			gui.enable();
			gui.repaint();
		}
	}
	
	public void sendMessage(GameMessage message) 
	{
		try {
			oos.writeObject(message);
		}
		catch(Exception e) {
			gui.printMsg("Unable to send message object to server!");
			e.printStackTrace();
		}
	}
	
	
    class ServerHandler implements Runnable
	{

		public void run() 
		{	
			CardGameMessage message;
			try
			{
				ois = new ObjectInputStream(sock.getInputStream());
				
				while(!sock.isClosed() || sock!=null) 
				{
					if((message = (CardGameMessage) ois.readObject()) != null)
					{
						parseMessage(message);
					}	
				}
				
				ois.close();
			} 
			
			catch (Exception e) 
			{
				gui.printMsg("Unable to recieve message from server\n");
				try 
				{
					sock.close();
				} 
				catch(Exception ex) 
				{
					System.out.println("Unable to close socket!");
				}

				e.printStackTrace();
			}
			
			gui.repaint();
		
		
		}

		
	
	}


	@Override
	public void start(Deck deck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeMove(int playerIdx, int[] cardIdx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkMove(int playerIdx, int[] cardIdx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}
	
}
