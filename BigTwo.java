import java.util.*;
/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game
 * @author Udaria
 *
 */
public class BigTwo implements CardGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentPlayerIdx;
	private BigTwoGUI ui;
	private BigTwoClient ct;
	/**
	 * Constructor for creating a Big Two card game
	 * Creates 4 players, BigTwoUI (and links it) and initializes the other instance variables
	 */

	public BigTwo()
	{
		playerList = new ArrayList<CardGamePlayer> ();
		currentPlayerIdx=-1;
		for(int i = 0; i < 4; i++)
		{
			CardGamePlayer player = new CardGamePlayer();
			playerList.add(player);
		}
		handsOnTable = new ArrayList<Hand>();
		ui = new BigTwoGUI(this);
		ct = new BigTwoClient(this, ui);
		
	}
	
	
	public String getPlayerName()
	{
		return ct.getPlayerName();
	}
	
	public int getPlayerID()
	{
		return ct.getPlayerID();
	}
	
	public synchronized void sendMessage(CardGameMessage message)
	{
		ct.sendMessage(message);
	}
	
	public void makeConnection()
	{
		ct.makeConnection();
	}
	
	public void disconnect()
	{
		ct.disconnect();
	}
	
	
	
	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */

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
	 * Returns the list of hands played on the table.
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
	/**
	 * Starts the card game.
	 * 
	 * @param deck the deck of (shuffled) cards to be used in this game
	 */

	public void start(Deck deck)
	{
		// Remove all cards from the player and table
		for(CardGamePlayer player : this.playerList)
		{
			player.removeAllCards();
		}
		
		this.handsOnTable.clear(); //clear all the cards from the table
		this.currentPlayerIdx = -1; //reset current player index
		
		//distribute cards to all player
		int player=0;
		
		for(int i=0; i<52; i++)
		{
			this.playerList.get(player%4).addCard(deck.getCard(i));
			player++;
			
		}
		
		Card startCard= new Card(0,2); //Helps in checking if card is three diamond
		this.deck=deck; 
		
		//Find out which player contains the three of diamonds
		for(int i=0; i<4 ;i++)
		{
			if(this.playerList.get(i).getCardsInHand().contains(startCard))
			{
				this.currentPlayerIdx=i;
				ui.setActivePlayer(i); ////Sets the player who contains the Three of Diamonds to the active player of the BigTwoUI object
				break;
			}
		}
		
		//Sort cards of all the players
		for(CardGamePlayer i: this.playerList)
		{
			i.sortCardsInHand();
		}
		
		//Calls the repaint() method to show the cards on the table and calls the promptActivePlayer() to prompt user to select cards and make
		ui.repaint();
		ui.setActivePlayer(this.currentPlayerIdx);
		if(ct.getPlayerID() == this.currentPlayerIdx)
			ui.enableTable();
		else ui.disableTable();
		ui.promptActivePlayer();
	}
	
	public void makeMove(int playerIdx, int[] cardIdx)
	{
		checkMove(playerIdx, cardIdx);
	}
	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerIdx the index of the player who makes the move
	 * @param cardIdx   the list of the indices of the cards selected by the player
	 */

	public void checkMove(int playerIdx, int[] cardIdx)
	{
			CardGamePlayer player = this.playerList.get(playerIdx);
			CardList selectedCards = new CardList();
			CardList playerCards = player.getCardsInHand();
			
			if(ct.getPlayerID() == this.currentPlayerIdx)
				ui.enableTable();
			else ui.disableTable();
			
			CardGamePlayer prevplayer;
			if(cardIdx == null || cardIdx.length == 0)
			{
				if(handsOnTable.size()==0)
				{
					prevplayer = null;
					
				}
				else prevplayer = this.handsOnTable.get(handsOnTable.size()-1).getPlayer();
				
				if(prevplayer==null || prevplayer.equals(player))
				{
					ui.printMsg("Not a legal move!!!");
					System.out.println("");
					if(ct.getPlayerID() == this.currentPlayerIdx)
						ui.enableTable();
					else ui.disableTable();
					ui.repaint();
					ui.promptActivePlayer();
					return;
					
				}
				else
					this.currentPlayerIdx = (this.currentPlayerIdx+1)%4;
					ui.printMsg("\n{Pass}");
					System.out.println("\n");
					ui.setActivePlayer(this.currentPlayerIdx);
					ui.repaint();
					if(ct.getPlayerID() == this.currentPlayerIdx)
						ui.enableTable();
					else ui.disableTable();
					ui.promptActivePlayer();
					return;
			}
			
			for (int i = 0; i < cardIdx.length; i++) 
			{
				selectedCards.addCard(playerCards.getCard(cardIdx[i]));
				if (!deck.contains(selectedCards.getCard(i))) 
				{
					
					ui.printMsg("Not a legal move!!!");
					System.out.println("");
					if(ct.getPlayerID() == this.currentPlayerIdx)
						ui.enableTable();
					else ui.disableTable();
					ui.promptActivePlayer();
					return;
					
				}
			}
			
			Hand playingHand = composeHand(player, selectedCards);
			
			if(playingHand == null)
			{
				ui.printMsg("Not a legal move!!!");
				System.out.println("");
				ui.promptActivePlayer();
				return;
				
			}
			
			if(handsOnTable.isEmpty())
			{
				if(!(selectedCards.contains(new Card(0,2))))
						{
							ui.printMsg("Not a legal move!!!");
							System.out.println("");
							ui.promptActivePlayer();
							return;
						}
			}
			
			Hand lastPlayed;
			if(handsOnTable.size() == 0)
			{
				lastPlayed=null;
			}
			else lastPlayed = this.handsOnTable.get(handsOnTable.size() - 1);
			
			if(lastPlayed == null || lastPlayed.getPlayer().equals(player)|| playingHand.beats(lastPlayed))
			{
				actionH(playerIdx, playingHand);
			}
			else
			{
				ui.printMsg("Not a legal move!!!");
				System.out.println("");
				ui.promptActivePlayer();
				return;
			}
	}
	
	private void actionH(int playerIdx, Hand playingHand) 
	{
			
			CardGamePlayer player = this.playerList.get(playerIdx);
			player.removeCards(playingHand);
			player.sortCardsInHand();
			
			for(int i = 0; i < playingHand.size(); i++) {
				deck.removeCard(playingHand.getCard(i));
			}
			
			handsOnTable.add(playingHand);
			
			ui.printMsg(String.format("{%s} ", playingHand.getType()));
			playingHand.print();
			ui.printMsg("\n");
			
			this.currentPlayerIdx = (this.currentPlayerIdx + 1) % 4;
			
			if (endOfGame())
			{
				this.currentPlayerIdx = -1;
				ui.setActivePlayer(currentPlayerIdx);
				ui.repaint();
				ui.disable();
				ui.printMsg("\nGame ends\n");
				for (int i = 0; i < playerList.size(); i++) {
					if (playerList.get(i).getNumOfCards() == 0)
						ui.printMsg(this.playerList.get(i).getName() +" wins the game.\n");
					else
						ui.printMsg(this.playerList.get(i).getName() + " has " + playerList.get(i).getNumOfCards() + " cards in hand.\n");
				}

			}
			else
			ui.setActivePlayer(this.currentPlayerIdx);
			ui.repaint();
			if(ct.getPlayerID() == this.currentPlayerIdx)
				ui.enableTable();
			else ui.disableTable();
			ui.promptActivePlayer();

		}

	
	public boolean endOfGame()
	{
		for(CardGamePlayer player : this.playerList)
		{
			if(player.getNumOfCards() == 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args)
	{
		BigTwo game = new BigTwo();
		BigTwoDeck deck = new BigTwoDeck();
		deck.shuffle(); 
		game.start(deck);	
	}
	
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		if(cards.size() == 1) 
		{
			Hand s = new Single(player,cards);
			
			if(s.isValid()) 
			{
				return s;
			}
		}
		
		else if(cards.size()==2) 
		{
			Hand p = new Pair(player,cards);
			
			if(p.isValid()) 
			{
				return p;
			}
		}
		else if(cards.size()==3) 
		{
			Hand tri = new Triple(player,cards);
			
			if(tri.isValid()) 
			{
				return tri;
			}
		}
		else if(cards.size()==5) 
		{
			Hand str = new Straight(player,cards);
			Hand flu = new Flush(player,cards);
			Hand fh = new FullHouse(player,cards);
			Hand qud = new Quad(player,cards);
			Hand stf = new StraightFlush(player,cards);
			
			if(str.isValid()) 
			{
				return str;
			}
			
			else if(flu.isValid()) 
			{
				return flu;
			}
			
			else if(fh.isValid()) 
			{
				return fh;
			}
			
			else if(qud.isValid()) 
			{
				return qud;
			}
			
			else if(stf.isValid()) 
			{
				return stf;
			}
			
			
		}
		
		return null;
		
		
	}
	
}


