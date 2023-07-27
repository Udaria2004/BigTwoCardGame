/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It has a
 * private instance variable for storing the player who plays this hand. It also has methods for
 * getting the player of this hand, checking if it is a valid hand, getting the type of this hand, getting
 * the top card of this hand, and checking if it beats a specified hand. Below is a detailed
 * description for the Hand class.
 * @author Udaria
 *
 */
abstract public class Hand extends CardList
{
	private static final long serialVersionUID = 1L;
	private CardGamePlayer player;
	/**
	 * a constructor for building a hand with the specified player and list of cards
	 * @param player player who is playing the current hand
	 * @param cards contains the list of cards
	 */
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player = player;
		for(int i = 0; i < cards.size(); i++)
		{
			this.addCard(cards.getCard(i));
		}
	}	
	/**
	 *  the player who plays this hand
	 * @return the player who is currently playing the hand
	 */
	public CardGamePlayer getPlayer()
	{
		return this.player;
		
	}
	/**
	 * a method for retrieving the player of this hand.

	 * @return the top of the card in hand
	 */
	
	public Card getTopCard() 
	{
		if (this.getType() == "FullHouse" || this.getType() == "Quad") 
		{
			CardList c1 = new CardList();
			CardList c2 = new CardList();
			
			// Full House and Quad have 2 ranks.
			int rank1 = this.getCard(0).getRank();
			int rank2 = -1;
			
			for (int i = 0; i < 5; i++) 
			{
				if (this.getCard(i).getRank() != rank1) 
				{
					rank2 = this.getCard(i).getRank();
				}
			}
			
			for (int i = 0; i < 5; i++) 
			{
				if (this.getCard(i).getRank() == rank1) 
				{
					c1.addCard(this.getCard(i));
				} 
				else if (this.getCard(i).getRank() == rank2) 
				{
					c2.addCard(this.getCard(i));
				}
			}
			
			// Default is FullHouse
			int templist1 = 3;
			if (this.getType() == "Quad")
			{
				templist1 = 4;
			}
			
			// Returns the 'strongest' card from the larger list (quadruple or triplet depending on FullHouse or Quad)
			if (c1.size() == templist1) 
			{
				c1.sort();
				return c1.getCard(templist1 - 1);
			}
			
			else 
			{
				c2.sort();
				return c2.getCard(templist1 - 1);
			}
		}
		
		// For hands other than Quad and Full House
		this.sort();
		return this.getCard(this.size() - 1);
	}
	
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * @param hand The hand we are checking against
	 * @return boolean value checking if it beats the specified hand or not
	 */
	public boolean beats(Hand hand)
	{
		if (hand == null || !hand.isValid() || !this.isValid() || this.size() != hand.size()) return false;
		
		// Getting the top cards
		Card tc = this.getTopCard();
		int tcRank = tc.getRank();
		tcRank = (tcRank - 2 < 0) ? 13 + tcRank - 2 : tcRank - 2;
		
		Card htc = hand.getTopCard();
		int htcRank = htc.getRank();
		htcRank = (htcRank - 2 < 0) ? 13 + htcRank - 2 : htcRank - 2;
		
		if (hand.size() == 1) 
		{
			return tc.compareTo(htc) == 1;
			
		} 
		else if (hand.size() == 2) 
		{
			if (tcRank > htcRank) 
			{
				return true;
			} 
			else if (tcRank < htcRank) 
			{
				return false;
			} 
			else if (tc.getSuit() > htc.getSuit()) 
			{
				return true;
			} 
			else 
			{
				return false;
			}
		} 
		else if (hand.size() == 3) 
		{
			if (tcRank > htcRank) 
			{
				return true;
			} 
			else 
			{
				return false;
			}
		} 
		else if (hand.size() == 5) 
		{
			if (this.getType() == hand.getType()) 
			{
				if (this.getType() == "Flush") 
				{
					if (tc.getSuit() > htc.getSuit()) 
					{
						return true;
					} 
					else if (tc.getSuit() < htc.getSuit()) 
					{
						return false;
					} 
					else 
					{
						if(tc.compareTo(htc) == 1)
						{
							return true;
						}
					}
				} 
				else 
				{
					return tc.compareTo(htc) ==  1;
				}
			} 
			else 
			{
				if (this.getType() == "Straight Flush") 
				{
					return true;
				} 
				else if (this.getType() == "Quad") 
				{
					if (hand.getType() != "Straight Flush") 
					{
						return true;
					} 
					else 
					{
						return false;
					}
				} 
				else if (this.getType() == "Full House") 
				{
					if (hand.getType() == "Flush" || hand.getType() == "Straight") 
					{
						return true;
					} 
					else 
					{
						return false;
					}
				} 
				else if (this.getType() == "Flush") 
				{
					if (hand.getType() == "Straight") 
					{
						return true;
					} 
					else 
					{
						return false;
					}
				} 
				else if (this.getType() == "Straight") 
				{
					return false;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * @return check if valid hand or not
	 */
	public abstract boolean isValid();
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return type of hand 
	 */
	public abstract String getType();
	
}
